using System;
using System.Collections.Generic;
using App.UI.Presenters;
using MyFramework.Runtime.Services.BusyMask;
using MyFramework.Runtime.Services.Event;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI
{
    public class UIService : AbstractService
    {
        private List<IDisposable> disposables;
        private BaseLocatorProcessor navigatedProcessor;
        private BaseLocatorProcessor dialogProcessor;
        private PresenterLocator errorNoticeLocator;
        public override void OnCreated()
        {
            errorNoticeLocator = PresenterLocator.Create<ErrorNoticeDialogPresenter>();
            disposables = new List<IDisposable>();
        }

        public override void Initialize()
        {
            navigatedProcessor = new NavigateLocatorProcessor(NavigationResultListener);
            dialogProcessor = new DialogLocatorProcessor(NavigationResultListener);
            Application.GetService<EventService>().Subscribe<BackKeyEvent>(OnBackKey).AddTo(disposables);
        }

        private void NavigationResultListener(PresenterLocator locator, NavigateResult result)
        {
            Application.GetService<BusyMaskService>().ReleaseBusy();
            if (locator == null || locator == errorNoticeLocator)
                return;
            var message = string.Empty;
            switch (result.Type)
            {
                case NavigateResult.ResultType.Ok:
                    break;
                case NavigateResult.ResultType.Canceled:
                    message = result.Message;
                    break;
                case NavigateResult.ResultType.Failed:
                case NavigateResult.ResultType.Exception:
                case NavigateResult.ResultType.None:
                    if (!string.IsNullOrEmpty(result.Message))
                    {
                        message = result.Message;
                    }
                    else
                    {
                        // message = result.InnerException.ToString();
                        message = "Some exception happened, please try again.";
                    }

                    break;
            }

            if (!string.IsNullOrEmpty(message))
            {
                errorNoticeLocator.Parameters.Put("message", message);
                dialogProcessor.Process(errorNoticeLocator);
            }
        }

        private void OnBackKey(BackKeyEvent obj)
        {
            if (dialogProcessor.DisplayedPresenter != null)
            {
                dialogProcessor.OnBackKey();
            }
            else if (navigatedProcessor.DisplayedPresenter != null)
            {
                navigatedProcessor.OnBackKey();
            }
        }


        public override void OnDestroy()
        {
            dialogProcessor.Dispose();
            dialogProcessor = null;

            navigatedProcessor.Dispose();
            navigatedProcessor = null;

            foreach (var disposable in disposables)
            {
                disposable.Dispose();
            }

            disposables.Clear();
            // if (transition != null)
            // {
            //     GameObject.Destroy(transition);
            //     transition = null;
            // }
        }

        public void NavigateTo<T>() where T : NavigatedPresenter
        {
            var locator = PresenterLocator.Create<T>();
            NavigateTo(locator);
        }


        public void NavigateTo(PresenterLocator locator)
        {
            if (string.IsNullOrEmpty(locator.ClassName))
            {
                Debug.LogError("locator target class name is null or empty");
                return;
            }

            var type = Type.GetType(locator.ClassName);
            if (type == null)
            {
                Debug.LogError($"locator target class not found, class name {locator.ClassName}");
                return;
            }

            if (type.IsSubclassOf(typeof(DialogPresenter)))
            {
                Application.GetService<BusyMaskService>().RetainBusy();
                dialogProcessor.Process(locator);
            }
            else if (type.IsSubclassOf(typeof(NavigatedPresenter)))
            {
                Application.GetService<BusyMaskService>().RetainBusy();
                navigatedProcessor.Process(locator);
            }
            else
            {
                Debug.LogError($"locator target class type invalid, class name is {locator.ClassName}");
            }
        }

        public void NavigatedBack()
        {
            if (dialogProcessor.DisplayedPresenter != null)
            {
                Debug.LogError("dialog have a displayed presenter, should close that first!");
                return;
            }

            navigatedProcessor.NavigatedBack();
        }

        public void ClearHistory()
        {
            navigatedProcessor.ClearHistory();
            dialogProcessor.ClearHistory();
        }
    }
}