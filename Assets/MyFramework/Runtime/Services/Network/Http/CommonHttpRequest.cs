using System.Collections.Generic;
using UnityEngine.Playables;

namespace MyFramework.Runtime.Services.Network.HTTP
{
    public class CommonHttpRequest : HttpRequest<CommonHttpResponse>
    {
        public override HttpMethod Method => method;
        public override string RequestPath => path;

        private HttpMethod method;
        private string path;
        private Dictionary<string, string> param;

        public override void SetRequestParam(IHttpRequestParam requestParam)
        {
            base.SetRequestParam(requestParam);
            if (param != null)
            {
                foreach (var pair in param)
                {
                    if (pair.Key != null && pair.Value != null)
                    {
                        requestParam.Set(pair.Key, pair.Value);
                    }
                }
            }
        }

        public static CommonHttpRequest Create(HttpMethod method, string path, Dictionary<string, string> param = null)
        {
            var request = new CommonHttpRequest();
            request.method = method;
            request.path = path;
            request.param = param;
            return request;
        }

        public static CommonHttpRequest GET(string path, Dictionary<string, string> param = null)
        {
            return Create(HttpMethod.GET, path, param);
        }

        public static CommonHttpRequest POST(string path, Dictionary<string, string> param = null)
        {
            return Create(HttpMethod.POST, path, param);
        }
    }
}