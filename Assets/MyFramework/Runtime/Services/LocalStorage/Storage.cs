using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using Newtonsoft.Json;
using UnityEngine;

namespace MyFramework.Runtime.Services.LocalStorage
{
    public class Storage : IDisposable
    {
        public readonly string RelativePath = "";
        public string FullPath => Path.Combine(UnityEngine.Application.persistentDataPath, RelativePath);

        private Dictionary<string, string> settings;

        public Storage(string relativePath)
        {
            this.RelativePath = relativePath;
            if (string.IsNullOrEmpty(this.RelativePath))
            {
                Debug.LogError("invalid relative path");
                return;
            }

            if (!File.Exists(FullPath))
            {
                settings = new Dictionary<string, string>();
                Flush(); // if not exists, create first
            }
            else
            {
                var content = File.ReadAllText(FullPath, Encoding.UTF8);
                settings = JsonConvert.DeserializeObject<Dictionary<string, string>>(content);
            }
        }

        public void Flush()
        {
            if (!File.Exists(FullPath))
            {
                var directoryName = Path.GetDirectoryName(FullPath);
                if (!Directory.Exists(directoryName))
                {
                    Directory.CreateDirectory(directoryName);
                }
            }

            var content = JsonConvert.SerializeObject(settings);
            File.WriteAllText(FullPath, content, Encoding.UTF8);
        }

        public void Set(string key, string value)
        {
            settings[key] = value;
        }

        public Storage Get(string key, out string value)
        {
            if (!settings.TryGetValue(key, out value))
            {
                value = null;
            }

            return this;
        }

        public Storage Set<T>(string key, T value)
        {
            var jsonValue = JsonConvert.SerializeObject(value);
            Set(key, jsonValue);
            return this;
        }

        public void Delete(string key)
        {
            if (!Has(key))
            {
                return;
            }

            settings.Remove(key);
        }

        public void Get<T>(string key, out T value)
        {
            value = default;
            Get(key, out var jsonVar);
            if (jsonVar != null)
            {
                value = JsonConvert.DeserializeObject<T>(jsonVar);
            }
        }

        public IEnumerable<string> GetKeys()
        {
            return settings.Keys;
        }

        public bool Has(string key)
        {
            return settings.ContainsKey(key);
        }

        public void Dispose()
        {
            Flush();
            settings.Clear();
        }
    }
}