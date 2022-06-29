using System.Text;
using UnityEngine;

namespace MyFramework.Runtime.Services.Network.HTTP
{
    public class CommonHttpResponse : HttpResponse
    {
        private string _text;
        public byte[] bytes { get; private set; }

        public string utf8text
        {
            get
            {
                if (_text == null)
                {
                    _text = Encoding.UTF8.GetString(bytes);
                }

                return _text;
            }
        }

        public CommonHttpResponse()
        {
            
        }
        public CommonHttpResponse(byte[] bytes)
        {
            bytes = this.bytes;
        }
    }
}