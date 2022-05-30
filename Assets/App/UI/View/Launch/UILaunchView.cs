using MyFramework.Services.Event;
using MyFramework.Services.UI;
using UnityEngine;
using UnityEngine.Serialization;
using UnityEngine.UI;

namespace App.UI.View.Launch
{
    public class UILaunchView : UIView
    {
        [SerializeField] private Text text;
        [SerializeField] private Button buttonAsync;
        [SerializeField] private Button buttonSync;

        public ObservableEvent<Button> AsyncClick = new ObservableEvent<Button>();
        public ObservableEvent<Button> SyncClick = new ObservableEvent<Button>();

        public void Initialize()
        {
            buttonAsync.onClick.AddListener(() => AsyncClick.OnNext(buttonAsync));
            buttonSync.onClick.AddListener(() => SyncClick.OnNext(buttonAsync));
        }

        public void SetMessage(string message)
        {
            text.text = message;
        }
    }
}