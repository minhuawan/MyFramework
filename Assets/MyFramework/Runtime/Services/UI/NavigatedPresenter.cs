using System;
using System.Collections.Generic;
using App.Event.Navigation;

namespace MyFramework.Runtime.Services.UI
{
    public abstract class NavigatedPresenter : IDisposable
    {
        protected List<IDisposable> _disposables = new List<IDisposable>();
        public virtual NavigatedView View { get; protected set; }

        protected PresenterLocator locator;

        protected void DispatchNavigateResult(NavigateResult result)
        {
            new NavigateResultEvent(result, this, locator).Dispatch();
        }

        protected void NavigateOk()
        {
            DispatchNavigateResult(NavigateResult.Ok);
        }

        protected void NavigateWithCancel(string reason = null)
        {
            var navigateResult = NavigateResult.Canceled(reason);
            DispatchNavigateResult(navigateResult);
        }

        protected void NavigateWithException(Exception ex)
        {
            var exp = NavigateResult.Exception(ex);
            DispatchNavigateResult(exp);
        }

        public abstract void OnNavigate(PresenterLocator locator);

        protected void InstantiateViewAsyncWithNavigateResult<T>() where T : NavigatedView
        {
            NavigatedView.InstantiateViewAsync<T>(OnInstantiateView, NavigateWithException);
        }

        protected virtual void OnInstantiateView<T>(T view) where T : NavigatedView
        {
            View = view;
            NavigateOk();
        }

        public virtual void OnWillAppear()
        {
            View.OnWillAppear();
        }

        public virtual void OnDidAppear()
        {
            View.OnDidAppear();
        }

        public virtual void OnWillDisappear()
        {
            View.OnWillDisappear();
        }

        public virtual void OnBack()
        {
        }

        public void Dispose()
        {
            if (View != null)
            {
                View.Dispose();
            }

            foreach (var disposable in _disposables)
            {
                disposable.Dispose();
            }

            _disposables.Clear();
        }
    }
}