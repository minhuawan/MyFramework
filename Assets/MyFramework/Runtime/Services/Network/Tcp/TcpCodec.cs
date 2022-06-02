using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Runtime.CompilerServices;
using System.Text;
using System.Text.RegularExpressions;
using Newtonsoft.Json;
using UnityEngine;

namespace MyFramework.Services.Network.Tcp
{
    public class TcpCodec
    {
        /*
         * 以下是消息头的组成部分，由 2 * byte + 2 * int 组成
         * 
         * (0-3) int32 length 表示消息的总长度 (包括了头部的 10 * byte)
         * (4-7) int32 id 表示消息的唯一 id
         * (8-8) byte 总共被分割了几份  最大支持 128 份
         * (9-9) byte 当前被分割的标号 从 0 开始
         * 
         * 以上消息的概念可能不等同于一个 TcpRequest 的长度
         * 因为限制了单个发出去的消息的大小，所以这里对超过了限制大小的消息进行拆分
         */
        private static int tcpSequence;
        private ConcurrentQueue<TcpHeader> waitingBodyHeaders = new ConcurrentQueue<TcpHeader>();
        private ConcurrentDictionary<int, Type> wellKnownTypeDict = new ConcurrentDictionary<int, Type>();

        private List<byte[]> EncodeInternal<T>(TcpRequest<T> request)
            where T : TcpResponse
        {
            var jsonStr = JsonConvert.SerializeObject(request);
            var body = Encoding.UTF8.GetBytes(jsonStr);

            var singleMaxLen = TcpHeader.headerSize + TcpStreamBuffer.SingleMessageSizeMax;
            var splitCount = (int) Mathf.Ceil(body.Length / singleMaxLen);
            var list = new List<byte[]>(splitCount);
            var position = 0;
            var totalLength = body.Length;
            while (position < totalLength - 1)
            {
                var remain = totalLength - position;
                var lengthWithHeader = remain >= singleMaxLen ? singleMaxLen : remain + TcpHeader.headerSize;
                var bytes = new byte[lengthWithHeader];
                var currentPackId = list.Count;
                var tcpHeader = TcpHeader.Create(
                    tcpSequence++,
                    request.MessageId,
                    lengthWithHeader,
                    currentPackId,
                    splitCount
                );
                Buffer.BlockCopy(tcpHeader.bytes, 0, bytes, 0, lengthWithHeader);
                Buffer.BlockCopy(body, position, bytes, TcpHeader.headerSize, lengthWithHeader);
                list.Add(bytes);
                position += lengthWithHeader;
            }

            return list;
        }

        public List<byte[]> Encode<T>(TcpRequest<T> request) where T : TcpResponse
        {
            if (request == null)
                return null;
            try
            {
                wellKnownTypeDict[request.MessageId] = typeof(T);
                var bytesList = EncodeInternal(request);
                return bytesList;
            }
            catch (Exception e)
            {
                Debug.LogError(e);
            }

            return null;
        }

        public List<TcpResponse> TryDecode(TcpStreamBuffer tcpStreamBuffer)
        {
            if (tcpStreamBuffer.DataLength < TcpHeader.headerSize)
                return null;

            var result = new List<TcpResponse>();
            while (!waitingBodyHeaders.IsEmpty)
            {
                if (waitingBodyHeaders.TryPeek(out var waitingHeader))
                {
                    if (tcpStreamBuffer.DataLength < waitingHeader.lengthWithHeader)
                    {
                        return null; // not enough data to get response
                    }

                    if (waitingBodyHeaders.TryDequeue(out waitingHeader))
                    {
                        result.Add(DecodeInternal(tcpStreamBuffer, waitingHeader));
                    }
                }
            }

            tcpStreamBuffer.Get(0, TcpHeader.headerSize, out var headerBytes);
            var tcpHeader = TcpHeader.Create(headerBytes);
            if (tcpStreamBuffer.DataLength < tcpHeader.lengthWithHeader)
            {
                waitingBodyHeaders.Enqueue(tcpHeader);
                return result;
            }

            result.Add(DecodeInternal(tcpStreamBuffer, tcpHeader));
            return result;
        }

        private TcpResponse DecodeInternal(TcpStreamBuffer tcpStreamBuffer, TcpHeader tcpHeader)
        {
            var responseType = wellKnownTypeDict[tcpHeader.messageId];
            var offset = TcpHeader.headerSize;
            var bodySize = tcpHeader.lengthWithHeader - TcpHeader.headerSize;
            tcpStreamBuffer.Get(offset, bodySize, out var bodyBytes);
            tcpStreamBuffer.Erase(0, tcpHeader.lengthWithHeader);
            var jsonStr = Encoding.UTF8.GetString(bodyBytes);
            var response = JsonConvert.DeserializeObject(jsonStr, responseType);
            return response as TcpResponse;
        }
    }
}