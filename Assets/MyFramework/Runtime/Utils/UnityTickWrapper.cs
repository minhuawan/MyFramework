using System;
using System.Collections;
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
            foreach(var w in createdInstances)
            {
                w.Value.updateTick = null;
                GameObject.Destroy(w.Value.gameObject);
            }
            createdInstances.Clear();
        }

        public void Dispose()
        {
            createdInstances.Remove(this.GetInstanceID());
            GameObject.Destroy(this.gameObject);
        }

        public delegate void UpdateTick(float time);

        private UpdateTick updateTick;
        public void BindUpdateTick(UpdateTick tick)
        {
            updateTick = tick;
        }

        private void Update()
        {
            if (updateTick != null)
            {
                updateTick.Invoke(Time.deltaTime);
            }
        }
    }
}