using System.Collections.Generic;
using System.Threading.Tasks;
using Unity.Profiling.LowLevel.Unsafe;

namespace MyFramework.Runtime.Services.UI
{
    public class PresenterLocatorParameters
    {
        public Dictionary<string, object> parameters;

        public PresenterLocatorParameters()
        {
            parameters = new Dictionary<string, object>();
        }

        public PresenterLocatorParameters(Dictionary<string, object> parameters)
        {
            this.parameters = parameters ?? new Dictionary<string, object>();
        }

        public PresenterLocatorParameters Put(string key, object value)
        {
            parameters[key] = value;
            return this;
        }

        public bool Contains(string key)
        {
            return parameters.ContainsKey(key);
        }

        public bool TryGet<T>(string key, out T value)
        {
            value = default;
            if (!parameters.TryGetValue(key, out var objectValue))
            {
                return false;
            }

            value = (T) objectValue;
            return true;
        }

        public T Get<T>(string key, T d = default(T))
        {
            if (TryGet<T>(key, out var value))
            {
                return value;
            }

            return d;
        }
    }

    public class PresenterLocator
    {
        public bool ClearHistory { get; set; } // todo clear history in locator processor
        public bool InUsing { get; set; }
        public string ClassName { get; protected set; }
        public PresenterLocatorParameters Parameters { get; protected set; }
        public List<PresenterLocator> SubLocators { get; protected set; }

        public static PresenterLocator Create(string className, PresenterLocatorParameters parameters = null)
        {
            var locator = new PresenterLocator();
            locator.ClassName = className;
            locator.Parameters = parameters ?? new PresenterLocatorParameters();
            locator.SubLocators = null;
            return locator;
        }

        public static PresenterLocator Create<T>(PresenterLocatorParameters parameters = null)
        {
            return Create(typeof(T).FullName, parameters);
        }

        public static PresenterLocator Create(string className, Dictionary<string, object> parameters = null)
        {
            return Create(className, new PresenterLocatorParameters(parameters));
        }

        public static PresenterLocator CreateWithSubLocators(
            string className,
            PresenterLocatorParameters parameters,
            List<PresenterLocator> subLocators)
        {
            var locator = Create(className, parameters);
            locator.SubLocators = subLocators;
            return locator;
        }
    }
}