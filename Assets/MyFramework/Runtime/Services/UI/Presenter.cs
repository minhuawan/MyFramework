using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using MyFramework.Services.StateMachine;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public abstract class Presenter : IDisposable
    {
        public abstract View View { get; }
        public abstract Task<TransitionResult> LoadAsync(PresenterLocatorParameters parameters);
        private bool _disposed;
        public bool Disposed => _disposed;
        protected List<IDisposable> _disposables = new List<IDisposable>();

        private int freezeCount;
        public bool IsFrozen => freezeCount > 0;
        private Queue<Func<Task>> pendingTasks = new Queue<Func<Task>>();

        public virtual void Freeze()
        {
            freezeCount++;
        }

        public virtual void Unfreeze()
        {
            if (freezeCount > 0)
            {
                freezeCount--;
                if (freezeCount == 0)
                {
                    OnTaskEnd();
                }
            }
        }

        private void OnTaskEnd()
        {
            if (pendingTasks.Count > 0)
            {
                var task = pendingTasks.Dequeue();
            }
        }

        protected void ExecuteTaskLater(Func<Task> task)
        {
            if (IsFrozen)
            {
                pendingTasks.Enqueue(task);
            }
            else
            {
                ExecuteTask(task);
            }
        }

        protected async Task ExecuteTask(Func<Task> task, Action<Exception> handler = null)
        {
            if (IsFrozen)
            {
                Debug.LogWarning("presenter frozen !");
                return;
            }

            try
            {
                freezeCount++;
                await task();
            }
            catch (Exception ex)
            {
                if (handler != null)
                {
                    handler.Invoke(ex);
                }
                else
                {
                    Debug.LogError("exception occurred during execute task. ex " + ex.ToString());
                }
            }
            finally
            {
                freezeCount--;
                OnTaskEnd();
            }
        }

        protected void ExecuteTask(Action task)
        {
            ExecuteTask(() =>
            {
                task();
                return Task.CompletedTask;
            });
        }


        public virtual void OnBackKey()
        {
        }

        public virtual void OnDidAppear()
        {
        }

        public virtual void Dispose()
        {
            foreach (var disposable in _disposables)
            {
                disposable.Dispose();
            }

            _disposables.Clear();
            if (View != null)
            {
                View.Dispose();
            }

            _disposed = true;
        }
    }
}