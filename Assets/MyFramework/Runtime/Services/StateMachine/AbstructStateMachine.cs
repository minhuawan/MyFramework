using System;
using System.Collections.Generic;

namespace MyFramework.Runtime.Services.StateMachine
{
    public abstract class AbstructStateMachine : IDisposable
    {
        protected List<IDisposable> _disposables = new List<IDisposable>();
        public Type MachineType => this.GetType();
        public abstract void OnEnter(StateMachineContext context);
        public abstract void OnExit(StateMachineContext context);

        public virtual void Dispose()
        {
            foreach (var disposable in _disposables)
            {
                disposable.Dispose();
            }
            _disposables.Clear();
        }
    }
}