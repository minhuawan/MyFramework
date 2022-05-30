using MyFramework.Services.Event;
using MyFramework.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace App.UI.View.Launch
{
    public class UILaunchView : UIView
    {
        [SerializeField] private Text text;
        [SerializeField] private Button button;

        public ObservableEvent<Button> ButtonClick = new ObservableEvent<Button>();

        public void Initialize()
        {
            button.onClick.AddListener(() => ButtonClick.OnNext(button));
        }

        public void SetMessage(string message)
        {
            text.text = message;
        }
    }
}