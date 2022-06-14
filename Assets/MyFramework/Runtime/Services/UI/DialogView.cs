using System.Threading.Tasks;
using DG.Tweening;
using UnityEngine;
using UnityEngine.UI;

namespace MyFramework.Services.UI
{
    public abstract class DialogView : View
    {
        [SerializeField] private RectTransform window;
        [SerializeField] private Image background;

        public override async Task AppearAsync()
        {
            var baskTask = base.AppearAsync();

            window.DOLocalMove(new Vector3(0f, -800f), 0.25f)
                .From(true)
                .SetEase(Ease.OutCirc);
            var from = 0f;
            var to = 0.75f;
            var duration = 0.3f;
            var graphic = background;
            var color = graphic.color;
            var fromColor = new Color(color.r, color.g, color.b, from);
            var toColor = new Color(color.r, color.g, color.b, to);
            await graphic.DOColor(toColor, duration)
                .From(fromColor)
                .SetEase(Ease.OutQuad).AsyncWaitForCompletion();
        }
    }
}