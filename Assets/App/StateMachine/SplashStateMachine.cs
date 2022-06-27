using App.UI.Presenters;
using MyFramework;
using MyFramework.Runtime.Services.Localization;
using MyFramework.Runtime.Services.StateMachine;
using MyFramework.Runtime.Services.Timer;
using MyFramework.Runtime.Services.UI;

namespace App.StateMachine
{
    public class SplashStateMachine : AbstructStateMachine
    {
        public override void OnEnter(StateMachineContext context)
        {
            Application.GetService<TimerService>().everyFrame.AddListener(OnUpdate);

            Application.GetService<UIService>().ClearHistory();
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
            var localizationService = Application.GetService<LocalizationService>();
            var gameName = localizationService.Translate("const", "game-name");
            var welcome = localizationService.Translate("ui.base", "welcome");
            Application.GetService<UIService>()
                .NavigateTo(
                    PresenterLocator.Create<SplashPresenter>(
                        new PresenterLocatorParameters()
                            .Put("welcome", gameName)
                            .Put("afterWelcome", welcome)
                    ));
        }
    }
}