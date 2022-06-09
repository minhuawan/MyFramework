using System;
using System.Threading;
using System.Threading.Tasks;
using App.UI.Presenters.Launch;
using MyFramework.Services.StateMachine;
using MyFramework.Services.UI;
using UnityEngine;
using Application = MyFramework.Application;

namespace App.StateMachine
{
    public class LaunchStateMachine : AStateMachine
    {
        public override async void OnEnter(StateMachineContext context)
        {
            await Application.GetService<UIService>().SwitchPresenterAsync(PresenterLocator.Launch);
        }

        public override void OnExit(StateMachineContext context)
        {
        }
    }
}