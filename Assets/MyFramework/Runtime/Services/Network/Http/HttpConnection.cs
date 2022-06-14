using System;
using System.Runtime.CompilerServices;
using System.Text;
using UnityEngine.Networking;

namespace MyFramework.Services.Network.HTTP
{
    public class HttpConnection : INotifyCompletion
    {
        private UnityWebRequest unityWebRequest;
        private UnityWebRequestAsyncOperation asyncOperation;
        private Action continuation;
        private HttpResponse httpResponse;

        public HttpConnection(UnityWebRequest request)
        {
            unityWebRequest = request;
            unityWebRequest.timeout = 30; // seconds
        }

        public static HttpConnection Create<T>(string protocol, string host, HttpRequest<T> httpRequest)
            where T : HttpResponse
        {
            UnityWebRequest unityWebRequest = null;
            string url = null;
            switch (httpRequest.Method)
            {
                case HttpMethod.GET:
                    var urlParam = new HttpUrlParam();
                    httpRequest.SetRequestParam(urlParam);
                    url = BuildUrl(protocol, host, httpRequest, urlParam);
                    unityWebRequest = new UnityWebRequest(url, "GET");
                    unityWebRequest.downloadHandler = new DownloadHandlerBuffer();
                    break;
                case HttpMethod.POST:
                    url = BuildUrl(protocol, host, httpRequest, null);
                    unityWebRequest = new UnityWebRequest(url, "POST");
                    var data = HttpCodec.Encode(httpRequest);
                    unityWebRequest.uploadHandler = new UploadHandlerRaw(data);
                    unityWebRequest.downloadHandler = new DownloadHandlerBuffer();
                    unityWebRequest.SetRequestHeader("Content-Type", "application/octet-stream");
                    break;
            }

            return new HttpConnection(unityWebRequest);
        }

        public HttpConnection Connect()
        {
            asyncOperation = unityWebRequest.SendWebRequest();
            asyncOperation.completed += ao =>
            {
                if (continuation != null)
                {
                    continuation();
                    continuation = null;
                }
            };
            return this;
        }

        private static string BuildUrl<T>(string protocol, string host, HttpRequest<T> request, IHttpRequestParam param)
            where T : HttpResponse
        {
            if (request.RequestPath.StartsWith("http"))
            {
                return request.RequestPath;
            }

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

        #region Awaiter

        public HttpConnection GetAwaiter()
        {
            return this;
        }

        public HttpConnection GetResult()
        {
            // if (IsCompleted)
            // {
            //     // return new HttpConnectionResult(unityWebRequest, this);
            // }

            // return HttpConnectionResult.NotCompleted;
            return this;
        }

        public bool IsCompleted => asyncOperation.isDone;

        public T GetResponse<T>() where T : HttpResponse
        {
            if (!IsCompleted)
                return null;
            if (httpResponse == null)
            {
                if (!string.IsNullOrWhiteSpace(unityWebRequest.error))
                {
                    httpResponse = HttpResponse.CreateFailedResponse<T>(unityWebRequest.responseCode, unityWebRequest.error);
                }
                else
                {
                    httpResponse = HttpCodec.Decode<T>(unityWebRequest.downloadHandler.data);
                }
            }

            return httpResponse as T;
        }

        public void OnCompleted(Action continuation)
        {
            if (IsCompleted)
            {
                continuation();
                return;
            }

            this.continuation = continuation;
        }

        #endregion
    }
}