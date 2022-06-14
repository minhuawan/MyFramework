using System;
using System.Net;
using System.Net.Sockets;
using System.Reflection;
using System.Runtime.CompilerServices;
using System.Threading;
using System.Threading.Tasks;
using App.UI.Presenters;
using MyFramework.Services;
using MyFramework.Services.UI;
using UniRx;
using Unity.Profiling.LowLevel.Unsafe;
using UnityEngine;

namespace MyFramework
{
    public interface ITaskExceptionAnalyzer
    {
        string Analysis(Exception e);
    }

    public class DefaultTaskExceptionAnalyzer : ITaskExceptionAnalyzer
    {
        public string Analysis(Exception e)
        {
            // Application.GetService<MainThreadService>().PushException(e);
            return e.ToString();
        }
    }

    public struct TaskExecuteResult<T>
    {
        public bool Successful;
        public T result;
    }

    public static class MyTaskExtension
    {
        // public static async Task ExecuteWithLoading(this Task task, CancellationToken cancellationToken)
        // {
        //     await Task.Run(async () =>
        //     {
        //         var transition = new TransitionPresenter();
        //         transition.Freeze();
        //         await transition.LoadAsync(null);
        //         await transition.View.AppearAsync();
        //         transition.Unfreeze();
        //         await task;
        //         transition.Dispose();
        //     }, cancellationToken);
        // }


        public static async Task Execute(Action task)
        {
            Execute(async () => task());
        }

        public static async Task Execute(Func<Task> task)
        {
            var target = Task.Run(async () =>
            {
                await task();
                return new TaskExecuteResult<string>()
                {
                    result = "nothing",
                    Successful = true
                };
            });
            await Execute(async () =>
            {
                await task();
                return Unit.Default;
            }, CancellationToken.None, null, true);
        }


        public static async Task<TaskExecuteResult<T>> Execute<T>(
            Func<Task<T>> task,
            CancellationToken cancellationToken,
            ITaskExceptionAnalyzer analyzer,
            bool withLoading)
        {
            TaskExecuteResult<T> result = default;
            try
            {
                if (withLoading)
                {
                    using (var transition = new TransitionPresenter())
                    {
                        transition.Freeze();
                        await transition.LoadAsync(null);
                        await transition.View.AppearAsync();
                        transition.Unfreeze();
                        result.result = await task();
                        result.Successful = true;
                    }
                }
            }
            catch (Exception e)
            {
                using (var errorDialog = new ErrorDialogPresenter())
                {
                    result.Successful = false;
                    var msg = analyzer == null ? e.ToString() : analyzer.Analysis(e);
                    errorDialog.Freeze();
                    await errorDialog
                        .LoadAsync(new PresenterLocatorParameters()
                            .Put(ErrorDialogPresenter.MessageKey, msg));
                    errorDialog.View.AppearAsync();
                    errorDialog.Unfreeze();
                    await errorDialog.CloseEvent.First();
                    errorDialog.Dispose();
                }
            }

            return result;
        }
    }
}