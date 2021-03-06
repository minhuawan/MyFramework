using System.IO;
using Newtonsoft.Json;
using UnityEngine;

namespace MyFramework.Runtime.Services.Localization
{
    public class LocalizeTextLoader
    {
        public LocalizeTextManager.LocalizeTextSpace Load(string language, string space)
        {
#if UNITY_EDITOR
            // todo 这里直接 load 了
            var filePath = Path.Combine(
                UnityEngine.Application.dataPath,
                "AppData",
                "LocalizationSpaces",
                language,
                space + ".json"
            );
            if (!File.Exists(filePath))
            {
                Debug.LogWarning($"language {language} space {space} load failed, file not found");
                return null;
            }


            var text = File.ReadAllText(filePath);
            var textSpace = JsonConvert.DeserializeObject<LocalizeTextManager.LocalizeTextSpace>(text);
            return textSpace;

#else
        return null; // todo
#endif
        }
    }
}