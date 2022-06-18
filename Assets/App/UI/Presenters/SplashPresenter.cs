using App.UI.Views;
using MyFramework.Runtime.Services.UI;

namespace App.UI.Presenters
{
    public class SplashPresenter : NavigatedPresenter
    {
        public override void OnNavigate(PresenterLocator locator)
        {
            this.locator = locator;
            InstantiateViewAsyncWithNavigateResult<SplashView>();
        }

        public override void OnWillAppear()
        {
            var view = View.As<SplashView>();
            var parameters = locator.Parameters;
            var welcome = parameters.Get<string>("welcome");
            var afterWelcome = parameters.Get<string>("afterWelcome");
            view.SetTexts(welcome, afterWelcome);
            base.OnWillAppear();
        }
    }
}