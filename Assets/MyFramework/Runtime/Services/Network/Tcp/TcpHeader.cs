using System;
using System.IO;
using System.Net;
using System.Runtime.CompilerServices;
using UnityEditor.MemoryProfiler;

namespace MyFramework.Services.Network.Tcp
{
    public class TcpHeader
    {
        /*
         * 以下是消息头的组成部分
         * 
         * (00-03) int32 length 表示消息的总长度 (包括了头部的 bytes)
         * (04-07) int32 sequence 消息的唯一标识符，由客户端生成，服务器返回同样的数即可
         * (08-11) int32 id 表示消息的唯一 id
         * (12-12) byte 总共被分割了几份  最大支持 128 份
         * (13-13) byte 当前被分割的标号 从 0 开始
         * 
         * 以上消息的概念可能不等同于一个 TcpRequest 的长度
         * 因为限制了单个发出去的消息的大小，所以这里对超过了限制大小的消息进行拆分
         */
        public const int headerSize = 14;
        public byte[] bytes { get; private set; }
        public int messageId { get; private set; }
        public int sequence { get; private set; }
        public int lengthWithHeader { get; private set; }
        public int currentPackId { get; private set; }
        public int totalPackCount { get; private set; }

        private TcpHeader()
        {
            bytes = new byte[headerSize];
        }


        public static TcpHeader Create(int sequence, int msgId, int lengthWithHeader, int currentPackId,
            int totalPackCount)
        {
            var tcpHeader = new TcpHeader()
            {
                messageId = msgId,
                sequence = sequence,
                lengthWithHeader = lengthWithHeader,
                currentPackId = currentPackId,
                totalPackCount = totalPackCount,
                bytes = new byte[headerSize],
            };

            using (var ms = new MemoryStream(tcpHeader.bytes))
            {
                using (var writer = new BinaryWriter(ms))
                {
                    writer.Write(IPAddress.HostToNetworkOrder(lengthWithHeader));
                    writer.Write(IPAddress.HostToNetworkOrder(sequence));
                    writer.Write(IPAddress.HostToNetworkOrder(msgId));
                    writer.Write((byte) totalPackCount);
                    writer.Write((byte) currentPackId);
                    writer.Flush();
                }
            }

            return tcpHeader;
        }

        public static TcpHeader Create(byte[] bytes)
        {
            if (bytes.Length < headerSize)
                return default;
            var tcpHeader = new TcpHeader();
            using (var ms = new MemoryStream(bytes))
            {
                using (var reader = new BinaryReader(ms))
                {
                    tcpHeader.lengthWithHeader = IPAddress.NetworkToHostOrder(reader.ReadInt32());
                    tcpHeader.sequence = IPAddress.NetworkToHostOrder(reader.ReadInt32());
                    tcpHeader.messageId = IPAddress.NetworkToHostOrder(reader.ReadInt32());
                    tcpHeader.totalPackCount = reader.ReadByte();
                    tcpHeader.currentPackId = reader.ReadByte();
                }
            }

            return tcpHeader;
        }
    }
}