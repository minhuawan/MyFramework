using MyFramework.Runtime.Services.Network.HTTP;
using Newtonsoft.Json;

namespace App.Network.Http
{
    public class TestGetResponse : HttpResponse
    {
        [JsonProperty("get_message")] public string GetMessage;
    }
}