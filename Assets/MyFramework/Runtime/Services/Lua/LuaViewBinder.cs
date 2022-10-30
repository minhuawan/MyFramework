#if UNITY_EDITOR
#endif

using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;
using MyFramework.Runtime.Services.UI;
using UnityEditor;
using UnityEditor.Experimental.SceneManagement;
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
    }
#if UNITY_EDITOR
    [CustomEditor(typeof(LuaViewBinder))]
    public class LuaViewBinderEditor : Editor
    {
        private List<Transform> transforms = new List<Transform>();
        private List<GameObject> gameObjects = new List<GameObject>();
        private List<Text> texts = new List<Text>();
        private List<Image> images = new List<Image>();
        private List<ButtonView> buttonViews = new List<ButtonView>();

        private Regex nameRegex = new Regex("^#_[a-zA-Z][a-zA-Z0-9_]*$");
        private Regex typeRegex = new Regex("");

        public override void OnInspectorGUI()
        {
            base.OnInspectorGUI();
            if (GUILayout.Button("export"))
            {
                Export();
            }
        }

        private void Export()
        {
            var binder = target as LuaViewBinder;

            transforms.Clear();
            gameObjects.Clear();
            texts.Clear();

            var children = binder.GetComponentsInChildren<Transform>(true);
            foreach (var child in children)
            {
                if (child.name.StartsWith("#_", StringComparison.Ordinal))
                {
                    if (!nameRegex.IsMatch(child.name))
                    {
                        Debug.LogError(
                            $"invalid export name: {child.name}, does not match regex: {nameRegex.ToString()}");
                        continue;
                    }

                    DealChild(child);
                }
            }


            binder.GameObjects = gameObjects;
            binder.Transforms = transforms;
            binder.Texts = texts;
            binder.Images = images;
            binder.ButtonViews = buttonViews;
            EditorUtility.SetDirty(binder.gameObject);

            var luaCode = ExportLuaCode();
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


        private void DealChild(Transform child)
        {
            transforms.Add(child);
            gameObjects.Add(child.gameObject);
            if (child.gameObject == null)
            {
                Debug.LogError("null!!");
            }

            var text = child.GetComponent<Text>();
            if (text != null)
            {
                texts.Add(text);
            }

            var image = child.GetComponent<Image>();
            if (image != null)
            {
                images.Add(image);
            }

            var buttonView = child.GetComponent<ButtonView>();
            if (buttonView != null)
            {
                buttonViews.Add(buttonView);
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
            var binder = this;
            var sb = new StringBuilder();
            AppendLine(sb, 0, "--");
            AppendLine(sb, 0, "-- date: " + DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
            AppendLine(sb, 0, "-- file: " + GetPrefabPath());
            AppendLine(sb, 0, "-- Auto generated, do not edit manually");
            AppendLine(sb, 0, "--");
            AppendLine(sb, 0, "local __return__");
            AppendLine(sb, 0, "local ButtonViewWrap = require('app.ui.base.ButtonViewWrap')");
            AppendLine(sb, 0, "return {");
            AppendLine(sb, 4, "attach = function(binder)");
            AppendLine(sb, 8, "__return__ = {");
            ExportComponent<Transform>(sb, 12, "Transforms", binder.transforms);
            ExportComponent<GameObject>(sb, 12, "GameObjects", binder.gameObjects);
            ExportComponent<Text>(sb, 12, "Texts", binder.texts);
            ExportComponent<Image>(sb, 12, "Images", binder.images);
            ExportComponent<ButtonView>(sb, 12, "ButtonViews", binder.buttonViews);
            AppendLine(sb, 12, "dispose = function()");
            DisposeComponent<ButtonView>(sb, 16, "ButtonViews", binder.buttonViews);
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

        private string GetPrefabPath()
        {
            var b = target as LuaViewBinder;
            var prefab = PrefabStageUtility.GetPrefabStage(b.gameObject);
            if (prefab != null)
            {
                return prefab.assetPath;
            }
            else
            {
                return UnityEditor.SceneManagement.EditorSceneManager.GetActiveScene().path;
            }
        }
    }
#endif
}