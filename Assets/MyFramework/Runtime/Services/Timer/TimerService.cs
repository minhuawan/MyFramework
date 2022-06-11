using System;
using UniRx;
using UnityEngine;

namespace MyFramework.Services.Timer
{
    public class TimerService : AbstractService
    {
        private double time = 0d;
        private uint seconds = 1;

        private Subject<Unit> secondSubject = new Subject<Unit>();
        private Subject<Unit> frameSubject = new Subject<Unit>();

        public IObservable<Unit> EverySecond => secondSubject;
        public IObservable<Unit> EveryFrame => frameSubject;

        private TimeKeeper keeper;

        public override void OnCreated()
        {
            var go = new GameObject($"{this.GetType().FullName}");
            GameObject.DontDestroyOnLoad(go);
            keeper = go.AddComponent<TimeKeeper>();
            keeper.UpdateAction += OnUpdateTick;
        }

        public override void OnDestroy()
        {
            secondSubject.Dispose();
            frameSubject.Dispose();

            GameObject.Destroy(keeper.gameObject);
            keeper = null;
            time = 0d;
            seconds = 1;
        }

        private void OnUpdateTick()
        {
            frameSubject.OnNext(Unit.Default);
            time = Time.realtimeSinceStartupAsDouble;
            if (time > seconds)
            {
                seconds++;
                secondSubject.OnNext(Unit.Default);
            }

            if (seconds > short.MaxValue)
            {
                seconds -= (uint) short.MaxValue;
                time -= short.MaxValue;
            }
        }
    }
}