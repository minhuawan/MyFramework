using App.UI.Views;
using MyFramework.Runtime.Services.UI;
using UnityEngine;

namespace App.UI.Presenters
{
    public class TestPagePresenter : NavigatedPresenter
    {
        private TestPageView view;

        public override void OnNavigate(PresenterLocator locator)
        {
            this.locator = locator;
            InstantiateViewAsyncWithNavigateResult<TestPageView>();
        }

        public override void OnWillAppear()
        {
            view = View.As<TestPageView>();
            view.SetTitle(locator.Parameters.Get<string>("title"));
            base.OnWillAppear();
        }
    }
}