using System;
using Newtonsoft.Json;
using UnityEngine;

namespace MyFramework.Runtime.Services.Network.HTTP
{
    public class HttpCodec
    {
        public static Type CommonType = typeof(CommonHttpResponse);
        public static byte[] Encode<T>(HttpRequest<T> httpRequest) where T : HttpResponse
        {
            if (httpRequest.Method == HttpMethod.POST)
            {
                var httpPostParam = new HttpPostParam();
                httpRequest.SetRequestParam(httpPostParam);
                var bytes = httpPostParam.data;
                return bytes;
            }

            return null;
        }

        public static T Decode<T>(byte[] bytes) where T : HttpResponse
        {
            var jsonStr = System.Text.Encoding.UTF8.GetString(bytes);
            var response = JsonConvert.DeserializeObject<T>(jsonStr);
            return response;
        }
    }
}