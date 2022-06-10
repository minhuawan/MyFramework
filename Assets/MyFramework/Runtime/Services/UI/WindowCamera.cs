using System;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public enum WindowLayer
    {
        MostBottom = 1,
        Normal = 1000,
        Dialog = 2000,
        MostTop = 9000,
    }

    [RequireComponent(typeof(UnityEngine.Camera))]
    public class WindowCamera : MonoBehaviour
    {
        [SerializeField] private Camera windowCamera;
        [SerializeField] private WindowLayer layer = WindowLayer.Normal;

        private void Awake()
        {
            depth = (int) windowCamera.depth;
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