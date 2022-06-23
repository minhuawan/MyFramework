using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.Navigation
{
    public class NavigationContext
    {
        public enum State
        {
            None = 0,
            Initialized,
            OnNavigated,
            OnAppear,
            OnDidAppear,
            OnDisappear,
            OnLeave,
        }

        public Dictionary<string, object> parameter;
        public State state;
        public INavigable Navigable;


        public void RequestNavigateFailed()
        {
        }

        public void RequestLeave()
        {
        }

        public void MoveNext()
        {
            if (state == State.None)
            {
                Debug.LogError("NavigationContext MoveNext error, current state is None");
                return;
            }

            if (state == State.OnLeave)
            {
                Debug.LogError("NavigationContext MoveNext error, current state is OnLeave");
                return;
            }

            state = state + 1;
            try
            {
                switch (state)
                {
                    case State.Initialized:
                        Navigable.OnNavigate();
                        break;
                    case State.OnNavigated:
                        Navigable.OnAppear();
                        break;
                    case State.OnDidAppear:
                        Navigable.OnDisappear();
                        break;
                    case State.OnDisappear:
                        Navigable.OnLeave();
                        break;
                    default:
                        Debug.LogError($"NavigationContext MoveNext error, state is {state.ToString()}");
                        break;
                }
            }
            catch (Exception e)
            {
                Debug.LogException(e);
            }
        }
    }
}