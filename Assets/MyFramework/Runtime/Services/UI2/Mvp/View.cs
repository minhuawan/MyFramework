using System;
using System.Collections.Generic;
using System.Reflection;
using System.Threading.Tasks;
using MyFramework.Runtime.Services.Content;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public abstract class View : MonoBehaviour, IDisposable
    {
        protected List<IDisposable> disposables = new List<IDisposable>();

        public T As<T>() where T : View
        {
            return this as T;
        }

        public virtual void Dispose()
        {
            foreach (var disposable in disposables)
            {
                disposable.Dispose();
            }

            disposables.Clear();

            UnityEngine.Object.Destroy(this.gameObject);
        }

        public static void InstantiateView<T>(Action<T> callback) where T : View
        {
            var viewType = typeof(T);
            var attribute = viewType.GetCustomAttribute<ViewPathAttribute>();
            if (attribute == null)
            {
                Debug.LogError($"typeof view ViewPathAttribute not found: {viewType.FullName}");
                callback(null);
                return;
            }

            if (string.IsNullOrEmpty(attribute.path))
            {
                Debug.LogError($"typeof view ViewPathAttribute path is null or empty: {viewType.FullName}");
                callback(null);
                return;
            }

            var viewPath = attribute.path;
            if (!viewPath.EndsWith(".prefab"))
            {
                viewPath += ".prefab";
            }
            // todo 这里需要换成正常的加载
#if UNITY_EDITOR
            var unityObject = UnityEditor.AssetDatabase.LoadAssetAtPath<T>(viewPath);
            var view = UnityEngine.Object.Instantiate(unityObject);
            callback(view);
#else
            callback(null);
#endif
        }
    }
}