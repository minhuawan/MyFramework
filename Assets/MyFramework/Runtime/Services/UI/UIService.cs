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

        public override void OnCreated()
        {
        }

        public override void OnDestroy()
        {
        }

        public async Task<TransitionResult> SwitchPresenterAsync(PresenterLocator locator)
        {
            TransitionPresenter transition = null;
            Presenter presenter = null;
            try
            {
                transition = new TransitionPresenter();
                await transition.LoadAsync(null);
                await transition.View.AppearAsync();
                await Task.Delay(3000);
                presenter = InstantiatePresenter(locator);
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

                return result;
            }
            catch (Exception e)
            {
                transition?.Dispose();
                presenter?.Dispose();
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