using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Net;

namespace MyFramework.Services.Network.Tcp
{
    /*                   1                   2                   3   (32bit) 
     *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     * ----------------------------------------------------------------
     * |                     LENGTH OF FRAME                          |  4 byte
     * ----------------------------------------------------------------
     * |                                                              |
     * |                                                              |
     * |                       FRAME DATA                             |
     * |                                                              |
     * |                                                              |
     * ----------------------------------------------------------------
     * 
     */
    public class TcpLengthBasedFrameCodec
    {
        private readonly int tcpFrameMaxSize;

        public TcpLengthBasedFrameCodec(int maxSize)
        {
            tcpFrameMaxSize = maxSize;
        }

        public byte[] Encode(TcpLengthBasedFrame frame)
        {
            return frame.data;
        }


        public int DecodeToQueue(byte[] bytes, int offset, int count, ConcurrentQueue<TcpLengthBasedFrame> frames)
        {
            if (bytes.Length < 4)
                return 0;

            var usedCount = 0;
            var validLength = count;
            var position = offset;
            while (position < validLength - 1)
            {
                var frameSize = Math.Min(tcpFrameMaxSize, validLength - position);
                position += frameSize;
                var data = new byte[frameSize + 4];
                var lenBytes = BitConverter.GetBytes(IPAddress.HostToNetworkOrder(frameSize));
                Buffer.BlockCopy(lenBytes, 0, data, 0, lenBytes.Length);
                Buffer.BlockCopy(bytes, position, data, 4, frameSize);
                var frame = new TcpLengthBasedFrame(data);
                frames.Enqueue(frame);
                usedCount += frameSize;
            }

            return usedCount;
        }
    }
}