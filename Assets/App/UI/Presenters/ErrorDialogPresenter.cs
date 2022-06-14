using System;
using System.Threading.Tasks;
using App.UI.Views;
using MyFramework.Services.UI;
using UniRx;
using UnityEngine;

namespace App.UI.Presenters
{
    public class ErrorDialogPresenter : DialogPresenter
    {
        public static readonly string MessageKey = "MessageKey";
        private ErrorDialogView _view;
        public override View View => _view;
        public override IObservable<Unit> CloseEvent => _view.CloseEvent;

        public override async Task<TransitionResult> LoadAsync(PresenterLocatorParameters parameters)
        {
            _view = await ErrorDialogView.LoadAsync();
            _view.Initialize(parameters.Get(MessageKey, "UNKNOWN ERROR"));
            _view.CopyEvent.Subscribe(_ =>
            {
                GUIUtility.systemCopyBuffer = parameters.Get(MessageKey, "");
            });
            await _view.AppearAsync();
            return TransitionResult.Ok;
        }
    }
}