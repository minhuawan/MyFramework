using MyFramework.Runtime.Services.UI;
using UnityEngine;

namespace App.UI.Views
{
    [ViewPath("Assets/AppData/Prefab/UI/View/MainPageView")]
    public class MainPageView : NavigatedView
    {
        [SerializeField] private ButtonView buttonDialog;
        [SerializeField] private ButtonView buttonTestPage;

        public ButtonView.ButtonEvent DialogEvent => buttonDialog.onClick;
        public ButtonView.ButtonEvent TestPageEvent => buttonTestPage.onClick;
    }
}