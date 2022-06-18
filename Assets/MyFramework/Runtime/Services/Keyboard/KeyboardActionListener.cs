using System;
using UnityEngine;

namespace MyFramework.Runtime.Services
{
    public class KeyboardActionListener : IDisposable
    {
        private Action action;
        private KeyCode keyCode;

        public KeyboardActionListener(Action action, KeyCode keyCode)
        {
            this.action = action;
            this.keyCode = keyCode;
        }

        public void Dispose()
        {
            Application.GetService<KeyboardService>().UnregisterDown(this);
        }

        public void Invoke()
        {
            action.Invoke();
        }
    }
}