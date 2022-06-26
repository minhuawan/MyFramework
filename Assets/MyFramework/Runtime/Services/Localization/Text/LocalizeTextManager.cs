using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.Localization
{
    public class LocalizeTextManager
    {
        public class LocalizeTextEntity
        {
            public string key;
            public string text;
        }

        public class LocalizeTextSpace
        {
            public string space;
            public string language;
            public Dictionary<string, LocalizeTextEntity> entities;
        }

        private Dictionary<string, LocalizeTextSpace> spaces;
        private string language;
        private LocalizeTextLoader loader;

        public LocalizeTextManager(string language)
        {
            this.language = language;
            loader = new LocalizeTextLoader();
            spaces = new Dictionary<string, LocalizeTextSpace>(8);
        }

        public void MountSpace(string space)
        {
            if (spaces.ContainsKey(space))
            {
                Debug.LogWarning($"Localize text space {space} load failed, loaded before!");
                return;
            }

            var list = loader.Load(language, space);
            var textSpace = new LocalizeTextSpace()
            {
                language = this.language,
                space = space,
                entities = new Dictionary<string, LocalizeTextEntity>(list.Count)
            };
            foreach (var entity in list)
            {
                textSpace.entities[entity.key] = entity;
            }
            if (!space.Equals(textSpace.space))
            {
                Debug.LogError($"Localize text space {space} load failed, space name not equals");
                return;
            }

            if (!language.Equals(textSpace.language))
            {
                Debug.LogError(
                    $"Localize text space {space} load failed, " +
                    $"space language not equals, current language is {this.language}, " +
                    $"loaded language is {textSpace.language}");
                return;
            }

            spaces.Add(space, textSpace);
        }

        public void UnmountSpace(string space)
        {
            if (!spaces.ContainsKey(space))
            {
                Debug.LogWarning($"Localize text space {space} unload failed, no loaded before!");
                return;
            }

            spaces.Remove(space);
        }

        public string Translate(string space, string key, bool returnKeyIfNotFound = true)
        {
            if (spaces.TryGetValue(space, out var textSpace))
            {
                if (textSpace.entities.TryGetValue(key, out var entity))
                {
                    return entity.key;
                }
            }

            return returnKeyIfNotFound ? key : null;
        }

        public bool HasKey(string space, string key)
        {
            if (spaces.TryGetValue(space, out var textSpace))
            {
                if (textSpace.entities.ContainsKey(key))
                {
                    return true;
                }
            }

            return false;
        }
    }
}