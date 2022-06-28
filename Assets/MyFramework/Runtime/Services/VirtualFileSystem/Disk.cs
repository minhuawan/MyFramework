using System;
using System.Linq;

namespace MyFramework.Runtime.Services.VirtualFileSystem
{
    public class Disk : IDisposable
    {
        private string writeablePath;

        // todo ios/android
        private long deviceAvailableDiskSpace;
        private long deviceTotalDiskSpace;
        private long appUsedDiskSpace;

        public int FreeDiskSpaceMB => (int) deviceAvailableDiskSpace / 1024;
        public int TotalDiskSpaceMB => (int) deviceTotalDiskSpace / 1024;
        public int AppUsedDiskSpaceMB => (int) appUsedDiskSpace / 1024;

        public Disk()
        {
            writeablePath = UnityEngine.Application.persistentDataPath;
        }

        public void RefreshDiskSpace()
        {
#if UNITY_EDITOR_WIN
            var info = new System.IO.DirectoryInfo(writeablePath);
            appUsedDiskSpace = 0;
            deviceAvailableDiskSpace = 1024 * 1024 * 10; // 10GB
            deviceTotalDiskSpace = 1024 * 1024 * 20; // 20GB
#elif UNITY_ANDROID
            throw new Exception("not implementation");
#elif UNITY_IOS
            throw new Exception("not implementation");
#endif
        }

        public void Dispose()
        {
        }
    }
}