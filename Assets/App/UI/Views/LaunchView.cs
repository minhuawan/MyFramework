using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using MyFramework.Services.UI;
using UniRx;
using UnityEngine;
using UnityEngine.UI;
using Application = MyFramework.Application;

namespace App.UI.Views.Launch
{
    [ViewPath("Assets/AppData/Prefab/UI/View/LaunchView")]
    public class LaunchView : View
    {
        [SerializeField] private Text text;
        [SerializeField] private ButtonView buttonView;
        public static async Task<LaunchView> LoadAsync()
        {
            return await Application.GetService<UIService>().InstantiateViewAsync<LaunchView>();
        }

        public void Initialize(Dictionary<string, Action> events)
        {
            var parent = buttonView.transform.parent;
            foreach (var kvp in events)
            {
                var button = GameObject.Instantiate(buttonView, parent);
                button.transform.GetComponentInChildren<Text>().text = kvp.Key;
                button.OnClickObservable.Subscribe(_ => kvp.Value());
            }
            buttonView.gameObject.SetActive(false);
        }

        public void SetMessage(string message)
        {
            text.text = message;
        }
    }
}