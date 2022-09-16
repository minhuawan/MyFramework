using System;
using System.Collections.Generic;
using MyFramework.Runtime.Services.Event;
using MyFramework.Runtime.Services.Timer;
using UnityEngine;

namespace MyFramework.Runtime.Services.Keyboard
{
    public class KeyboardService : AbstractService
    {
        private Dictionary<KeyCode, List<KeyboardActionListener>> listeners;

        public override void Initialize()
        {
            listeners = new Dictionary<KeyCode, List<KeyboardActionListener>>();
            Application.GetService<TimerService>().everyFrame.AddListener(OnUpdate);
        }

        private void OnUpdate()
        {
            if (!Input.anyKeyDown)
                return;
            foreach (var keyValuePair in listeners)
            {
                try
                {
                    if (Input.GetKeyDown(keyValuePair.Key))
                    {
                        foreach (var keyboardActionListener in keyValuePair.Value)
                        {
                            keyboardActionListener.Invoke();
                        }
                    }
                }
                catch (Exception e)
                {
                    Debug.LogException(e);
                }
            }

            if (Input.GetKeyDown(KeyCode.Escape))
            {
                new BackKeyEvent().Dispatch();
            }
        }

        public IDisposable RegisterDown(KeyCode keyCode, Action action)
        {
            List<KeyboardActionListener> list = null;
            if (!listeners.ContainsKey(keyCode))
            {
                list = new List<KeyboardActionListener>();
                listeners[keyCode] = list;
            }

            var listener = new KeyboardActionListener(action, keyCode);
            list.Add(listener);
            return listener;
        }

        public void UnregisterDown(KeyboardActionListener keyboardActionListener)
        {
        }
    }
}