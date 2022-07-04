using System;
using System.IO;
using UnityEngine.Rendering;

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
            PrepareDirectories();
        }

        private void PrepareDirectories()
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

        public void WriteBytes(string filePath, ArraySegment<byte> bytes)
        {
            var vfsPath = GetVFSPath(filePath);
            File.WriteAllBytes(vfsPath, bytes.Array);
        }

        private void WriteAsync(string filePath, ArraySegment<byte> bytes, Action onFinished)
        {
            throw new NotImplementedException("todo");
        }

        public ArraySegment<byte> ReadBytes(string filePath)
        {
            var vfsPath = GetVFSPath(filePath);
            if (!File.Exists(vfsPath))
            {
                return default;
            }

            var bytes = File.ReadAllBytes(vfsPath);
            return new ArraySegment<byte>(bytes);
        }

        public void ReadBytesAsync(string filePath, Action<ArraySegment<byte>> onFinished)
        {
            throw new NotImplementedException("todo");
        }
    }
}