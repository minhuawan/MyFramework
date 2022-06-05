using System.Collections.Generic;

namespace MyFramework.Services.UI
{
    public class UIManager
    {
        private Queue<UIPresenter> queue;

        public UIManager()
        {
            queue = new Queue<UIPresenter>();
        }
    }
}