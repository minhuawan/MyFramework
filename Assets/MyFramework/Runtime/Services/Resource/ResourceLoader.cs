using UnityEngine;

namespace MyFramework.Runtime.Services.Resource
{
    public static class ResourceLoader
    {
        public static T Load<T>(ResourcePath resourcePath)
        {
            object result = null;
            #if UNITY_EDITOR
            result = UnityEditor.AssetDatabase.LoadAssetAtPath(resourcePath.path, typeof(T));
            #endif
            return (T) result;
        }
    }
}