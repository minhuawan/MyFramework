using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using App.Network.Http;
using App.Network.Tcp;
using App.UI.Views.Launch;
using MyFramework;
using MyFramework.Services.Asset;
using MyFramework.Services.Network;
using MyFramework.Services.Network.HTTP;
using MyFramework.Services.Network.Tcp;
using MyFramework.Services.Timer;
using MyFramework.Services.UI;
using Newtonsoft.Json;
using UniRx;
using UnityEngine;
using UnityEngine.Assertions.Must;
using Application = MyFramework.Application;

namespace App.UI.Presenters.Launch
{
    public class LaunchPresenter : Presenter,
        IHttpResponseHandler<TestGetResponse>,
        IHttpResponseHandler<TestPostResponse>,
        ITcpProtocolHandler<TestTcpResponse>
    {
        private LaunchView _view;
        public override View View => _view;

        public override async Task<TransitionResult> LoadAsync(PresenterLocatorParameters parameters)
        {
            var networkService = Application.GetService<NetworkService>();
            networkService.Http.RegisterHttpResponseHandler<TestGetResponse>(this);
            networkService.Http.RegisterHttpResponseHandler<TestPostResponse>(this);
            networkService.Tcp.Dispatcher.RegisterTcpProtocolHandler<TestTcpResponse>(this);

            _view = await LaunchView.LoadAsync();

            var dict = new Dictionary<string, Action>()
            {
                [nameof(TestDownload)] = () => ExecuteTask(TestDownload),
                [nameof(Presenter)] = () => ExecuteTask(Presenter),
                [nameof(Dialog)] = () => ExecuteTask(Dialog),
                [nameof(DialogWithError)] = () => ExecuteTask(DialogWithError),
                [nameof(HttpGet)] = () => ExecuteTask(HttpGet),
                [nameof(Tcp)] = () => ExecuteTask(Tcp),
                [nameof(Websocket)] = () => ExecuteTask(Websocket),
            };

            _view.Initialize(dict);
            int sec = 0;
            Application.GetService<TimerService>().EverySecond.Subscribe(_ => { _view.SetMessage(sec++.ToString()); })
                .AddTo(_disposables);


            return TransitionResult.Ok;
        }

        private async Task DialogWithError()
        {
            var locator = PresenterLocator.Create<TestDialogPresenter>();
            var result = await Application.GetService<UIService>().SwitchPresenterAsync(locator);
            if (result.Type == TransitionResult.ResultType.Successful)
            {
                await MyTaskExtension.Execute(TestDownload);
            }
        }

        public async void Presenter()
        {
            var locator = PresenterLocator.Create<TestPresenter>();
            locator.Parameters.Put("title", "1");
            var result = await Application.GetService<UIService>().SwitchPresenterAsync(locator);
            Debug.Log("switch test presenter result " + result);
        }

        public async Task TestDownload()
        {
            var j = 0;
            for (var i = 0; i < 100; i++)
            {
                var downloader = Application.GetService<AssetService>().downloader;
                var uri = new Uri($"https://www.google.com/search?q={i}");
                var path = $@"E:\repo\unity\saki_formal_2020\Assets\StreamingAssets\{i}.html";
                downloader.OnDownloadEvent.Where(e => e.uri == uri).Subscribe(e =>
                {
                    j++;
                    Debug.LogError("finished on @" + e.uri);
                }).AddTo(_disposables);
                downloader.CreateDownload(uri, path);
            }

            while (j < 100)
            {
                await Task.Yield();
            }
        }

        public async void Dialog()
        {
            var locator = PresenterLocator.Create<TestDialogPresenter>();
            locator.Parameters.Put("title", "dialog test");
            await Application.GetService<UIService>().SwitchPresenterAsync(locator);
        }

        public async void HttpGet()
        {
            Debug.Log("HttpGet clicked");
            var response = await Application.GetService<NetworkService>().Http.SendAsync(new TestGetRequest());
            Debug.Log("http get response " + JsonConvert.SerializeObject(response));
        }

        public async void HttpPost()
        {
            var response = await Application.GetService<NetworkService>().Http.SendAsync(new TestPostRequest());
            Debug.Log("http post response " + JsonConvert.SerializeObject(response));
        }

        public async void Tcp()
        {
            await Application.GetService<NetworkService>().Tcp.ConnectAsync();
            var result = await Application.GetService<NetworkService>().Tcp.SendAsync(new TestTcpRequest());
            Debug.Log("TcpTest send request result: " + result.ToString());
        }

        public async void Websocket()
        {
            var b = false;
            var webSocket = Application.GetService<NetworkService>().WebSocket;
            if (!b)
            {
                b = true;
                await webSocket.ConnectAsync(new Uri(
                    "wss://demo.piesocket.com/v3/channel_1?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self"));
                // "ws://127.0.0.1:1111"));
            }

            _ = webSocket.SendAsync("message from my framework. 中文测试! 😄");
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
        }
    }
}