using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public class SwitchableMvpContextManager : IMvpContextManager
    {
        private MvpContext current;
        private MvpContext waitingContext;
        private Stack<MvpContext> history = new Stack<MvpContext>();
        private bool backLocking = false;


        private void SwitchContextInternal(MvpContext mvpContext)
        {
            try
            {
                if (current != null)
                {
                    waitingContext = mvpContext;
                    current.MoveNextState();
                    current.WhenDisposed(() =>
                    {
                        current = waitingContext;
                        waitingContext = null;
                        current.MoveNextState();
                    });
                }
                else
                {
                    mvpContext.MoveNextState();
                    current = mvpContext;
                }
            }
            catch (Exception ex)
            {
                HandleException(ex);
            }
        }

        public void SwitchContext<T>(Model model = null) where T : Presenter
        {
            if (waitingContext != null)
            {
#if UNITY_EDITOR
                Debug.LogWarning("have a waiting context, please wait");
                return;
#endif
            }

            if (current == null || current.state >= MvpContext.PresenterState.Appeared)
            {
#if UNITY_EDITOR
                Debug.LogWarning("current state not appeared, please wait");
                return;
#endif
            }

            if (backLocking)
            {
#if UNITY_EDITOR
                Debug.LogError("back locking");
                return;
#endif
            }

            var mvpContext = MvpContext.OfType<T>(this, model);
            SwitchContextInternal(mvpContext);
        }

        private void HandleException(Exception ex)
        {
            Debug.LogException(ex);
        }

        public void Abort(MvpContext mvpContext, string message)
        {
            // todo message ?
            Debug.LogError("context abort message: " + message ?? "");
            if (mvpContext == null)
            {
                return;
            }

            if (mvpContext != current)
            {
                Debug.LogError("abort MvpContext should be current context, " +
                               $"mvpContext type: {mvpContext.presenter.GetType().FullName}, " +
                               $"current type: {current.presenter.GetType().FullName}");
                return;
            }

            if (current.state < MvpContext.PresenterState.Appeared)
            {
                current.Dispose();
                current = null;
            }
            else if (current.state == MvpContext.PresenterState.Appeared)
            {
                Back();
            }
            else
            {
                return; // already in dispose progress ?
            }
        }

        public void Back()
        {
            if (history == null || history.Count == 0)
            {
                Debug.LogError("no history to back !!");
                return;
            }

            if (backLocking)
            {
#if UNITY_EDITOR
                Debug.LogWarning("back locking !!");
                return;
#endif
            }

            backLocking = true;

            if (current.state == MvpContext.PresenterState.Appeared)
            {
                current.WhenDisposed(() => { });
                current.MoveNextState();
            }
        }
    }
}