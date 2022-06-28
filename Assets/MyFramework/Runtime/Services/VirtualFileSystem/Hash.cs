using System;
using System.Security.Cryptography;
using System.Text;

namespace MyFramework.Runtime.Services.VirtualFileSystem
{
    public class Hash : IDisposable
    {
        private MD5 md5;
        private StringBuilder sb;


        public Hash()
        {
            sb = new StringBuilder();
            md5 = MD5.Create();
        }

        public string Hash128(string str, out string head)
        {
            var bytes = Encoding.UTF8.GetBytes(str);
            sb.Clear();
            var hash = md5.ComputeHash(bytes);
            for (var i = 0; i < hash.Length; i++)
            {
                sb.Append(hash[i].ToString("x2"));
            }

            head = $"{hash[0]:x2}";

            return sb.ToString();
        }

        public void Dispose()
        {
            md5?.Dispose();
            sb.Clear();
        }
    }
}