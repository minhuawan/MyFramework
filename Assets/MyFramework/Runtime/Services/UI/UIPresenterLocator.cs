using System;
using System.Collections.Generic;
using System.Security.Cryptography;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public class UIPresenterLocatorParameters
    {
        public Dictionary<string, object> parameters;

        public UIPresenterLocatorParameters()
        {
            parameters = new Dictionary<string, object>();
        }

        public UIPresenterLocatorParameters(Dictionary<string, object> parameters)
        {
            this.parameters = parameters ?? new Dictionary<string, object>();
        }

        public UIPresenterLocatorParameters Put(string key, object value)
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

    public class UIPresenterLocator
    {
        public string TargetName { get; protected set; }
        public UIPresenterLocatorParameters Parameters { get; protected set; }

        public List<UIPresenterLocator> SubLocators { get; protected set; }

        public static UIPresenterLocator Create(string targetName, UIPresenterLocatorParameters parameters = null)
        {
            var locator = new UIPresenterLocator();
            locator.TargetName = targetName;
            locator.Parameters = parameters ?? new UIPresenterLocatorParameters();
            locator.SubLocators = null;
            return locator;
        }

        public static UIPresenterLocator Create(string targetName, Dictionary<string, object> parameters = null)
        {
            var locator = new UIPresenterLocator();
            locator.TargetName = targetName;
            locator.Parameters = new UIPresenterLocatorParameters(parameters);
            locator.SubLocators = null;
            return locator;
        }

        public static UIPresenterLocator CreateWithSubLocators(
            string targetName,
            UIPresenterLocatorParameters parameters,
            List<UIPresenterLocator> subLocators)
        {
            var locator = new UIPresenterLocator();
            locator.Parameters = parameters;
            locator.SubLocators = subLocators;
            return locator;
        }
    }
}