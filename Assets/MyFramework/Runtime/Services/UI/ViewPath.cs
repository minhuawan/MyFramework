using System;
using MyFramework.Services.Resource;

namespace MyFramework.Services.UI
{
    public class ViewPath : Attribute
    {
        private string path;

        public ViewPath(string path)
        {
            this.path = path;
        }

        public string GetPath()
        {
            var isEditor = UnityEngine.Application.isEditor;
            var schema = isEditor ? 
                    ResourcePath.RESOURCE_SCHEMA :
                    ResourcePath.ASSET_BUNDLE_SCHEMA;
            string schemaPath = schema + path;
            if (isEditor && !schemaPath.EndsWith(".prefab"))
            {
                schemaPath += ".prefab";
            }

            return schemaPath;
        }
    }
}