using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public abstract class Presenter : IMvpFlow
    {
        private bool inViewLoadAsync = false;
        protected View view { get; set; }
        protected MvpContext context;
        protected List<IDisposable> disposables = new List<IDisposable>();

        public virtual void Initialize(MvpContext context)
        {
        }

        public virtual void DidAppeared()
        {
            if (view != null && !view.gameObject.activeSelf)
            {
                view.gameObject.SetActive(true);
            }
        }

        public virtual void OnBackKey()
        {
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

        public static void InstantiateViewAsync<T>(MvpContext context, Action<T> action) where T : View
        {
            if (context == null)
            {
                Debug.LogError("InstantiateView error context is null");
                action(null);
            }
            else if (context.presenter == null)
            {
                Debug.LogError("InstantiateView error presenter is null");
                action(null);
            }
            else if (context.state != MvpContext.PresenterState.Initialize)
            {
                Debug.LogError("InstantiateView state not Initialize");
                action(null);
            }
            else if (context.presenter.inViewLoadAsync)
            {
                Debug.LogError("InstantiateView error presenter inViewLoadAsync is True");
                action(null);
            }
            else
            {
                context.presenter.context = context;
                context.presenter.inViewLoadAsync = true;
                View.InstantiateView<T>(v =>
                {
                    context.presenter.inViewLoadAsync = false;
                    if (v == null)
                    {
                        action(null);
                        context.Abort($"InstantiateView failed, type: {typeof(T).FullName}");
                        return;
                    }

                    v.gameObject.SetActive(false); // inactive first, active on DidAppeared method
                    context.presenter.view = v;
                    action(v);
                });
            }
        }
    }
}