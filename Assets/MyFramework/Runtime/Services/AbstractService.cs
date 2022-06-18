using System;

namespace MyFramework.Runtime.Services
{
    public abstract class AbstractService
    {
        public virtual byte CreatePriority => 255;

        public Type ServiceType => this.GetType();

        public virtual void OnCreated()
        {
        }

        public virtual void OnDestroy()
        {
        }

        public virtual void Initialize()
        {
        }
    }
}