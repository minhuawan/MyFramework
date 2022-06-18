using System;
using System.Runtime.InteropServices;
using Newtonsoft.Json;
using UnityEditorInternal;

namespace MyFramework.Runtime.Services.Network.HTTP
{
    public class HttpResponse
    {
        [JsonProperty("response_code")] public long ResponseCode;
        [JsonProperty("message")] public string Message;
        public bool IsSuccessful => ResponseCode == 200;


        public static T CreateFailedResponse<T>(long code, string message) where T : HttpResponse
        {
            var httpResponse = Activator.CreateInstance<T>();
            httpResponse.Message = message;
            httpResponse.ResponseCode = code;
            return httpResponse;
        }
        
    }
}