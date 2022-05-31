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

        public async Task<T> CommunicateAsync<T>(HttpRequest<T> httpRequest) where T : HttpResponse
        {
            var response = await httpNetworkHandler.SendAsync<T>(httpRequest);
            return response;
        }

        public void Communicate<T>(HttpRequest<T> httpRequest) where T : HttpResponse
        {
            _ = httpNetworkHandler.SendAsync<T>(httpRequest);
        }

        public void RegisterHttpResponseHandler<T>(IHttpResponseHandler<T> handler) where T : HttpResponse
        {
            httpNetworkHandler.RegisterHttpResponseHandler(handler);
        }
    }
}