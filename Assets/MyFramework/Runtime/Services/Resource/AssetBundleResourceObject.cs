using System.Linq;
using UnityEngine;

namespace MyFramework.Runtime.Services.Resource
{
    public class AssetBundleResourceObject : IResourceObject
    {
        protected ResourceReference reference;
        private static string prefixPath;
        private AssetBundle assetBundle;
        private GameObject rawObject;

        public AssetBundleResourceObject(ResourcePath path) 
        {
            reference = new ResourceReference(path);
            if (prefixPath == null)
            {
                prefixPath = ResourcePath.AssetBundleRootPath;
            }
        }

        private void Load()
        {
            if (assetBundle == null)
            {
                Debug.Log($"Load asset bundle from file, path: {reference.resourcePath.path}");
                assetBundle = AssetBundle.LoadFromFile(reference.resourcePath.path);
                rawObject = assetBundle.LoadAllAssets().First() as GameObject;
            }
        }

        private void Unload()
        {
            if (assetBundle != null)
            {
                assetBundle.Unload(true);
                assetBundle = null;
            }
        }

        public int ReferenceCount => reference.refer;

        public T CreateObject<T>() where T : Object
        {
            if (assetBundle == null)
            {
                Load();
            }

            var cloned = GameObject.Instantiate(rawObject);
            reference.Retain(cloned);
            return cloned as T;
        }

        public void RecycleObject<T>(T obj) where T : Object
        {
            reference.Release(obj as GameObject);
        }

        public void CleanUp()
        {
            reference.CleanUp();
            Unload();
        }
    }
}