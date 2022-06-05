using MyFramework.Services.Event;
using MyFramework.Services.UI;
using UnityEngine;
using UnityEngine.Serialization;
using UnityEngine.UI;

namespace App.UI.View.Launch
{
    public class LaunchView : MyFramework.Services.UI.View
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