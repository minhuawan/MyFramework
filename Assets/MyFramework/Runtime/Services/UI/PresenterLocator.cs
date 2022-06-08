using System.Collections.Generic;

namespace MyFramework.Services.UI
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
    }

    public class PresenterLocator
    {
        public string TargetName { get; protected set; }
        public PresenterLocatorParameters Parameters { get; protected set; }

        public List<PresenterLocator> SubLocators { get; protected set; }

        public static PresenterLocator Create(string targetName, PresenterLocatorParameters parameters = null)
        {
            var locator = new PresenterLocator();
            locator.TargetName = targetName;
            locator.Parameters = parameters ?? new PresenterLocatorParameters();
            locator.SubLocators = null;
            return locator;
        }

        public static PresenterLocator Create(string targetName, Dictionary<string, object> parameters = null)
        {
            var locator = new PresenterLocator();
            locator.TargetName = targetName;
            locator.Parameters = new PresenterLocatorParameters(parameters);
            locator.SubLocators = null;
            return locator;
        }

        public static PresenterLocator CreateWithSubLocators(
            string targetName,
            PresenterLocatorParameters parameters,
            List<PresenterLocator> subLocators)
        {
            var locator = new PresenterLocator();
            locator.Parameters = parameters;
            locator.SubLocators = subLocators;
            return locator;
        }
    }
}