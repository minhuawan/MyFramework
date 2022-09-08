using UnityEngine;

namespace MyFramework.Runtime.Utils
{
    public static class Storage
    {
        public static void Write(string path, string value)
        {
            PlayerPrefs.SetString(path, value);
            PlayerPrefs.Save();
        }

        public static string Read(string path)
        {
            return PlayerPrefs.GetString(path);
        }

        public static bool Has(string path)
        {
            return PlayerPrefs.HasKey(path);
        }

        public static void Delete(string path)
        {
            PlayerPrefs.DeleteKey(path);
        }
    }
}