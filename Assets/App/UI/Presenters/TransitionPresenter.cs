// using System.Collections;
// using System.Collections.Generic;
// using System.Threading.Tasks;
// using App.UI.Views;
// using MyFramework.Services.Timer;
// using MyFramework.Services.UI;
// using UnityEngine;
// using Application = MyFramework.Application;
//
// namespace App.UI.Presenters
// {
//     public class TransitionPresenter : Presenter
//     {
//         private static readonly List<string> texts = new List<string>()
//         {
//             "L O A D I N G",
//             "L O A D I N G .",
//             "L O A D I N G . .",
//             "L O A D I N G . . .",
//         };
//
//         private float time = 0;
//         private int index = 0;
//         private TransitionView view;
//         public override View View => view;
//
//         public override NavigateResult OnNavigate(PresenterLocatorParameters parameters)
//         {
//             view = View.Instantiate<TransitionView>();
//             Application.GetService<TimerService>().everyFrame.AddListener(OnEveryFrame);
//             return NavigateResult.Ok;
//         }
//
//         private void OnEveryFrame()
//         {
//             time += Time.deltaTime;
//             if (time < 0.3f)
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
//             view.SetLoadingText(texts[index]);
//         }
//     }
// }