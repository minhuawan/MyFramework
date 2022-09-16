using System;
using System.Collections.Generic;
using System.Reflection;
using MyFramework.Runtime.Services.Event.UI;
using UnityEditor;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI
{
    public abstract class NavigatedView : MonoBehaviour, IDisposable
    {
        protected List<IDisposable> _disposables = new List<IDisposable>();

        public virtual bool IsDialog => false;

        public T As<T>() where T : NavigatedView
        {
            return this as T;
        }

        public virtual void OnWillAppear()
        {
            // animation
            DispatchAppearCompletedEvent();
        }

        public virtual void OnDidAppear()
        {
        }

        public virtual void OnWillDisappear()
        {
            // animation
            DispatchDisappearCompletedEvent();
        }

        private void DispatchAppearCompletedEvent()
        {
            new NavigatedViewAppearCompletedEvent(this, IsDialog).Dispatch();
        }

        private void DispatchDisappearCompletedEvent()
        {
            new NavigatedViewDisappearCompletedEvent(this, IsDialog).Dispatch();
        }

        public void Dispose()
        {
            foreach (var disposable in _disposables)
            {
                disposable.Dispose();
            }

            _disposables.Clear();

            Destroy(this.gameObject);
        }

        public static T InstantiateView<T>() where T : NavigatedView
        {
            var viewPath = typeof(T).GetCustomAttribute<ViewPath>();
            if (viewPath == null)
            {
                throw new Exception($"typeof {typeof(T).FullName} get view path null");
            }

            var path = viewPath.GetPath();
            if (viewPath == null || string.IsNullOrEmpty(path))
            {
                throw new Exception($"instantiate view async failed, view path not found typeof {typeof(T)}");
            }

#if UNITY_EDITOR
            // todo 这里直接 load 了
            var asset = AssetDatabase.LoadAssetAtPath<T>(path);
            var view = GameObject.Instantiate(asset);
            view.gameObject.SetActive(false); // hide before all load process finish
            return view;
#else
            return null;
#endif
        }

        public static async void InstantiateViewAsync<T>(Action<T> action, Action<Exception> errorHandler)
            where T : NavigatedView
        {
            // var type = typeof(T);
            // var dialogType = typeof(DialogView);
            // if (type.IsSubclassOf(dialogType) && type != typeof(ErrorNoticeDialogView))
            // {
            //     errorHandler.Invoke(new Exception("100% test exception"));
            //     return;
            // }

            // if (new Random().Next(1, 100) < 20) // for test
            // {
            //     errorHandler.Invoke(new Exception("InstantiateViewAsync test random exception"));
            // }
            // else
            // {
            //     var view = InstantiateView<T>();
            //     action(view);
            // }

            // await Task.Delay(3000);
            var view = InstantiateView<T>();
            action(view);
        }
    }
}