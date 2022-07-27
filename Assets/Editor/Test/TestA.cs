using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace Edtior.Test
{
    public class TestA : MonoBehaviour
    {

        IEnumerator Start()
        {
            var result = DoSomething();
            yield return StartCoroutine(result);
            Debug.Log(result.Current);
        }
        IEnumerator<int> DoSomething()
        {
            yield return 1;
        }
    }
}