using System.Collections.Generic;
using System.Threading.Tasks;
using App.UI.Views;
using MyFramework.Services.Timer;
using MyFramework.Services.UI;
using UniRx;
using UnityEngine;
using Application = MyFramework.Application;

namespace App.UI.Presenters
{
    public class TransitionPresenter : Presenter
    {
        private static readonly List<string> texts = new List<string>()
        {
            "L O A D I N G",
            "L O A D I N G .",
            "L O A D I N G . .",
            "L O A D I N G . . .",
        };

        private float time = 0;
        private int index = 0;
        private TransitionView view;
        public override View View => view;

        public override async Task<TransitionResult> LoadAsync(PresenterLocatorParameters parameters)
        {
            view = await TransitionView.LoadAsync();

            Application.GetService<TimerService>().EveryFrame.Subscribe(OnEveryFrame).AddTo(_disposables);
            return TransitionResult.Ok;
        }

        private void OnEveryFrame(Unit _)
        {
            time += Time.deltaTime;
            if (time < 0.3f)
            {
                return;
            }

            time = 0f;
            if (index + 1 < texts.Count)
            {
                index++;
            }
            else
            {
                index = 0;
            }

            view.SetLoadingText(texts[index]);
        }
    }
}