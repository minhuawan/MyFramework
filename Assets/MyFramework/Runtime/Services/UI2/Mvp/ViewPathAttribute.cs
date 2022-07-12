using System;

namespace MyFramework.Runtime.Services.UI2
{
    public class ViewPathAttribute : Attribute
    {
        private static string ViewPathPrefix = "Assets/AppData/Prefab/";
        public string path;

        public ViewPathAttribute(string path)
        {
            this.path = ViewPathPrefix + path;
        }
    }
}