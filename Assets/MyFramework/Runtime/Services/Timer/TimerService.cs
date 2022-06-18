using UnityEngine;
using UnityEngine.Events;

namespace MyFramework.Runtime.Services.Timer
{
    public class TimerService : AbstractService
    {
        private double time = 0d;
        private uint seconds = 1;

        public UnityEvent everySecond = new UnityEvent();
        public UnityEvent everyFrame = new UnityEvent();
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
            everySecond.RemoveAllListeners();
            everyFrame.RemoveAllListeners();

            GameObject.Destroy(keeper.gameObject);
            keeper = null;
            time = 0d;
            seconds = 1;
        }

        private void OnUpdateTick()
        {
            everyFrame.Invoke();
            time = Time.realtimeSinceStartupAsDouble;
            if (time > seconds)
            {
                seconds++;
                everySecond.Invoke();
            }

            if (seconds > short.MaxValue)
            {
                seconds -= (uint) short.MaxValue;
                time -= short.MaxValue;
            }
        }
    }
}