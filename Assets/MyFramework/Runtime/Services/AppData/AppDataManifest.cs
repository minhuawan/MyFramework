using System.Collections.Generic;
using System.Text;

namespace MyFramework.Runtime.Services.AppData
{
    public class AppDataItem
    {
        public string path;
        public string hash128;
        public List<string> dependencies;
    }

    public class AppDataManifest
    {
        public string version;
        public List<AppDataItem> staticList;
        public List<AppDataItem> nonstaticList;
    }
}