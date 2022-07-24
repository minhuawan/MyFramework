using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using MyFramework.Runtime.Services.UI;
using UnityEditor;
using UnityEngine;
using XLua;

namespace MyFramework.Runtime.Services.Lua
{
    public class LuaService : AbstractService
    {
        private LuaEnv luaEnv;
        private List<LuaFunction> functions;

        public override void OnCreated()
        {
            functions = new List<LuaFunction>();
        }

        public override void OnDestroy()
        {
            foreach (var fn in functions)
            {
                fn.Dispose();
            }

            functions.Clear();

            DisposeLuaEnv();
        }

        public void Start()
        {
            luaEnv = new LuaEnv();
            luaEnv.AddLoader(CustomLoader);
            luaEnv.DoString("require 'GameStart'");
        }

        private void DisposeLuaEnv()
        {
            if (luaEnv != null)
            {
                var disposeFunc = luaEnv.Global.Get<LuaFunction>("OnLuaEnvDisposeBefore");
                if (disposeFunc != null)
                {
                    disposeFunc.Call();
                    disposeFunc.Dispose();
                }
                luaEnv.Dispose();
            }
        }

        public void Restart()
        {
            DisposeLuaEnv();
            Start();
        }

        private byte[] CustomLoader(ref string filepath)
        {
            byte[] bytes = null;
#if UNITY_EDITOR
            bytes = LoadLuaEditor(ref filepath);
#else
            bytes = LoadLuaRuntime(ref filepath);
#endif
            return bytes;
        }

        private byte[] LoadLuaRuntime(ref string filepath)
        {
            return null;
        }

        private byte[] LoadLuaEditor(ref string filepath)
        {
            if (string.IsNullOrEmpty(filepath))
                return null;
            var path = filepath.Replace(".", "/");
            path = Path.Combine(UnityEngine.Application.dataPath, "App/Lua~/src", path) + ".lua";
            filepath = path;
            var content = File.ReadAllText(path);
            return Encoding.UTF8.GetBytes(content);
        }
    }
}