using System;
using System.Collections.Generic;
using UnityEngine;

namespace MyFramework.Runtime.Services.UI2
{
    public class UIService : AbstractService
    {
        private IMvpContextManager switchableMvpContextManager;
        private IMvpContextManager singleMvpContextManager;
        public override void OnCreated()
        {
            switchableMvpContextManager = new SwitchableMvpContextManager();
            singleMvpContextManager = new SingleMvpContextManager();
        }
    }
}