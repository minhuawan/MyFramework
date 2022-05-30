using System;
using System.Globalization;
using System.IO;

namespace MyFramework.Services.Resource
{
    public class ResourcePath
    {
        public static string StreamingAssetPath => UnityEngine.Application.streamingAssetsPath;
        public static string PersistentPath => UnityEngine.Application.persistentDataPath;
        public static string AssetBundleRootPath =>
            Path.Combine(UnityEngine.Application.dataPath, "..", "asset_bundle");

        public const string RESOURCE_SCHEMA = "res://";
        public const string ASSET_BUNDLE_SCHEMA = "ab://";
        public string fullPath { get; private set; }
        public string path { get; private set; }
        public string schema { get; private set; }


        public ResourcePath(string fullPath)
        {
            this.fullPath = fullPath;
            if (fullPath.StartsWith(RESOURCE_SCHEMA))
            {
                schema = RESOURCE_SCHEMA;
            }
            else if (fullPath.StartsWith(ASSET_BUNDLE_SCHEMA))
            {
                schema = ASSET_BUNDLE_SCHEMA;
            }
            else
            {
                throw new Exception($"schema not found in full path: {fullPath}");
            }

            path = fullPath.Substring(schema.Length);
        }
    }
}