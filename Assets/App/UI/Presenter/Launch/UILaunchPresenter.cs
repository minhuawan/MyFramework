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
    public class UILaunchPresenter : UIPresenter, IHttpResponseHandler<TestResponse>, IHttpResponseHandler<HelloResponse>
    {
        private UILaunchView launchView;
        public ObservableEvent<UILaunchPresenter> OnLaunchFinished = new ObservableEvent<UILaunchPresenter>();

        public override void OnCreated(UIView view)
        {
            var networkService = Application.GetService<NetworkService>();
            networkService.RegisterHttpResponseHandler<TestResponse>(this);
            networkService.RegisterHttpResponseHandler<HelloResponse>(this);

            launchView = view as UILaunchView;
            launchView.Initialize();
            int sec = 0;
            Application.GetService<TimerService>().EveryFrame.Subscribe(ts =>
            {
                sec++;
                launchView.SetMessage(sec++.ToString());
            });
            launchView.AsyncClick.Subscribe(async btn =>
            {
                Debug.LogError("AsyncClick clicked");
                var request = new TestRequest();
                var response = await networkService.CommunicateAsync<TestResponse>(request);
                Debug.LogError("message from await/async: " + response.message);
            });
            
            launchView.SyncClick.Subscribe(b =>
            {
                Debug.LogError("SyncClick clicked");
                networkService.CommunicateAsync<TestResponse>(new TestRequest());
                Debug.LogError("SyncClick clicked next");
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

        public void OnHttpResponse(TestResponse response)
        {
            Debug.LogError("message from OnHttpResponse: " + response.message);
        }

        public void OnHttpResponse(HelloResponse response)
        {
            Debug.LogError("message from OnHttpResponse: " + response.message);
        }
    }
}