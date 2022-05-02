using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Services.Resource
{
    public sealed class ResourceService : AService
    {
        private Dictionary<string, ResourceReference> references;
        public override void OnCreated()
        {
            references = new Dictionary<string, ResourceReference>();
        }

        public override void OnDestroy()
        {
            foreach (var referencesValue in references.Values)
            {
                referencesValue.Dispose();
            }

            references.Clear();
        }

        public T InstantiateResource<T>(string path) where T : Object
        {
            ResourceReference reference = null;
            if (!references.TryGetValue(path, out reference))
            {
                reference = CreateResourceReference(path);
                references[path] = reference;
            }
            return reference.Instantiate<T>();
        }

        private ResourceReference CreateResourceReference(string path)
        {
            var rawObject = Resources.Load<GameObject>(path);
            ResourceReference reference = null; //new ResourceReference(path, rawObject, 0);
            return reference;
        }
    }
}