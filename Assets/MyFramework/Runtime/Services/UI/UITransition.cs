using System;
using System.Security.Cryptography;
using System.Threading.Tasks;
using MyFramework.Services.Resource;
using UnityEditor;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public enum UITransitionResult
    {
        None,
        Ok,
        Exception,
    }

    public class UITransition : IDisposable
    {
        private UIPresenter previous;
        private UIPresenter next;

        private ResourceService resourceService => Application.GetService<ResourceService>();

        public UITransition(UIPresenter previous, UIPresenter next)
        {
            this.previous = previous;
            this.next = next;
        }

        public async Task<UITransitionResult> Run()
        {
            var result = UITransitionResult.None;
            var type = next.GetType();
            return result;
        }

        public void Dispose()
        {
        }

        // private
    }
}