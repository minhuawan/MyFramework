using System;
using System.Threading.Tasks;
using MyFramework.Services.Event;
using MyFramework.Services.UI;
using UnityEngine;
using UnityEngine.UI;
using Application = MyFramework.Application;

namespace App.UI.Views.Launch
{
    [ViewPath("Assets/AppData/Prefab/UI/View/UILaunchView")]
    public class LaunchView : View
    {
        [SerializeField] private Text text;
        [SerializeField] private Button buttonAsyncGet;
        [SerializeField] private Button buttonSyncGet;
        [SerializeField] private Button buttonAsyncPost;
        [SerializeField] private Button buttonAsyncGetBaidu;

        public ObservableEvent<LaunchView> AsyncGet = new ObservableEvent<LaunchView>();
        public ObservableEvent<LaunchView> SyncGet = new ObservableEvent<LaunchView>();
        public ObservableEvent<LaunchView> AsyncPost = new ObservableEvent<LaunchView>();
        public ObservableEvent<LaunchView> AsyncGetBaidu = new ObservableEvent<LaunchView>();

        public static async Task<LaunchView> LoadAsync()
        {
            // try
            {
                Debug.LogError("start ThreadId" + System.Threading.Thread.CurrentThread.ManagedThreadId);
                var result = await Application.GetService<UIService>().InstantiateViewAsync<LaunchView>();
                // var task = Application.GetService<UIService>().InstantiateViewAsync<LaunchView>();
                // LaunchView result = null;
                // if (task.Exception == null)
                // {
                //     result = await task;
                // }
                // else
                // {
                //     Debug.LogError(task.Exception);
                // }

                Debug.LogError("end   ThreadId" + System.Threading.Thread.CurrentThread.ManagedThreadId);
                return result;
            }
            // catch (Exception e)
            // {
            //     // 这里会捕获到
            //     Debug.LogError(e);
            //     return null;
            // }
        }

        public void Initialize()
        {
            buttonAsyncGet.onClick.AddListener(() => AsyncGet.OnNext(this));
            buttonSyncGet.onClick.AddListener(() => SyncGet.OnNext(this));
            buttonAsyncPost.onClick.AddListener(() => AsyncPost.OnNext(this));
            buttonAsyncGetBaidu.onClick.AddListener(() => AsyncGetBaidu.OnNext(this));
        }

        public void SetMessage(string message)
        {
            text.text = message;
        }
    }
}