using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Services.Resource
{
    public class NormalResourceObject : IResourceObject
    {
        private UnityEngine.GameObject rawObject;
        private ResourceReference reference;

        public NormalResourceObject(ResourcePath path)
        {
            reference = new ResourceReference(path);
        }

        private void Load()
        {
            if (rawObject == null)
            {
#if UNITY_EDITOR
                rawObject = UnityEditor.AssetDatabase.LoadAssetAtPath<GameObject>(reference.resourcePath.path);
#endif
            }
        }

        private void Unload()
        {
        }


        public int ReferenceCount => reference.refer;
        public T CreateObject<T>() where T : Object
        {
            if (rawObject == null)
            {
                Load();
            }
            
            var cloned = GameObject.Instantiate<GameObject>(rawObject);
            var subject = cloned.GetComponent<T>();
            reference.Retain(cloned);
            return subject;
        }

        public void RecycleObject<T>(T obj) where T : Object
        {
            reference.Release(obj as GameObject);
        }

        public void CleanUp()
        {
            reference.CleanUp();
            Unload();
        }
    }
}