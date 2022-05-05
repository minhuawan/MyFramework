namespace MyFramework.Services.Resource
{
    public interface IResourceObject
    {
        T Instantiate<T>() where T : UnityEngine.Object;
    }
}