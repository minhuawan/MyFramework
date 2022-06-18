using MyFramework.Runtime.Services.Network.HTTP;
using MyFramework.Runtime.Services.Network.Tcp;
using MyFramework.Runtime.Services.Network.WebSocket;

namespace MyFramework.Runtime.Services.Network
{
    public class NetworkService : AbstractService
    {
        public HttpNetworkHandler Http { get; private set; }
        public TcpSocketHandler Tcp { get; private set; }

        public WebSocketHandler WebSocket { get; private set; }

        public override void OnCreated()
        {
            Tcp = new TcpSocketHandler(new TcpConfiguration("127.0.0.1", 1234));
            Http = new HttpNetworkHandler();
            WebSocket = new WebSocketHandler();
        }

        public override void OnDestroy()
        {
            WebSocket.Dispose();
            Tcp.Dispose();
            Http.Dispose();
        }
    }
}