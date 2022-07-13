namespace MyFramework.Runtime.Services.UI2
{
    public class DialogPresenter : Presenter
    {
        protected DialogView dialogView => base.view as DialogView;

        public override void OnBackKey()
        {
        }
    }
}