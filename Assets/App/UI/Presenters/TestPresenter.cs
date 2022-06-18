// using System;
// using System.Threading;
// using System.Threading.Tasks;
// using App.UI.Views;
// using MyFramework.Services.Asset;
// using MyFramework.Services.UI;
// using UniRx;
// using UnityEngine;
// using Application = MyFramework.Application;
//
// namespace App.UI.Presenters
// {
//     public class TestPresenter : Presenter
//     {
//         private TestView view;
//         public override View View => view;
//
//         public override async Task<NavigateResult> LoadAsync(PresenterLocatorParameters parameters)
//         {
//             view = await TestView.LoadAsync();
//             view.Initialize(parameters.Get<string>("title"));
//             Initialize();
//             return NavigateResult.Ok;
//         }
//
//         public void Initialize()
//         {
//             for (var i = 0; i < 100; i++)
//             {
//                 var downloader = Application.GetService<AssetService>().downloader;
//                 var uri = new Uri($"https://www.baidu.com/s?ie=UTF-8&wd={i}");
//                 var path = $@"E:\repo\unity\saki_formal_2020\Assets\StreamingAssets\{i}.html";
//                 downloader.OnDownloadEvent.Where(e => e.uri == uri).Subscribe(e =>
//                 {
//                     Debug.LogError("finished on @" + e.uri);
//                 }).AddTo(_disposables);
//                 downloader.CreateDownload(uri, path);
//             }
//         }
//     }
// }