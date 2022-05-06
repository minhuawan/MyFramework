using App.UI.View.Launch;
using MyFramework.Services.Event;
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
            launchView.Initialize("FUCK OFF");
            int sec = 0;
            Application.GetService<TimerService>().EverySecond.Subscribe(ts =>
            {
                launchView.Initialize(sec++.ToString());
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