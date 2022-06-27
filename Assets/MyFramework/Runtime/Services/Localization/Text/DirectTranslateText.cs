using System;
using UnityEngine;
using UnityEngine.UI;

namespace MyFramework.Runtime.Services.Localization
{
    [RequireComponent(typeof(Text))]
    public class DirectTranslateText : MonoBehaviour
    {
        public Text text;
        public string space;
        public string key;

#if UNITY_EDITOR
        private void OnValidate()
        {
            text = GetComponent<Text>();
            if (!string.IsNullOrEmpty(space))
            {
                space = space.ToLower();
            }

            if (string.IsNullOrEmpty(key))
            {
                key = $"{transform.parent.name}-{gameObject.name}".ToLower();
            }
            else
            {
                key = key.ToLower();
            }
        }
#endif
    }
}