using System;
using System.Threading.Tasks;
using App.UI.Views;
using MyFramework.Services.UI;
using UniRx;

namespace App.UI.Presenters
{
    public class TestDialogPresenter : DialogPresenter
    {
        private ISubject<Unit> closeSubject = new Subject<Unit>();
        private TestDialogView _view;
        public override View View => _view;
        public override IObservable<Unit> CloseEvent => closeSubject;

        public override async Task<TransitionResult> LoadAsync(PresenterLocatorParameters parameters)
        {
            _view = await TestDialogView.LoadAsync();
            _view.SetTitle(parameters.Get("title", "no title"));
            _view.CloseEvent.Subscribe(closeSubject);
            
            
            return TransitionResult.Ok;
        }
    }
}