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
    public class UILaunchPresenter : UIPresenter, IHttpResponseHandler<TestGetResponse>,
        IHttpResponseHandler<TestPostResponse>
    {
        private UILaunchView launchView;
        public ObservableEvent<UILaunchPresenter> OnLaunchFinished = new ObservableEvent<UILaunchPresenter>();

        public override void OnCreated(UIView view)
        {
            var networkService = Application.GetService<NetworkService>();
            networkService.RegisterHttpResponseHandler<TestGetResponse>(this);
            networkService.RegisterHttpResponseHandler<TestPostResponse>(this);

            launchView = view as UILaunchView;
            launchView.Initialize();
            int sec = 0;
            Application.GetService<TimerService>().EverySecond.Subscribe(ts =>
            {
                sec++;
                launchView.SetMessage(sec++.ToString());
            });
            launchView.AsyncGet.Subscribe(async view =>
            {
                Debug.Log("AsyncGet clicked");
                var request = new TestGetRequest();
                var response = await networkService.CommunicateAsync(request);
                if (response.IsSuccessful)
                {
                    Debug.Log("AsyncGet clicked fetch ok, from await/async responded: " + response.GetMessage);
                }
                else
                {
                    Debug.Log("AsyncGet clicked fetch failed, message: " + response.Message);
                }
            });

            launchView.SyncGet.Subscribe(view =>
            {
                Debug.Log("SyncGet clicked");
                networkService.Communicate(new TestGetRequest());
                Debug.Log("SyncClick clicked next");
            });

            launchView.AsyncPost.Subscribe(async view =>
            {
                Debug.Log("AsyncPost clicked");
                var response = await networkService.CommunicateAsync(new TestPostRequest());
                if (response.IsSuccessful)
                {
                    Debug.Log("AsyncPost clicked fetch ok, responded: " + response.PostMessage);
                }
                else
                {
                    Debug.Log("AsyncPost clicked fetch failed, message: " + response.Message);
                }
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

        public void OnHttpResponse(TestGetResponse response)
        {
            Debug.Log("message from OnHttpResponse, get respond: " + response.GetMessage);
        }

        public void OnHttpResponse(TestPostResponse response)
        {
            Debug.Log("message from OnHttpResponse, post respond: : " + response.PostMessage);
        }
    }
}