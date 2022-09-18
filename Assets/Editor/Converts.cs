using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using Newtonsoft.Json;
using UnityEditor;
using UnityEditor.Experimental;
using UnityEngine;

public class Converts
{
    [MenuItem("Tools/Convert Texture2d To Sprites")]
    public static void ConvertTexture2DToSprite()
    {
        var guids = AssetDatabase.FindAssets("t: texture2d", new string[] {"Assets/AppData"});
        foreach (var guid in guids)
        {
            var path = AssetDatabase.GUIDToAssetPath(guid);
            var importer = AssetImporter.GetAtPath(path) as TextureImporter;
            if (importer == null)
            {
                Debug.LogError($"texture is null at {path}");
                continue;
            }

            if (importer.textureType != TextureImporterType.Sprite)
            {
                importer.textureType = TextureImporterType.Sprite;
            }
        }

        AssetDatabase.Refresh();
    }


    [MenuItem("Tools/Show Build Player Window")]
    public static void ShowBuildPlayerWindow()
    {
        UnityEditor.BuildPlayerWindow.ShowBuildPlayerWindow();
    }


    [MenuItem("Tools/Print All Asset Labels")]
    public static void PrintAllAssetLabels()
    {
        var names = AssetDatabase.GetAllAssetBundleNames();
        for (var index = 0; index < names.Length; index++)
        {
            var n = names[index];
            Debug.Log($"asset bundle name index@`{index + 1}` : " + n);
            var dependencies = AssetDatabase.GetAssetBundleDependencies(n, true);
            for (var i = 0; i < dependencies.Length; i++)
            {
                Debug.Log($"     dependencies {i + 1}: {dependencies[i]}");
            }
        }

        var allAssetPaths = AssetDatabase.GetAllAssetPaths();
        foreach (var path in allAssetPaths)
        {
            var guid = AssetDatabase.GUIDFromAssetPath(path);
            var labels = AssetDatabase.GetLabels(guid);
            if (labels.Length > 0)
            {
                Debug.Log($"asset {path}, labels: [{string.Join(", ", labels)}]");
            }
        }
    }

    [MenuItem("Tools/Build Asset Bundles")]
    public static void BuildAssetBundles()
    {
        var outputPath = "AssetsBundles/Windows64/";
        if (Directory.Exists(outputPath))
        {
            Directory.Delete(outputPath, true);
        }

        Directory.CreateDirectory(outputPath);

        var manifest = BuildPipeline.BuildAssetBundles(
            outputPath,
            BuildAssetBundleOptions.None,
            EditorUserBuildSettings.activeBuildTarget
        );

        Debug.Log("built all asset bundle length: " + manifest.GetAllAssetBundles().Length);
        Debug.Log("all asset bundle name length: " + AssetDatabase.GetAllAssetBundleNames().Length);
        foreach (var assetBundle in manifest.GetAllAssetBundles())
        {
            Debug.Log($"built {assetBundle}");
        }

        // var builds1 = Manually1(outputPath);
        // BuildPipeline.BuildAssetBundles(
        //     outputPath,
        //     builds1.ToArray(),
        //     BuildAssetBundleOptions.None,
        //     EditorUserBuildSettings.activeBuildTarget);
        //
        //
        // var builds2 = Manually2(outputPath);
        // BuildPipeline.BuildAssetBundles(
        //     outputPath,
        //     builds2.ToArray(),
        //     BuildAssetBundleOptions.None,
        //     EditorUserBuildSettings.activeBuildTarget);

        var targetPath = "Assets/AppData/Prefab/STS/test";
        var dirs = System.IO.Directory.GetDirectories(targetPath);
        var builds = dirs.Select(dir => CreateAssetBundleBuildWithFolder(dir)).ToArray();
        foreach (var build in builds)
        {
            Debug.Log($"bundle name {build.assetBundleName}, assets: {string.Join(", ", build.assetNames)}");
        }

        BuildPipeline.BuildAssetBundles(
            outputPath,
            builds,
            BuildAssetBundleOptions.None,
            EditorUserBuildSettings.activeBuildTarget);
    }

    // public static List<AssetBundleBuild> Manually1(string outputPath)
    // {
    //     Debug.Log("Start build manually1");
    //
    //     var builds = new List<AssetBundleBuild>();
    //     // E:\repo\MyFramework\Assets\AppData\Prefab\STS\test003
    //     var targetPath = "Assets/AppData/Prefab/STS/test";
    //
    //     var directories = System.IO.Directory.GetDirectories(targetPath);
    //     foreach (var directory in directories)
    //     {
    //         var files = System.IO.Directory.GetFiles(directory, "*", SearchOption.AllDirectories);
    //         var dir = directory.Substring(targetPath.Length + 1);
    //         var assetBundleName = dir + "_manually1";
    //         string[] names = files;
    //         builds.Add(new AssetBundleBuild()
    //         {
    //             assetNames = names,
    //             addressableNames = names,
    //             assetBundleName = assetBundleName,
    //         });
    //         Debug.Log($"added bundle: {assetBundleName}, names: [{string.Join(", ", names)}]");
    //     }
    //
    //     // BuildPipeline.BuildAssetBundles(
    //     //     outputPath,
    //     //     builds.ToArray(),
    //     //     BuildAssetBundleOptions.None,
    //     //     EditorUserBuildSettings.activeBuildTarget);
    //     return builds;
    // }
    //
    // public static List<AssetBundleBuild> Manually2(string outputPath)
    // {
    //     Debug.Log("Start build manually2");
    //
    //     var dataPath = Application.dataPath;
    //     Debug.Log("data path: " + Application.dataPath);
    //     var builds = new List<AssetBundleBuild>();
    //     // E:\repo\MyFramework\Assets\AppData\Prefab\STS\test003
    //     var targetPath = "Assets/AppData/Prefab/STS/test";
    //
    //     var dir = new System.IO.DirectoryInfo(targetPath);
    //     foreach (var sub in dir.GetDirectories())
    //     {
    //         var names = sub
    //                 .GetFiles("*", SearchOption.AllDirectories)
    //                 .Where(f => f.Extension != ".meta")
    //                 .Select(f => "Assets/" + f.FullName.Replace("\\", "/").Substring(dataPath.Length + 1))
    //                 .ToArray()
    //             ;
    //
    //         var assetBundleName = sub.Name + "_manually2";
    //         builds.Add(new AssetBundleBuild()
    //         {
    //             assetNames = names,
    //             addressableNames = names,
    //             assetBundleName = assetBundleName,
    //         });
    //         Debug.Log($"added bundle: {assetBundleName}, names: [{string.Join(", ", names)}]");
    //     }
    //
    //     return builds;
    // }

    public static AssetBundleBuild CreateAssetBundleBuildWithFolder(
        string folderPath,
        string bundleName = null,
        string searchPattern = "*",
        SearchOption searchOption = SearchOption.AllDirectories)
    {
        var headLength = Application.dataPath.Length;
        if (!System.IO.Directory.Exists(folderPath))
        {
            throw new Exception($"folder path not existed! path: {folderPath}");
        }

        var sub = new System.IO.DirectoryInfo(folderPath);
        var names = sub
                .GetFiles(searchPattern, searchOption)
                .Where(f => f.Extension != ".meta")
                .Select(f => "Assets/" + f.FullName.Replace("\\", "/").Substring(headLength + 1))
                .ToArray()
            ;
        if (names.Length == 0)
        {
            Debug.LogWarning($"folder is empty, path: {folderPath}");
        }

        var assetBundleName = bundleName ?? sub.Name;
        return new AssetBundleBuild()
        {
            assetNames = names,
            addressableNames = names,
            assetBundleName = assetBundleName,
        };
    }


    private class Asset
    {
        public string name;
        public string[] dependencies;
    }

    private class AssetManifest
    {
        public string datetime;
        public string version;
        public Dictionary<string, Asset> assets;
    }

    [MenuItem("Tools/Calculate Dependencies")]
    public static void CalculateDependencies()
    {
        var objects = UnityEditor.Selection.objects;
        if (objects == null || objects.Length == 0)
        {
            return;
        }

        var manifest = new AssetManifest();
        manifest.assets = new Dictionary<string, Asset>();
        var exportTypes = new List<Type> {typeof(UnityEngine.Texture2D), typeof(UnityEngine.Font)};
        foreach (var obj in objects)
        {
            var path = AssetDatabase.GetAssetPath(obj);
            var dependencies = AssetDatabase.GetDependencies(path, true);
            dependencies = dependencies
                .Select(d => d.ToLower())
                .Where(d => exportTypes.Contains(AssetDatabase.GetMainAssetTypeAtPath(d)))
                .Where(d => !d.Contains("/editor/"))
                .ToArray();
            Array.Sort(dependencies);
            var asset = new Asset();
            asset.name = obj.name;
            asset.dependencies = dependencies;
            var lp = path.ToLower();
            if (manifest.assets.ContainsKey(lp))
            {
                Debug.LogError($"have same key {lp}");
                return;
            }

            manifest.assets[lp] = asset;
        }

        manifest.datetime = System.DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
        manifest.version = "0.1.dev";
        var jsonStr = Newtonsoft.Json.JsonConvert.SerializeObject(manifest, Formatting.Indented);
        File.WriteAllText(UnityEngine.Application.dataPath + "/assets.json", jsonStr);
    }
}