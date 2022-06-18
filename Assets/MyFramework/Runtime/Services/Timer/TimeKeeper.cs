using System;
using UnityEngine;

namespace MyFramework.Runtime.Services.Timer
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