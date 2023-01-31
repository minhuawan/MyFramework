using System.Collections.Generic;
using System.Text;
using UnityEngine;

namespace MyFramework.Utils
{
    public static class TransformUtils
    {
        public static string GetHierarchyPath(Transform transform)
        {
            var p = transform;
            var names = new List<string>();
            while (p != null)
            {
                names.Add(p.name);
                p = p.parent;
            }
            names.Reverse();
            var path = string.Join("/", names);
            return path;
        }
    }
}