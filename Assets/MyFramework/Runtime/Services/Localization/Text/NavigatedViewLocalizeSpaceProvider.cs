using System;
using System.Collections.Generic;
using System.Linq;
using MyFramework.Runtime.Services.UI;
using UnityEngine;

namespace MyFramework.Runtime.Services.Localization
{
    [RequireComponent(typeof(NavigatedView))]
    public class NavigatedViewLocalizeSpaceProvider : MonoBehaviour
    {
        [SerializeField, Header("Translate text space, # means ignore")]
        private string _space = "#";

        public string Space => _space == "#" ? null : _space;

        [Header("The texts will auto refresh on validate")]
        public List<DirectTranslateText> texts;

#if UNITY_EDITOR
        private void OnValidate()
        {
            if (string.IsNullOrEmpty(_space))
            {
                _space = "ui." + gameObject.name.ToLower();
            }
            else
            {
                _space = _space.ToLower();
            }

            texts = GetComponentsInChildren<DirectTranslateText>(true).ToList();
        }
#endif

        public void DirectTranslateTexts()
        {
            if (texts == null || texts.Count == 0)
            {
                return;
            }


            var localizationService = Application.GetService<LocalizationService>();
            for (var i = 0; i < texts.Count; i++)
            {
                var translateText = texts[i];
                if (translateText == null || translateText.text == null)
                {
                    continue;
                }

                if (string.IsNullOrEmpty(translateText.space))
                {
                    translateText.text.text = "$EMPTY_SPACE$";
                }
                else if (string.IsNullOrEmpty(translateText.key))
                {
                    translateText.text.text = "$EMPTY_KEY$";
                }
                else
                {
                    var translated = localizationService.Translate(translateText.space, translateText.key);
                    translateText.text.text = translated;
                }
            }
        }
    }
}