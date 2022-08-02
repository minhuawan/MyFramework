using System.Collections;
using System.Collections.Generic;
using UnityEditor;
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
}