using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public abstract class Presenter : IMvpFlow
    {
        protected View view { get; set; }
        protected MvpContext context;
        protected List<IDisposable> disposables = new List<IDisposable>();

        public virtual void OnCreated(MvpContext context)
        {
            context.MoveNextState();
        }

        public virtual void WillAppear()
        {
            context.MoveNextState();
        }

        public virtual void DidAppeared()
        {
        }

        public virtual void WillDisappear()
        {
            context.MoveNextState();
        }

        public virtual void DidDisappeared()
        {
            context.MoveNextState();
        }

        public void OnBackKey()
        {
            context.Back();
        }

        public virtual void Dispose()
        {
            if (view != null)
            {
                view.Dispose();
            }

            foreach (var disposable in disposables)
            {
                disposable.Dispose();
            }

            disposables.Clear();
        }

        public static void InstantiateView<T>(MvpContext context) where T : View
        {
            if (context == null)
            {
                Debug.LogError("InstantiateView error context is null");
            }
            else if (context.presenter == null)
            {
                Debug.LogError("InstantiateView error presenter is null");
            }
            else if (context.state != MvpContext.PresenterState.Initialize)
            {
                Debug.LogError("InstantiateView state not Initialize");
            }
            else
            {
                View.InstantiateView<T>(v =>
                {
                    if (v == null)
                    {
                        context.Abort($"InstantiateView failed, type: {typeof(T).FullName}");
                        return;
                    }
                    context.presenter.view = v;
                    context.presenter.context = context;
                    context.MoveNextState();
                });
            }
        }
    }
}