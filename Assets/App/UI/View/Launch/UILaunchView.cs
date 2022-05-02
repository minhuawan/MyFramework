using MyFramework.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace App.UI.View.Launch
{
    public class UILaunchView : UIView
    {
        [SerializeField] private Text text;

        public void Initialize(string message)
        {
            text.text = message;
        }
    }
}