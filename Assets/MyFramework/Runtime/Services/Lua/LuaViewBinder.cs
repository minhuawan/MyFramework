#if UNITY_EDITOR
#endif

using System;
using UnityEngine;
using System.Collections.Generic;
using System.Linq;
using MyFramework.Runtime.Services.UI;
using UnityEditor;
using UnityEngine.UI;

namespace MyFramework.Runtime.Services.Lua
{
    public class LuaViewBinder : MonoBehaviour
    {
         
        public SerializedDictionary<GameObject, SerializedType> dictionary;
    }

    [CustomEditor(typeof(LuaViewBinder))]
    public class LuaViewBinderEditor : Editor
    {
        public override void OnInspectorGUI()
        {
            base.OnInspectorGUI();
            if (GUILayout.Button("Refresh"))
            {
                Refresh();
            }
        }

        private void Refresh()
        {
        }
    }
}