using System.Linq;
using UnityEngine;

namespace MyFramework.Services.Resource
{
    public class AssetBundleResourceObject : IResourceObject
    {
        private AssetBundle assetBundle;
        private ResourceReference resourceReference;
        public AssetBundleResourceObject(ResourcePath resourcePath)
        {
            resourceReference = new ResourceReference(resourcePath);
        }

        public void Load()
        {
            if (assetBundle == null)
            {
                Debug.Log($"Load asset bundle from file, path: {resourceReference.resourcePath.path}");
                assetBundle = AssetBundle.LoadFromFile(resourceReference.resourcePath.path);
            }
        }

        public T Instantiate<T>() where T : Object
        {
            if (assetBundle == null)
            {
                Load();
            }

            return assetBundle.LoadAllAssets().First() as T;
        }
    }
}