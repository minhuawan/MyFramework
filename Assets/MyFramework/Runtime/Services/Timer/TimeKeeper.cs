using System;
using UniRx;
using UnityEngine;

namespace MyFramework.Services.Timer
{
    public class TimeKeeper : MonoBehaviour
    {
        private Subject<float> subject = new Subject<float>();
        public IObservable<float> OnUpdateEvent => subject;

        private void Update()
        {
            subject.OnNext(Time.deltaTime);
        }
    }
}