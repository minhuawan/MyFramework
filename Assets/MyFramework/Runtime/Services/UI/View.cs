using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public abstract class View : MonoBehaviour, IDisposable
    {
        public virtual void Dispose()
        {
            // todo destroy this view
        }
    }
}