using MyFramework.Runtime.Services.UI;

namespace MyFramework.Runtime.Services.Event.UI
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