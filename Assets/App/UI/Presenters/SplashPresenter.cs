using App.StateMachine;
using App.UI.Views;
using MyFramework;
using MyFramework.Runtime.Services.StateMachine;
using MyFramework.Runtime.Services.UI;

namespace App.UI.Presenters
{
    public class SplashPresenter : NavigatedPresenter
    {
        private SplashView view;

        public override void OnNavigate(PresenterLocator locator)
        {
            this.locator = locator;
            InstantiateViewAsyncWithNavigateResult<SplashView>();
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