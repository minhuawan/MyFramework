using MyFramework.Runtime.Services.UI;

namespace MyFramework.Runtime.Services.Event.UI
{
    public class NavigateResultEvent : MyFramework.Runtime.Services.Event.Event
    {
        public NavigateResult Result { get; private set; }
        public NavigatedPresenter Presenter { get; private set; }

        public bool IsDialogEvent { get; }


        public NavigateResultEvent(NavigateResult result, NavigatedPresenter presenter, PresenterLocator locator,
            bool isDialogEvent)
        {
            Result = result;
            Presenter = presenter;
            IsDialogEvent = isDialogEvent;
        }
    }
}