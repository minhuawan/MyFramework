using MyFramework.Runtime.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace MyFramework.Runtime.Services.UI2
{
    [UI2.ViewPath("UI/View/MainView")]
    public class MainView : View
    {
        [SerializeField] private ButtonView button;
        [SerializeField] private Text text;

        public ButtonView.ButtonEvent OnClick => button.onClick;

        public void SetText(string title)
        {
            text.text = title;
        }
    }
}