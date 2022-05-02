﻿using System;

namespace MyFramework.Services
{
    public abstract class AService
    {
        public virtual byte CreatePriority => 255;

        public Type ServiceType => this.GetType();
        public abstract void OnCreated();
        public abstract void OnDestroy();
    }
}