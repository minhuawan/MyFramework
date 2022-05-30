using System;
using System.Collections.Generic;

namespace MyFramework.Services.Event
{
    public class ObservableEvent<T>
    {
        private List<Action<T>> observers = new List<Action<T>>();

        public void Subscribe(Action<T> observer)
        {
            if (!observers.Contains(observer))
            {
                observers.Add(observer);
            }
        }

        public void Unsubscribe(Action<T> observer)
        {
            observers.Remove(observer);
        }

        public ObservableEvent<T> Merge(ObservableEvent<T> other)
        {
            var merged = new ObservableEvent<T>();
            this.Subscribe(merged.OnNext);
            other.Subscribe(merged.OnNext);
            return merged;
        }

        public void OnNext(T t)
        {
            for (var index = 0; index < observers.Count; index++)
            {
                var observer = observers[index];
                if (observer != null)
                {
                    observer(t);
                }
            }
        }

        public void Clear()
        {
            observers.Clear();
        }
    }
}