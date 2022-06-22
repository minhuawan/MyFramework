using MyFramework.Runtime.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace App.UI.Views
{
    [ViewPath("Assets/AppData/Prefab/UI/View/ErrorNoticeDialogView")]
    public class ErrorNoticeDialogView : DialogView
    {
        [SerializeField] private Text notice;
        [SerializeField] private ButtonView closeButton;


        public ButtonView.ButtonEvent CloseClickEvent => closeButton.onClick;

        public void SetNotice(string notice)
        {
            this.notice.text = notice;
        }
    }
}