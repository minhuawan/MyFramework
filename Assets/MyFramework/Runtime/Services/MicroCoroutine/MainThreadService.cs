using System;
using UnityEngine;

namespace MyFramework.Services
{
    public class MainThreadService : AbstractService
    {
        public override byte CreatePriority => 0;

        public MicroCoroutine EndOfFrame => endOfFrame;
        public MicroCoroutine EndOfSeconds => endOfSeconds;

        private MicroCoroutine endOfFrame;
        private MicroCoroutine endOfSeconds;

        public override void OnCreated()
        {
            endOfFrame = CreateMicroCoroutine(MicroCoroutine.MicroCoroutineType.WaitEndOfFrame);
            endOfSeconds = CreateMicroCoroutine(MicroCoroutine.MicroCoroutineType.WaitFromSeconds);
        }

        public override void OnDestroy()
        {
        }

        private MicroCoroutine CreateMicroCoroutine(MicroCoroutine.MicroCoroutineType type)
        {
            return new MicroCoroutine(ex =>
            {
                Debug.LogError($"micro coroutine [{type}] catch a unwrapped exception " + ex);
            });
        }
    }
}