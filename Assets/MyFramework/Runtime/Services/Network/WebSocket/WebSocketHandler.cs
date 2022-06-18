using System;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using UnityEngine;
using UnityEngine.Assertions;

namespace MyFramework.Runtime.Services.Network.WebSocket
{
    /*
     * You can test on: https://www.piesocket.com/websocket-tester
     */
    public class WebSocketHandler : IDisposable
    {
        private ClientWebSocket webSocket;
        private CancellationTokenSource connectTs;
        private CancellationTokenSource sendTs;
        private CancellationTokenSource receiveTs;
        private const int ReceiveBufferSize = 8192;
        private readonly TimeSpan ConnectTimeout = TimeSpan.FromSeconds(5);
        private readonly TimeSpan SendTimeout = TimeSpan.FromSeconds(5);

        public WebSocketHandler()
        {
        }

        public async Task ConnectAsync(Uri uri)
        {
            try
            {
                if (webSocket != null)
                {
                    throw new Exception("client web socket has been created. ");
                }

                webSocket = new ClientWebSocket();
                connectTs = new CancellationTokenSource(ConnectTimeout);
                await webSocket.ConnectAsync(uri, connectTs.Token);
                if (connectTs.IsCancellationRequested)
                {
                    Debug.LogError("client web socket connect timeout, clean up");
                    Cleanup();
                    return;
                }

                connectTs.Dispose();

                _ = ReceiveLoop();
            }
            catch (Exception e)
            {
                HandleConnectException(e);
            }
        }

        public async Task SendAsync(string jsonStr)
        {
            try
            {
                if (string.IsNullOrEmpty(jsonStr))
                    return;
                var data = Encoding.UTF8.GetBytes(jsonStr);
                sendTs = new CancellationTokenSource(SendTimeout);
                await webSocket.SendAsync(
                    new ArraySegment<byte>(data),
                    WebSocketMessageType.Text, // for json
                    true,
                    sendTs.Token);
                if (sendTs.IsCancellationRequested)
                {
                    Debug.LogError("client web socket send cancel with token");
                }

                sendTs.Dispose();
            }
            catch (Exception e)
            {
                HandleSendException(e);
            }
        }

        private async Task ReceiveLoop()
        {
            try
            {
                var buffer = new ArraySegment<byte>(new byte[ReceiveBufferSize]);
                receiveTs = new CancellationTokenSource();
                while (!receiveTs.IsCancellationRequested)
                {
                    if (webSocket == null && webSocket.State != WebSocketState.Open)
                    {
                        Cleanup();
                        break;
                    }

                    var receiveAsync = await webSocket.ReceiveAsync(buffer, receiveTs.Token);
                    if (receiveAsync.MessageType == WebSocketMessageType.Close)
                    {
                        Cleanup();
                        break;
                    }

                    Assert.IsTrue(receiveAsync.EndOfMessage, "receiveAsync.EndOfMessage");
                    Assert.IsTrue(receiveAsync.MessageType == WebSocketMessageType.Text,
                        "receiveAsync.MessageType == WebSocketMessageType.Text");

                    DispatchMessage(new ArraySegment<byte>(buffer.Array, 0, receiveAsync.Count));
                }
            }
            catch (Exception e)
            {
                HandleReceiveLoopException(e);
            }
        }

        private void DispatchMessage(ArraySegment<byte> buffer)
        {
            var jsonStr = Encoding.UTF8.GetString(buffer.Array, buffer.Offset, buffer.Count);
            Debug.Log($"DispatchMessage: {jsonStr}");
        }

        private void Cleanup()
        {
            webSocket = null;
        }

        private void HandleReceiveLoopException(Exception exception)
        {
            Debug.LogError("exception on web socket receive loop. " + exception.ToString());
        }

        private void HandleSendException(Exception exception)
        {
            Debug.LogError("exception on web socket send. " + exception.ToString());
        }

        private void HandleConnectException(Exception exception)
        {
            Debug.LogError("exception on web socket connect. " + exception.ToString());
        }

        public void Dispose()
        {
            webSocket?.CloseAsync(WebSocketCloseStatus.Empty, null, CancellationToken.None).Wait();
            webSocket?.Dispose();

            sendTs?.Dispose();
            connectTs?.Dispose();
            receiveTs?.Dispose();
        }
    }
}