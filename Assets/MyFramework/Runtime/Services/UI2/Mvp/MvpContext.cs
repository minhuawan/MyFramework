using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public class MvpContext : IDisposable
    {
        public enum PresenterState
        {
            Create,
            Initialize,
            Appeared,
            Dispose,
        }

        private static readonly Dictionary<PresenterState, PresenterState> NextStateMap
            = new Dictionary<PresenterState, PresenterState>()
            {
                [PresenterState.Create] = PresenterState.Initialize,
                [PresenterState.Initialize] = PresenterState.Appeared,
                [PresenterState.Appeared] = PresenterState.Dispose,
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
            state = PresenterState.Create;
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
            if (state == PresenterState.Dispose)
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
                state = nextState;
                if (nextState == PresenterState.Initialize)
                {
                    presenter.Initialize(this);
                }
                else if (nextState == PresenterState.Appeared)
                {
                    presenter.DidAppeared();
                    if (whenAppeared != null)
                    {
                        whenAppeared.Invoke();
                        whenAppeared = null;
                    }
                }
                else if (nextState == PresenterState.Dispose)
                {
                    Dispose();
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
            state = PresenterState.Dispose;
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