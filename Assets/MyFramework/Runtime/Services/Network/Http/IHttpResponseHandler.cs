namespace MyFramework.Services.Network.HTTP
{
    public interface IHttpResponseHandler<T> where T : INetworkResponse
    {
        public void OnHttpResponse(T response);
    }
}