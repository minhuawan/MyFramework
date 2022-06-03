using Newtonsoft.Json;

namespace MyFramework.Services.Network.Tcp
{
    public class TcpProtocol
    {
        public virtual int protocolId { get; }

        [JsonProperty("code")] public int code { get; set; }
    }
}