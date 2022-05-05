using UnityEngine;

namespace MyFramework.Services.Resource
{
    public class ResourceObject : IResourceObject
    {
        private UnityEngine.GameObject rawObject;
        private ResourceReference reference;

        public ResourceObject(ResourcePath path)
        {
            reference = new ResourceReference(path);
        }

        private void Load()
        {
            if (rawObject == null)
            {
                rawObject = ResourceLoader.Load<GameObject>(reference.resourcePath);
            }
        }

        public T Instantiate<T>() where T : UnityEngine.Object
        {
            if (rawObject == null)
            {
                Load();
            }

            reference.Retain();
            var cloned = GameObject.Instantiate<GameObject>(rawObject);
            var subject = cloned.GetComponent<T>();
            return subject;
        }
    }
}