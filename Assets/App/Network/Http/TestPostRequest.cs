using MyFramework.Runtime.Services.Network.HTTP;

namespace App.Network.Http
{
    public class TestPostRequest : HttpRequest<TestPostResponse>
    {
        public override HttpMethod Method => HttpMethod.POST;
        public override string RequestPath => "/test/post";

        public override void SetRequestParam(IHttpRequestParam requestParam)
        {
            requestParam.Set("message", "post message");
        }
    }
}