using System.IO;

namespace MyFramework.Runtime.Services.VirtualFileSystem
{
    public class VirtualFileSystemService : AbstractService
    {
        private readonly string version = "1.0";
        private string basePath;
        private Hash hash;
        private Disk disk;

        public override void OnCreated()
        {
            hash = new Hash();
            disk = new Disk();
            basePath = Path.Combine(UnityEngine.Application.persistentDataPath, "vfs");
            TryInstall();
        }

        private void TryInstall()
        {
            if (!Directory.Exists(basePath))
            {
                Directory.CreateDirectory(basePath);
            }

            var vfsVersionFilePath = Path.Combine(basePath, "version");
            if (!File.Exists(vfsVersionFilePath))
            {
                File.WriteAllText(vfsVersionFilePath, version);
            }

            for (var i = 0; i < 256; i++)
            {
                var path = Path.Combine(basePath, i.ToString("x2"));
                if (!Directory.Exists(path))
                {
                    Directory.CreateDirectory(path);
                }
            }
        }

        public override void OnDestroy()
        {
            hash.Dispose();
            disk.Dispose();
        }

        public string GetVFSPath(string filePath)
        {
            var hash128 = hash.Hash128(filePath, out var head);
            var vfsPath = Path.Combine(basePath, head, hash128);
            return vfsPath;
        }
    }
}