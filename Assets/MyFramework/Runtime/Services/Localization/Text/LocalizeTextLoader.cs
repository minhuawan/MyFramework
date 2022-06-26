using System.Collections.Generic;
using UnityEditor.AddressableAssets.HostingServices;
using UnityEditor.Build.Pipeline;
using UnityEditor.UIElements;

namespace MyFramework.Runtime.Services.Localization
{
    public class LocalizeTextLoader
    {
        public List<LocalizeTextManager.LocalizeTextEntity> Load(string language, string space)
        {
            if ("const".Equals(space))
                return TestConst(language);
            if ("ui.base".Equals(space))
                return TestUIBase(language);
            return null;
        }

        private List<LocalizeTextManager.LocalizeTextEntity> TestUIBase(string language)
        {
            return new List<LocalizeTextManager.LocalizeTextEntity>()
            {
                new LocalizeTextManager.LocalizeTextEntity()
                {
                    key = "ui.base.button-ok",
                    text = language == "en-us" ? "OK" : "好的",
                },
                new LocalizeTextManager.LocalizeTextEntity()
                {
                    key = "ui.base.welcome",
                    text = language == "en-us" ? "welcome" : "欢迎",
                },                
            };
        }

        private List<LocalizeTextManager.LocalizeTextEntity> TestConst(string language)
        {
            return new List<LocalizeTextManager.LocalizeTextEntity>()
            {
                new LocalizeTextManager.LocalizeTextEntity()
                {
                    key = "const.game-name",
                    text = language == "en-us" ? "TestDemo" : "测试项目",
                }
            };
        }
    }
}