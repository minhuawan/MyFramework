using System;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI
{
    [RequireComponent(typeof(RectTransform))]
    public class SafeArea : MonoBehaviour
    {
        public bool stretchHorizontally;
        public bool stretchVertically;

        RectTransform _rectTransform;
        DeviceOrientation _previousOrientation;

        private void OnEnable()
        {
            _rectTransform = GetComponent<RectTransform>();
            _previousOrientation = Input.deviceOrientation;
            ApplySafeArea();
        }

        private void Update()
        {
            if (_previousOrientation != Input.deviceOrientation)
            {
                _previousOrientation = Input.deviceOrientation;
                ApplySafeArea();
            }
        }

        public void ApplySafeArea()
        {
            var safeAreaSize = Screen.safeArea;
            if (stretchHorizontally)
            {
                safeAreaSize.x = 0f;
                safeAreaSize.width = Screen.width;
            }

            if (stretchVertically)
            {
                safeAreaSize.y = 0f;
                safeAreaSize.height = Screen.height;
            }

            _rectTransform.anchorMin = new Vector2(0f, 0f);
            _rectTransform.anchorMax = new Vector2(1f, 1f);
            _rectTransform.anchoredPosition = safeAreaSize.center - new Vector2(Screen.width / 2f, Screen.height / 2f);
            _rectTransform.sizeDelta =
                new Vector2(safeAreaSize.width - Screen.width, safeAreaSize.height - Screen.height);
        }
    }
}