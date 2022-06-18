using System;
using System.Text;

namespace MyFramework.Runtime.Services.UI
{
    public struct NavigateResult
    {
        public enum ResultType
        {
            None = 0,
            Ok = 1,
            Failed = 2,
            Canceled = 3,
            Exception = 4,
        }

        public ResultType Type;
        public string Message;
        public Exception InnerException;

        public override string ToString()
        {
            var sb = new StringBuilder();
            sb.Append($"{nameof(NavigateResult)} type: {Type}, message: {Message}");
            // if (InnerException != null)
            // {
            //     sb.Append(", exception: " + InnerException.ToString());
            // }

            return sb.ToString();
        }

        public static NavigateResult Failed(string msg = null) =>
            new NavigateResult()
            {
                Type = ResultType.Failed,
                Message = msg,
            };

        public static NavigateResult Exception(Exception e) =>
            new NavigateResult()
            {
                Type = ResultType.Exception,
                InnerException = e,
                Message = e.ToString(),
            };

        public static NavigateResult Canceled(string msg) =>
            new NavigateResult()
            {
                Type = ResultType.Canceled,
                Message = msg,
            };

        public static NavigateResult Ok =>
            new NavigateResult()
            {
                Type = ResultType.Ok,
                Message = null,
            };
    }
}