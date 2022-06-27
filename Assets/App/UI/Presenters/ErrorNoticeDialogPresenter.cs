using App.UI.Views;
using MyFramework.Runtime.Services.UI;
using UnityEngine;

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

        public void OnCopy()
        {
            GUIUtility.systemCopyBuffer = locator.Parameters.Get<string>("message");
        }

        public override void OnWillAppear()
        {
            view = View.As<ErrorNoticeDialogView>();
            view.SetNotice(locator.Parameters.Get<string>("message"));
            view.CloseClickEvent.AddListener(OnBack);
            view.CopyClickEvent.AddListener(OnCopy);
            base.OnWillAppear();
        }

        public override void OnWillDisappear()
        {
            view.CloseClickEvent.RemoveListener(OnBack);
            view.CopyClickEvent.RemoveListener(OnCopy);
            base.OnWillDisappear();
        }
    }
}