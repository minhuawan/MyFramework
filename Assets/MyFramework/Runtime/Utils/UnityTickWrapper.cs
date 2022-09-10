using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Utils
{
    public class UnityTickWrapper : MonoBehaviour, IDisposable
    {
        private static Dictionary<int, UnityTickWrapper> createdInstances = new Dictionary<int, UnityTickWrapper>();

        public static UnityTickWrapper Create(string name)
        {
            var go = new GameObject(name);
            var wrapper = go.AddComponent<UnityTickWrapper>();
            DontDestroyOnLoad(wrapper);
            createdInstances[wrapper.GetInstanceID()] = wrapper;
            return wrapper;
        }

        public static void ForceCleanAll()
        {
            foreach (var w in createdInstances)
            {
                w.Value.updateTick = null;
                Destroy(w.Value.gameObject);
            }

            createdInstances.Clear();
        }

        public void Dispose()
        {
            createdInstances.Remove(this.GetInstanceID());
            Destroy(this.gameObject);
        }

        public delegate void UpdateTick(float realtimeSinceStartup, float deltaTime);

        private UpdateTick updateTick;
        private int rate = 1; // 1 frame 1 call, if rate is 2, 2 frame 1 call
        private int frame = 0;

        public void BindUpdateTick(UpdateTick tick, int rate)
        {
            Debug.Assert(rate >= 1, "rate >= 1");
            updateTick = tick;
            this.rate = rate;
            frame = 0;
        }

        private void Update()
        {
            if (updateTick != null)
            {
                if (rate > 1 && frame < rate)
                {
                    frame++;
                    return;
                }

                frame = 0;
                updateTick.Invoke(Time.realtimeSinceStartup, Time.deltaTime);
            }
        }
    }
}