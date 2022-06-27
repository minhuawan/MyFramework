using UnityEngine;

namespace MyFramework.Runtime.Services.LocalStorage
{
    public class LocalStorageService : AbstractService
    {
        private Storage _user;
        public Storage User
        {
            get
            {
                if (_user == null)
                {
                    Debug.LogError("LocalStorageService user storage uninitialized");
                }
                return _user;
            }
        }

        public Storage Global { get; private set; }

        public override void Initialize()
        {
            Global = new Storage("local-storage/global.json");
        }

        public void CreateUserStorage(string identification)
        {
            if (_user != null)
            {
                _user.Dispose();
            }

            _user = null;
            if (string.IsNullOrEmpty(identification))
            {
                Debug.LogError("LocalStorageService CreateUserStorage failed, invalid identification");
                return;
            }

            var path = $"local-storage/{identification}/setting.json";
            _user = new Storage(path);
        }
        
        #if UNITY_EDITOR
        [UnityEditor.MenuItem("MyFramework/Open Storage Folder")]
        private static void OpenStorageFolder()
        {
            UnityEngine.Application.OpenURL(UnityEngine.Application.persistentDataPath + "/local-storage");
        }
        #endif
    }
}