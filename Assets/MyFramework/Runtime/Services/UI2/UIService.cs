using System;

namespace MyFramework.Runtime.Services.UI2
{
    public class UIService : AbstractService
    {
        private SwitchableMvpContextManager switchableMvpContextManager;
        private SingleMvpContextManager singleMvpContextManager;

        public override void OnCreated()
        {
            switchableMvpContextManager = new SwitchableMvpContextManager();
            singleMvpContextManager = new SingleMvpContextManager();
        }

        public void SwitchRoot<T>(Model model = null) where T : RootPresenter
        {
            var type = typeof(T);
            switchableMvpContextManager.Switch(type, model);
        }

        public void SwitchRoot(Type presenterType, Model model = null)
        {
            switchableMvpContextManager.Switch(presenterType, model);
        }
        
        public void ShowDialog<T>(Model model = null) where T : DialogPresenter
        {
            singleMvpContextManager.Show(typeof(T), model);
        }

        public void ShowDialog(Type presenterType, Model model = null)
        {
            singleMvpContextManager.Show(presenterType, model);
        }
    }
}