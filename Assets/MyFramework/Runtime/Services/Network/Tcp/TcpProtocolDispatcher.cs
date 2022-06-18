using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Reflection;
using UnityEngine;

namespace MyFramework.Runtime.Services.Network.Tcp
{
    public class TcpProtocolDispatcher
    {
        private ConcurrentDictionary<Type, List<object>> protocolHandlers;
        private ConcurrentDictionary<Type, MethodInfo> methodInfoCache;

        public TcpProtocolDispatcher()
        {
            protocolHandlers = new ConcurrentDictionary<Type, List<object>>();
            methodInfoCache = new ConcurrentDictionary<Type, MethodInfo>();
        }

        public void Dispatch(TcpProtocol tcpProtocol)
        {
            var type = tcpProtocol.GetType();
            if (protocolHandlers.TryGetValue(type, out var handlers))
            {
                foreach (var handler in handlers)
                {
                    try
                    {
                        MethodInfo method = null;
                        if (!methodInfoCache.TryGetValue(type, out method))
                        {
                            var genericType = typeof(ITcpProtocolHandler<>).MakeGenericType(type);
                            method = genericType.GetMethod("OnTcpProtocol",
                                BindingFlags.Instance | BindingFlags.Public);
                            methodInfoCache.TryAdd(type, method);
                        }

                        method.Invoke(handler, new[] {tcpProtocol});
                    }
                    catch (Exception e)
                    {
                        Debug.LogError($"catch a exception on dispatch protocol handle type: {type}. " + e.ToString());
                    }
                }
            }
        }

        public void RegisterTcpProtocolHandler<T>(ITcpProtocolHandler<T> protocolHandler) where T : TcpProtocol
        {
            var type = typeof(T);
            if (!protocolHandlers.ContainsKey(type))
            {
                protocolHandlers[type] = new List<object>();
            }

            var handlers = protocolHandlers[type];
            if (!handlers.Contains(protocolHandler))
            {
                handlers.Add(protocolHandler);
            }
        }

        public void UnregisterTcpProtocolHandler<T>(ITcpProtocolHandler<T> handler) where T : TcpProtocol
        {
            var type = typeof(T);
            if (!protocolHandlers.ContainsKey(type))
                return;
            protocolHandlers[type].Remove(handler);
        }
    }
}