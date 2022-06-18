using MyFramework.Runtime.Services.UI;

namespace App.Event.Navigation
{
    public class NavigatedViewAppearCompletedEvent : MyFramework.Runtime.Services.Event.Event
    {
        public NavigatedView View { get; private set; }

        public NavigatedViewAppearCompletedEvent(NavigatedView view)
        {
            View = view;
        }
    }
}