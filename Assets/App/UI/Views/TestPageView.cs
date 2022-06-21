using MyFramework.Runtime.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace App.UI.Views
{
    [ViewPath("Assets/AppData/Prefab/UI/View/TestPageView")]
    public class TestPageView : NavigatedView
    {
        [SerializeField] private Text text;

        public void SetTitle(string title)
        {
            text.text = title;
        }
    }
}