using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.Event
{
    public class EventService : AbstractService
    {
        private Dictionary<Type, List<Action<Event>>> dictionary;

        public override void OnCreated()
        {
            dictionary = new Dictionary<Type, List<Action<Event>>>();
        }

        public override void OnDestroy()
        {
            dictionary.Clear();
        }

        public EventSubscribeToken Subscribe(Type eventType, Action<Event> call)
        {
            if (!dictionary.ContainsKey(eventType))
            {
                dictionary[eventType] = new List<Action<Event>>();
            }

            var actions = dictionary[eventType];
            actions.Add(call);
            return new EventSubscribeToken(call.GetHashCode(), eventType);
        }

        public EventSubscribeToken Subscribe<T>(Action<T> call) where T : Event
        {
            return Subscribe(typeof(T), (evt) => { call.Invoke(evt.As<T>()); });
        }

        public void Unsubscribe(EventSubscribeToken token)
        {
            if (!dictionary.ContainsKey(token.type))
            {
                return;
            }

            var actions = dictionary[token.type];
            actions.RemoveAll(call => call.GetHashCode() == token.hash);
        }

        public void Dispatch(Event evt)
        {
            var type = evt.GetType();
            if (!dictionary.ContainsKey(type))
                return;
            var actions = dictionary[type];
            for (var i = actions.Count - 1; i >= 0; i--)
            {
                try
                {
                    actions[i].Invoke(evt);
                }
                catch (Exception e)
                {
                    Debug.LogException(e);
                }
            }
        }
    }
}