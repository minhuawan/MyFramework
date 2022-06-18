using MyFramework.Runtime.Services.UI;

namespace App.Event.Navigation
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