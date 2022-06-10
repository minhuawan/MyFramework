using System.Threading.Tasks;
using MyFramework.Services.UI;
using UnityEngine;
using UnityEngine.UI;

namespace App.UI.Views
{
    [ViewPath("Assets/AppData/Prefab/UI/View/TestView")]
    public class TestView : View
    {
        [SerializeField] private Text title;

        public static async Task<TestView> LoadAsync() => await InstantiatePrefabAsync<TestView>();

        public void Initialize(string message)
        {
            title.text = message;
        }
    }
}