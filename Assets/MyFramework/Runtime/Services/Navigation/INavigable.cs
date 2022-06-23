using System;

namespace MyFramework.Runtime.Services.Navigation
{
    public interface INavigable
    {
        void OnNavigate();
        void OnAppear();
        void OnDidAppear();
        void OnDisappear();
        void OnLeave();
    }
}