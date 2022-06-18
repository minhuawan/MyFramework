using System;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI
{
    public enum WindowLayer
    {
        MostBottom = 1,
        Normal = 1000,
        Middle = 2000,
        Modal = 3000,
        MostTop = 9000,
    }

    [RequireComponent(typeof(UnityEngine.Camera))]
    public class WindowCamera : MonoBehaviour
    {
        [SerializeField] private Camera windowCamera;
        [SerializeField] private WindowLayer layer = WindowLayer.Normal;

        private void Awake()
        {
            var rawDepth = (int) windowCamera.depth;
            if (rawDepth >= 1000)
            {
                Debug.LogError("window camera depth raw value should less than 1000!");
                rawDepth = rawDepth % 1000;
            }
            Depth = (int) layer + (int) rawDepth;
        }

        public Camera Camera => windowCamera;
        public WindowLayer Layer => layer;
        private int depth;

        public int Depth
        {
            get => depth;
            set
            {
                depth = value;
                windowCamera.depth = depth;
            }
        }
    }
}