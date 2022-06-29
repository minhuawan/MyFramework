using DG.Tweening;
using MyFramework.Runtime.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace App.UI.Views
{
    [ViewPath("Assets/AppData/Prefab/UI/View/TestDialogView2")]
    public class TestDialogView2 : DialogView
    {
        [SerializeField] private RectTransform window;
        [SerializeField] private Text text;
        [SerializeField] private ButtonView backButton;


        public ButtonView.ButtonEvent ButtonClickedEvent => backButton.onClick;

        public void SetText(string text)
        {
            this.text.text = text;
        }

        public override void OnWillAppear()
        {
            window.DOLocalMove(new Vector3(0, -900, 0), 0.2f)
                .From(true)
                .SetEase(Ease.InBack)
                .OnComplete(base.OnWillAppear);
        }

        public override void OnWillDisappear()
        {
            window.DOLocalMove(new Vector3(0, -900, 0), 0.2f)
                .SetEase(Ease.OutBack)
                .OnComplete(base.OnWillDisappear);
        }
    }
}