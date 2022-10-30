using System;
using System.Collections.Generic;
using System.IO;
using UnityEngine;
using XLua;

namespace MyFramework.Runtime.Utils
{
    [LuaCallCSharp]
    public static class AssetLoader
    {

        private static Dictionary<string, object> delegates = new Dictionary<string, object>();

        public static bool Exist(string path)
        {
            return File.Exists(path);
        }

        public static string LoadSprite(string path, Action<Sprite> action)
        {
            return Load<UnityEngine.Sprite>(path, action);
        }

        public static void RemoveDelegateByGuid(string guid)
        {
            if (delegates.ContainsKey(guid))
            {
                delegates.Remove(guid);
            }
        }

        private static string Load<T>(string path, Action<T> action) where T : UnityEngine.Object
        {
            var guid = Guid.NewGuid().ToString();
            delegates.Add(guid, action);
            var asset = UnityEditor.AssetDatabase.LoadAssetAtPath<T>(path);
            action(asset);
            RemoveDelegateByGuid(guid);
            return guid;
        }
    }
}
