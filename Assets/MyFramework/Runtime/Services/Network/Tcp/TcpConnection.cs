using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Threading.Tasks;
using MyFramework.Services.Resource;
using UnityEngine;

namespace MyFramework.Services.Network.Tcp
{
    public class TcpConnection
    {
        private TcpClient tcpClient;
        private TcpCodec tcpCodec;
        private TcpStreamBuffer inputBuffer;
        private Task loopTask;
        private ConcurrentQueue<byte[]> pendingSendDataQueue;
        private List<TcpResponse> pendingProcessResponse;

        public TcpConnection()
        {
            pendingSendDataQueue = new ConcurrentQueue<byte[]>();
            inputBuffer = new TcpStreamBuffer();
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
            tcpClient.ReceiveBufferSize = 0;
            await tcpClient.ConnectAsync(builder.host, builder.port);
            if (tcpClient.Connected)
            {
                StartLoop();
            }

            return tcpClient.Connected;
        }

        public async Task<T> SendRequestAsync<T>(TcpRequest<T> request) where T : TcpResponse
        {
            var list = tcpCodec.Encode(request);
            if (list == null || list.Count == 0)
                return null; // todo fallback

            foreach (var element in list)
            {
                pendingSendDataQueue.Enqueue(element);
            }

            T tcpResponse = null;
            // todo 这里有许多问题：
            // 1、 超时了，那就会一直等待
            // 2、 可能会开过多的线程
            // 3、 20毫秒总不是一个好的方案
            // 方向： 可能需要通过熟练应用 task 模块来做这个吧
            await Task.Run(() =>
            {
                while (true)
                {
                    Thread.Sleep(20); // 每过 20 毫秒再检查一次 如果要求实时性非常高，可能需要修改这个时间
                    lock (pendingProcessResponse)
                    {
                        if (pendingProcessResponse == null)
                            continue;
                        var index = -1;
                        for (var i = 0; i < pendingProcessResponse.Count; i++)
                        {
                            if (pendingProcessResponse[i] != null &&
                                pendingProcessResponse[i].messageId == request.MessageId)
                            {
                                index = i;
                                break;
                            }
                        }

                        if (index >= 0)
                        {
                            tcpResponse = pendingProcessResponse[index] as T;
                            pendingProcessResponse.RemoveAt(index);
                            break;
                        }
                    }
                }
            });
            return tcpResponse;
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
                    // 1. read
                    if (inputBuffer.ReadFromNetworkStream(stream) > 0)
                    {
                        // check read data
                        TryDecodeResponse();
                    }

                    ;
                    // 2. write
                    if (pendingSendDataQueue.TryDequeue(out var buffer))
                    {
                        stream.Write(buffer, 0, buffer.Length);
                    }
                }
            });
        }

        private void TryDecodeResponse()
        {
            pendingProcessResponse = tcpCodec.TryDecode(inputBuffer);
        }
    }
}