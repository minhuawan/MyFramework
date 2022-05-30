using UnityEngine;

namespace MyFramework.Services.Network.HTTP
{
    public class HttpCodec : INetworkCodec
    {
        public byte[] Encode(INetworkRequest request)
        {
            var jsonStr = JsonUtility.ToJson(request);
            var bytes = System.Text.Encoding.UTF8.GetBytes(jsonStr);
            return bytes;
        }

        public T Decode<T>(byte[] bytes) where T : INetworkResponse
        {
            var jsonStr = System.Text.Encoding.UTF8.GetString(bytes);
            var obj = JsonUtility.FromJson<T>(jsonStr);
            return obj;
        }
    }
}