using System;

namespace MyFramework.Runtime.Services.UI2
{
    public class ViewAttribute : Attribute
    {
        public string path;

        public ViewAttribute(string path)
        {
            this.path = path;
        }
    }
}