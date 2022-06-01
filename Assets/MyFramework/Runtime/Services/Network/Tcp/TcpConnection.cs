using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using UnityEngine;

namespace MyFramework.Services.Network.Tcp
{

    public class TcpConnection
    {
        private TcpClient tcpClient;
        private TcpCodec tcpCodec;
        private TcpStreamBuffer inputBuffer;
        private TcpStreamBuffer outputBuffer;
        private Task loopTask;

        public TcpConnection()
        {
            inputBuffer = new TcpStreamBuffer();
            outputBuffer = new TcpStreamBuffer();
            tcpCodec = new TcpCodec();
        }

        public async Task<bool> Connect(TcpConnectionBuilder builder)
        {
            if (tcpClient != null)
            {
                // Close before re-connect
                return false;
            }

            tcpClient = new TcpClient();
            tcpClient.NoDelay = true;
            await tcpClient.ConnectAsync(builder.host, builder.port);
            if (tcpClient.Connected)
            {
                StartLoop();
            }
            return tcpClient.Connected;
        }

        public async Task<T> SendRequestAsync<T>(TcpRequest<T> request) where T : TcpResponse
        {
            
        }

        private void StartLoop()
        {
            loopTask = Task.Run(() =>
            {
                while (true)
                {
                    if (tcpClient == null || tcpClient.Connected)
                        break;
                    var stream = tcpClient.GetStream();
                    inputBuffer.ReadFromNetworkStream(stream);
                    outputBuffer.WriteToNetworkStream(stream);
                    tcpCodec.
                }
            });
        }
    }
}