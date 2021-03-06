using App.UI.Views;
using MyFramework;
using MyFramework.Runtime.Services.Localization;
using MyFramework.Runtime.Services.LocalStorage;
using MyFramework.Runtime.Services.UI;

namespace App.UI.Presenters
{
    public class MainPagePresenter : NavigatedPresenter
    {
        private MainPageView view;

        public override void OnNavigate(PresenterLocator locator)
        {
            InstantiateViewAsyncWithNavigateResult<MainPageView>();
        }

        public override void OnWillAppear()
        {
            view = View.As<MainPageView>();
            view.DialogEvent.AddListener(OnDialog);
            view.TestPageEvent.AddListener(TestPage);

            var storage = Application.GetService<LocalStorageService>().User;
            storage.Get("playCount", out int count);
            storage.Set("playCount", ++count).Flush();
            var content = LocalizeText("welcome");
            var title = string.Format(content, count);
            view.SetTitle(title);
            base.OnWillAppear();
        }

        public override void OnWillDisappear()
        {
            view.DialogEvent.RemoveListener(OnDialog);
            view.TestPageEvent.RemoveListener(TestPage);
            base.OnWillDisappear();
        }

        private void TestPage()
        {
            var locator = PresenterLocator.Create<TestPagePresenter>(
                new PresenterLocatorParameters().Put("title", "test page title"));
            Application.GetService<UIService>().NavigateTo(locator);
        }

        private void OnDialog()
        {
            var locator = PresenterLocator.Create<TestDialogPresenter2>(
                new PresenterLocatorParameters().Put("title", "test dialog title"));
            Application.GetService<UIService>().NavigateTo(locator);
        }
    }
}