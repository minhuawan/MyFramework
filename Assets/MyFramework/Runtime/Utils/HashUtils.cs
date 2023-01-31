using System.IO;
using System.Security.Cryptography;
using System.Text;
using UnityEngine;

namespace MyFramework.Utils
{
    public class HashUtils
    {
        public static string FileHash(string filePath)
        {
            if (!File.Exists(filePath))
            {
                Debug.LogError($"FileHash failed, file not existed, path: {filePath}");
                return string.Empty;
            }

            using (var md5 = MD5.Create())
            {
                var bytes = File.ReadAllBytes(filePath);
                var hashs = md5.ComputeHash(bytes);
                var sb = new StringBuilder();
                foreach (var h in hashs)
                {
                    sb.Append(h.ToString("x2"));
                }
                return sb.ToString();
            }
        }

        public static string StringHash(string str)
        {
            using (var md5 = MD5.Create())
            {
                var bytes = Encoding.UTF8.GetBytes(str);
                var hashs = md5.ComputeHash(bytes);
                var sb = new StringBuilder();
                foreach (var h in hashs)
                {
                    sb.Append(h.ToString("x2"));
                }
                return sb.ToString();
            }
        }
    }
}