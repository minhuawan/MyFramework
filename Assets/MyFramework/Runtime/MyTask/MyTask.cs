using System;
using System.Runtime.CompilerServices;

namespace MyFramework.MyTask
{
    public class MyTask : INotifyCompletion

    {
        public void OnCompleted(Action continuation)
        {
            throw new NotImplementedException();
        }
    }
}