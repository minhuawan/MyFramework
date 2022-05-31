using MyFramework.Services.Network.HTTP;
using Newtonsoft.Json;

namespace App.Network.Http
{
    public class TestPostResponse : HttpResponse
    {
        [JsonProperty("post_message")] public string PostMessage;
    }
}