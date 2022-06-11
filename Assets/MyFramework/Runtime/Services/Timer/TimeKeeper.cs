using System;
using UniRx;
using UnityEngine;

namespace MyFramework.Services.Timer
{
    public class TimeKeeper : MonoBehaviour
    {
        public Action UpdateAction;
        public Action FixedUpdateAction;

        private void Update()
        {
            UpdateAction?.Invoke();
        }

        private void FixedUpdate()
        {
            FixedUpdateAction?.Invoke();
        }
    }
}