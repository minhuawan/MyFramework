using MyFramework.Runtime.Services.Event.UI;

namespace MyFramework.Runtime.Services.UI
{
    public abstract class DialogPresenter : NavigatedPresenter
    {
        protected void DispatchDialogCompletedEvent()
        {
            new DialogPresenterCompletedEvent(this).Dispatch();
        }

        public abstract override void OnBack();
    }
}