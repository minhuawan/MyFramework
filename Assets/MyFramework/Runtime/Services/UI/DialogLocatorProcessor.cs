using System;
using MyFramework.Runtime.Services.Event;
using MyFramework.Runtime.Services.Event.UI;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI
{
    public class DialogLocatorProcessor : BaseLocatorProcessor
    {
        protected override bool IsDialogProcessor => true;

        public DialogLocatorProcessor(Action<PresenterLocator, NavigateResult> resultListener) : base(resultListener)
        {
            var eventService = Application.GetService<EventService>();
            eventService.Subscribe<DialogPresenterCompletedEvent>(OnDialogPresenterCompletedEvent).AddTo(_disposed);
        }


        public override void Process(PresenterLocator locator)
        {
            if (runningTarget != null)
            {
#if UNITY_EDITOR
                Debug.LogError($"have a running dialog presenter to waiting result," +
                               $" class name is {runningTarget.GetType().FullName}");
#endif
                return;
            }

            base.Process(locator);
        }

        private void OnDialogPresenterCompletedEvent(DialogPresenterCompletedEvent completedEvent)
        {
            if (runningTarget == null)
                return;
            disAppearFinish = false;
            runningTarget.OnWillDisappear();
        }
    }
}