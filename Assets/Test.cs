#if UNITY_EDITOR
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEditor;
using UnityEngine;

public class Test : MonoBehaviour
{
    public string path;

    [MenuItem("Test/Run")]
    static void BuildAssetBundle()
    {
        string outputPath = Path.Combine(Application.dataPath, "assetbundleoutput");
        if (!Directory.Exists(outputPath))
        {
            Directory.CreateDirectory(outputPath);
        }

        List<AssetBundleBuild> builds = new List<AssetBundleBuild>();
        builds.Add(new AssetBundleBuild()
        {
            assetBundleName = "Assets/AppData/Prefab/UI/View/UILaunchView.prefab",
            assetNames = new string[]
            {
                "Assets/AppData/Prefab/UI/View/UILaunchView.prefab"
            },
            
        });
        BuildPipeline.BuildAssetBundles(outputPath, builds.ToArray(), BuildAssetBundleOptions.ChunkBasedCompression, BuildTarget.StandaloneWindows);
    }
}
#endif
