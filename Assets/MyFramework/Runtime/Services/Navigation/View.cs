using System;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MyFramework.Runtime.Services.Navigation
{
    public abstract class View : MonoBehaviour, IDisposable
    {
        public virtual void Dispose()
        {
            Object.Destroy(gameObject);
        }
    }
}