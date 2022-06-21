using MyFramework.Runtime.Services.Event;
using MyFramework.Runtime.Services.Event.UI;

namespace MyFramework.Runtime.Services.UI
{
    public class NavigateLocatorProcessor : BaseLocatorProcessor
    {
        public NavigateLocatorProcessor()
        {
        }

        protected override bool IsDialogProcessor => false;
    }
}