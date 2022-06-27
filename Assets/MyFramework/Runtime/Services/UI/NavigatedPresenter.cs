using System;
using System.Collections.Generic;
using MyFramework.Runtime.Services.Event.UI;
using MyFramework.Runtime.Services.Localization;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI
{
    public abstract class NavigatedPresenter : IDisposable
    {
        protected List<IDisposable> _disposables = new List<IDisposable>();
        public virtual NavigatedView View { get; protected set; }

        protected PresenterLocator locator;

        protected string requiredLocalizeSpace;

        public virtual bool IsDialog => false;

        protected void DispatchNavigateResult(NavigateResult result)
        {
            new NavigateResultEvent(result, this, locator, IsDialog).Dispatch();
        }

        protected void NavigateOk()
        {
            if (!string.IsNullOrEmpty(requiredLocalizeSpace))
            {
                var localizationService = Application.GetService<LocalizationService>();
                localizationService.MountTextSpace(requiredLocalizeSpace);
            }

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
            if (view != null)
            {
                var provider = view.GetComponent<NavigatedViewLocalizeSpaceProvider>();
                if (provider != null)
                {
                    requiredLocalizeSpace = provider.Space;
                }
            }

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
            Application.GetService<UIService>().NavigatedBack();
        }

        protected string LocalizeText(string key)
        {
            if (requiredLocalizeSpace == null)
            {
                Debug.LogError($"Localize text failed, current view no space required {this.GetType().FullName}");
                return key;
            }
            var localizationService = Application.GetService<LocalizationService>();
            return localizationService.Translate(requiredLocalizeSpace, key);
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

            if (!string.IsNullOrEmpty(requiredLocalizeSpace))
            {
                var localizationService = Application.GetService<LocalizationService>();
                localizationService.UnmountTextSpace(requiredLocalizeSpace);
            }
        }
    }
}