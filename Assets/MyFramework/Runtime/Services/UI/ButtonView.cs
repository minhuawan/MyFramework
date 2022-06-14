using System;
using DG.Tweening;
using UniRx;
using UnityEngine;
using UnityEngine.EventSystems;

namespace MyFramework.Services.UI
{
    public class ButtonView : MonoBehaviour,
        IPointerDownHandler,
        IPointerUpHandler,
        IPointerClickHandler,
        IPointerExitHandler
    {
        public static readonly float LongClickTimeSecond = 0.3f;
        protected bool hasPlayExitTween;
        protected double pointerDownTime;

        protected Subject<Unit> pointerClickSubject = new Subject<Unit>();
        protected Subject<Unit> pointerDownSubject = new Subject<Unit>();
        protected Subject<Unit> pointerUpSubject = new Subject<Unit>();
        protected Subject<Unit> pointerLongClickSubject = new Subject<Unit>();

        public IObservable<Unit> OnClickObservable => pointerClickSubject;
        public IObservable<Unit> OnPointerUpObservable => pointerUpSubject;
        public IObservable<Unit> OnPointerDownObservable => pointerDownSubject;
        public IObservable<Unit> OnLongClickObservable => pointerLongClickSubject;


        void IPointerDownHandler.OnPointerDown(PointerEventData eventData)
        {
            pointerDownSubject.OnNext(Unit.Default);
            pointerDownTime = UnityEngine.Time.timeSinceLevelLoad;
            hasPlayExitTween = false;
            PlayTween(true);
        }


        void IPointerUpHandler.OnPointerUp(PointerEventData eventData)
        {
            pointerUpSubject.OnNext(Unit.Default);
            if (UnityEngine.Time.timeSinceLevelLoad - pointerDownTime >= LongClickTimeSecond)
            {
                pointerLongClickSubject.OnNext(Unit.Default);
            }

            if (!hasPlayExitTween)
            {
                hasPlayExitTween = true;
                PlayTween(false);
            }
        }

        void IPointerClickHandler.OnPointerClick(PointerEventData eventData)
        {
            pointerClickSubject.OnNext(Unit.Default);
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