using System.Threading.Tasks;
using MyFramework.Services.Network.HTTP;

namespace MyFramework.Services.Network
{
    public class NetworkService : AbstractService
    {
        private HTTPNetworkHandler httpNetworkHandler;

        public override void OnCreated()
        {
            httpNetworkHandler = new HTTPNetworkHandler();
        }

        public override void OnDestroy()
        {
        }

        public async Task<T> Communicate<T>(HttpRequest httpRequest) where T : HttpResponse
        {
            var response = await httpNetworkHandler.SendAsync<T>(httpRequest);
            return response;
        }
    }
}