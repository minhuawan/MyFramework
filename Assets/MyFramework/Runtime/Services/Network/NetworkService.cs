using System.Threading.Tasks;
using MyFramework.Services.Network.HTTP;
using MyFramework.Services.Network.Tcp;

namespace MyFramework.Services.Network
{
    public class NetworkService : AbstractService
    {
        public HttpNetworkHandler Http { get; private set; }
        public TcpSocketHandler Tcp { get; private set;  }

        public override void OnCreated()
        {
            Tcp = new TcpSocketHandler(new TcpConfiguration("localhost", 1234));
            Http = new HttpNetworkHandler();
        }

        public override void OnDestroy()
        {
        }
    }
}