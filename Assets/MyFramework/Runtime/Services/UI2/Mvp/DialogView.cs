using System;
using DG.Tweening;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public class DialogView : View
    {
        [SerializeField] private RectTransform window;

        public virtual void ShowAsync(Action onCompleted)
        {
            window.DOLocalMove(new Vector3(0, -900, 0), 0.2f)
                .From(true)
                .SetEase(Ease.OutCirc)
                .OnStart(() => this.gameObject.SetActive(true))
                .OnComplete(() => onCompleted?.Invoke());
        }

        public virtual void HideAsync(Action onCompleted)
        {
            window.DOLocalMove(new Vector3(0, -900, 0), 0.2f)
                .SetEase(Ease.InOutCirc)
                .OnComplete(() =>
                {
                    this.gameObject.SetActive(false);
                    onCompleted?.Invoke();
                });
        }
    }
}