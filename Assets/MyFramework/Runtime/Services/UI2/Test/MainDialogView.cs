using MyFramework.Runtime.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace MyFramework.Runtime.Services.UI2
{
    [UI2.ViewPath("UI/View/MainDialogView")]
    public class MainDialogView : DialogView
    {
        [SerializeField] private Text text;
        [SerializeField] private ButtonView backButton;

        public ButtonView.ButtonEvent OnBackClick => backButton.onClick;

        public void SetData(MainModel model)
        {
            text.text = model.DialogTitle;
        }
    }
}