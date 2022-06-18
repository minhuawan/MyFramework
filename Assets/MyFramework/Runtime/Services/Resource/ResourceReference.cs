using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.LowLevel;

namespace MyFramework.Runtime.Services.Resource
{
    public class ResourceReference
    {
        public LinkedList<UnityEngine.Object> referenceObjects { get; private set; }
        public ResourcePath resourcePath { get; private set; }
        public int refer => referenceObjects.Count;

        public ResourceReference(ResourcePath resourcePath)
        {
            referenceObjects = new LinkedList<UnityEngine.Object>();
            this.resourcePath = resourcePath;
        }

        public void Retain(UnityEngine.Object obj)
        {
            referenceObjects.AddLast(obj);
        }

        public int Release(UnityEngine.Object obj)
        {
            referenceObjects.Remove(obj);
            return refer;
        }

        public void CleanUp()
        {
            referenceObjects.Clear();
        }
    }
}