using App.StateMachine;
using MyFramework.Runtime.Services.StateMachine;

namespace MyFramework.Runtime.Services.Bootstrap
{
    public class BootstrapService : AbstractService
    {
        public void Run()
        {
            //
            GotoSplash();
        }

        public void Quit()
        {
        }

        public void Reboot()
        {
            // todo reboot
        }

        private void GotoSplash()
        {
            Application.GetService<StateMachineService>().ChangeState<SplashStateMachine>();
        }
    }
}