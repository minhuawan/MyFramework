using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;

namespace MyFramework.Services.Network.Tcp
{
    public class TcpProtocolCodec
    {
        public void Encode(TcpLengthBasedFrameCodec codec, TcpProtocol protocol, ConcurrentQueue<TcpLengthBasedFrame> frames)
        {
            var jsonStr = JsonConvert.SerializeObject(protocol);
            var bytes = Encoding.UTF8.GetBytes(jsonStr);
            codec.DecodeToQueue(bytes, 0, bytes.Length, frames);
        }
    }
}