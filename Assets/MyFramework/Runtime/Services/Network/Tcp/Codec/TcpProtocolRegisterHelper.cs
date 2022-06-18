using App.Network.Tcp;

namespace MyFramework.Runtime.Services.Network.Tcp
{
    public class TcpProtocolRegisterHelper
    {
        public void Register(TcpProtocolCodec codec)
        {
            codec.RegisterProtocolType(1, typeof(TestTcpRequest));
            codec.RegisterProtocolType(2, typeof(TestTcpResponse));
        }
    }
}