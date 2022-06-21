using MyFramework.Runtime.Services.UI;

namespace MyFramework.Runtime.Services.Event.UI
{
    public class DialogPresenterCompletedEvent : MyFramework.Runtime.Services.Event.Event
    {
        public DialogPresenter DialogPresenter { get; private set; }
        public bool IsDialogEvent => true;

        public DialogPresenterCompletedEvent(DialogPresenter dialogPresenter)
        {
            DialogPresenter = dialogPresenter;
        }
    }
}