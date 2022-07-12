using System;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public class SingleMvpContextManager : IMvpContextManager
    {
        private MvpContext current;

        public void Abort(MvpContext context, string message)
        {
            Debug.LogError($"abort message: {message}");
            if (context != null && current != context)
            {
                Debug.LogError("abort error context != current");
                return;
            }

            current.Dispose();
        }

        public void Back()
        {
            if (current != null)
            {
                current.HandleBackKey();
            }
        }

        public void Show(Type presenterType, Model model)
        {
            if (current != null)
            {
#if UNITY_EDITOR
                Debug.LogWarning($"have a dialog showing type: {current.presenter.GetType().FullName}");
#endif
                return;
            }

            var mvpContext = MvpContext.OfType(this, presenterType, model);
            current = mvpContext;
            current.WhenDisposed(() => { current = null; });
            mvpContext.MoveNextState();
        }
    }
}