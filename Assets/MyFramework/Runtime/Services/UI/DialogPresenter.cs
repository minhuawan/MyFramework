using MyFramework.Runtime.Services.Event.UI;

namespace MyFramework.Runtime.Services.UI
{
    public abstract class DialogPresenter : NavigatedPresenter
    {
        public override bool IsDialog => true;

        public override void OnBack()
        {
            // 这里不做任何事情，由子类具体实现
        }

        protected void DispatchDialogCompletedEvent()
        {
            new DialogPresenterCompletedEvent(this).Dispatch();
        }
    }
}