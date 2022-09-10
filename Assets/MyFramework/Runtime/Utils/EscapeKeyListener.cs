using System;
using UnityEngine;

namespace MyFramework.Runtime.Utils
{
    public class EscapeKeyListener : MonoBehaviour, IDisposable
    {
        public Action listener;
        private void Update()
        {
            if (Input.GetKeyDown(KeyCode.Escape))
            {
                if (listener != null)
                {
                    listener();
                }
            }
        }

        public void Dispose()
        {
            listener = null;
            GameObject.Destroy(this.gameObject);
        }

        public static EscapeKeyListener Create(string name, Action action)
        {
            var go = new GameObject(name);
            DontDestroyOnLoad(go);
            var escapeKeyListener = go.AddComponent<EscapeKeyListener>();
            escapeKeyListener.listener = action;
            return escapeKeyListener;
        }
    }
}