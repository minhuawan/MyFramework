using System;
using System.Collections.Generic;
using Object = UnityEngine.Object;

namespace MyFramework.Services.Resource
{
    public sealed class ResourceService : AService
    {
        private Dictionary<string, IResourceObject> resourceObjects;
        public override void OnCreated()
        {
            resourceObjects = new Dictionary<string, IResourceObject>();
        }

        public override void OnDestroy()
        {
        }

        public T InstantiateResource<T>(string schemaPath) where T : Object
        {
            IResourceObject resourceObject;
            if (!resourceObjects.TryGetValue(schemaPath, out resourceObject))
            {
                resourceObject = CreateResourceObject(schemaPath);
                resourceObjects[schemaPath] = resourceObject;
            }

            return resourceObject.Instantiate<T>();
        }

        private IResourceObject CreateResourceObject(string schemaPath)
        {
            var resourcePath = new ResourcePath(schemaPath);
            switch (resourcePath.schema)
            {
                case ResourcePath.RESOURCE_SCHEMA:
                    return new ResourceObject(resourcePath);
                case ResourcePath.ASSET_BUNDLE_SCHEMA:
                    return new AssetBundleResourceObject(resourcePath);
            }

            throw new Exception($"create resource object failed, schema path: {schemaPath}");
        }
    }
}