using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using Newtonsoft.Json;
using UnityEngine;

namespace MyFramework.Services.Network.Tcp
{
    public class TcpCodec
    {
        
        // byte
        // byte
        // id int32
        // length int32
        private const int headerSize = 10;
        private List<byte[]> EncodeInternal<T>(TcpRequest<T> request, byte[] body, int offset, int count)
            where T : TcpResponse
        {
            List<byte[]> bytesList;
            if (body.Length + headerSize > TcpStreamBuffer.SingleMessageSizeMax)
            {
                var splitCount = Mathf.CeilToInt(body.Length + headerSize / TcpStreamBuffer.SingleMessageSizeMax);
                bytesList = new List<byte[]>(splitCount);
                var position = offset;
                var maxRange = TcpStreamBuffer.SingleMessageSizeMax - headerSize;
                while (position >= body.Length - 1)
                {
                    var range = maxRange;
                    if (position + count > body.Length)
                    {
                        range = body.Length - (offset + count);
                    }
                    bytesList.Add(EncodeInternal<T>(request, body, position, range).First());
                    position += range;
                }
            }
            else
            {
                bytesList = new List<byte[]>(1);
                var header = new byte[headerSize];
                header[0] = 0;
                header[1] = 0;
            }
        }

        public List<byte[]> Encode<T>(TcpRequest<T> request) where T : TcpResponse
        {
            if (request == null)
                return null;
            var jsonStr = JsonConvert.SerializeObject(request);
            var body = Encoding.UTF8.GetBytes(jsonStr);
            var bytesList = EncodeInternal(request, body, 0, body.Length);
        }

        public T Decode<T>(byte[] bytes) where T : TcpResponse
        {
            if (bytes == null || bytes.Length == 0)
                return null;
            var jsonStr = Encoding.UTF8.GetString(bytes);
            var response = JsonConvert.DeserializeObject<T>(jsonStr);
            return response;
        }
    }
}