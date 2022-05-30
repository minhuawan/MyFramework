using System;
using UnityEngine;

namespace MyFramework.Services.StateMachine
{
    public sealed class StateMachineService : AbstractService
    {
        public override byte CreatePriority => 0;

        private StateMachineContext context;

        public override void OnCreated()
        {
            context = new StateMachineContext();
        }

        public override void OnDestroy()
        {
        }

        public void ChangeState<T>(StateMachineParam param = null) where T : AStateMachine, new()
        {
            var newStateMachine = new T();
            context.Param = param;
            context.previous = context.current;
            context.previous?.OnExit(context);
            context.current = newStateMachine;
            context.current?.OnEnter(context);
        }
    }
}