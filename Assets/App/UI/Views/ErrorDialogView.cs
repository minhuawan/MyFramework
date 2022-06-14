using System;
using System.Threading.Tasks;
using MyFramework.Services.UI;
using UniRx;
using UnityEngine;
using UnityEngine.UI;
using Application = MyFramework.Application;

namespace App.UI.Views
{
    
    [ViewPath("Assets/AppData/Prefab/UI/View/ErrorDialogView")]
    public class ErrorDialogView : DialogView
    {
        [SerializeField] private Text message;
        [SerializeField] private ButtonView button;

        public IObservable<Unit> CloseEvent => button.OnClickObservable;
        public IObservable<Unit> CopyEvent => button.OnLongClickObservable;

        public static async Task<ErrorDialogView> LoadAsync()
        {
            return await Application.GetService<UIService>().InstantiateViewAsync<ErrorDialogView>();
        }

        public void Initialize(string msg)
        {
            message.text = msg;
        }
    }
}