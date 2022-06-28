using System.IO;
using App.StateMachine;
using App.UI.Views;
using MyFramework.Runtime.Services.StateMachine;
using MyFramework.Runtime.Services.UI;
using MyFramework.Runtime.Services.VirtualFileSystem;
using UnityEngine;
using Application = MyFramework.Application;

namespace App.UI.Presenters
{
    public class SplashPresenter : NavigatedPresenter
    {
        private SplashView view;

        public override void OnNavigate(PresenterLocator locator)
        {
            this.locator = locator;
            InstantiateViewAsyncWithNavigateResult<SplashView>();

            var virtualFileSystemService = Application.GetService<VirtualFileSystemService>();
            var path = virtualFileSystemService.GetVFSPath("test/hello.txt");
            Debug.Log($"test/hello.txt --> {path}");
            if (File.Exists(path))
            {
                Debug.Log(File.ReadAllText(path));
            }
            else
            {
                File.WriteAllText(path, "I am hello.txt");
            }
        }

        public override void OnWillAppear() // 这里直接把 view 传递过来？
        {
            view = View.As<SplashView>();
            var parameters = locator.Parameters;
            var welcome = parameters.Get<string>("welcome");
            var afterWelcome = parameters.Get<string>("afterWelcome");
            view.Initialize(welcome, afterWelcome);
            view.ButtonClickEvent.AddListener(OnButtonClick);
            base.OnWillAppear();
        }

        public override void OnWillDisappear()
        {
            view.ButtonClickEvent.RemoveListener(OnButtonClick);
            base.OnWillDisappear();
        }

        private void OnButtonClick()
        {
            var stateMachineService = Application.GetService<StateMachineService>();
            stateMachineService.ChangeState<MainStateMachine>();
        }
    }
}