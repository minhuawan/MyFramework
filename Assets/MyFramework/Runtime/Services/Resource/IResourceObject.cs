namespace MyFramework.Services.Resource
{
    public interface IResourceObject
    {
        public T CreateObject<T>() where T : UnityEngine.Object;
        public void RecycleObject<T>(T obj) where T : UnityEngine.Object;
        public void CleanUp();
    }
}