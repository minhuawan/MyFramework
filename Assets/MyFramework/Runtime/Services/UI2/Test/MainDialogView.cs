using UnityEngine;
using UnityEngine.UI;

namespace MyFramework.Runtime.Services.UI2
{
    [ViewPath("UI/View/MainDialogView")]
    public class MainDialogView : DialogView
    {
        [SerializeField] private Text text;

        public void SetData(MainModel model)
        {
            text.text = model.DialogTitle;
        }
    }
}