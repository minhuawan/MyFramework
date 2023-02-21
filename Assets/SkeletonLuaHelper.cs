using System;
using System.Collections;
using Spine;
using Spine.Unity;
using UnityEngine;

public class SkeletonLuaHelper : MonoBehaviour {
    private SkeletonAnimation skeletonAnimation;

    [SpineAnimation] public string initialAnimationName;
    [SpineAnimation] public string secondAnimationName;
    [SpineSkin] public string initialSkin;
    public bool initialAnimationLoop = true;

    public int mainTrackIndex = 0;
    public bool playOnAwake = true;

    private string currentAnimationName;
    private bool currentAnimationLoop;
    private Action callback;
    private float delaySecondCall;

    private void Awake() {
        skeletonAnimation = GetComponent<SkeletonAnimation>();
        skeletonAnimation.AnimationState.SetAnimation(mainTrackIndex, initialAnimationName, initialAnimationLoop);
        skeletonAnimation.AnimationState.Complete += OnTrackCompleted;

        currentAnimationName = null;
        currentAnimationLoop = false;

        SetToDefaultAnimation();
    }

    private void OnTrackCompleted(TrackEntry entry) {
        if (currentAnimationName == null
            || currentAnimationLoop
            || currentAnimationName != entry.Animation.Name)
            return;
        currentAnimationName = null;
        StartCoroutine(RunCallback());
    }

    private IEnumerator RunCallback() {
        yield return new WaitForSeconds(delaySecondCall);
        callback();
        callback = null;
        delaySecondCall = 0f;
    }

    public void SetToDefaultAnimation() {
        SetAnimation(initialAnimationName, initialAnimationLoop);
    }

    public void SetAnimation(string animationName, bool loop) {
        skeletonAnimation.AnimationState.SetAnimation(mainTrackIndex, animationName, loop);
        currentAnimationName = animationName;
        currentAnimationLoop = loop;
    }

    public void SetAnimationWithCompletedCallback(string animationName, Action action, float delay) {
        if (this.callback != null) {
            throw new Exception("current callback in waiting");
        }

        SetAnimation(animationName, false);
        this.callback = action;
        this.delaySecondCall = delay;
    }

    void Update() {
        if (Input.GetKeyDown(KeyCode.Space)) {
            SetAnimationWithCompletedCallback(secondAnimationName,
                () => {
                    Debug.Log("second animation play finished with delay 0.3f seconds");
                    SetToDefaultAnimation();
                }, 0.3f);
        }
    }
}