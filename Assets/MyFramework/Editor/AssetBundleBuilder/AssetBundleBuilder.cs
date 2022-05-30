#if UNITY_EDITOR
using System.Collections.Generic;
using System.IO;
using System.Linq;
using MyFramework.Services.Resource;
using UnityEditor;
using UnityEngine;

namespace MyFramework.AssetBundleBuilder
{
    public class AssetBundleBuilder
    {
        [MenuItem("MyFramework/AssetBundleBuilder/Build")]
        private static void Build()
        {
            var rootPath = ResourcePath.AssetBundleRootPath;
            if (Directory.Exists(rootPath))
            {
                Directory.Delete(rootPath, true);
            }

            Directory.CreateDirectory(rootPath);


            var builds = CollectBuildsForUI();
            for (var index = 0; index < builds.Count; index++)
            {
                var assetBundleBuild = builds[index];
                Debug.Log($"{index} bundle name: " + assetBundleBuild.assetBundleName);
                Debug.Log($"{index} asset name: " + assetBundleBuild.assetNames.First());
            }

            var manifest = BuildPipeline.BuildAssetBundles(rootPath, builds.ToArray(),
                BuildAssetBundleOptions.ChunkBasedCompression,
                BuildTarget.StandaloneWindows
            );
            var myManifest = new Manifest();
            myManifest.patch = "1.0.0.alpha";
            myManifest.resources = new List<ManifestItem>();
            foreach (var build in builds)
            {
                var hash128 = manifest.GetAssetBundleHash(build.assetBundleName).ToString();
                myManifest.resources.Add(new ManifestItem()
                {
                    asset_names = build.assetNames,
                    hash128 = hash128,
                    path = build.assetBundleName,
                });
            }

            var jsonStr = EditorJsonUtility.ToJson(myManifest, true);
            File.WriteAllText($"{rootPath}/manifest.json", jsonStr);
        }


        private static List<AssetBundleBuild> CollectBuildsForUI()
        {
            var builds = new List<AssetBundleBuild>();
            var dataPath = UnityEngine.Application.dataPath;
            var targetPath = $"{dataPath}/AppData/Prefab/UI";
            var directoryInfo = new DirectoryInfo(targetPath);
            var prefabs = directoryInfo.GetFiles("*.prefab", SearchOption.AllDirectories);
            foreach (var prefab in prefabs)
            {
                var subPath = "Assets/" + prefab.FullName.Substring(dataPath.Length + 1); // + 1 for split char (/)
                var bundleName = Path.ChangeExtension(subPath, ".bundle");
                var build = new AssetBundleBuild()
                {
                    assetNames = new string[] {subPath},
                    assetBundleName = bundleName,
                };
                builds.Add(build);
            }

            return builds;
        }
    }
}
#endif