// using System;
// using System.Threading.Tasks;
// using App.UI.Views;
// using MyFramework.Services.UI;
// using UnityEngine;
//
// namespace App.UI.Presenters
// {
//     public class ErrorDialogPresenter : DialogPresenter
//     {
//         public static readonly string MessageKey = "MessageKey";
//         private ErrorDialogView _view;
//         public override View View => _view;
//
//         public override async Task<NavigateResult> LoadAsync(PresenterLocatorParameters parameters)
//         {
//             _view = await ErrorDialogView.LoadAsync();
//             _view.Initialize(parameters.Get(MessageKey, "UNKNOWN ERROR"));
//             _view.LongClickEvent.AddListener(() => { GUIUtility.systemCopyBuffer = parameters.Get(MessageKey, ""); });
//             await _view.AppearAsync();
//             return NavigateResult.Ok;
//         }
//     }
// }