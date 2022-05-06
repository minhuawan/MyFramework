using System;
using System.Collections.Generic;

namespace MyFramework.AssetBundleBuilder
{
    [Serializable]
    public class Manifest
    {
        public string patch;
        public List<ManifestItem> resources;
    }

    [Serializable]
    public class ManifestItem
    {
        public string hash128;
        public string path;
        public string[] asset_names;
    }
}