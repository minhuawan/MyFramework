using System;
using MyFramework.Runtime.Services.Event;
using MyFramework.Runtime.Services.Event.UI;

namespace MyFramework.Runtime.Services.UI
{
    public class NavigateLocatorProcessor : BaseLocatorProcessor
    {
        public NavigateLocatorProcessor(Action<PresenterLocator, NavigateResult> resultListener) : base(resultListener)
        {
        }

        protected override bool IsDialogProcessor => false;
    }
}