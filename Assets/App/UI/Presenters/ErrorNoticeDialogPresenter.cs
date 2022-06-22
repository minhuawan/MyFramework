using App.UI.Views;
using MyFramework.Runtime.Services.UI;

namespace App.UI.Presenters
{
    public class ErrorNoticeDialogPresenter : DialogPresenter
    {
        private ErrorNoticeDialogView view;

        public override void OnNavigate(PresenterLocator locator)
        {
            this.locator = locator;
            InstantiateViewAsyncWithNavigateResult<ErrorNoticeDialogView>();
        }

        public override void OnBack()
        {
            OnWillDisappear();
        }

        public override void OnWillAppear()
        {
            view = View.As<ErrorNoticeDialogView>();
            view.SetNotice(locator.Parameters.Get<string>("message"));
            view.CloseClickEvent.AddListener(OnBack);
            base.OnWillAppear();
        }

        public override void OnWillDisappear()
        {
            view.CloseClickEvent.RemoveListener(OnBack);
            base.OnWillDisappear();
        }
    }
}