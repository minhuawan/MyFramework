#if UNITY_EDITOR
#endif

using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;
using MyFramework.Runtime.Services.UI;
using UnityEditor;
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
                        Debug.LogError($"invalid export name: {child.name}, does not match regex: {nameRegex.ToString()}");
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
            if (EditorUtility.DisplayDialog("Result", luaCode, "Copy"))
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

        private string ExportLuaCode()
        {
            var binder = this;
            var sb = new StringBuilder();
            sb.AppendLine("--");
            sb.AppendLine("-- Auto generated, do not edit manually");
            sb.AppendLine("-- date " + DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
            sb.AppendLine("--");
            sb.AppendLine("return {");
            sb.AppendLine("    attach = function(binder)");
            sb.AppendLine("       return {");
            ExportComponent<Transform>(sb, "Transforms", binder.transforms);
            ExportComponent<GameObject>(sb, "GameObjects", binder.gameObjects);
            ExportComponent<Text>(sb, "Texts", binder.texts);
            ExportComponent<Image>(sb, "Images", binder.images);
            ExportComponent<ButtonView>(sb, "ButtonViews", binder.buttonViews);
            sb.AppendLine("       }");
            sb.AppendLine("    end");
            sb.AppendLine("}");
            return sb.ToString();
        }

        private void ExportComponent<T>(StringBuilder sb, string name, List<T> list) where T : Object
        {
            sb.AppendLine($"        {name} = {"{"}");
            for (var i = 0; i < list.Count; i++)
            {
                var itemName = list[i].name.Substring(2);
                var line = $"            {itemName} = binder.{name}[{i}],";
                sb.AppendLine(line);
            }

            sb.AppendLine("        },");
        }
    }
}