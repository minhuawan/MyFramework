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
        protected abstract View View { get; }
        private bool _disposed;
        public bool Disposed => _disposed;
        protected List<IDisposable> _disposables = new List<IDisposable>();

        public abstract Task<TransitionResult> LoadAsync();

        public virtual void OnBackKey()
        {
        }

        public virtual void OnDidAppear()
        {
        }

        public virtual void Dispose()
        {
            foreach (var disposable in _disposables)
            {
                disposable.Dispose();
            }

            _disposables.Clear();
            if (View != null)
            {
                View.Dispose();
            }

            _disposed = true;
        }
    }
}