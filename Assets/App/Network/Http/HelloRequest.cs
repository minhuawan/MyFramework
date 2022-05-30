using MyFramework.Services.Network.HTTP;

namespace App.Network.Http
{
    public class HelloRequest : HttpRequest
    {
        public override HttpMethod Method => HttpMethod.GET;
        public override string RequestPath => "/hello";
    }
}