using System.IO;
using System.Text;
using XLua;

namespace MyFramework.Runtime.Services.Lua
{
    public class LuaService : AbstractService
    {
        private LuaEnv luaEnv;

        public override void OnCreated()
        {
        }

        public override void OnDestroy()
        {
            if (luaEnv != null)
            {
                luaEnv.Dispose();
                luaEnv = null;
            }
        }

        public void Start()
        {
            luaEnv = new LuaEnv();
            luaEnv.AddLoader(CustomLoader);
            luaEnv.DoString("require 'GameStart'");
        }

        public void Restart()
        {
            if (luaEnv != null)
            {
                luaEnv.Dispose();
            }
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