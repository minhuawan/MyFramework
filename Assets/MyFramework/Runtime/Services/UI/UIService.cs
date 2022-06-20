using System;
using System.Collections.Generic;
using MyFramework.Runtime.Services.Event;
using MyFramework.Runtime.Services.Event.UI;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI
{
    public class UIService : AbstractService
    {
        private List<IDisposable> _disposed;
        private Queue<PresenterLocator> locators;

        // todo 这里存在一个问题，如果是 dialog， waitingResultDialogPresenter 会与这个值相等
        // todo 并且关闭 dialog 后 waitingResultDialogPresenter 变为了 null ，但是这个值不会变
        private NavigatedPresenter runningTarget;
        private NavigatedPresenter processingTarget;
        private PresenterLocator processingLocator;
        private DialogPresenter waitingResultDialogPresenter;
        private bool appearFinish = true;
        private bool disAppearFinish = true;

        public override void OnCreated()
        {
            locators = new Queue<PresenterLocator>();
            _disposed = new List<IDisposable>();
        }

        public override void Initialize()
        {
            var eventService = Application.GetService<EventService>();
            eventService.Subscribe<NavigateResultEvent>(OnNavigateResult).AddTo(_disposed);
            eventService.Subscribe<NavigatedViewDisappearCompletedEvent>(OnDisappearCompletedEvent).AddTo(_disposed);
            eventService.Subscribe<NavigatedViewAppearCompletedEvent>(OnAppearCompletedEvent).AddTo(_disposed);
            eventService.Subscribe<DialogPresenterCompletedEvent>(OnDialogPresenterCompletedEvent)
                .AddTo(_disposed);
            eventService.Subscribe<BackKeyEvent>(OnBackKey).AddTo(_disposed);
        }

        public override void OnDestroy()
        {
            locators.Clear();
            foreach (var disposable in _disposed)
            {
                disposable.Dispose();
            }

            _disposed.Clear();
        }

        public void NavigateTo(PresenterLocator locator)
        {
            if (locator.InUsing)
            {
#if UNITY_EDITOR
                Debug.LogError($"locator already in using!");
#endif
                return;
            }

            if (processingTarget != null)
            {
#if UNITY_EDITOR
                Debug.LogError($"have a target in processing, name is {processingTarget.GetType().FullName}");
#endif
                return;
            }

            if (!appearFinish || !disAppearFinish)
            {
#if UNITY_EDITOR
                Debug.LogError("waiting in presenter appear/disappear lift-cycle");
#endif
                return;
            }

            if (waitingResultDialogPresenter != null)
            {
#if UNITY_EDITOR
                Debug.LogError($"there has a working dialog presenter for waiting result," +
                               $" name is {waitingResultDialogPresenter.GetType().FullName}");
#endif
                return;
            }

            try
            {
                locator.InUsing = true;
                processingLocator = locator;
                var presenter = CreatePresenter(locator);
                processingTarget = presenter;
                presenter.OnNavigate(locator);
            }
            catch (Exception e)
            {
                Debug.LogError($"exception on navigate to {processingTarget.GetType().FullName}, message: {e}");
                locator.InUsing = false;
                processingTarget = null;
                waitingResultDialogPresenter = null;
            }
        }

        private void OnNavigateResult(NavigateResultEvent navigateResultEvent)
        {
            if (navigateResultEvent.Presenter != processingTarget)
            {
                var name1 = navigateResultEvent.Presenter.GetType().FullName;
                var name2 = processingTarget.GetType().FullName;
                Debug.LogWarning($"current navigate result presenter are not processing target ? {name1}:{name2}");
                return;
            }

            try
            {
                navigateResultEvent.Locator.InUsing = false;
                var result = navigateResultEvent.Result;
                switch (result.Type)
                {
                    case NavigateResult.ResultType.Ok:
                        AfterNavigationOk(navigateResultEvent);
                        break;
                    case NavigateResult.ResultType.Canceled:
                        AfterNavigationCanceled(navigateResultEvent);
                        break;
                    case NavigateResult.ResultType.Exception:
                        AfterNavigationException(navigateResultEvent);
                        break;
                    case NavigateResult.ResultType.None:
                    case NavigateResult.ResultType.Failed:
                    default:
                        AfterNavigationFailed(navigateResultEvent);
                        break;
                }
            }
            catch (Exception ex)
            {
                var presenter = navigateResultEvent.Presenter;
                var msg =
                    $"exception on OnNavigateResult processing type is " +
                    $"{navigateResultEvent.Locator.ClassName} message:\n {ex}";
                Debug.LogError(msg);
                presenter.Dispose();
                navigateResultEvent.Locator.InUsing = false;
                processingTarget = null;
            }
        }

        private void AfterNavigationCanceled(NavigateResultEvent navigateResultEvent)
        {
            Debug.LogError($"after navigation with canceled, message {navigateResultEvent.Result.Message}");
            var navigatedPresenter = navigateResultEvent.Presenter;
            navigatedPresenter.Dispose();
            // popup message ?
        }

        private void AfterNavigationException(NavigateResultEvent navigateResultEvent)
        {
            Debug.LogError($"after navigation with exception, message {navigateResultEvent.Result.Message}");
            var navigatedPresenter = navigateResultEvent.Presenter;
            navigatedPresenter.Dispose();
            navigateResultEvent.Locator.InUsing = false;
            // popup message ?
            processingTarget = null;
            disAppearFinish = true;
            appearFinish = true;
        }

        private void AfterNavigationFailed(NavigateResultEvent navigateResultEvent)
        {
            Debug.LogError($"after navigation with failed, message {navigateResultEvent.Result.Message}");
            var navigatedPresenter = navigateResultEvent.Presenter;
            navigatedPresenter.Dispose();
            // popup message ?
            processingTarget = null;
            disAppearFinish = true;
            appearFinish = true;
        }

        private void AfterNavigationOk(NavigateResultEvent navigateResultEvent)
        {
            Debug.Log($"after navigation with ok, class name {navigateResultEvent.Locator.ClassName}");
            var presenterLocator = navigateResultEvent.Locator;
            var presenter = navigateResultEvent.Presenter;
            var result = navigateResultEvent.Result;

            appearFinish = false;
            if (!presenter.View.gameObject.activeSelf)
            {
                presenter.View.gameObject.SetActive(true);
            }

            presenter.OnWillAppear();
            if (presenter is DialogPresenter dialogPresenter)
            {
                waitingResultDialogPresenter = dialogPresenter;
                disAppearFinish = true; // no need to disappear last navigated presenter
            }
            else if (runningTarget != null)
            {
                disAppearFinish = false;
                runningTarget.OnWillDisappear();
            }
        }


        private void OnDisappearCompletedEvent(NavigatedViewDisappearCompletedEvent disappearCompletedEvent)
        {
            if (disappearCompletedEvent == null)
            {
                Debug.LogError("UIService received an disappear null event");
                return;
            }

            if (disappearCompletedEvent.View == null)
            {
                Debug.LogError($"UIService received an disappear event, view is null, " +
                               $"runningTarget is {runningTarget.GetType().FullName}");
                return;
            }

            if (waitingResultDialogPresenter?.View == disappearCompletedEvent.View)
            {
                try
                {
                    waitingResultDialogPresenter.Dispose();
                }
                catch (Exception e)
                {
                    Debug.LogException(e);
                }

                waitingResultDialogPresenter = null;
                return;
            }

            if (runningTarget == null || runningTarget.View == null)
            {
                Debug.LogError("UIService received an disappear event, runningTarget is null");
                return;
            }

            if (disappearCompletedEvent.View != runningTarget.View)
            {
                Debug.LogError("UIService received an disappear event, view not equals " +
                               $"{disappearCompletedEvent.View.GetType().FullName}" +
                               $" <--> " +
                               $"{runningTarget.View.GetType().FullName}");
            }

            try
            {
                runningTarget.Dispose();
            }
            catch (Exception e)
            {
                Debug.LogException(e);
            }

            runningTarget = null;
            disAppearFinish = true;
        }


        private void OnAppearCompletedEvent(NavigatedViewAppearCompletedEvent appearCompletedEvent)
        {
            if (appearCompletedEvent == null)
            {
                Debug.LogError("UIService received an appear null event");
                return;
            }

            if (processingTarget == null)
            {
                Debug.LogError("UIService received an appear event, current processing target is null");
                return;
            }

            if (appearCompletedEvent.View == null)
            {
                Debug.LogError($"UIService received an appear event, view is null, " +
                               $"processingTarget is {processingTarget.GetType().FullName}");
                return;
            }

            if (appearCompletedEvent.View != processingTarget.View)
            {
                Debug.LogError("UIService received an appear event, view not equals " +
                               $"{appearCompletedEvent.View.GetType().FullName}" +
                               $" <--> " +
                               $"{runningTarget.View.GetType().FullName}");
            }

            var view = appearCompletedEvent.View;
            locators.Enqueue(processingLocator);
            try
            {
                processingTarget.OnDidAppear();
            }
            catch (Exception exception)
            {
                Debug.LogException(exception);
            }

            runningTarget = processingTarget;
            processingLocator = null;
            processingTarget = null;
            appearFinish = true;
        }

        private void OnDialogPresenterCompletedEvent(DialogPresenterCompletedEvent completedEvent)
        {
            if (waitingResultDialogPresenter == null)
            {
                Debug.LogError("UIService received a dialog completed event," +
                               " but waitingResultDialogPresenter is null");
                return;
            }

            if (completedEvent == null || completedEvent.DialogPresenter == null)
            {
                Debug.LogError("UIService received a dialog completed event," +
                               " but completedEvent or completedEvent.DialogPresenter is null");
                return;
            }

            if (completedEvent.DialogPresenter != waitingResultDialogPresenter)
            {
                Debug.LogError("UIService received a dialog completed event," +
                               " but disappearEvent.DialogPresenter != waitingResultDialogPresenter" +
                               $"{completedEvent.DialogPresenter.GetType().FullName}" +
                               $" <--> " +
                               $"{waitingResultDialogPresenter.GetType().FullName}");
                return;
            }

            waitingResultDialogPresenter.OnWillDisappear();
        }


        private NavigatedPresenter CreatePresenter(PresenterLocator locator)
        {
            var type = Type.GetType(locator.ClassName);
            var instance = Activator.CreateInstance(type) as NavigatedPresenter;
            if (instance == null)
            {
                throw new Exception($"instantiate presenter failed, typeof {type}");
            }

            return instance;
        }

        private void OnBackKey(BackKeyEvent backKeyEvent)
        {
            if (processingTarget != null)
            {
                // in loading or other waiting task
                return;
            }

            if (waitingResultDialogPresenter != null)
            {
                waitingResultDialogPresenter.OnBack();
                return;
            }

            if (runningTarget != null)
            {
                runningTarget.OnBack();
                return;
            }
        }
    }
}