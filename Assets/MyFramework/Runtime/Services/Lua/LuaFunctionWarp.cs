using System;
using UnityEngine;
using XLua;

namespace MyFramework.Runtime.Services.Lua
{
    public struct ActionWrapVoid : IDisposable
    {
        public Action actionVoid;

        public static ActionWrapVoid Create(Action action)
        {
            var wrapper = new ActionWrapVoid();
            wrapper.actionVoid = action;
            return wrapper;
        }


        public void Dispose()
        {
        }
    }

    public struct ActionWarpGeneric<T>
    {
        public Action<T> action;
    }

    public struct ActionWarpGeneric<T, K>
    {
        public Action<T, K> action;
    }

    public struct ActionWarpGeneric<T, K, U>
    {
        public Action<T, K, U> action;
    }

    public struct ActionWarpGeneric<T, K, U, V>
    {
        public Action<T, K, U, V> action;
    }
}