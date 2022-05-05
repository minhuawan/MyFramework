using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.LowLevel;

namespace MyFramework.Services.Resource
{
    public class ResourceReference
    {
        public ResourcePath resourcePath { get; private set; }
        public int refer { get; private set; }

        public ResourceReference(ResourcePath resourcePath, int refer = 0)
        {
            this.resourcePath = resourcePath;
            this.refer = refer;
        }

        public void Retain()
        {
            refer++;
        }

        public void Release()
        {
            refer--;
        }
    }
}