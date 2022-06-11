// using System;
// using System.Collections.Generic;
// using System.Linq;
// using System.Text.RegularExpressions;
// using MyFramework.Services.Network.HTTP;
// using Newtonsoft.Json;
// using UnityEditor.VersionControl;
//
// namespace MyFramework.Services.Content
// {
//     /*
//      * asset:[0-9a-z/.:]+([;][a-z0-9]*=[a-z0-9]*)*
//      * passed case:
//      *     asset:assets/prefab/ui/view/test.prefab;hash=12345;ab=true;pooled=true
//      *     asset:assets/prefab/ui/view/test.prefab;hash=12345;ab=true;pooled=true
//      *     asset:assets/prefab/ui/view/test.prefab
//      *     asset:http://www.baidu.com/1.bundle
//      *     asset:http://127.0.0.1/1.bundle
//      *     asset:assets/prefab/ui/view/test.prefab;1=12345;ab=true;pooled=true
//      */
//     public sealed class ContentUri
//     {
//         public const string schema = "assets:";
//         public string uri { get; private set; }
//         public string innerUri { get; private set; }
//         public Dictionary<string, object> query { get; private set; }
//
//
//         private ContentUri()
//         {
//         }
//
//         // public static readonly Regex regex = new Regex("asset:[0-9a-z/.:]+([;][a-z0-9]*=[a-z0-9]*)*");
//
//         public static ContentUri Parse(string uri)
//         {
//             if (string.IsNullOrEmpty(uri) || !uri.StartsWith(schema))
//             {
//                 throw new ArgumentException(uri);
//             }
//
//             var innerUrl = "";
//             var index = uri.IndexOf(';');
//             if (index < 0)
//             {
//                 innerUrl = uri.Substring(schema.Length);
//                 return Create(innerUrl, null);
//             }
//
//             List<string> keys = new List<string>();
//             List<string> values = new List<string>();
//             for (var i = index; i < uri.Length; i++)
//             {
//                 if (uri[i] == ';')
//                 {
//                     var start = i;
//                     for (; i < uri.Length; i++)
//                     {
//                         if (uri[i] == '=')
//                         {
//                             keys.Add(uri.Substring(start, i - start));
//                             start = i;
//                         }
//                         else if (uri[i] == ';')
//                         {
//                             values.Add(uri.Substring(start, i - start));
//                             break;
//                         }
//                     }
//                 }
//             }
//
//             return null;
//         }
//
//         public static ContentUri Create(string innerUri, Dictionary<string, object> query)
//         {
//             var contentUri = new ContentUri()
//             {
//                 innerUri = innerUri,
//                 query = query
//             };
//             return contentUri;
//         }
//     }
// }