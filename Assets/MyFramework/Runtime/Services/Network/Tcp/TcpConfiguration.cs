using System;
using System.Linq.Expressions;
using System.Net;

namespace MyFramework.Services.Network.Tcp
{
    public class TcpConfiguration
    {
        public TimeSpan ConnectTimeout { get; protected set; }
        public TimeSpan SendTimeout { get; protected set; } // 只对同步方法有效
        public TimeSpan ReceiveTimeout { get; private set; } // 只对同步方法有效
        public bool NoDelay { get; protected set; }

        public IPEndPoint IpEndPoint { get; protected set; }
        public int FrameMaxSize { get; protected set; }
        public TcpLengthBasedFrameCodec FrameCodec { get; protected set; }
        public TcpProtocolCodec ProtocolCodec { get; protected set; }
        public TcpProtocolDispatcher ProtocolDispatcher { get; protected set; }

        public TcpConfiguration(string host, int port)
        {
            ConnectTimeout = TimeSpan.FromSeconds(10);
            SendTimeout = TimeSpan.Zero;
            ReceiveTimeout = TimeSpan.Zero;
            NoDelay = true;

            IpEndPoint = new IPEndPoint(IPAddress.Parse(host), port);
            FrameMaxSize = 1024;
            FrameCodec = new TcpLengthBasedFrameCodec(FrameMaxSize);
            ProtocolCodec = new TcpProtocolCodec();
            ProtocolDispatcher = new TcpProtocolDispatcher();

            new TcpProtocolRegisterHelper().Register(ProtocolCodec);
        }
    }
}