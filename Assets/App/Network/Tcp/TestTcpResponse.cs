using MyFramework.Runtime.Services.Network.Tcp;

namespace App.Network.Tcp
{
    public class TestTcpResponse : TcpResponse
    {
        public override int protocolId => 2;
    }
}