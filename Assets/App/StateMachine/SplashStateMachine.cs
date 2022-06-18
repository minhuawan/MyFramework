using App.UI.Presenters;
using MyFramework;
using MyFramework.Runtime.Services.StateMachine;
using MyFramework.Runtime.Services.Timer;
using MyFramework.Runtime.Services.UI;

namespace App.StateMachine
{
    public class SplashStateMachine : AStateMachine
    {
        public override void OnEnter(StateMachineContext context)
        {
            Application.GetService<TimerService>().everyFrame.AddListener(OnUpdate);
            // await Application.GetService<UIService>().NavigateLocator(PresenterLocator.Launch);
            OpenSplash();
        }

        public override void OnExit(StateMachineContext context)
        {
            Application.GetService<TimerService>().everyFrame.RemoveListener(OnUpdate);
        }

        private void OnUpdate()
        {
            // if (Input.GetKeyDown(KeyCode.Space))
            // {
            // }
        }

        private void OpenSplash()
        {
            Application.GetService<TimerService>().everyFrame.RemoveListener(OnUpdate);
            Application.GetService<UIService>()
                .NavigateTo(
                    PresenterLocator.Create<SplashPresenter>(
                        new PresenterLocatorParameters()
                            .Put("welcome", "splash view")
                            .Put("afterWelcome", "YOU ARE WINNER!")
                    ));
        }
    }
}