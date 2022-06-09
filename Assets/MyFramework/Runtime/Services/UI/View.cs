using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MyFramework.Services.UI
{
    public abstract class View : MonoBehaviour, IDisposable
    {
        [SerializeField] private Camera windowCamera;
        public virtual void Dispose()
        {
            // todo destroy this view
            GameObject.DestroyImmediate(gameObject); // 先临时这么写吧
        }

        public virtual Task AppearAsync()
        {
            if (!gameObject.activeSelf)
                gameObject.SetActive(true);
            return Task.CompletedTask;
        }

        public virtual Task DisappearAsync()
        {
            return Task.CompletedTask;
        }

        public static async Task<T> InstantiatePrefabAsync<T>() where T : View
        {
            var view = await Application.GetService<UIService>().InstantiateViewAsync<T>();
            view.gameObject.SetActive(false);
            return view;
        }
    }
}