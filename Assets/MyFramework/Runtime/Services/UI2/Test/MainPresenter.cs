using DG.Tweening;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public class MainPresenter : Presenter
    {
        private MainView _view;

        public override void OnCreated(MvpContext context)
        {
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
        }
    }
}