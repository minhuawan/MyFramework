using System;
using System.Runtime.CompilerServices;
using UnityEngine;
using UnityEngine.Networking;

namespace MyFramework.Services.Network.HTTP
{
    public class HttpConnection : INotifyCompletion
    {
        private UnityWebRequest unityWebRequest;
        private UnityWebRequestAsyncOperation asyncOperation;
        private Action continuation;

        public HttpConnection(UnityWebRequest request)
        {
            unityWebRequest = request;
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

        #region Awaiter

        public HttpConnection GetAwaiter()
        {
            return this;
        }

        public HttpConnection GetResult()
        {
            return this;
        }

        public bool IsCompleted => asyncOperation.isDone;

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