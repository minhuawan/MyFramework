using System;
using System.Collections.Generic;

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

        private SwitchableMvpContextManager manager;
        private Action whenDisposed;


        public void WhenDisposed(Action when)
        {
            whenDisposed -= when;
            whenDisposed += when;
        }

        private MvpContext(SwitchableMvpContextManager manager, Presenter presenter, Model model)
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

        public static MvpContext OfType<T>(SwitchableMvpContextManager manager, Model model = null) where T : Presenter
        {
            var presenter = Activator.CreateInstance<T>();
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

            state = nextState;
            if (state == PresenterState.Created)
            {
                presenter.OnCreated(this);
            }
            else if (state == PresenterState.Initialize)
            {
                presenter.WillAppear();
            }
            else if (state == PresenterState.Appearing)
            {
                presenter.DidAppeared();
            }
            else if (state == PresenterState.Appeared)
            {
                presenter.WillDisappear();
            }
            else if (state == PresenterState.Disappearing)
            {
                presenter.DidDisappeared();
            }
            else if (state == PresenterState.Disappeared)
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