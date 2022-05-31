using MyFramework.Services.Event;
using MyFramework.Services.UI;
using UnityEngine;
using UnityEngine.Serialization;
using UnityEngine.UI;

namespace App.UI.View.Launch
{
    public class UILaunchView : UIView
    {
        [SerializeField] private Text text;
        [SerializeField] private Button buttonAsyncGet;
        [SerializeField] private Button buttonSyncGet;
        [SerializeField] private Button buttonAsyncPost;
        [SerializeField] private Button buttonAsyncGetBaidu;

        public ObservableEvent<UILaunchView> AsyncGet = new ObservableEvent<UILaunchView>();
        public ObservableEvent<UILaunchView> SyncGet = new ObservableEvent<UILaunchView>();
        public ObservableEvent<UILaunchView> AsyncPost = new ObservableEvent<UILaunchView>();
        public ObservableEvent<UILaunchView> AsyncGetBaidu = new ObservableEvent<UILaunchView>();

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