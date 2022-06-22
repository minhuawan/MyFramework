using System;
using MyFramework.Runtime.Services.Event;
using MyFramework.Runtime.Services.Event.UI;

namespace MyFramework.Runtime.Services.UI
{
    public class NavigateLocatorProcessor : BaseLocatorProcessor
    {
        public NavigateLocatorProcessor(Action<PresenterLocator, NavigateResult> handler) : base(handler)
        {
        }

        protected override bool IsDialogProcessor => false;
    }
}