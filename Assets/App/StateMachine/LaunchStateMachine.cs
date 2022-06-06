using App.UI.Presenter.Launch;
using MyFramework.Services.StateMachine;
using MyFramework.Services.UI;
using UnityEngine;
using Application = MyFramework.Application;

namespace App.StateMachine
{
    public class LaunchStateMachine : AStateMachine
    {
        public override void OnEnter(StateMachineContext context)
        {
            var presenter = Application.GetService<UIService>().Open<LaunchPresenter>();
        }

        public override void OnExit(StateMachineContext context)
        {
        }
    }
}