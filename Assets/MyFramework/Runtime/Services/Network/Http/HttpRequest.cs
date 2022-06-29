using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using UnityEngine.Networking;

namespace MyFramework.Runtime.Services.Network.HTTP
{
    public enum HttpMethod
    {
        GET,
        POST,
    }

    public enum HttpResponseType
    {
        Json,
        PlainText,
    }

    public abstract class HttpRequest<T> where T : HttpResponse
    {
        public abstract HttpMethod Method { get; }
        public abstract string RequestPath { get; }
        public virtual HttpResponseType ResponseType => HttpResponseType.Json;


        public virtual void SetRequestParam(IHttpRequestParam requestParam)
        {
        }

        public void OnCompleted(Action continuation)
        {
        }
    }
}