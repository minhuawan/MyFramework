using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using MyFramework.Services.StateMachine;

namespace MyFramework.Services.UI
{
    public abstract class Presenter : IDisposable
    {
        private bool _disposed;
        public bool Disposed => _disposed;
        protected List<IDisposable> _disposables = new List<IDisposable>();

        public abstract Task<UITransitionResult> LoadAsync(View view);

        public virtual void OnBackKey()
        {
        }

        public virtual void OnDidAppear()
        {
        }

        public virtual void Dispose()
        {
            _disposed = true;
            foreach (var disposable in _disposables)
            {
                disposable.Dispose();
            }

            _disposables.Clear();
        }
    }
}