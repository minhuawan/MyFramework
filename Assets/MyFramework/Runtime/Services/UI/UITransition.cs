using System;
using System.Security.Cryptography;
using System.Threading.Tasks;
using MyFramework.Services.Resource;
using UnityEditor;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public struct UITransitionResult
    {
        public enum ResultType
        {
            Successful = 1,
            Failed = 2,
            Canceled = 3,
        }

        public ResultType Result;
        public string Message;

        public static UITransitionResult Failed(string msg) =>
            new UITransitionResult()
            {
                Result = ResultType.Failed,
                Message = msg,
            };

        public static UITransitionResult Cancel(string msg) =>
            new UITransitionResult()
            {
                Result = ResultType.Canceled,
                Message = msg,
            };

        public static UITransitionResult Ok =>
            new UITransitionResult()
            {
                Result = ResultType.Successful,
                Message = null,
            };
    }
}