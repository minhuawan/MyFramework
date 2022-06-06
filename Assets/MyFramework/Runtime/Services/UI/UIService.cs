using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using App.UI.View.Launch;
using MyFramework.Services.Resource;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public class UIService : AbstractService
    {
        private Stack<Presenter> stack;

        public override void OnCreated()
        {
            stack = new Stack<Presenter>();
        }

        public override void OnDestroy()
        {
            foreach (var uiPresenter in stack)
            {
                uiPresenter.Dispose();
            }

            stack.Clear();
        }

        public async Task SwitchPresenterAsync()
        {
            
        }

        public void Back()
        {
            if (stack.Count > 0)
            {
                var presenter = stack.Peek();
                presenter.OnBackKey();
            }
        }
    }
}