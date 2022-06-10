using System.Threading.Tasks;
using MyFramework.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace App.UI.Views
{
    [ViewPath("Assets/AppData/Prefab/UI/View/TestDialogView")]
    public class TestDialogView : DialogView
    {
        [SerializeField] private Text title;
        public static Task<TestDialogView> LoadAsync() => InstantiatePrefabAsync<TestDialogView>();

        public void SetTitle(string txt)
        {
            title.text = txt;
        }
    }
}