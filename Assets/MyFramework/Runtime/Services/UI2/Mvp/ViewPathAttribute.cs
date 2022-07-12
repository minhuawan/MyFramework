using System;

namespace MyFramework.Runtime.Services.UI2
{
    public class ViewPathAttribute : Attribute
    {
        public string path;

        public ViewPathAttribute(string path)
        {
            this.path = path;
        }
    }
}