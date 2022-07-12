using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public class MvpContext : IDisposable
    {
        public enum PresenterState
        {
            Created,
            Initialize,
            Appearing,
            Appeared,
            Disappearing,
            Disappeared,
            Disposed,
        }

        /*
         *
         * 普通的全局界面， 也不用那么详细的生命周期

Initialize
DidAppaerd
OnBack  
Dispose

就够了吧 


WillAppeare
DidAppeared
WillDisappeare
DidDisappeared

         */

        private static readonly Dictionary<PresenterState, PresenterState> NextStateMap
            = new Dictionary<PresenterState, PresenterState>()
            {
                [PresenterState.Created] = PresenterState.Initialize,
                [PresenterState.Initialize] = PresenterState.Appearing,
                [PresenterState.Appearing] = PresenterState.Appeared,
                [PresenterState.Appeared] = PresenterState.Disappearing,
                [PresenterState.Disappearing] = PresenterState.Disappeared,
                [PresenterState.Disappeared] = PresenterState.Disposed,
            };

        public Presenter presenter { get; private set; }
        public PresenterState state { get; private set; }
        public Model model { get; private set; }

        private IMvpContextManager manager;
        private Action whenDisposed;
        private Action whenAppeared;


        public void WhenDisposed(Action when)
        {
            whenDisposed -= when;
            whenDisposed += when;
        }


        public void WhenAppeared(Action when)
        {
            whenAppeared -= when;
            whenAppeared += when;
        }

        private MvpContext(IMvpContextManager manager, Presenter presenter, Model model)
        {
            if (presenter == null)
            {
                throw new Exception("create MvpContext error, presenter is null");
            }

            this.manager = manager;
            this.model = model;
            this.presenter = presenter;
            state = PresenterState.Created;
        }

        public static MvpContext OfType<T>(IMvpContextManager manager, Model model = null) where T : Presenter
        {
            var presenter = Activator.CreateInstance<T>();
            return new MvpContext(manager, presenter, model);
        }

        public static MvpContext OfType(IMvpContextManager manager, Type presenterType, Model model = null)
        {
            var presenter = Activator.CreateInstance(presenterType) as Presenter;
            return new MvpContext(manager, presenter, model);
        }

        public void MoveNextState()
        {
            if (state == PresenterState.Disposed)
            {
                throw new Exception($"move next state error, state is disposed, " +
                                    $"presenter type: {presenter.GetType().FullName}");
            }

            if (!NextStateMap.TryGetValue(state, out var nextState))
            {
                throw new Exception($"move next state error, peer state not found, " +
                                    $"current state is {state}, " +
                                    $"presenter type: {presenter.GetType().FullName}");
            }

            try
            {
                var previous = state;
                state = nextState;
                if (previous == PresenterState.Created)
                {
                    presenter.OnCreated(this);
                }
                else if (previous == PresenterState.Initialize)
                {
                    presenter.WillAppear();
                }
                else if (previous == PresenterState.Appearing)
                {
                    presenter.DidAppeared();
                    if (whenAppeared != null)
                    {
                        whenAppeared.Invoke();
                        whenAppeared = null;
                    }
                }
                else if (previous == PresenterState.Appeared)
                {
                    presenter.WillDisappear();
                }
                else if (previous == PresenterState.Disappearing)
                {
                    presenter.DidDisappeared();
                }
                else if (previous == PresenterState.Disappeared)
                {
                    presenter.Dispose();
                }
                else
                {
                    throw new Exception("move next state error, next state peer method not found, " +
                                        $"next state is {nextState}, " +
                                        $"presenter type {presenter.GetType().FullName}");
                }
            }
            catch (Exception e)
            {
                Debug.LogException(e);
            }
        }

        public void HandleBackKey()
        {
            if (state == PresenterState.Appeared)
            {
                presenter.OnBackKey();
            }
        }

        public void Abort(string message)
        {
            manager.Abort(this, message);
        }

        public void Dispose()
        {
            state = PresenterState.Disposed;
            presenter?.Dispose();
            if (whenDisposed != null)
            {
                whenDisposed.Invoke();
                whenDisposed = null;
            }
        }

        public void Back()
        {
            manager.Back();
        }
    }
}