using System;
using System.Threading.Tasks;
using UnityEngine;

public static class TestTaskExtenstion
{
    public static Task UnWrap(this Task task)
    {
        if (task.IsCompleted)
        {
            return task;
        }

        if (task.Exception != null)
        {
            Debug.LogError("task exception " + task.Exception);
            return task;
        }

        task.ContinueWith(t =>
        {
            if (t.Exception != null)
            {
                var token = DateTime.Now.ToString();
                for (var i = 0; i < t.Exception.InnerExceptions.Count; i++)
                {
                    Debug.LogError($"{token} {i} / {t.Exception.InnerExceptions.Count} task exception on continue with " + t.Exception.InnerExceptions[i]);
                }
            }
        });
        return task;
    }
}