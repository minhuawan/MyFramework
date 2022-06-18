namespace MyFramework.Runtime.Services.Network.HTTP
{
    public interface IHttpResponseHandler<T> where T : HttpResponse
    {
        public void OnHttpResponse(T response);
    }
}