using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using Newtonsoft.Json;

namespace MyFramework.Runtime.Services.Network.Tcp
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
        private Dictionary<int, Type> protocolTypes;

        public TcpProtocolCodec()
        {
            protocolTypes = new Dictionary<int, Type>();
        }

        public void RegisterProtocolType(int protocolId, Type t)
        {
            protocolTypes[protocolId] = t;
        }

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
            if (frames.IsEmpty)
                return null;
            var continueDecode = false;
            if (frames.TryPeek(out var first))
            {
                if (first.length < 12)
                    return null;

                var frameLengthSum = frames.Sum(f => f.length);
                using (var ms = new MemoryStream(first.data))
                {
                    using (var reader = new BinaryReader(ms))
                    {
                        var protocolLength = IPAddress.NetworkToHostOrder(reader.ReadInt32());
                        var protocolId = IPAddress.NetworkToHostOrder(reader.ReadInt32());
                        if (frameLengthSum < protocolLength)
                        {
                            return null;
                        }else if (!protocolTypes.ContainsKey(protocolId))
                        {
                            throw new Exception($"exception on protocol decode, protocol id not registered. [{protocolId}]");
                        }

                        continueDecode = true;
                    }
                }
            }

            if (continueDecode)
            {
                return DecodeInternal(frames);
            }

            return null;
        }

        private TcpProtocol DecodeInternal(ConcurrentQueue<TcpLengthBasedFrame> frames)
        {
            TcpProtocol protocol = null;
            var done = false;
            var protocolLength = 0;
            var protocolId = 0;
            var connectionId = 0;
            byte[] protocolData = null;
            int protocolDataOffset = 0;
            while (!done)
            {
                if (frames.IsEmpty)
                {
                    throw new Exception(
                        "exception on protocol decode internal, no enough frame to decode to protocol. ");
                }

                var first = true;
                if (frames.TryDequeue(out var frame))
                {
                    if (first)
                    {
                        first = false;
                        using (var ms = new MemoryStream(frame.data))
                        {
                            using (var reader = new BinaryReader(ms))
                            {
                                protocolLength = IPAddress.NetworkToHostOrder(reader.ReadInt32());
                                protocolId = IPAddress.NetworkToHostOrder(reader.ReadInt32());
                                connectionId = IPAddress.NetworkToHostOrder(reader.ReadInt32());
                                protocolData = new byte[protocolLength];
                                Buffer.BlockCopy(frame.data, 12, protocolData, 0, frame.length - 12);
                                protocolDataOffset += frame.length - 12;
                            }
                        }
                    }
                    else
                    {
                        Buffer.BlockCopy(frame.data, 0, protocolData, protocolDataOffset, frame.length);
                        protocolDataOffset += frame.length;
                    }

                    if (protocolDataOffset < protocolLength)
                    {
                        continue;
                    }

                    if (protocolDataOffset > protocolLength)
                    {
                        throw new Exception(
                            "exception on protocol decode internal, why protocolDataOffset bigger than protocolLength? ");
                    }

                    // equals
                    protocol = DecodeFromBytes(protocolId, connectionId, protocolData);
                    break;
                }
            }

            return protocol;
        }

        private TcpProtocol DecodeFromBytes(int protocolId, int connectionId, byte[] bytes)
        {
            if (protocolTypes.TryGetValue(protocolId, out var type))
            {
                var jsonStr = Encoding.UTF8.GetString(bytes);
                return JsonConvert.DeserializeObject(jsonStr, type) as TcpProtocol;
            }

            return null;
        }
    }
}