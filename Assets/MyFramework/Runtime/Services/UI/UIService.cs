using System;
using System.Collections.Generic;
using System.Reflection;
using System.Threading;
using System.Threading.Tasks;
using App.UI.Presenters;
using App.UI.Views.Launch;
using MyFramework.Services.Resource;
using UniRx;
using UnityEditor;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public class UIService : AbstractService
    {
        private Presenter current;

        // private Queue<DialogPresenter> dialogPresenters = new Queue<DialogPresenter>();
        private Dictionary<WindowLayer, int> currentWindowLayerDepths = new Dictionary<WindowLayer, int>();

        public override void OnCreated()
        {
        }

        public override void OnDestroy()
        {
        }

        public async Task<TransitionResult> SwitchPresenterAsync(PresenterLocator locator)
        {
            TransitionResult result;
            try
            {
                var presenter = InstantiatePresenter(locator);
                if (presenter is DialogPresenter dialogPresenter)
                {
                    result = await SwitchDialogPresenterAsyncInternal(dialogPresenter, locator);
                }
                else
                {
                    result = await SwitchPresenterAsyncInternal(presenter, locator);
                }
            }
            catch (Exception e)
            {
                result = TransitionResult.Exception(e);
            }

            if (result.ExceptionInfo != null)
            {
            }

            return result;
        }

        private async Task<TransitionResult> SwitchPresenterAsyncInternal(
            Presenter presenter,
            PresenterLocator locator)
        {
            var r = await MyTaskExtension.Execute<TransitionResult>(
                analyzer: null,
                withLoading: true,
                task: new Func<Task<TransitionResult>>(async () =>
                    {
                        presenter.Freeze();
                        current?.Freeze();
                        var result = await presenter.LoadAsync(locator.Parameters);
                        if (result.Type == TransitionResult.ResultType.Successful)
                        {
                            await Task.WhenAll(
                                current == null ? Task.CompletedTask : current.View.DisappearAsync(),
                                presenter.View.AppearAsync()
                            );
                            current?.Dispose();
                            current = presenter;
                        }
                        presenter.Unfreeze();

                        return result;
                    }
                )
            );
            return r.result;


            // TransitionPresenter transition = null;
            // try
            // {
            //     presenter.Freeze();
            //     current?.Freeze();
            //     transition = new TransitionPresenter();
            //     transition.Freeze();
            //     await transition.LoadAsync(null);
            //     await transition.View.AppearAsync();
            //     // await Task.Delay(3000);
            //     transition.Unfreeze();
            //     var result = await presenter.LoadAsync(locator.Parameters);
            //     if (result.Type == TransitionResult.ResultType.Successful)
            //     {
            //         await Task.WhenAll(
            //             current == null ? Task.CompletedTask : current.View.DisappearAsync(),
            //             presenter.View.AppearAsync(),
            //             transition.View.DisappearAsync()
            //         );
            //         current?.Dispose();
            //         current = presenter;
            //     }
            //
            //     transition.Dispose();
            //     presenter.Unfreeze();
            //
            //     return result;
            // }
            // catch (Exception e)
            // {
            //     current?.Unfreeze();
            //     transition?.Dispose();
            //     presenter?.Dispose();
            //     Debug.LogError(e);
            //     return TransitionResult.Exception(e);
            // }
        }

        private async Task<TransitionResult> SwitchDialogPresenterAsyncInternal(
            DialogPresenter dialogPresenter,
            PresenterLocator locator)
        {
            TransitionPresenter transition = null;
            try

            {
                current?.Freeze();
                dialogPresenter.Freeze();
                transition = new TransitionPresenter();
                transition.Freeze();
                await transition.LoadAsync(null);
                await transition.View.AppearAsync();
                // await Task.Delay(3000);
                transition.Unfreeze();
                var result = await dialogPresenter.LoadAsync(locator.Parameters);
                if (result.Type == TransitionResult.ResultType.Successful)
                {
                    await Task.WhenAll(
                        dialogPresenter.View.AppearAsync(),
                        transition.View.DisappearAsync()
                    );
                }

                transition.Dispose();
                dialogPresenter.Unfreeze();
                current?.Unfreeze();

                // dialogPresenters.Enqueue(dialogPresenter);

                // how to unset the dictionary ?
                var camera = dialogPresenter.View.windowCamera;
                if (currentWindowLayerDepths.ContainsKey(camera.Layer))
                {
                    var newDepth = currentWindowLayerDepths[camera.Layer] + 1;
                    camera.Depth = newDepth;
                    currentWindowLayerDepths[camera.Layer] = camera.Depth;
                }

                await dialogPresenter.CloseEvent.First();
                dialogPresenter.Dispose();

                return result;
            }
            catch (Exception e)
            {
                current?.Unfreeze();
                transition?.Dispose();
                dialogPresenter?.Dispose();
                return TransitionResult.Exception(e);
            }
        }

        // public async Task<bool> ExecuteTaskWithErrorDialog(Func<Task<TransitionResult>> task)
        // {
        //     var transitionResult = await task();
        //     if (transitionResult.Type == TransitionResult.ResultType.Successful)
        //         return true;
        //     return false;
        // }

        // public async Task ExecuteTaskWithLoading(Func<Task> task)
        // {
        //     var transition = new TransitionPresenter();
        //     transition.Freeze();
        //     await transition.LoadAsync(null);
        //     await transition.View.AppearAsync();
        //     transition.Unfreeze();
        //     await task();
        //     transition.Dispose();
        // }

        // public async Task<TransitionResult> HandleTaskException<T>(Func<Task<TransitionResult>> task)
        // {
        //     try
        //     {
        //         return await task();
        //     }
        //     catch (Exception e)
        //     {
        //         return TransitionResult.Exception(e);
        //     }
        // }

        private Presenter InstantiatePresenter(PresenterLocator locator)
        {
            var type = Type.GetType(locator.ClassName);

            var instance = Activator.CreateInstance(type) as Presenter;
            if (instance == null)
            {
                throw new Exception($"instantiate presenter failed, typeof {type}");
            }

            return instance;
        }

        public void Back()
        {
        }

        public async Task<T> InstantiateViewAsync<T>() where T : View
        {
            var viewPath = typeof(T).GetCustomAttribute<ViewPath>();

            var path = viewPath.GetPath();
            if (viewPath == null || string.IsNullOrEmpty(path))
            {
                throw new Exception($"instantiate view async failed, view path not found typeof {typeof(T)}");
            }

            // Application.GetService<ResourceService>()
            // todo 这里直接 load 了
            var asset = AssetDatabase.LoadAssetAtPath<T>(path);
            var view = UnityEngine.GameObject.Instantiate(asset);
            view.gameObject.SetActive(false); // hide before all load process finish
            return view;
        }
    }
}