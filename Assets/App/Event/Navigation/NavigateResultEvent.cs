using MyFramework.Runtime.Services.UI;

namespace App.Event.Navigation
{
    public class NavigateResultEvent : MyFramework.Runtime.Services.Event.Event
    {
        public NavigateResult Result { get; private set; }
        public NavigatedPresenter Presenter { get; private set; }
        public PresenterLocator Locator { get; private set; }


        public NavigateResultEvent(NavigateResult result, NavigatedPresenter presenter, PresenterLocator locator)
        {
            Locator = locator;
            Result = result;
            Presenter = presenter;
        }
    }
}