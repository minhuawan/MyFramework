#if UNITY_EDITOR
using System.IO;
using MyFramework.Utils;
using UnityEditor;
using UnityEditor.Experimental.SceneManagement;
#endif

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using MyFramework.Runtime.Services.UI;
using UnityEngine;
using UnityEngine.UI;
using Object = UnityEngine.Object;

namespace MyFramework.Runtime.Services.Lua
{
    public class LuaViewBinder : MonoBehaviour
    {
        public List<GameObject> GameObjects;
        public List<Transform> Transforms;
        public List<Text> Texts;
        public List<Image> Images;
        public List<ButtonView> ButtonViews;

        //
        // UNITY EDITOR EXTENSION START
        //
#if UNITY_EDITOR
        public static Regex RegexName = new Regex("^#_[a-zA-Z][a-zA-Z0-9_]*$");
        public static Regex RegexSaveFileName = new Regex("^[a-zA-Z0-9_]*$");

        private void Clear()
        {
            GameObjects.Clear();
            Transforms.Clear();
            Texts.Clear();
            Images.Clear();
            ButtonViews.Clear();
        }

        public void GenLuaCodeAndSaveFile(string filePath)
        {
            var directoryName = Path.GetDirectoryName(filePath);
            if (!Directory.Exists(directoryName))
            {
                Directory.CreateDirectory(directoryName);
            }

            File.WriteAllText(filePath, GenLuaCode());
        }

        public string GenLuaCode()
        {
            Clear();
            var children = GetComponentsInChildren<Transform>(true);
            foreach (var child in children)
            {
                if (child.name.StartsWith("#_", StringComparison.Ordinal))
                {
                    if (!RegexName.IsMatch(child.name))
                    {
                        Debug.LogError(
                            $"invalid export name: {child.name}, does not match regex: {RegexName.ToString()}");
                        continue;
                    }

                    DealChild(child);
                }
            }

            EditorUtility.SetDirty(gameObject);

            var luaCode = ExportLuaCode();
            return luaCode;
            // var select = EditorUtility.DisplayDialogComplex("Result", luaCode, "CopyToFile", "Close", "Copy");
            // switch (select)
            // {
            //     case 0:
            //         break;
            //     case 1: break;
            //     case 2:
            //         GUIUtility.systemCopyBuffer = luaCode;
            //         break;
            // }
        }


        private void DealChild(Transform child)
        {
            Transforms.Add(child);
            GameObjects.Add(child.gameObject);
            if (child.gameObject == null)
            {
                Debug.LogError("null!!");
            }

            var text = child.GetComponent<Text>();
            if (text != null)
            {
                Texts.Add(text);
            }

            var image = child.GetComponent<Image>();
            if (image != null)
            {
                Images.Add(image);
            }

            var buttonView = child.GetComponent<ButtonView>();
            if (buttonView != null)
            {
                ButtonViews.Add(buttonView);
            }
        }

        private void AppendLine(StringBuilder sb, int space, string line)
        {
            for (int i = 0; i < space; i++)
            {
                sb.Append(" ");
            }

            sb.Append(line + "\n");
        }

        private string ExportLuaCode()
        {
            var path = GetPrefabPath();
            var fullPath = UnityEngine.Application.dataPath + "/" + path.Substring("Assets/".Length);
            var fileHash = HashUtils.FileHash(fullPath);
            var sb = new StringBuilder();
            AppendLine(sb, 0, "--");
            AppendLine(sb, 0, "-- file: " + path);
            AppendLine(sb, 0, "-- hash: " + fileHash);
            // AppendLine(sb, 0, "-- date: " + DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
            AppendLine(sb, 0,
                "-- path: " + MyFramework.Utils.TransformUtils.GetHierarchyPath(transform));
            AppendLine(sb, 0, "-- Auto generated, do not edit manually");
            AppendLine(sb, 0, "--");
            AppendLine(sb, 0, "local __return__");
            AppendLine(sb, 0, "local ButtonViewWrap = require('app.ui.base.ButtonViewWrap')");
            AppendLine(sb, 0, "return {");
            AppendLine(sb, 4, "attach = function(binder)");
            AppendLine(sb, 8, "__return__ = {");
            ExportComponent<Transform>(sb, 12, "Transforms", Transforms);
            ExportComponent<GameObject>(sb, 12, "GameObjects", GameObjects);
            ExportComponent<Text>(sb, 12, "Texts", Texts);
            ExportComponent<Image>(sb, 12, "Images", Images);
            ExportComponent<ButtonView>(sb, 12, "ButtonViews", ButtonViews);
            AppendLine(sb, 12, "dispose = function()");
            DisposeComponent<ButtonView>(sb, 16, "ButtonViews", ButtonViews);
            AppendLine(sb, 12, "end,");
            AppendLine(sb, 8, "}");
            AppendLine(sb, 8, "return __return__");
            AppendLine(sb, 4, "end,");
            AppendLine(sb, 0, "}");
            return sb.ToString();
        }

        private void ExportComponent<T>(StringBuilder sb, int space, string name, List<T> list) where T : Object
        {
            AppendLine(sb, space, $"{name} = {"{"}");
            for (var i = 0; i < list.Count; i++)
            {
                var itemName = list[i].name.Substring(2);
                var line = WrapComponent<T>($"binder.{name}[{i}]");
                line = $"{itemName} = {line},";
                AppendLine(sb, space + 4, line);
            }

            AppendLine(sb, space, "},");
        }

        private string WrapComponent<T>(string exp) where T : Object
        {
            if (typeof(T) == typeof(ButtonView))
            {
                return $"ButtonViewWrap({exp})";
            }

            return exp;
        }

        private void DisposeComponent<T>(StringBuilder stringBuilder, int space, string name, List<T> list)
            where T : Object
        {
            if (typeof(T) == typeof(ButtonView))
            {
                for (int i = 0; i < list.Count; i++)
                {
                    var itemName = list[i].name.Substring(2);
                    AppendLine(stringBuilder, space, $"__return__.{name}.{itemName}:dispose()");
                }
            }
        }

        public string GetPrefabPath()
        {
            var prefab = PrefabStageUtility.GetPrefabStage(gameObject);
            if (prefab != null)
            {
                return prefab.assetPath;
            }
            else
            {
                var path = AssetDatabase.GetAssetPath(gameObject);
                if (!string.IsNullOrEmpty(path))
                {
                    return path;
                }

                return UnityEditor.SceneManagement.EditorSceneManager.GetActiveScene().path;
            }
        }

        [MenuItem("MyFramework/Lua/CleanAllGenLuaFiles")]
        public static void CleanAllGenLuaFiles()
        {
            var path = LuaViewBinderEditor.LuaGenCodePrefix;
            if (!Directory.Exists(path))
                return;
            Directory.Delete(path, true);
        }

        [MenuItem("MyFramework/Lua/GenAllLuaFiles")]
        public static void GenAllLuaFiles()
        {
            var viewDir = LuaViewBinderEditor.PrefabViewFullPath;
            var di = new DirectoryInfo(viewDir);
            var pf = di.GetFiles("*.prefab", SearchOption.AllDirectories);
            var dataPath = UnityEngine.Application.dataPath;
            foreach (var fi in pf)
            {
                var assetPath = fi.FullName;
                assetPath = "Assets/" + assetPath.Substring(dataPath.Length + 1);
                assetPath = assetPath.Replace("\\", "/");
                Debug.Log(assetPath);
                var binder = UnityEditor.AssetDatabase.LoadAssetAtPath<LuaViewBinder>(assetPath);
                if (binder == null)
                {
                    Debug.LogError($"Gen failed at `{assetPath}`");
                    continue;
                }
                var fullPath = LuaViewBinderEditor.GetLuaCodeFilePath(binder);
                binder.GenLuaCodeAndSaveFile(fullPath);
            }
        }
#endif
    }

    //
    // INSPECTOR BUTTON 
    //
#if UNITY_EDITOR
    [CustomEditor(typeof(LuaViewBinder))]
    public class LuaViewBinderEditor : Editor
    {
        public static string LuaGenCodePrefix => Path.Combine(UnityEngine.Application.dataPath,
            "App", "Lua~", "src",
            "app", "ui", "generated"
        );

        public static string PrefabViewFullPath => Path.Combine(UnityEngine.Application.dataPath,
            "AppData", "Prefab", "STS", "View");

        public static string PrefabViewAssetPath = Path.Combine("AppData", "Prefab", "STS", "View");


        public override void OnInspectorGUI()
        {
            base.OnInspectorGUI();
            if (GUILayout.Button("GenLuaCode"))
            {
                var binder = target as LuaViewBinder;
                var luaCode = binder.GenLuaCode();
                var message = luaCode;
                if (message.Length > 600)
                {
                    message = message.Substring(0, 600) + "\n......";
                }

                if (EditorUtility.DisplayDialog("Result", message, "Copy"))
                {
                    GUIUtility.systemCopyBuffer = luaCode;
                }
            }

            if (GUILayout.Button("GenLuaCodeAndSaveFile"))
            {
                var binder = target as LuaViewBinder;
                binder.GenLuaCodeAndSaveFile(GetLuaCodeFilePath(binder));
            }
        }

        public static string GetLuaCodeFilePath(LuaViewBinder binder)
        {
            var path = binder.GetPrefabPath();
            // default start with : Assets/AppData/Prefab/STS/View/ 
            var substring = path.Substring("Assets/AppData/Prefab/STS/View/".Length);
            var parts = substring.Split('/');
            var newPath = string.Join("/", parts.Take(parts.Length - 1).Select(p => p.ToLower()));
            var fileName = parts.Last().Split('.').First();
            fileName = $"{fileName}Vars";
            if (!LuaViewBinder.RegexSaveFileName.IsMatch(newPath))
            {
                Debug.LogError($"Invalid path: `{newPath}` at `{path}`");
                return null;
            }

            if (!LuaViewBinder.RegexSaveFileName.IsMatch(fileName))
            {
                Debug.LogError($"Invalid file name: `{fileName}` at `{path}`");
                return null;
            }


            newPath = Path.Combine(LuaGenCodePrefix, newPath, fileName + ".lua");

            Debug.Log($"Save path: {newPath}");
            return newPath;
        }
    }
#endif
}