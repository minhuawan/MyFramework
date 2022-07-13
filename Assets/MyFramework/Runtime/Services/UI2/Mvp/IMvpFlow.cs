using System;

namespace MyFramework.Runtime.Services.UI2
{
    public interface IMvpFlow : IDisposable
    {
        public void Initialize(MvpContext mvpContext);
        public void DidAppeared();
        public void OnBackKey();
        public void Dispose();
    }
}