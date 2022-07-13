namespace MyFramework.Runtime.Services.UI2
{
    public class MainPresenter : RootPresenter
    {
        private MainView _view;
        private MainModel model;

        public override void Initialize(MvpContext context)
        {
            model = new MainModel();
            model.DialogTitle = "hello world";
            // send network message
            InstantiateViewAsync<MainView>(context, view =>
            {
                _view = view;
                _view.OnClick.AddListener(OnClick);
                context.MoveNextState();
            });
        }

        private void OnClick()
        {
            Application.GetService<UIService>().ShowDialog<MainDialogPresenter>(model);
        }
    }
}