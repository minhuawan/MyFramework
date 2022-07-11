using System;

namespace MyFramework.Runtime.Services.UI2
{
    public interface IMvpFlow : IDisposable
    {
        public void OnCreated(MvpContext context);
        public void WillAppear();
        public void DidAppeared();
        public void WillDisappear();
        public void DidDisappeared();
        public void OnBackKey();
        public void Dispose();
    }
}