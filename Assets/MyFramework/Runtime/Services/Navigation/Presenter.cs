using System;
using System.Collections.Generic;

namespace MyFramework.Runtime.Services.Navigation
{
    public abstract class Presenter : INavigable, IDisposable
    {
        protected NavigationContext context { get; private set; }
        protected List<IDisposable> disposables = new List<IDisposable>();

        public void OnCreate(NavigationContext context)
        {
            this.context = context;
        }

        public virtual void OnNavigate()
        {
            context.MoveNext();
        }

        public virtual void OnAppear()
        {
            context.MoveNext();
        }

        public virtual void OnDidAppear()
        {
            context.MoveNext();
        }

        public virtual void OnDisappear()
        {
            context.MoveNext();
        }

        public virtual void OnLeave()
        {
        }

        public virtual void Dispose()
        {
            foreach (var disposable in disposables)
            {
                disposable.Dispose();
            }

            disposables.Clear();
        }
    }
}