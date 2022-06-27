using App.UI.Presenters;
using MyFramework;
using MyFramework.Runtime.Services.LocalStorage;
using MyFramework.Runtime.Services.StateMachine;
using MyFramework.Runtime.Services.UI;

namespace App.StateMachine
{
    public class MainStateMachine : AbstructStateMachine
    {
        public override void OnEnter(StateMachineContext context)
        {
            FetchUserData();
        }

        private void FetchUserData()
        {
            // here to fetch user data
            var userIdentification = "uuid-aa-bb-cc-dd-ee-ff";
            var localStorageService = Application.GetService<LocalStorageService>();
            localStorageService.CreateUserStorage(userIdentification);
            OpenMain();
        }

        public override void OnExit(StateMachineContext context)
        {
        }

        private void OpenMain()
        {
            var locator = PresenterLocator.Create<MainPagePresenter>();
            var uiService = Application.GetService<UIService>();
            uiService.ClearHistory();
            uiService.NavigateTo(locator);
        }
    }
}