using MyFramework.Runtime.Services.UI;

namespace MyFramework.Runtime.Services.Event.UI
{
    public class NavigatedViewDisappearCompletedEvent : MyFramework.Runtime.Services.Event.Event
    {
        public NavigatedView View { get; private set; }

        public NavigatedViewDisappearCompletedEvent(NavigatedView view)
        {
            View = view;
        }
    }
}