using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Net;
using System.Text;
using Newtonsoft.Json;

namespace MyFramework.Services.Network.Tcp
{
    /*                   1                   2                   3   (32bit) 
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * ----------------------------------------------------------------
     * |                     LENGTH OF PROTOCOL                       |  4 byte
     * ----------------------------------------------------------------
     * |                       ID OF PROTOCOL                         |  4 byte
     * ----------------------------------------------------------------
     * |                 CONNECTION ID OF PROTOCOL                    |  4 byte  
     * ----------------------------------------------------------------
     * |                                                              |
     * |                                                              |
     * |                       PROTOCOL DATA                          |
     * |                                                              |
     * |                                                              |
     * ----------------------------------------------------------------
     *
     * CONNECTION ID OF PROTOCOL
     * (auto increased)  由发送端生成，响应端返回相同数值即可， 主要是用来做相同协议发送多次的区分使用
     *
     */
    public class TcpProtocolCodec
    {
        private int increaseConnId;

        public void Encode(
            TcpLengthBasedFrameCodec codec,
            TcpProtocol protocol,
            ConcurrentQueue<TcpLengthBasedFrame> frames)
        {
            var jsonStr = JsonConvert.SerializeObject(protocol);
            var data = Encoding.UTF8.GetBytes(jsonStr);
            var dataLength = data.Length;
            var bDataLength = BitConverter.GetBytes(IPAddress.HostToNetworkOrder(dataLength));
            var bConnId = BitConverter.GetBytes(IPAddress.HostToNetworkOrder(increaseConnId++));
            var bPid = BitConverter.GetBytes(IPAddress.HostToNetworkOrder(protocol.protocolId));
            var bytes = new byte[12 + dataLength];
            Buffer.BlockCopy(bDataLength, 0, bytes, 0, 4);
            Buffer.BlockCopy(bPid, 0, bytes, 4, 4);
            Buffer.BlockCopy(bConnId, 0, bytes, 8, 4);
            Buffer.BlockCopy(data, 0, bytes, 12, data.Length);
            codec.DecodeToQueue(bytes, 0, bytes.Length, frames);
        }

        public TcpProtocol Decode(ConcurrentQueue<TcpLengthBasedFrame> frames)
        {
            return null; // todo
        }
    }
}