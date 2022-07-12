using DG.Tweening;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public class MainPresenter : RootPresenter
    {
        private MainView _view;
        private MainModel model;

        public override void OnCreated(MvpContext context)
        {
            model = new MainModel();
            model.DialogTitle = "hello world";
            // send network message
            InstantiateView<MainView>(context);
        }

        public override void WillAppear()
        {
            _view = view.As<MainView>();
            
            _view.OnClick.AddListener(OnClick);
            
            _view.transform.DOMove(Vector3.zero, 0.2f).SetDelay(3f).OnComplete(base.WillAppear);
        }

        private void OnClick()
        {
            Application.GetService<UIService>().ShowDialog<MainDialogPresenter>(model);
        }
    }
}