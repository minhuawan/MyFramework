using System;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MyFramework.Runtime.Services.Content
{
    public class ContentLoadTask : IDisposable
    {
        public enum TaskState
        {
            Initialized,
            Loading,
            Ok,
            Failed,
        }

        public string path { get; private set; }
        public TaskState state { get; private set; }
        public bool Ok => state == TaskState.Ok;

        private Object target;
        private int referenceCount;


        public ContentLoadTask(string path)
        {
            if (string.IsNullOrEmpty(path))
            {
                Debug.LogError("create content load task error, path is null or empty");
            }

            referenceCount = 0;
            state = TaskState.Initialized;
            this.path = path;
        }

        public void StartLoad()
        {
            if (state != TaskState.Initialized)
                return;
            if (string.IsNullOrEmpty(path))
                return;
#if UNITY_EDITOR
            StartLoadEditor();
#else
            StartLoadInternal();
#endif
        }

#if UNITY_EDITOR
        private void StartLoadEditor()
        {
            try
            {
                // todo 这里貌似不太行， 还要先加载依赖 
                // 难受
                
                var unityObject = UnityEditor.AssetDatabase.LoadAssetAtPath<UnityEngine.Object>(path);
                if (unityObject == null)
                {
                    throw new Exception("loaded unity object is null");
                }
            }
            catch (Exception ex)
            {
                HandleException(ex);
            }
        }
#endif

        private void StartLoadInternal()
        {
            throw new NotImplementedException();
        }

        private void HandleException(Exception exception)
        {
            state = TaskState.Failed;
            Debug.LogException(exception);
        }


        public void Dispose()
        {
        }
    }
}