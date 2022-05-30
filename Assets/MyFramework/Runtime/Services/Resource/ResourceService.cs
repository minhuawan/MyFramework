using System;
using System.Collections.Concurrent;
using System.Collections.Generic;

namespace MyFramework.Services.Resource
{
    public sealed class ResourceService : AbstractService
    {
        private object locker = new object();
        private ConcurrentDictionary<string, IResourceObject> activeObjects;
        private Dictionary<string, IResourceObject> inactiveObjects;
        private Dictionary<int, IResourceObject> resourceObjects;
        public override void OnCreated()
        {
            activeObjects = new ConcurrentDictionary<string, IResourceObject>();
            inactiveObjects = new Dictionary<string, IResourceObject>();
            resourceObjects = new Dictionary<int, IResourceObject>();
        }

        public override void OnDestroy()
        {
        }

        public IResourceObject GetResourceObject(string schemaPath)
        {
            IResourceObject resourceObject;
            lock (locker)
            {
                if (activeObjects.TryGetValue(schemaPath, out resourceObject))
                {
                }
                else if (inactiveObjects.TryGetValue(schemaPath, out resourceObject))
                {
                    inactiveObjects.Remove(schemaPath);
                    activeObjects[schemaPath] = resourceObject;
                }
                else
                {
                    resourceObject = CreateResourceObject(schemaPath);
                    inactiveObjects[schemaPath] = resourceObject;
                    resourceObjects[resourceObject.GetHashCode()] = resourceObject;
                }
            }
            return resourceObject;
        }

        public void CleanupResourceObject(IResourceObject resourceObject)
        {
            if (resourceObject == null)
                return;
            if (!resourceObjects.ContainsKey(resourceObject.GetHashCode()))
                return;
            resourceObject.CleanUp();
        }
        
        private IResourceObject CreateResourceObject(string schemaPath)
        {
            var resourcePath = new ResourcePath(schemaPath);
            switch (resourcePath.schema)
            {
                case ResourcePath.RESOURCE_SCHEMA:
                    return new NormalResourceObject(resourcePath);
                case ResourcePath.ASSET_BUNDLE_SCHEMA:
                    return new AssetBundleResourceObject(resourcePath);
            }

            throw new Exception($"create resource object failed, schema path: {schemaPath}");
        }
    }
}