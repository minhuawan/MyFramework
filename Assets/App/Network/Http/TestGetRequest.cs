using MyFramework.Runtime.Services.Network.HTTP;

namespace App.Network.Http
{
    public class TestGetRequest : HttpRequest<TestGetResponse>
    {
        public override HttpMethod Method => HttpMethod.GET;
        public override string RequestPath => "/test/get";
    }
}