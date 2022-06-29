// using System;
// using System.Collections.Generic;
// using App.UI.Views;
// using UnityEngine;
// using UnityEngine.UI;
// using Application = MyFramework.Application;
//
// namespace MyFramework.Runtime.Services.UI
// {
//     public class UITransition : MonoBehaviour
//     {
//         [SerializeField] private List<string> texts = new List<string>()
//         {
//             "L O A D I N G",
//             "L O A D I N G .",
//             "L O A D I N G . .",
//             "L O A D I N G . . .",
//         };
//
//         [SerializeField] private Text loading;
//         [SerializeField] private float interval = 0.3f;
//
//         private float time = 0;
//         private int index = 0;
//
//         public void OnEnable()
//         {
//             time = 0;
//             index = 0;
//         }
//
//
//         private void Update()
//         {
//             time += Time.deltaTime;
//             if (time < interval)
//             {
//                 return;
//             }
//
//             time = 0f;
//             if (index + 1 < texts.Count)
//             {
//                 index++;
//             }
//             else
//             {
//                 index = 0;
//             }
//
//             loading.text = texts[index];
//         }
//     }
// }