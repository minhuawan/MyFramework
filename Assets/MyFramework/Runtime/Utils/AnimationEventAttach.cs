using System;
using System.Collections.Generic;
using System.Globalization;
using UnityEngine;

namespace MyFramework.Utils
{
    public class AnimationEventAttach : MonoBehaviour
    {
        public static void Play(Animator animator, string animationName, Action callback = null)
        {
            
            animator.enabled = true;
            var ex = animator.gameObject.AddComponent<AnimationEventAttach>();
            animator.Play(animationName, -1, 0);
            ex.Initialize(animator, animationName, callback);
        }

        private Action callback;
        [SerializeField] private Animator animator;
        [SerializeField] private string animationName;
        [SerializeField] private bool isEventAttached = false;

        private void Initialize(Animator animator, string animationName, Action callback)
        {
            this.animationName = animationName;
            this.animator = animator;
            this.callback = callback;
            isEventAttached = false;
        }

        private void LateUpdate()
        {
            if (!isEventAttached)
            {
                var clip = animator.runtimeAnimatorController.animationClips[0];
                clip.AddEvent(new AnimationEvent()
                {
                    functionName = "OnAnimationFinished",
                    time = clip.length,
                    intParameter = this.GetHashCode(),
                });
                isEventAttached = true;
            }
        }


        public void OnAnimationFinished(int hashCode)
        {
            if (hashCode != this.GetHashCode())
                return;
            animator.enabled = false;
            callback?.Invoke();
            Destroy(this);
        }
    }
}