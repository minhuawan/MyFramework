using App.Network.Http;
using App.UI.View.Launch;
using MyFramework.Services.Event;
using MyFramework.Services.Network;
using MyFramework.Services.Network.HTTP;
using MyFramework.Services.Resource;
using MyFramework.Services.Timer;
using MyFramework.Services.UI;
using UnityEngine;
using Application = MyFramework.Application;

namespace App.UI.Presenter.Launch
{
    [ViewPath("Assets/AppData/Prefab/UI/View/UILaunchView")]
    public class UILaunchPresenter : UIPresenter
    {
        private UILaunchView launchView;
        public ObservableEvent<UILaunchPresenter> OnLaunchFinished = new ObservableEvent<UILaunchPresenter>();

        public override void OnCreated(UIView view)
        {
            launchView = view as UILaunchView;
            launchView.Initialize();
            int sec = 0;
            Application.GetService<TimerService>().EverySecond.Subscribe(ts =>
            {
                launchView.SetMessage(sec++.ToString());
            });
            launchView.ButtonClick.Subscribe(async btn =>
            {
                Debug.LogError("button clicked");
                var request = new TestRequest();
                var response = await Application.GetService<NetworkService>().Communicate<TestResponse>(request);
                Debug.LogError("message from net" + response.message);
            });
        }

        public override void OnDestroy()
        {
            
        }

        public override void OnOpened()
        {
        }

        public override void OnClose()
        {
        }
    }
}