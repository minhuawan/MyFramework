using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using MyFramework.Runtime.Services.BusyMask;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

namespace MyFramework.Runtime.Services.Network.HTTP
{
    public class HttpNetworkHandler : IDisposable
    {
        public const string Protocol = "http";
        public const string Host = "localhost:5000";

        private Dictionary<Type, object> responseHandlers =
            new Dictionary<Type, object>();

        public async Task<T> SendAsync<T>(HttpRequest<T> request) where T : HttpResponse
        {
            try
            {
                var httpRequest = request as HttpRequest<T>;
                if (httpRequest == null)
                    return await Task.FromException<T>(new Exception($"request not a http request type: {typeof(T)}"));

                if (string.IsNullOrWhiteSpace(httpRequest.RequestPath))
                {
                    return await Task.FromException<T>(new Exception($"request path is empty type: {typeof(T)}"));
                }

                T response;
                switch (httpRequest.Method)
                {
                    case HttpMethod.GET:
                    case HttpMethod.POST:
                        var httpConnection = HttpConnection.Create(Protocol, Host, httpRequest);
                        await httpConnection.Connect();
                        response = httpConnection.GetResponse<T>();
                        break;
                    default:
                        return await Task.FromException<T>(
                            new Exception($"unsupported http method {httpRequest.Method} type: {typeof(T)}"));
                }

                if (response == null)
                {
                    return await Task.FromException<T>(new Exception($"response is null, type: {typeof(T)}"));
                }

                var key = typeof(T);
                if (response != null && responseHandlers.TryGetValue(key, out var handler))
                {
                    var responseHandler = handler as IHttpResponseHandler<T>;
                    if (responseHandler != null)
                    {
                        responseHandler.OnHttpResponse(response);
                    }
                }

                return response;
            }
            catch (Exception e)
            {
                return HttpResponse.CreateFailedResponse<T>(500, e.ToString());
            }
        }

        public async Task<T> SendAsyncWithBusyMask<T>(HttpRequest<T> request) where T: HttpResponse
        {
            Application.GetService<BusyMaskService>().RetainBusy();
            var httpResponse = await SendAsync<T>(request);
            Application.GetService<BusyMaskService>().ReleaseBusy();
            return httpResponse;
        }

        public void RegisterHttpResponseHandler<T>(IHttpResponseHandler<T> handler) where T : HttpResponse
        {
            responseHandlers[typeof(T)] = handler;
        }
        public void UnregisterHttpResponseHandler<T>(IHttpResponseHandler<T> handler) where T : HttpResponse
        {
            var key = typeof(T);
            if (responseHandlers.ContainsKey(key))
            {
                responseHandlers.Remove(key);
            }
        }

        public void Dispose()
        {
        }
    }
}