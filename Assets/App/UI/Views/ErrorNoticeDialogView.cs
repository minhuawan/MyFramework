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
        [SerializeField] private ButtonView copyButton;


        public ButtonView.ButtonEvent CloseClickEvent => closeButton.onClick;
        public ButtonView.ButtonEvent CopyClickEvent => copyButton.onClick;

        public void SetNotice(string notice)
        {
            this.notice.text = notice;
        }
    }
}