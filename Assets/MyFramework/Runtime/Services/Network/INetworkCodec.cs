namespace MyFramework.Services.Network
{
    public interface INetworkCodec
    {
        public byte[] Encode(INetworkRequest request);
        public T Decode<T>(byte[] bytes) where T : INetworkResponse;
    }
}