using System;
using App.Network.Http;
using App.Network.Tcp;
using App.UI.View.Launch;
using MyFramework.Services.Event;
using MyFramework.Services.Network;
using MyFramework.Services.Network.HTTP;
using MyFramework.Services.Network.Tcp;
using MyFramework.Services.Resource;
using MyFramework.Services.Timer;
using MyFramework.Services.UI;
using UnityEngine;
using Application = MyFramework.Application;

namespace App.UI.Presenter.Launch
{
    [ViewPath("Assets/AppData/Prefab/UI/View/UILaunchView")]
    public class LaunchPresenter : MyFramework.Services.UI.Presenter, 
        IHttpResponseHandler<TestGetResponse>,
        IHttpResponseHandler<TestPostResponse>,
        ITcpProtocolHandler<TestTcpResponse>
    {
        private LaunchView launchView;
        public ObservableEvent<LaunchPresenter> OnLaunchFinished = new ObservableEvent<LaunchPresenter>();

        public void OnCreated(MyFramework.Services.UI.View view)
        {
            var networkService = Application.GetService<NetworkService>();
            networkService.Http.RegisterHttpResponseHandler<TestGetResponse>(this);
            networkService.Http.RegisterHttpResponseHandler<TestPostResponse>(this);
            networkService.Tcp.Dispatcher.RegisterTcpProtocolHandler<TestTcpResponse>(this);

            launchView = view as LaunchView;
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
                var response = await networkService.Http.SendAsync(request);
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
                _ = networkService.Http.SendAsync(new TestGetRequest());
                Debug.Log("SyncClick clicked next");
            });

            launchView.AsyncPost.Subscribe(async view =>
            {
                Debug.Log("AsyncPost clicked");
                var response = await networkService.Http.SendAsync(new TestPostRequest());
                if (response.IsSuccessful)
                {
                    Debug.Log("AsyncPost clicked fetch ok, responded: " + response.PostMessage);
                }
                else
                {
                    Debug.Log("AsyncPost clicked fetch failed, message: " + response.Message);
                }
            });

            var b = false;
            launchView.AsyncGetBaidu.Subscribe(async view =>
            {
                // await Application.GetService<NetworkService>().Tcp.ConnectAsync();
                // await Application.GetService<NetworkService>().Tcp.SendAsync(new TestTcpRequest());

                var webSocket = Application.GetService<NetworkService>().WebSocket;
                if (!b)
                {
                    await webSocket.ConnectAsync(new Uri(
                        "wss://demo.piesocket.com/v3/channel_1?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self"));
                        // "ws://127.0.0.1:1111"));
                    // b = true;
                }
                
                webSocket.SendAsync("message from my framework. 中文测试! 😄");
            });
        }
        public void OnHttpResponse(TestGetResponse response)
        {
            Debug.Log("message from OnHttpResponse, get respond: " + response.GetMessage);
        }

        public void OnHttpResponse(TestPostResponse response)
        {
            Debug.Log("message from OnHttpResponse, post respond: : " + response.PostMessage);
        }

        public void OnTcpProtocol(TestTcpResponse protocol)
        {
            throw new System.NotImplementedException();
        }
    }
}