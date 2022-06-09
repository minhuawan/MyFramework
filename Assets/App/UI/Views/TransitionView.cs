using System.Collections.Generic;
using System.Threading.Tasks;
using System.Timers;
using MyFramework.Services.Timer;
using MyFramework.Services.UI;
using UnityEngine;
using UnityEngine.UI;
using Application = MyFramework.Application;

namespace App.UI.Views
{
    [ViewPath("Assets/AppData/Prefab/UI/View/TransitionView")]
    public class TransitionView : View
    {
        [SerializeField] private Text loadingText;

        public static async Task<TransitionView> LoadAsync()
        {
            return await InstantiatePrefabAsync<TransitionView>();
        }

        public void SetLoadingText(string text)
        {
            loadingText.text = text;
        }
    }
}