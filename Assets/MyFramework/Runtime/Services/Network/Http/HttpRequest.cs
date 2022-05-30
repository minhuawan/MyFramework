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

    public abstract class HttpRequest : INetworkRequest
    {
        public abstract HttpMethod Method { get; }
        public abstract string RequestPath { get; }

        
        public virtual void SetRequestParam(IHttpRequestParam requestParam)
        {
        }

        public void OnCompleted(Action continuation)
        {
        }
    }
}