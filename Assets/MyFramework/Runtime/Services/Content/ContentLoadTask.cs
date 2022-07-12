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
        }
#endif
        private void StartLoadInternal()
        {
        }


        public void Dispose()
        {
        }
    }
}