using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using MyFramework.Services.StateMachine;

namespace MyFramework.Services.UI
{
    public abstract class UIPresenter : IDisposable
    {
        private bool _disposed;
        public bool Disposed => _disposed;
        protected List<IDisposable> _disposables = new List<IDisposable>();

        public static string GetPrefabSchemaPath<T>() where T : UIPresenter
        {
            var type = typeof(T);
            return GetPrefabSchemaPathWithType(type);
        }

        public static string GetPrefabSchemaPathWithType(Type presenterType)
        {
            var attribute = presenterType.GetCustomAttribute<ViewPath>(false);
            return attribute?.GetPath();
        }

        public virtual void OnBack()
        {
            
        }
        public virtual void Dispose()
        {
            _disposed = true;
            foreach (var disposable in _disposables)
            {
                disposable.Dispose();
            }

            _disposables.Clear();
        }
    }
}