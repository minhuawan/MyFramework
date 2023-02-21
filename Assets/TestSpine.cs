using System;
using Spine.Unity;
using UnityEngine;

public class TestSpine : MonoBehaviour {
    private SkeletonAnimation animation;


    [SpineAnimation] public string animationName;

    private void Awake() {
        animation = GetComponent<SkeletonAnimation>();

        animation.AnimationState.SetAnimation(0, animationName, true);
    }
}