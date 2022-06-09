using System;

namespace MyFramework.Services.UI
{
    public class TransitionScope : IDisposable
    {
        private Presenter current;
        private Presenter next;

        public TransitionScope(Presenter current, Presenter next)
        {
            this.current = current;
            this.next = next;
        }

        public void Dispose()
        {
            this.current?.Dispose();
        }
    }
}