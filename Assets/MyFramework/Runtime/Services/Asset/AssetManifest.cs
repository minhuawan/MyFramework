using System;
using System.Collections.Generic;
using System.Linq;
using UnityEditor.PackageManager;
using UnityEngine;

namespace MyFramework.Runtime.Services.Asset
{
    public class Version
    {
        public int major;
        public int minor;
        public int patch;
        public string channel;

        // 3.1.0.dev
        // 3.1.0.alpha
        // 3.1.0.in-house
        // 3.1.0.beta
        // 3.1.0.rc
        // 3.1.0.xxxx
        // 3.1.0.stable (stable will invisible) 3.1.0
        public string versionString
        {
            get
            {
                if (channel == "stable")
                {
                    return $"{major}.{minor}.{patch}";
                }

                return $"{major}.{minor}.{patch}.{channel}";
            }
        }

        public static Version FromString(string str)
        {
            if (string.IsNullOrEmpty(str))
                throw new ArgumentException("version str is null or empty");
            var parts = str.Trim().Split('.').ToList();
            if (parts.Count != 4)
            {
                throw new ArgumentException("invalid version str " + str);
            }

            var version = new Version();
            version.major = int.Parse(parts[0]);
            version.minor = int.Parse(parts[1]);
            version.patch = int.Parse(parts[2]);
            version.channel = parts[3];
            return version;
        }
    }

    public class AssetInfo
    {
        public string hash128;
        public string path;
    }

    public class AssetManifest
    {
        private string version;
        private List<AssetInfo> assets;
        private Dictionary<string, AssetInfo> pathToAsset;
        private Dictionary<string, AssetInfo> hash128ToAsset;
        public Version Version => Version.FromString(version);

        public AssetManifest(string version)
        {
            this.version = version;
            pathToAsset = pathToAsset ?? new Dictionary<string, AssetInfo>();
            hash128ToAsset = hash128ToAsset ?? new Dictionary<string, AssetInfo>();
        }

        public void Initialize()
        {
            if (pathToAsset.Count == 0)
            {
                for (var index = 0; index < assets.Count; index++)
                {
                    var assetInfo = assets[index];
                    var has128 = assetInfo.hash128;
                    var path = assetInfo.path;
                    var value = assetInfo;
                    if (string.IsNullOrEmpty(path) || string.IsNullOrEmpty(has128))
                    {
                        Debug.LogError($"invalid asset config found at {index}, skip");
                        continue;
                    }

                    if (pathToAsset.ContainsKey(path) || hash128ToAsset.ContainsKey(has128))
                    {
                        Debug.LogError($"duplicated asset found at {has128}, overwrite by newer");
                    }

                    pathToAsset[path] = value;
                    hash128ToAsset[has128] = value;
                }
            }
        }

        public bool ExistByPath(string path) => pathToAsset.ContainsKey(path);
        public bool ExistByHash128(string path) => hash128ToAsset.ContainsKey(path);

        public AssetInfo GetByPath(string path)
        {
            if (pathToAsset.TryGetValue(path, out var ai))
            {
                return ai;
            }

            return null;
        }

        public AssetInfo GetByHash128(string hash)
        {
            if (hash128ToAsset.TryGetValue(hash, out var ai))
            {
                return ai;
            }

            return null;
        }
    }

    //
    // public class CategoryInfo
    // {
    //     public string hash128;
    //     public string path;
    // }
    //
    // public class AssetManifest
    // {
    //     public string version;
    //     public Version Version => Version.FromString(version);
    // }
}
/*
 * json
 */