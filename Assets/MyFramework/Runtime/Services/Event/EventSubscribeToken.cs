using System;
using System.Collections.Generic;

namespace MyFramework.Runtime.Services.Event
{
    public struct EventSubscribeToken : IDisposable
    {
        public int hash { get; private set; }
        public Type type { get; private set; }

        public EventSubscribeToken(int hash, Type type)
        {
            this.hash = hash;
            this.type = type;
        }

        public void Dispose()
        {
            Application.GetService<EventService>().Unsubscribe(this);
        }

        public void AddTo(List<IDisposable> disposables)
        {
            disposables.Add(this);
        }
    }
}