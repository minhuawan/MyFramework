using System;
using System.Collections.Concurrent;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MyFramework.Runtime.Services
{
    public class MainThreadService : AbstractService
    {
        private ConcurrentQueue<Exception> exceptions = new ConcurrentQueue<Exception>();
        // public override byte CreatePriority => 0;
        //
        // public MicroCoroutine EndOfFrame => endOfFrame;
        // public MicroCoroutine EndOfSeconds => endOfSeconds;
        //
        // private MicroCoroutine endOfFrame;
        // private MicroCoroutine endOfSeconds;
        //

        private GameObject gameObject;

        public override void OnCreated()
        {
            // endOfFrame = CreateMicroCoroutine(MicroCoroutine.MicroCoroutineType.WaitEndOfFrame);
            // endOfSeconds = CreateMicroCoroutine(MicroCoroutine.MicroCoroutineType.WaitFromSeconds);
            gameObject = new GameObject(this.GetType().FullName);
            var update = gameObject.AddComponent<MainThreadUpdate>();
            update.onUpdate = OnMainThreadUpdate;
        }

        public override void OnDestroy()
        {
            Object.Destroy(gameObject);
            gameObject = null;
        }

        private void OnMainThreadUpdate()
        {
            while (!exceptions.IsEmpty)
            {
                if (exceptions.TryDequeue(out var ex))
                {
                    Debug.LogException(ex);
                }
            }
        }

        // private MicroCoroutine CreateMicroCoroutine(MicroCoroutine.MicroCoroutineType type)
        // {
        //     return new MicroCoroutine(ex =>
        //     {
        //         Debug.LogError($"micro coroutine [{type}] catch a unwrapped exception " + ex);
        //     });
        // }

        public void PushException(Exception e)
        {
            exceptions.Enqueue(e);
        }
    }
}