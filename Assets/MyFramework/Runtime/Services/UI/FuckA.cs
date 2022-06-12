using System;
using System.Linq;
using System.Reflection;
using UnityEngine;

namespace PSDtoUI
{
    /// <summary>
    ///     Screen.safeAreaが返す値をシミュレーションする
    ///     1. `Default`の場合
    ///         セーフエリアの矩形がパディングなしとして解釈される
    ///     2. それ以外の場合
    ///         実機確認時に`Screen.safeArea`で取得できる値を`SizeAttribute`に記載する
    /// </summary>
    public enum SafeAreaSimulationType
    {
        Default = 0,

        [Size("iPhone X/XS 2436x1125 Landscape", 132f, 63f, 2172f, 1062f)]
        iPhoneXLandscape,
        [Size("Xiaomi RedMi Note7 pro 2260x1080 Landscape", 80, 0, 2260, 1080)]
        XiaomiNote7,
    }

    /// <summary>
    ///     セーフエリアの矩形情報を保持するための属性
    /// </summary>
    public class SizeAttribute : Attribute
    {
        public string Name;
        public Rect SafeArea;

        public SizeAttribute(string name, float x, float y, float width, float height)
        {
            Name = name;
            SafeArea = new Rect(x, y, width, height);
        }
    }

    /// <summary>
    ///     `SafeAreaSimulationType`からセーフエリアを取得するための拡張メソッド
    /// </summary>
    public static class SafeAreaSimulationTypeExtension
    {
        public static string Name(this SafeAreaSimulationType simulationType)
        {
            var value = simulationType.ToString();
            var attribute = simulationType.GetType()
                .GetMember(value)
                .First()
                .GetCustomAttribute<SizeAttribute>();
            return attribute.Name;
        }

        public static Rect SafeArea(this SafeAreaSimulationType simulationType)
        {
            var value = simulationType.ToString();
            var attribute = simulationType.GetType()
                .GetMember(value)
                .First()
                .GetCustomAttribute<SizeAttribute>();
            return attribute.SafeArea;
        }
    }
}