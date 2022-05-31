﻿using System;
 using System.Collections.Generic;
 using System.Text;
 using Newtonsoft.Json;
 using UnityEngine;

namespace MyFramework.Services.Network.HTTP
{
    public class HttpPostParam : IHttpRequestParam
    {
        private readonly Encoding Encoding = Encoding.UTF8;
        // private WWWForm form = new WWWForm();
        // public WWWForm Form => form;

        public byte[] data => Encoding.GetBytes(JsonConvert.SerializeObject(jsonDict));

        private Dictionary<string, object> jsonDict = new Dictionary<string, object>();

        public void Set(string key, string value)
        {
            // form.AddBinaryData(key, Encoding.GetBytes(value));
            jsonDict[key] = value;
        }

        public void Set(string key, long value)
        {
            // form.AddBinaryData(key, BitConverter.GetBytes(value));
            jsonDict[key] = value;
        }

        public void Set(string key, double value)
        {
            // form.AddBinaryData(key, BitConverter.GetBytes(value));
            jsonDict[key] = value;
        }

        public void Set(string key, byte value)
        {
            // form.AddBinaryData(key, BitConverter.GetBytes(value));
            jsonDict[key] = value;
        }

        public void Set(string key, bool value)
        {
            // form.AddBinaryData(key, BitConverter.GetBytes(value));
            jsonDict[key] = value;
        }

        public void Set(string key, byte[] value)
        {
            // form.AddBinaryData(key, value);
            jsonDict[key] = value;
        }
    }
}