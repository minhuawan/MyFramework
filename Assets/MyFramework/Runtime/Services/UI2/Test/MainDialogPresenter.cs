namespace MyFramework.Runtime.Services.UI2
{
    public class MainDialogPresenter : DialogPresenter
    {
        public override void OnCreated(MvpContext context)
        {
            InstantiateView<MainDialogView>(context);
        }

        public override void WillAppear()
        {
            view.As<MainDialogView>().SetData(context.model.As<MainModel>());
            base.WillAppear();
        }
    }
}