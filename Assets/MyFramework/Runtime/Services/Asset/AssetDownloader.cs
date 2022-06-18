using System;
using System.Collections;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using UnityEditor;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.Networking;

namespace MyFramework.Runtime.Services.Asset
{
    public class AssetDownloadResult
    {
        public enum AssetDownloadResultType
        {
            None = 0,
            Successful = 1,
            Canceled = 2,
            Exception = 3,
        }

        public AssetDownloadResultType resultType = AssetDownloadResultType.None;
        public UnityWebRequest unityWebRequest;
        public Exception exception;
        public Uri uri => unityWebRequest == null ? null : unityWebRequest.uri;
    }

    public class AssetDownloader : MonoBehaviour
    {
        public class DownloadEvent : UnityEvent<AssetDownloadResult>
        {
        }

        private DownloadEvent onDownloadFinish = new DownloadEvent();
        private int runningTasks = 0;
        private int maxParallelTask = 10;
        private Queue<Func<IEnumerator>> pendingTasks;

        void Awake()
        {
            pendingTasks = new Queue<Func<IEnumerator>>();
        }

        void Update()
        {
            if (pendingTasks.Count > 0 && runningTasks < maxParallelTask)
            {
                var task = pendingTasks.Dequeue();
                StartCoroutine(task());
            }
        }

        public void CreateDownload(UnityWebRequest request, string savePath,
            CancellationToken token = default(CancellationToken))
        {
            if (request == null || string.IsNullOrEmpty(savePath))
                throw new ArgumentNullException("request or savePath is null value");
            Func<IEnumerator> task = () => CreateRequestInternal(request, token);
            pendingTasks.Enqueue(task);
        }

        public void CreateDownload(Uri uri, string savePath = null,
            CancellationToken token = default(CancellationToken))
        {
            var request = new UnityWebRequest(
                uri,
                "GET",
                savePath == null ? (DownloadHandler) new DownloadHandlerBuffer() : new DownloadHandlerFile(savePath),
                null);
            Func<IEnumerator> task = () => CreateRequestInternal(request, token);
            pendingTasks.Enqueue(task);
        }

        private IEnumerator CreateRequestInternal(UnityWebRequest request, CancellationToken cancellationToken)
        {
            runningTasks++;
            Debug.Log($"task begin runningTakes {runningTasks}, uri {request.uri}");
            request.SendWebRequest();
            while (!request.isDone)
            {
                if (cancellationToken.IsCancellationRequested)
                {
                    Debug.Log($"task canceled runningTakes {runningTasks}, uri {request.uri}");
                    onDownloadFinish.Invoke(new AssetDownloadResult()
                    {
                        exception = null,
                        resultType = AssetDownloadResult.AssetDownloadResultType.Canceled,
                        unityWebRequest = request,
                    });
                    yield break;
                }

                yield return null;
            }

            runningTasks--;
            onDownloadFinish.Invoke(new AssetDownloadResult()
            {
                exception = null,
                resultType = AssetDownloadResult.AssetDownloadResultType.Successful,
                unityWebRequest = request,
            });
            Debug.Log($"task finished runningTakes {runningTasks}, uri {request.uri}");
        }
    }
}