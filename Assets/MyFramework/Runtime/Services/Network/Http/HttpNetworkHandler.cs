using System;
using System.Text;
using System.Threading.Tasks;
using UnityEngine;
using UnityEngine.Networking;

namespace MyFramework.Services.Network.HTTP
{
    public class HTTPNetworkHandler : INetworkHandler
    {
        public const string Protocol = "http";
        public const string Host = "localhost:8080";

        public async Task<T> SendAsync<T>(INetworkRequest request) where T : INetworkResponse
        {
            var httpRequest = request as HttpRequest;
            if (httpRequest == null)
                return await Task.FromException<T>(new Exception($"request not a http request type: {typeof(T)}"));

            if (string.IsNullOrWhiteSpace(httpRequest.RequestPath))
            {
                return await Task.FromException<T>(new Exception($"request path is empty type: {typeof(T)}"));
            }

            switch (httpRequest.Method)
            {
                case HttpMethod.GET: return await Get<T>(httpRequest);
                case HttpMethod.POST: return await Post<T>(httpRequest);
                default:
                    return await Task.FromException<T>(
                        new Exception($"unsupported http method {httpRequest.Method} type: {typeof(T)}"));
            }
        }

        private async Task<T> Get<T>(HttpRequest httpRequest) where T : INetworkResponse
        {
            var httpUrlParam = new HttpUrlParam();
            httpRequest.SetRequestParam(httpUrlParam);
            var url = BuildUrl(Protocol, Host, httpRequest, httpUrlParam);
            var unityWebRequest = UnityWebRequest.Get(url);
            var httpConnection = new HttpConnection(unityWebRequest);
            await httpConnection.Connect();
            var bytes = unityWebRequest.downloadHandler.data;
            var jsonStr = Encoding.UTF8.GetString(bytes);
            var response = JsonUtility.FromJson<T>(jsonStr);
            return response;
        }

        private async Task<T> Post<T>(HttpRequest httpRequest) where T : INetworkResponse
        {
            throw new NotImplementedException();
        }

        public static string BuildUrl(string protocol, string host, HttpRequest request, IHttpRequestParam param)
        {
            var sb = new StringBuilder(128);
            sb.Append(protocol);
            sb.Append("://");
            sb.Append(host);
            sb.Append(request.RequestPath);
            if (request.Method == HttpMethod.GET && param is HttpUrlParam httpUrlParam && httpUrlParam != null)
            {
                bool isFirst = true;
                foreach (var p in httpUrlParam.Query)
                {
                    sb.Append(isFirst ? '?' : '&');
                    sb.Append(p.Key);
                    sb.Append('=');
                    sb.Append(p.Value);
                    isFirst = false;
                }
            }

            return sb.ToString();
        }
    }
}