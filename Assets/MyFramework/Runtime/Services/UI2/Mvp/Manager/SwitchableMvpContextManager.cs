using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public class SwitchableMvpContextManager : IMvpContextManager
    {
        private MvpContext current;
        private MvpContext processing;
        private Stack<MvpContext> history = new Stack<MvpContext>();
        private bool backLocking = false;

        public void Switch(Type presenterType, Model model = null)
        {
            if (processing != null && processing == current && current.state >= MvpContext.PresenterState.Appeared)
            {
                processing = null;
            }

            if (processing != null)
            {
#if UNITY_EDITOR
                Debug.LogWarning("have a processing context, please wait");
#endif
                return;
            }

            if (current != null && current.state <= MvpContext.PresenterState.Appeared)
            {
#if UNITY_EDITOR
                Debug.LogWarning("current state not appeared, please wait");
#endif
                return;
            }

            if (backLocking)
            {
#if UNITY_EDITOR
                Debug.LogError("back locking");
#endif
                return;
            }


            var mvpContext = MvpContext.OfType(this, presenterType, model);
            SwitchContextInternal(mvpContext);
        }

        private void SwitchContextInternal(MvpContext mvpContext)
        {
            try
            {
                if (mvpContext == null)
                {
                    return;
                }

                if (current != null)
                {
                    processing = mvpContext;
                    processing.WhenAppeared(() =>
                    {
                        current.MoveNextState();
                        current.WhenDisposed(() =>
                        {
                            history.Push(current); // store history
                            current = processing;
                        });
                    });
                    processing.MoveNextState();
                }
                else
                {
                    current = mvpContext;
                    processing = mvpContext;
                    mvpContext.MoveNextState();
                }
            }
            catch (Exception ex)
            {
                HandleException(ex);
            }
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

            if (mvpContext != processing)
            {
                Debug.LogError("abort MvpContext should be processing context, " +
                               $"mvpContext type: {mvpContext.presenter.GetType().FullName}, " +
                               $"current type: {processing.presenter.GetType().FullName}");
                return;
            }

            processing.Dispose();
            processing = null;
        }

        public void Back()
        {
            if (history == null || history.Count == 0)
            {
                Debug.LogError("no history to back !!");
                return;
            }

            if (processing != null)
            {
                Debug.LogError("have a context in processing, please wait");
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