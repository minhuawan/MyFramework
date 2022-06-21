using App.UI.Views;
using DG.Tweening;
using MyFramework.Runtime.Services.UI;

namespace App.UI.Presenters
{
    public class TestDialogPresenter2 : DialogPresenter
    {
        private TestDialogView2 view;

        public override void OnNavigate(PresenterLocator locator)
        {
            this.locator = locator;
            InstantiateViewAsyncWithNavigateResult<TestDialogView2>();
        }

        public override void OnWillAppear()
        {
            view = View.As<TestDialogView2>();
            view.SetText(this.locator.Parameters.Get<string>("title"));
            view.ButtonClickedEvent.AddListener(OnBackClick);
            base.OnWillAppear();
        }

        public override void OnWillDisappear()
        {
            view.ButtonClickedEvent.RemoveListener(OnBackClick);
            base.OnWillDisappear();
        }

        public override void OnBack() => OnBackClick();

        private void OnBackClick()
        {
            DispatchDialogCompletedEvent();
        }
    }
}