using System.Linq;
using System.Reflection;
using System.Text;
using MyFramework.Services.UI;
using UnityEditor;
using UnityEngine;
using UnityEngine.SceneManagement;

namespace PSDtoUI
{
    public class SafeAreaWindow : EditorWindow
    {
        public static string PrefsKey = "SafeAreaSimulationType";

        private SafeAreaSimulationType _safeAreaSimulationType;

        /// <summary>
        ///     ウィンドウを開く
        /// </summary>
        [MenuItem("Tools/PSDtoUI/セーフエリア")]
        public static void OpenWindow()
        {
            GetWindow<SafeAreaWindow>("セーフエリア");
        }

        /// <summary>
        ///     ウィンドウのUI
        /// </summary>
        void OnGUI()
        {
            _safeAreaSimulationType = Load();

            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.LabelField("セーフエリアのシミュレーション");
            var prev = _safeAreaSimulationType;
            _safeAreaSimulationType = (SafeAreaSimulationType)EditorGUILayout.EnumPopup(_safeAreaSimulationType);

            if (_safeAreaSimulationType != prev)
            {
                Execute();
                Save(_safeAreaSimulationType);
            }

            if (GUILayout.Button("Reload"))
            {
                Execute();
            }

            EditorGUILayout.EndHorizontal();
            EditorGUILayout.Space();
        }

        void Execute()
        {
            var safeAreas = SceneManager.GetActiveScene()
                .GetRootGameObjects()
                .SelectMany(go => go.GetComponentsInChildren<SafeArea>());
            foreach (var safeArea in safeAreas)
            {
                SimulateSafeArea(safeArea, _safeAreaSimulationType);
            }
        }

        /// <summary>
        ///     `Screen.safeArea`をシミュレーションするヘルパー
        /// </summary>
        /// <param name="safeArea">セーフエリア</param>
        /// <param name="simulationType">セーフエリアのシミュレーションタイプ</param>
        public static void SimulateSafeArea(SafeArea safeArea, SafeAreaSimulationType simulationType)
        {
            var rect = safeArea.GetComponent<RectTransform>();
            if (rect == null)
            {
                Debug.LogError($"{safeArea.name}にRectTransformがアタッチされていません");
                return;
            }

            var screen = GetScreenSize();

            var value = simulationType.ToString();
            var sizeAttribute = simulationType.GetType()
                .GetMember(value)
                .FirstOrDefault()
                .GetCustomAttribute<SizeAttribute>();

            Rect safeAreaSize;
            if (sizeAttribute != null)
            {
                safeAreaSize = simulationType.SafeArea();
                if (!safeArea.stretchHorizontally)
                {
                    safeAreaSize.x = 0f;
                    safeAreaSize.width = screen.width;
                }
                if (!safeArea.stretchVertically)
                {
                    safeAreaSize.y = 0f;
                    safeAreaSize.height = screen.height;
                }
            }
            else
            {
                safeAreaSize = new Rect(0f, 0f, screen.width, screen.height);
            }


            rect.anchorMin = new Vector2(0f, 0f);
            rect.anchorMax = new Vector2(1f, 1f);
            rect.anchoredPosition = safeAreaSize.center - new Vector2(screen.width / 2f, screen.height / 2f);
            rect.sizeDelta = new Vector2(safeAreaSize.width - screen.width, safeAreaSize.height - screen.height);

            var sb = new StringBuilder();
            sb.Append($"SafeArea is simulated as {simulationType}\n");
            sb.Append($"ScreenSize is {screen}, SafeArea is pos ({safeAreaSize.x}, {safeAreaSize.y}) size ({safeAreaSize.width}, {safeAreaSize.height})");
            Debug.Log(sb);
        }

        public static void Save(SafeAreaSimulationType simulationType)
        {
            PlayerPrefs.SetInt(SafeAreaWindow.PrefsKey, (int)simulationType);
        }

        public static SafeAreaSimulationType Load()
        {
            var value = PlayerPrefs.GetInt(SafeAreaWindow.PrefsKey);
            var result = (SafeAreaSimulationType) value;
            return result;
        }

        static (int width, int height) GetScreenSize()
        {
            string[] resolution = UnityStats.screenRes.Split('x');
            int width = int.Parse(resolution[0]);
            int height = int.Parse(resolution[1]);
            return (width, height);
        }
    }
}

