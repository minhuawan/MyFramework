using System;
using DG.Tweening;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.EventSystems;
using UnityEngine.UI;

namespace MyFramework.Runtime.Services.UI
{
    public class ButtonView : MonoBehaviour,
        IPointerDownHandler,
        IPointerUpHandler,
        IPointerClickHandler,
        IPointerExitHandler
    {
        public class ButtonEvent : UnityEvent
        {
        }

        public static readonly float LongClickTimeSecond = 0.3f;
        protected bool hasPlayExitTween;
        protected double pointerDownTime;


        public ButtonEvent onClick = new ButtonEvent();
        public ButtonEvent onPointerUp = new ButtonEvent();
        public ButtonEvent onPointerDown = new ButtonEvent();
        public ButtonEvent onLongClick = new ButtonEvent();

        void IPointerDownHandler.OnPointerDown(PointerEventData eventData)
        {
            onPointerDown.Invoke();
            pointerDownTime = UnityEngine.Time.realtimeSinceStartup;
            hasPlayExitTween = false;
            PlayTween(true);
        }


        void IPointerUpHandler.OnPointerUp(PointerEventData eventData)
        {
            onPointerUp.Invoke();
            if (UnityEngine.Time.realtimeSinceStartup - pointerDownTime >= LongClickTimeSecond)
            {
                onLongClick.Invoke();
            }

            if (!hasPlayExitTween)
            {
                hasPlayExitTween = true;
                PlayTween(false);
            }
        }

        void IPointerClickHandler.OnPointerClick(PointerEventData eventData)
        {
            onClick.Invoke();
        }

        void IPointerExitHandler.OnPointerExit(PointerEventData eventData)
        {
            if (!hasPlayExitTween)
            {
                hasPlayExitTween = true;
                PlayTween(false);
            }
        }

        private void PlayTween(bool enter)
        {
            if (transform == null)
                return;
            if (enter)
            {
                transform.DOScale(0.94f, 0.07f);
            }
            else
            {
                transform.DOScale(1f, 0.25f);
            }
        }

        private void OnDestroy()
        {
            DOTween.Kill(transform);
        }
    }
}