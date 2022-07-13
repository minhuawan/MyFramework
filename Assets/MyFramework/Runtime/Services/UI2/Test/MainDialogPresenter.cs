namespace MyFramework.Runtime.Services.UI2
{
    public class MainDialogPresenter : DialogPresenter
    {
        private MainDialogView _view;

        public override void Initialize(MvpContext context)
        {
            InstantiateViewAsync<MainDialogView>(context, view =>
            {
                _view = view;
                _view.SetData(context.model.As<MainModel>());
                context.MoveNextState();
            });
        }

        public override void DidAppeared()
        {
            _view.ShowAsync(base.DidAppeared);
            _view.OnBackClick.AddListener(OnBackKey);
        }

        public override void OnBackKey()
        {
            _view.HideAsync(() => { context.MoveNextState(); });
        }
    }
}