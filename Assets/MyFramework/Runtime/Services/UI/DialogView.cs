namespace MyFramework.Runtime.Services.UI
{
    public abstract class DialogView : NavigatedView
    {
        public override void OnWillDisappear()
        {
            base.OnWillDisappear();
            // new DialogViewDisappearCompletedEvent(this).Dispatch();
        }
    }
}