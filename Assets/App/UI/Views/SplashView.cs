using MyFramework.Runtime.Services.UI;
using MyFramework.Utils;
using UnityEngine;
using UnityEngine.UI;

namespace App.UI.Views
{
    [ViewPath("Assets/AppData/Prefab/UI/View/SplashView")]
    public class SplashView : NavigatedView
    {
        [SerializeField] private RectTransform window;
        [SerializeField] private Animator animator;
        [SerializeField] private Text text;
        [SerializeField] private Image background;
        [SerializeField] private ButtonView buttonDialog;

        private string welcome;
        private string afterWelcome;

        public ButtonView.ButtonEvent ButtonClickEvent => buttonDialog.onClick;

        public void Initialize(string welcome, string afterWelcome)
        {
            this.welcome = welcome;
            this.afterWelcome = afterWelcome;
            text.text = welcome;
        }

        public override void OnWillAppear()
        {
            // var count = 0;
            // TweenCallback check = () =>
            // {
            //     count++;
            //     if (count >= 2)
            //     {
            //         base.OnWillAppear();
            //     }
            // };
            // var tween1 = window.DOLocalMove(new Vector3(0f, -800f), 0.25f)
            //     .From(true)
            //     .SetEase(Ease.OutCirc)
            //     .OnComplete(check);
            // var from = 0f;
            // var to = 0.75f;
            // var duration = 0.3f;
            // var graphic = background;
            // var color = graphic.color;
            // var fromColor = new Color(color.r, color.g, color.b, from);
            // var toColor = new Color(color.r, color.g, color.b, to);
            // var tween2 = graphic.DOColor(toColor, duration)
            //     .From(fromColor)
            //     .SetEase(Ease.OutQuad)
            //     .OnComplete(check);


            buttonDialog.gameObject.SetActive(false);
            AnimationEventAttach.Play(animator, "SplashViewOpen", base.OnWillAppear);
        }

        public override void OnDidAppear()
        {
            base.OnDidAppear();
            text.text = afterWelcome;
            buttonDialog.gameObject.SetActive(true);
        }
    }
}