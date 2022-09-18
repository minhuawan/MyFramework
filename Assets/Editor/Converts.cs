using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
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
        [JsonProperty(NullValueHandling= NullValueHandling.Ignore)]
        public string[] dependencies;
    }

    private class AssetManifest
    {
        public string datetime;
        public string version;
        public Dictionary<string, Asset> assets;
    }


    private static List<string> ExportAssetPath = new List<string>()
    {
        "AppData/Prefab/STS",
        "AppData/STS",
    };

    private static List<Type> ExportTypes = new List<Type>()
    {
        typeof(UnityEngine.Texture2D),
        // typeof(UnityEngine.Font)
        typeof(UnityEngine.GameObject),
    };

    [MenuItem("Tools/Export/Asset Manifest")]
    public static void ExportAssetManifest()
    {
        var filePaths = new List<string>();

        var root = UnityEngine.Application.dataPath;
        foreach (var p in ExportAssetPath)
        {
            var path = Path.Combine(UnityEngine.Application.dataPath, p);
            if (File.Exists(path))
            {
                filePaths.Add(path);
            }
            else if (Directory.Exists(path))
            {
                var files = Directory
                        .GetFiles(path, "*", SearchOption.AllDirectories)
                        .Where(f => !f.EndsWith(".meta"))
                        .Select(f => "Assets/" + f.Substring(root.Length + 1).Replace("\\", "/"))
                    ;
                filePaths.AddRange(files);
            }
            else
            {
                Debug.LogError($"path not existed {path}");
                return;
            }
        }

        filePaths.Sort();
        var assets = new Dictionary<string, Asset>();
        foreach (var filePath in filePaths)
        {
            var type = AssetDatabase.GetMainAssetTypeAtPath(filePath);
            if (!ExportTypes.Contains(type))
            {
                continue;
            }

            var lower = filePath.ToLower();
            var dependencies = AssetDatabase.GetDependencies(filePath);
            dependencies = dependencies
                .Where(d => ExportTypes.Contains(AssetDatabase.GetMainAssetTypeAtPath(d)))
                .Select(d => d.ToLower())
                .Where(d => !d.Contains("/editor/") && d != lower)
                .ToArray();
            Array.Sort(dependencies);
            if (assets.ContainsKey(filePath))
            {
                throw new Exception($"duplicated key {filePath}");
            }
            
            assets.Add(lower, new Asset()
            {
                dependencies = dependencies.Length > 0 ? dependencies : null,
            });
        }

        var manifest = new AssetManifest();
        manifest.assets = assets;
        manifest.datetime = System.DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
        manifest.version = "0.1.dev";
        EditorUtility.DisplayProgressBar("Exporting", "Please wait", 0f);
        var jsonStr = Newtonsoft.Json.JsonConvert.SerializeObject(manifest, Formatting.Indented);
        File.WriteAllText(UnityEngine.Application.dataPath + "/App/Lua~/res/assets/manifest.json", jsonStr);
        EditorUtility.DisplayProgressBar("Exporting", "Please wait", 1f);
        EditorUtility.ClearProgressBar();
    }
}