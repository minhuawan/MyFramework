using MyFramework.Services.Network.Tcp;

namespace App.Network.Tcp
{
    public class TestTcpRequest : TcpRequest<TestTcpResponse>
    {
        public override int protocolId => 1;
        public string message = "hello";
    }
}