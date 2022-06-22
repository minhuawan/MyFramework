using System;
using System.Collections.Generic;
using MyFramework.Runtime.Services.Event;
using MyFramework.Runtime.Services.Event.UI;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI
{
    public abstract class BaseLocatorProcessor : IDisposable
    {
        protected abstract bool IsDialogProcessor { get; }
        protected List<IDisposable> _disposed;
        protected NavigatedPresenter runningTarget;
        protected NavigatedPresenter processingTarget;
        protected PresenterLocator processingLocator;
        protected PresenterLocator runningLocator;
        protected bool appearFinish = true;
        protected bool disAppearFinish = true;
        protected bool isBackOperation = false;
        protected List<PresenterLocator> history;
        private Action<PresenterLocator, NavigateResult> navigationResultListener;

        public NavigatedPresenter DisplayedPresenter => runningTarget;

        public BaseLocatorProcessor(Action<PresenterLocator, NavigateResult> resultListener)
        {
            navigationResultListener = resultListener;
            history = new List<PresenterLocator>();
            _disposed = new List<IDisposable>();

            var eventService = Application.GetService<EventService>();
            eventService.Subscribe<NavigateResultEvent>(OnNavigateResult).AddTo(_disposed);
            eventService.Subscribe<NavigatedViewDisappearCompletedEvent>(OnDisappearCompletedEvent).AddTo(_disposed);
            eventService.Subscribe<NavigatedViewAppearCompletedEvent>(OnAppearCompletedEvent).AddTo(_disposed);
        }

        public virtual void Process(PresenterLocator locator)
        {
            if (locator.InUsing)
            {
#if UNITY_EDITOR
                Debug.LogError($"locator already in using! class name: {locator.ClassName}");
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


            try
            {
                var presenter = CreatePresenter(locator);
                processingTarget = presenter;
                processingLocator = locator;
                processingLocator.InUsing = true;
                presenter.OnNavigate(locator);
            }
            catch (Exception e)
            {
                Debug.LogError($"exception on navigate to {processingTarget.GetType().FullName}, message: {e}");
                ResetStates();
                navigationResultListener?.Invoke(locator, NavigateResult.Exception(e));
            }
        }

        protected void OnNavigateResult(NavigateResultEvent navigateResultEvent)
        {
            if (navigateResultEvent.IsDialogEvent != IsDialogProcessor)
            {
                return;
            }


            if (navigateResultEvent.Presenter != processingTarget)
            {
                var name1 = navigateResultEvent.Presenter.GetType().FullName;
                var name2 = processingTarget.GetType().FullName;
                Debug.LogWarning($"current navigate result presenter are not processing target ? {name1}:{name2}");
                return;
            }

            try
            {
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
                    $"{processingLocator.ClassName} message:\n {ex}";
                Debug.LogError(msg);
                ResetStates(); // reset if catch exception
                presenter.Dispose();
            }
        }

        private void AfterNavigationCanceled(NavigateResultEvent navigateResultEvent)
        {
            Debug.Log($"after navigation with canceled, message {navigateResultEvent.Result.Message}");
            var navigatedPresenter = navigateResultEvent.Presenter;
            navigatedPresenter.Dispose();
            var previous = processingLocator;
            ResetStates();
            navigationResultListener?.Invoke(previous, navigateResultEvent.Result);
            // popup message ?
        }

        private void AfterNavigationException(NavigateResultEvent navigateResultEvent)
        {
            Debug.LogError($"after navigation with exception, message {navigateResultEvent.Result.Message}");
            var navigatedPresenter = navigateResultEvent.Presenter;
            navigatedPresenter.Dispose();
            var previous = processingLocator;
            ResetStates();
            navigationResultListener?.Invoke(previous, navigateResultEvent.Result);
            // popup message ?
        }

        private void AfterNavigationFailed(NavigateResultEvent navigateResultEvent)
        {
            Debug.LogError($"after navigation with failed, message {navigateResultEvent.Result.Message}");
            var navigatedPresenter = navigateResultEvent.Presenter;
            navigatedPresenter.Dispose();
            var previous = processingLocator;
            ResetStates();
            navigationResultListener?.Invoke(previous, navigateResultEvent.Result);
            // popup message ?
        }

        private void AfterNavigationOk(NavigateResultEvent navigateResultEvent)
        {
            var presenter = navigateResultEvent.Presenter;
            var result = navigateResultEvent.Result;

            appearFinish = false;
            if (!presenter.View.gameObject.activeSelf)
            {
                presenter.View.gameObject.SetActive(true);
            }

            disAppearFinish = false;
            presenter.OnWillAppear();
            if (runningTarget != null)
            {
                runningTarget.OnWillDisappear();
            }
            else
            {
                disAppearFinish = true;
                TrySwitchRunningAndProcessingTarget();
            }
        }

        private void ResetStates()
        {
            Debug.Log("ResetStates");
            if (processingLocator != null)
                processingLocator.InUsing = false;
            processingLocator = null;
            processingTarget = null;
            disAppearFinish = true;
            appearFinish = true;
            isBackOperation = false;
        }


        protected void OnDisappearCompletedEvent(NavigatedViewDisappearCompletedEvent disappearCompletedEvent)
        {
            if (disappearCompletedEvent.IsDialogEvent != IsDialogProcessor)
            {
                return;
            }


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

            disAppearFinish = true;
            TrySwitchRunningAndProcessingTarget();
        }


        protected void OnAppearCompletedEvent(NavigatedViewAppearCompletedEvent appearCompletedEvent)
        {
            if (appearCompletedEvent.IsDialogEvent != IsDialogProcessor)
            {
                return;
            }


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
            try
            {
                processingTarget.OnDidAppear();
            }
            catch (Exception exception)
            {
                Debug.LogException(exception);
            }

            appearFinish = true;
            TrySwitchRunningAndProcessingTarget();
        }

        private void TrySwitchRunningAndProcessingTarget()
        {
            if (disAppearFinish && appearFinish)
            {
                if (isBackOperation)
                {
                    history.RemoveAt(history.Count - 1);
                }
                else
                {
                    if (runningLocator != null)
                    {
                        history.Add(runningLocator);
                    }
                }

                // last running target should disposed in disappear callback
                runningTarget = processingTarget;
                runningLocator = processingLocator;
                ResetStates();
                // runningTarget and processingLocator both could be an null value
                navigationResultListener?.Invoke(runningLocator, NavigateResult.Ok);
            }
        }


        protected NavigatedPresenter CreatePresenter(PresenterLocator locator)
        {
            var type = Type.GetType(locator.ClassName);
            var instance = Activator.CreateInstance(type) as NavigatedPresenter;
            if (instance == null)
            {
                throw new Exception($"instantiate presenter failed, typeof {type}");
            }

            return instance;
        }

        public void OnBackKey()
        {
            if (processingTarget != null)
            {
                // in loading or other waiting task
                return;
            }

            // if (waitingResultDialogPresenter != null)
            // {
            //     waitingResultDialogPresenter.OnBack();
            //     return;
            // }

            if (runningTarget != null)
            {
                runningTarget.OnBack();
                return;
            }
        }

        public void Dispose()
        {
            if (runningTarget != null)
            {
                runningTarget.Dispose();
                runningTarget = null;
            }

            if (processingTarget != null)
            {
                processingTarget.Dispose();
                processingTarget = null;
            }

            history.Clear();
            foreach (var disposable in _disposed)
            {
                disposable.Dispose();
            }

            _disposed.Clear();
        }

        public void NavigatedBack()
        {
            if (processingTarget != null)
                return;
            if (runningTarget == null)
                return;
            if (!appearFinish || !disAppearFinish)
                return;
            if (history.Count == 0)
            {
                // todo go to first page?
                return;
            }

            // todo 这里返回怎么处理，如果返回过程中出错，可当前的栈已经被修改了
            // 难受
            var backTarget = history[history.Count - 1];
            backTarget.InUsing = false;
            isBackOperation = true;
            // Process(backTarget);
            Application.GetService<UIService>().NavigateTo(backTarget);
        }
    }
}