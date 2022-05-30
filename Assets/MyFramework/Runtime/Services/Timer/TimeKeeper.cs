using System;
using MyFramework.Services.Event;
using UnityEngine;

namespace MyFramework.Services.Timer
{
    public class TimeKeeper : MonoBehaviour
    {
        public ObservableEvent<float> OnUpdateEvent = new ObservableEvent<float>(); 
        private void Update()
        {
            OnUpdateEvent.OnNext(Time.deltaTime);
        }
    }
}