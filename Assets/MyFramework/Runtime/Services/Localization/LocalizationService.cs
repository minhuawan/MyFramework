using System;
using System.Runtime.CompilerServices;
using UnityEngine;

namespace MyFramework.Runtime.Services.Localization
{
    public class LocalizationService : AbstractService
    {
        private LocalizeTextManager textManager;

        public override void Initialize()
        {
            // read
            textManager = new LocalizeTextManager("zh-cn"); // todo read language from system or setting
            
            // preload spaces
            MountTextSpace("const");
            MountTextSpace("ui.base");
        }

        public void MountTextSpace(string space) => textManager.MountSpace(space);
        public void UnmountTextSpace(string space) => textManager.UnmountSpace(space);

        public string Translate(string space, string key)
        {
            if (string.IsNullOrEmpty(key))
            {
                Debug.LogError("Translate failed, key is null or empty");
                return key;
            }

            var result = textManager.Translate(space, key, false);
            if (result == null)
            {
                Debug.LogError($"Translate failed, key({key}) not found in space({space})");
                return key;
            }

            return result;
        }
    }
}