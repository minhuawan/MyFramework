using System;
using System.Collections;
using System.Collections.Concurrent;
using System.Collections.Generic;

namespace MyFramework.Runtime.Services
{
    public class MicroCoroutine
    {
        public enum MicroCoroutineType
        {
            WaitEndOfFrame,
            WaitFromSeconds,
        }
        private Action<Exception> unhandledExceptionHandler;

        private List<IEnumerator> coroutines = new List<IEnumerator>();

        public MicroCoroutine(Action<Exception> unhandledExceptionHandler)
        {
            this.unhandledExceptionHandler = unhandledExceptionHandler;
        }

        public void AddCoroutine(IEnumerator enumerator)
        {
        }

        public void Run()
        {
            var enumerator = coroutines.GetEnumerator();
            while (!enumerator.MoveNext())
            {
                enumerator.Current.MoveNext();
            }
        }
    }
}