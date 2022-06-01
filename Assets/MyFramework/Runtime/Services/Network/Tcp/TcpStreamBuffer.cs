using System;
using System.Net.Sockets;

namespace MyFramework.Services.Network.Tcp
{
    public class TcpStreamBuffer
    {
        public const int SingleMessageSizeMax = capacity / 2;
        private const int capacity = 1024 * 256; // 256KB 单个消息应该小于 128KB
        private byte[] data;

        private int dataLength;

        public int DataLength => dataLength;

        public TcpStreamBuffer()
        {
            data = new byte[capacity];
        }

        public bool Put(byte[] putBytes)
        {
            if (putBytes.Length + dataLength > capacity)
                return false;

            Buffer.BlockCopy(putBytes, 0, data, dataLength, putBytes.Length);
            dataLength += putBytes.Length;
            return true;
        }

        public bool Get(int offset, int count, out byte[] bytes)
        {
            bytes = null;
            if (offset < 0 || count <= 0)
                return false;
            if (offset + count > dataLength)
                return false;

            bytes = new byte[count];
            Buffer.BlockCopy(data, offset, bytes, 0, count);

            return true;
        }

        public void Erase(int begin, int eraseCount, out bool ok)
        {
            if (begin < 0 || eraseCount <= 0)
                return;
            if (begin + eraseCount > dataLength)
                return;

            var srcOffset = begin + eraseCount + 1;
            var dstOffset = begin;
            var count = dataLength - srcOffset;
            if (count > 0)
            {
                Buffer.BlockCopy(data, srcOffset, data, dstOffset, count);
            }

            dataLength -= eraseCount;
            ok = true;
        }

        public void ReadFromNetworkStream(NetworkStream ns)
        {
            if (ns == null || ns.Length == 0)
                return;
            var size = capacity - dataLength;
            var readCount = ns.Read(data, dataLength, size);
            if (readCount > 0)
            {
                dataLength += readCount;
            }
        }

        public void WriteToNetworkStream(NetworkStream ns)
        {
            if (ns == null)
                return;
            if (dataLength == 0)
                return;

            ns.Write(data, 0, dataLength);
            dataLength = 0;
        }

        public void Cleanup()
        {
            data = new byte[capacity];
            dataLength = 0;
        }
    }
}