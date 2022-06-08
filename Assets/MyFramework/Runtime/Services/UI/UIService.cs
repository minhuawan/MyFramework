using System;
using System.Collections.Generic;
using System.Reflection;
using System.Threading;
using System.Threading.Tasks;
using App.UI.Views.Launch;
using MyFramework.Services.Resource;
using UnityEditor;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public class UIService : AbstractService
    {
        private Stack<Presenter> stack;

        public override void OnCreated()
        {
            stack = new Stack<Presenter>();
        }

        public override void OnDestroy()
        {
            foreach (var uiPresenter in stack)
            {
                uiPresenter.Dispose();
            }

            stack.Clear();
        }

        public async Task<TransitionResult> SwitchPresenterAsync<T>() where T : Presenter
        {
            T presenter = null;
            try
            {
                presenter = InstantiatePresenter<T>();
                return await presenter.LoadAsync();
            }
            catch (Exception e)
            {
                presenter?.Dispose();
                return TransitionResult.Exception(e);
            }
        }

        private T InstantiatePresenter<T>() where T : Presenter
        {
            var instance = Activator.CreateInstance<T>();
            if (instance == null)
            {
                throw new Exception($"instantiate presenter failed, typeof {typeof(T)}");
            }

            return instance;
        }

        public void Back()
        {
            if (stack.Count > 0)
            {
                var presenter = stack.Peek();
                presenter.OnBackKey();
            }
        }

        public async Task<T> InstantiateViewAsync<T>() where T : View
        {
            Debug.LogError("ThreadId" + Thread.CurrentThread.ManagedThreadId);
            throw new Exception("abc");
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
            return view;
        }
    }
}