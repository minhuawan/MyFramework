using System.Linq;

namespace MyFramework.Services.UI
{
    public abstract class UIPresenter
    {
        public abstract void OnCreated(UIView view);
        public abstract void OnDestroy();
        public abstract void OnOpened();
        public abstract void OnClose();

        public static string GetPrefabPath<T>() where T : UIPresenter
        {
            var attributes = typeof(T).GetCustomAttributes(typeof(ViewPath), false);
            if (attributes == null || attributes.Length == 0)
                return null;
            foreach (var attribute in attributes)
            {
                if (attribute is ViewPath viewPath)
                {
                    return viewPath.GetPath();
                }
            }

            return null;
        }
    }
}