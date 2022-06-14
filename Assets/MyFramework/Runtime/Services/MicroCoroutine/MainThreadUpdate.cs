using System;
using UnityEngine;

namespace MyFramework.Services
{
    public class MainThreadUpdate : MonoBehaviour
    {
        public Action onUpdate;

        void Update()
        {
            if (onUpdate != null)
            {
                onUpdate();
            }
        }
    }
}