namespace MyFramework.Services.Resource
{
    public interface IResourceLoader
    {
        ResourceReference Load(string path);
    }
}