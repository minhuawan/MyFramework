using System;

namespace MyFramework.Services.StateMachine
{
    public abstract class AStateMachine
    {
        public Type MachineType => this.GetType();
        public abstract void OnEnter(StateMachineContext context);
        public abstract void OnExit(StateMachineContext context);
    }
}