using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using UnityEngine.Networking;

namespace MyFramework.Services.Network.HTTP
{
    public enum HttpMethod
    {
        GET,
        POST,
    }

    public class HttpRequest : INetworkRequest
    {
        public virtual HttpMethod Method => HttpMethod.GET;
        public virtual string RequestPath => null;

        
        public virtual void SetRequestParam(IHttpRequestParam requestParam)
        {
        }

        public void OnCompleted(Action continuation)
        {
        }
    }
}