using MyFramework.Runtime.Services.Network.HTTP;

namespace App.Network.Http
{
    public class BaiduHttpRequest : HttpRequest<BaiduHttpResponse>
    {
        public override HttpMethod Method => HttpMethod.GET;
        public override string RequestPath => "http://localhost:8080/test/get2";
    }

    public class BaiduHttpResponse : HttpResponse
    {
    }
}