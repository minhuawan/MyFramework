// using System;
// using System.Collections.Generic;
// using System.Linq;
// using System.Threading;
// using System.Threading.Tasks;
// using MyFramework.Runtime.Services.Network.HTTP;
// using UnityEngine;
// using UnityEngine.Networking;
// using UnityEngine.UI;
//
// public class Test : MonoBehaviour
// {
//     [SerializeField] private Text text;
//
//     [SerializeField] private Button button;
//
//     async void Start()
//     {
//         var dict = new Dictionary<string, Action>();
//
//         // await Delay().ConfigureAwait(false);
//
//         dict["VoidTask"] = VoidTask;
//         dict["TaskFun"] = () => TaskFun();
//         dict["Http"] = () => Http();
//         dict["ClickButton"] = () => ClickButton();
//
//         var parent = button.transform.parent;
//         foreach (var keyValuePair in dict)
//         {
//             var clone = Instantiate(button, parent);
//             clone.transform.localScale = Vector3.one;
//             clone.GetComponentInChildren<Text>().text = keyValuePair.Key;
//             clone.onClick.AddListener(() => { keyValuePair.Value.Invoke(); });
//         }
//
//         button.gameObject.SetActive(false);
//     }
//
//     async Task Delay()
//     {
//         await Task.Delay(1000);
//     }
//
//     void Update()
//     {
//         text.text = Time.frameCount.ToString();
//     }
//
//     private async void VoidTask()
//     {
//         await Task.Run(async () =>
//         {
//             await Task.Delay(3000);
//             Debug.LogError("thread id: " + Thread.CurrentThread.ManagedThreadId);
//             throw new Exception("On VoidTask exception");
//         });
//     }
//
//
//     private async Task TaskFun()
//     {
//         var task = new Task(async () =>
//         {
//             await Task.Delay(1000);
//             Debug.LogError("thread id: " + Thread.CurrentThread.ManagedThreadId);
//             await Task.Run(async () =>
//             {
//                 Debug.LogError("thread id: " + Thread.CurrentThread.ManagedThreadId);
//                 await Task.Delay(1000);
//                 Debug.LogError("thread id: " + Thread.CurrentThread.ManagedThreadId);
//                 throw new Exception("On TaskFun internal exception");
//             });
//             throw new Exception("On TaskFun exception");
//         });
//         task.UnWrap();
//         task.ContinueWith(task => { Debug.LogError("final ?"); });
//         task.Start();
//     }
//
//     private async Task Http()
//     {
//         Debug.LogError("thread id: " + Thread.CurrentThread.ManagedThreadId);
//         var request = UnityWebRequest.Get("http://www.baidu.com");
//         // var task = Task.Run(async () =>
//         // {
//         //     
//         // }).UnWrap();
//         Debug.LogError("thread id: " + Thread.CurrentThread.ManagedThreadId);
//         var connection = new HttpConnection(request);
//         var result = await connection.Connect();
//         Debug.LogError("response data: \n" + request.downloadHandler.text);
//     }
//
//     private async Task ClickButton()
//     {
//         var buttons = FindObjectsOfType<Button>();
//         var tasks = buttons.Select(PressButton);
//         Task<Button> finishedButton = await Task.WhenAny(tasks);
//         Debug.LogError("finished clicked " + finishedButton.Result.GetComponentInChildren<Text>().text);
//     }
//
//     async Task<Button> PressButton(Button button)
//     {
//         bool isPress = false;
//         button.onClick.AddListener(() => isPress = true);
//         while (!isPress)
//         {
//             await Task.Yield();
//         }
//
//         return button;
//     }
// }