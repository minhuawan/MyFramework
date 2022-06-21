using UnityEngine.UIElements;

namespace MyFramework.Runtime.Services.UI
{
    public abstract class DialogView : NavigatedView
    {
        public override bool IsDialog => true;
    }
}