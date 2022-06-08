using System;
using System.Security.Cryptography;
using System.Threading.Tasks;
using MyFramework.Services.Resource;
using UnityEditor;
using UnityEngine;

namespace MyFramework.Services.UI
{
    public struct TransitionResult
    {
        public enum ResultType
        {
            None = 0,
            Successful = 1,
            Failed = 2,
            Canceled = 3,
            Exception = 4,
        }

        public ResultType Type;
        public string Message;
        public Exception ExceptionInfo;

        public static TransitionResult Failed(string msg = null) =>
            new TransitionResult()
            {
                Type = ResultType.Failed,
                Message = msg,
            };

        public static TransitionResult Exception(Exception e) =>
            new TransitionResult()
            {
                Type = ResultType.Exception,
                ExceptionInfo = e,
                Message = null,
            };

        public static TransitionResult Canceled(string msg) =>
            new TransitionResult()
            {
                Type = ResultType.Canceled,
                Message = msg,
            };

        public static TransitionResult Ok =>
            new TransitionResult()
            {
                Type = ResultType.Successful,
                Message = null,
            };
    }
}