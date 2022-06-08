// #if UNITY_EDITOR
// using System;
// using System.Collections;
// using System.Collections.Generic;
// using System.IO;
// using System.Threading;
// using System.Threading.Tasks;
// using UnityEditor;
// using UnityEngine;
//
// public class Test : MonoBehaviour
// {
//     void Awake()
//     {
//         TaskScheduler.UnobservedTaskException += (sender, args) =>
//         {
//             Debug.LogError("UnobservedTaskException " + args.Exception);
//         };
//         Debug.LogError("thread id " + Thread.CurrentThread.ManagedThreadId);
//         Fun1();
//         Fun1();
//     }
//
//     async void Fun1()
//     {
//         // return
//         var task = Task.Run(async () =>
//         {
//             Debug.LogError("thread id " + Thread.CurrentThread.ManagedThreadId);
//             // Fun2();
//             throw new Exception("Fun1");
//         });
//         task.GetAwaiter().OnCompleted(() =>
//         {
//             Debug.LogError("im over");
//         });
//     }
//     
//     
//     async void Fun2()
//     {
//         await Task.Run(async () =>
//         {
//             Debug.LogError("thread id " + Thread.CurrentThread.ManagedThreadId);
//             Thread.Sleep(1000);
//             throw new Exception("Fun2");
//             return 1;
//         });
//     }
//     
//     async Task<int> Fun3()
//     {
//         await Task.Run(async () =>
//         {
//             Debug.LogError("thread id " + Thread.CurrentThread.ManagedThreadId);
//             Thread.Sleep(1000);
//             throw new Exception("Fun3");
//         });
//         return 1;
//     }
//
//     IEnumerator TestCoroutine()
//     {
//         int i = 0;
//         while (i++ < 4)
//         {
//             Debug.LogError("tick " + i);
//             yield return new WaitForSeconds(1);
//         }
//         Debug.LogError("finished");
//     }
// }
// #endif