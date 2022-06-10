using System;
using System.Collections.Generic;
using System.Reflection;
using System.Threading;
using System.Threading.Tasks;
using App.UI.Presenters;
using App.UI.Views.Launch;
using MyFramework.Services.Resource;
using UnityEditor;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public class UIService : AbstractService
    {
        private Presenter current;
        private Queue<DialogPresenter> dialogPresenters = new Queue<DialogPresenter>();
        private Dictionary<WindowLayer, int> currentWindowLayerDepths = new Dictionary<WindowLayer, int>();

        public override void OnCreated()
        {
        }

        public override void OnDestroy()
        {
        }

        public async Task<TransitionResult> SwitchPresenterAsync(PresenterLocator locator)
        {
            try
            {
                var presenter = InstantiatePresenter(locator);
                if (presenter is DialogPresenter dialogPresenter)
                {
                    return await SwitchDialogPresenterAsyncInternal(dialogPresenter, locator);
                }
                else
                {
                    return await SwitchPresenterAsyncInternal(presenter, locator);
                }
            }
            catch (Exception e)
            {
                return TransitionResult.Exception(e);
            }
        }

        private async Task<TransitionResult> SwitchPresenterAsyncInternal(
            Presenter presenter,
            PresenterLocator locator)
        {
            TransitionPresenter transition = null;
            try
            {
                presenter.Freeze();
                current?.Freeze();
                transition = new TransitionPresenter();
                transition.Freeze();
                await transition.LoadAsync(null);
                await transition.View.AppearAsync();
                await Task.Delay(3000);
                transition.Unfreeze();
                var result = await presenter.LoadAsync(locator.Parameters);
                if (result.Type == TransitionResult.ResultType.Successful)
                {
                    await Task.WhenAll(
                        current == null ? Task.CompletedTask : current.View.DisappearAsync(),
                        presenter.View.AppearAsync(),
                        transition.View.DisappearAsync()
                    );
                    current?.Dispose();
                    current = presenter;
                }

                transition.Dispose();
                presenter.Unfreeze();
                
                return result;
            }
            catch (Exception e)
            {
                current?.Unfreeze();
                transition?.Dispose();
                presenter?.Dispose();
                Debug.LogError(e);
                return TransitionResult.Exception(e);
            }
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
                await Task.Delay(3000);
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

                dialogPresenters.Enqueue(dialogPresenter);
                
                // how to unset the dictionary ?
                var camera = dialogPresenter.View.windowCamera;
                if (currentWindowLayerDepths.ContainsKey(camera.Layer))
                {
                    var newDepth = currentWindowLayerDepths[camera.Layer] + 1;
                    camera.Depth = newDepth;
                    currentWindowLayerDepths[camera.Layer] = camera.Depth;
                }
                
                return result;
            }
            catch (Exception e)
            {
                current?.Unfreeze();
                transition?.Dispose();
                dialogPresenter?.Dispose();
                Debug.LogError(e);
                return TransitionResult.Exception(e);
            }
        }

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