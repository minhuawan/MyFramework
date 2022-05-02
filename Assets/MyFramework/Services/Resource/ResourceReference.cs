using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.LowLevel;

namespace MyFramework.Services.Resource
{
    public class ResourceReference : IDisposable
    {
        private List<GameObject> cloned = new List<GameObject>();

        public string path { get; private set; }
        public GameObject rawObject { get; private set; }
        public int refer { get; private set; }

        public ResourceReference(string path, GameObject rawObject)
        {
            this.path = path;
            this.rawObject = rawObject;
            this.refer = 0;
        }

        public T Instantiate<T>() where T : UnityEngine.Object
        {
            var obj = GameObject.Instantiate(rawObject);
            var component = obj.GetComponent<T>();
            refer++;
            cloned.Add(obj);

            PlayerLoop.GetCurrentPlayerLoop();
            return component;
        }

        public void Release(GameObject gameObject)
        {
            refer--;
            if (cloned.Contains(gameObject))
            {
                cloned.Remove(gameObject);
            }
        }

        public void Dispose()
        {
            cloned.Clear();
            GameObject.Destroy(rawObject);
        }
    }
}