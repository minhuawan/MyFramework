using System;

namespace MyFramework.Services.UI
{
    public class ViewPath : Attribute
    {
        public string Path { get; private set; }
        public ViewPath(string path)
        {
            Path = path;
        }
    }
}