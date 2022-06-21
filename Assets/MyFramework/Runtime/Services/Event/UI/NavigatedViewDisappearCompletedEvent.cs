using MyFramework.Runtime.Services.UI;

namespace MyFramework.Runtime.Services.Event.UI
{
    public class NavigatedViewDisappearCompletedEvent : MyFramework.Runtime.Services.Event.Event
    {
        public NavigatedView View { get; private set; }

        public bool IsDialogEvent { get; }

        public NavigatedViewDisappearCompletedEvent(NavigatedView view, bool isDialogEvent)
        {
            View = view;
            IsDialogEvent = isDialogEvent;
        }
    }
}