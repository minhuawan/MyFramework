using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public abstract class View : MonoBehaviour, IDisposable
    {
        protected List<IDisposable> disposables;

        public T As<T>() where T : View
        {
            return this as T;
        }

        public virtual void Dispose()
        {
        }

        public static void InstantiateView<T>(Action<T> callback) where T : View
        {
            callback(null);
        }
    }
}