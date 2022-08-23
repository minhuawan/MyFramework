using System;
using System.Collections.Generic;
using System.Text;

namespace MyFramework.Runtime.Services.Network.HTTP
{
    public class HttpUrlParam : IHttpRequestParam
    {
        public IEnumerable<KeyValuePair<string, string>> Query => query;

        private Dictionary<string, string> query = new Dictionary<string, string>();

        public void Set(string key, string value)
        {
            query[key] = value;
        }

        public void Set(string key, long value)
        {
            query[key] = value.ToString();
        }

        public void Set(string key, double value)
        {
            query[key] = value.ToString();
        }

        public void Set(string key, byte value)
        {
            query[key] = value.ToString();
        }

        public void Set(string key, bool value)
        {
            query[key] = value.ToString();
        }

        public void Set(string key, byte[] value)
        {
            query[key] = value.ToString();
        }

        public void SetJson(string json)
        {
            throw new NotImplementedException();
        }
    }
}