// using System;
// using System.Threading.Tasks;
// using MyFramework.Services.UI;
// using UniRx;
// using UnityEngine;
// using UnityEngine.UI;
//
// namespace App.UI.Views
// {
//     [ViewPath("Assets/AppData/Prefab/UI/View/TestDialogView")]
//     public class TestDialogView : DialogView
//     {
//         [SerializeField] private Text title;
//         [SerializeField] private ButtonView button;
//
//         public IObservable<Unit> CloseEvent => button.OnClickObservable;
//         public static Task<TestDialogView> LoadAsync() => Instantiate<TestDialogView>();
//
//         public void SetTitle(string txt)
//         {
//             title.text = txt;
//         }
//     }
// }