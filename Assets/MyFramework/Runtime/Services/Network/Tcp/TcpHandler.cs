using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Net.Security;
using System.Net.Sockets;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading;
using System.Threading.Tasks;
using UnityEngine;

namespace MyFramework.Services.Network.Tcp
{
    public enum TcpConnectionResult
    {
        Ok = 1,
        Timeout = 2,
        Error = 3,
        AlreadyConnected = 4,
    }

    public enum TcpSendResult
    {
        Ok = 1,
        Timeout = 2,
        Error = 3,
        NoConnection = 4,
        Unknown = 5,
    }

    public class TcpSocketHandler
    {
        private TcpClient tcpClient;
        private TcpConfiguration configuration;
        private ConcurrentQueue<TcpLengthBasedFrame> readQueue;
        private ConcurrentQueue<TcpLengthBasedFrame> sendQueue;
        private byte[] readBuffer;
        private int readOffset;
        private volatile bool connecting;

        public bool Connected => tcpClient != null && tcpClient.Connected;
        public bool Connecting => connecting;
        public TcpSocketHandler(TcpConfiguration configuration)
        {
            Cleanup();
        }

        private void Cleanup()
        {
            tcpClient = new TcpClient();
            readQueue = new ConcurrentQueue<TcpLengthBasedFrame>();
            sendQueue = new ConcurrentQueue<TcpLengthBasedFrame>();
            readBuffer = new byte[2 * configuration.FrameMaxSize];
            readOffset = 0;
        }

        private void ApplyTcpClientOptions()
        {
            tcpClient.SendTimeout = configuration.SendTimeout.Milliseconds;
            tcpClient.ReceiveTimeout = configuration.SendTimeout.Milliseconds;
            tcpClient.NoDelay = configuration.NoDelay;
        }

        public async Task<TcpConnectionResult> ConnectAsync()
        {
            try
            {
                if (tcpClient.Connected)
                    return TcpConnectionResult.AlreadyConnected;

                ApplyTcpClientOptions();
                connecting = true;
                var connWithoutTimeout = tcpClient
                    .ConnectAsync(configuration.IpEndPoint.Address, configuration.IpEndPoint.Port)
                    .Wait(configuration.ConnectTimeout);
                connecting = false;
                if (!connWithoutTimeout)
                {
                    await CloseAsync();
                    return TcpConnectionResult.Timeout;
                }

                if (tcpClient.Connected)
                {
                    _ = StartReadLoop();
                }

                return TcpConnectionResult.Ok;
            }
            catch (Exception e)
            {
                Debug.LogError("exception on connect. " + e.ToString());
            }

            return TcpConnectionResult.Error;
        }

        public async Task CloseAsync()
        {
            try
            {
                if (tcpClient != null)
                {
                    tcpClient.Close();
                }
            }
            catch (Exception e)
            {
                Debug.LogError("exception on close. " + e.ToString());
            }
            finally
            {
                Cleanup();
            }

            await Task.CompletedTask;
        }

        public async Task<TcpSendResult> SendAsync(TcpProtocol protocol)
        {
            try
            {
                if (!tcpClient.Connected)
                    return TcpSendResult.NoConnection;

                var frameCodec = configuration.FrameCodec;
                configuration.ProtocolCodec.Encode(frameCodec, protocol, sendQueue);
                while (!sendQueue.IsEmpty)
                {
                    if (sendQueue.TryDequeue(out var frame))
                    {
                        await tcpClient.GetStream().WriteAsync(frame.data, 0, frame.length);
                    }
                }
            }
            catch (Exception e)
            {
                Debug.LogError("exception on send. " + e.ToString());
                return TcpSendResult.Error;
            }

            return TcpSendResult.Ok;
        }
        


        private async Task StartReadLoop()
        {
            try
            {
                while (tcpClient.Connected)
                {
                    var readCount = await tcpClient.GetStream()
                        .ReadAsync(readBuffer, readOffset, readBuffer.Length - readOffset);
                    if (readCount > 0)
                    {
                        readOffset += readCount;
                        TryDecodeFrame();
                    }
                }
            }
            catch (Exception e)
            {
                await HandleReadLoopException(e);
            }
        }

        private async Task HandleReadLoopException(Exception exception)
        {
            Debug.LogError("exception on read loop. " + exception.ToString());
            Debug.LogError("try to close and re-reconnect ... ");
            await CloseAsync();
            await ConnectAsync();
        }

        private void TryDecodeFrame()
        {
            var codec = configuration.FrameCodec;
            var dispatcher = configuration.FrameDispatcher;
            var consumeCount = codec.DecodeToQueue(readBuffer, 0, readBuffer.Length, readQueue);
            if (consumeCount > 0)
            {
                // shift the buffer
                Buffer.BlockCopy(readBuffer, consumeCount, readBuffer, 0, readOffset - consumeCount);
                readOffset -= consumeCount;
            }


            while (!readQueue.IsEmpty)
            {
                if (readQueue.TryDequeue(out var tcpLengthBasedFrame))
                {
                    dispatcher.Dispatch(tcpLengthBasedFrame);
                }
            }
        }
    }
}