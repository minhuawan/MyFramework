using System.IO;
using XLua;

namespace MyFramework.Runtime.Utils
{
    [LuaCallCSharp]
    public static class LuaResourceUtils
    {
        public static readonly string RootPath =
#if UNITY_EDITOR
            Path.Combine(UnityEngine.Application.dataPath, "App/Lua~/res");
#else
                // todo
#endif

        public static string Load(string subPath)
        {
            var path = Path.Combine(RootPath, subPath);
            if (!File.Exists(path))
            {
                return null;
            }

            return File.ReadAllText(path);
        }
    }
}