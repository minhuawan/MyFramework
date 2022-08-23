namespace MyFramework.Runtime.Services.Network.HTTP
{
    public interface IHttpRequestParam
    {
        public void Set(string key, string value);
        public void Set(string key, long value);
        public void Set(string key, double value);
        public void Set(string key, byte value);
        public void Set(string key, bool value);
        public void Set(string key, byte[] value);

        public void SetJson(string json);
    }
}