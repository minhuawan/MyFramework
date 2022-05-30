using MyFramework.Services.Network.HTTP;

namespace App.Network.Http
{
    public class TestRequest : HttpRequest
    {
        public override HttpMethod Method => HttpMethod.GET;
        public override string RequestPath => "/test";
    }
}