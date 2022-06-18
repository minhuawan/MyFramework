using System.Collections.Generic;

namespace MyFramework.Runtime.Services.StateMachine
{
    public class StateMachineContext
    {
        public StateMachineService service;
        public AStateMachine previous;
        public AStateMachine current;
        public StateMachineParam Param;
    }

    public class StateMachineParam
    {
        private Dictionary<string, object> data;

        public Dictionary<string, object> Data => data;

        public T Get<T>(string key, T def = default(T))
        {
            if (data != null && data.TryGetValue(key, out var value))
                return (T)value;
            return def;
        }

        public void Set(string key, object value)
        {
            if (data == null)
                data = new Dictionary<string, object>();
            if (key == null)
                return;
            data[key] = value;
        }
    }
}