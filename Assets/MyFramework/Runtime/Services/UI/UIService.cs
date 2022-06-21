using System;
using System.Collections.Generic;
using MyFramework.Runtime.Services.Event;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI
{
    public class UIService : AbstractService
    {
        private List<IDisposable> disposables;
        private BaseLocatorProcessor navigatedProcessor;
        private BaseLocatorProcessor dialogProcessor;

        public override void OnCreated()
        {
            disposables = new List<IDisposable>();
        }

        public override void Initialize()
        {
            navigatedProcessor = new NavigateLocatorProcessor();
            dialogProcessor = new DialogLocatorProcessor();
            Application.GetService<EventService>().Subscribe<BackKeyEvent>(OnBackKey).AddTo(disposables);
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
                dialogProcessor.Process(locator);
            }
            else if (type.IsSubclassOf(typeof(NavigatedPresenter)))
            {
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
    }
}