using MyFramework.Runtime.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace App.UI.Views
{
    [ViewPath("Assets/AppData/Prefab/UI/View/MainPageView")]
    public class MainPageView : NavigatedView
    {
        [SerializeField] private ButtonView buttonDialog;
        [SerializeField] private ButtonView buttonTestPage;
        [SerializeField] private Text title;

        public ButtonView.ButtonEvent DialogEvent => buttonDialog.onClick;
        public ButtonView.ButtonEvent TestPageEvent => buttonTestPage.onClick;

        public void SetTitle(string content)
        {
            title.text = content;
        }
    }
}