using System;
using UnityEngine;

namespace MyFramework.Runtime.Services.Asset
{
    public class AssetService : AbstractService
    {
        public AssetDownloader downloader;
        public override void OnCreated()
        {
            var go = new GameObject($"{this.GetType().FullName}");
            downloader = go.AddComponent<AssetDownloader>();
            GameObject.DontDestroyOnLoad(downloader);
        }

        public override void OnDestroy()
        {
            UnityEngine.Object.Destroy(downloader);
        }

        public UnityEngine.Object LoadAsset(string path, Type type)
        {
            var asset = UnityEditor.AssetDatabase.LoadAssetAtPath(path, type);
            return asset;
        }

        public T LoadAsset<T>(string path) where T : UnityEngine.Object
        {
            var asset = UnityEditor.AssetDatabase.LoadAssetAtPath<T>(path);
            return asset;
        }
    }
}