using MyFramework.Services.Event;
using UnityEngine;

namespace MyFramework.Services.Timer
{
    public class TimerService : AbstractService
    {
        private double time = 0d;
        private uint seconds = 1;
        public ObservableEvent<TimerService> EverySecond = new ObservableEvent<TimerService>();
        public ObservableEvent<TimerService> EveryFrame = new ObservableEvent<TimerService>();

        private TimeKeeper keeper;
        public override void OnCreated()
        {
            var go = new GameObject($"{this.GetType().FullName}");
            GameObject.DontDestroyOnLoad(go);
            keeper = go.AddComponent<TimeKeeper>();
            keeper.OnUpdateEvent.Subscribe(OnUpdateTick);
        }

        public override void OnDestroy()
        {
            GameObject.Destroy(keeper.gameObject);
            keeper = null;
            time = 0d;
            seconds = 1;
            EverySecond.Clear();
            EveryFrame.Clear();
        }

        private void OnUpdateTick(float deltaTime)
        {
            EveryFrame.OnNext(this);
            time += deltaTime;
            if (time > seconds)
            {
                seconds++;
                EverySecond.OnNext(this);
            }

            if (seconds > short.MaxValue)
            {
                seconds -= (uint)short.MaxValue;
                time -= short.MaxValue;
            }
        }
    }
}