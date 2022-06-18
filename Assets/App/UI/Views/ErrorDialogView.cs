// using System;
// using System.Threading.Tasks;
// using MyFramework.Services.UI;
// using UnityEngine;
// using UnityEngine.Events;
// using UnityEngine.UI;
// using Application = MyFramework.Application;
//
// namespace App.UI.Views
// {
//     [ViewPath("Assets/AppData/Prefab/UI/View/ErrorDialogView")]
//     public class ErrorDialogView : DialogView
//     {
//         [SerializeField] private Text message;
//         [SerializeField] private ButtonView button;
//
//         public UnityEvent CloseEvent => button.onClick;
//         public UnityEvent LongClickEvent => button.onLongClick;
//
//         public static async Task<ErrorDialogView> LoadAsync()
//         {
//             return await Application.GetService<UIService>().InstantiateView<ErrorDialogView>();
//         }
//
//         public void Initialize(string msg)
//         {
//             message.text = msg;
//         }
//     }
// }