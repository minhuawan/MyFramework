using System;
using UnityEngine;

namespace MyFramework.Runtime.Services.Content
{
    public class ContentService : AbstractService
    {
        public override void OnCreated()
        {
        }

        public override void OnDestroy()
        {
        }

        public void LoadContent()
        {
        }
        
        

        public void LoadAsync(Action<UnityEngine.Object> action)
        {
#if UNITY_EDITOR
#else
#endif
        }

#if UNITY_EDITOR
        private void LoadAsyncEditor(string path, Action<UnityEngine.Object> action)
        {
            // UnityEditor.AssetDatabase.LoadAssetAtPath(path)
        }
#endif
    }
}