/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Animation<T>
/*     */ {
/*     */   T[] keyFrames;
/*     */   private float frameDuration;
/*     */   private float animationDuration;
/*     */   private int lastFrameNumber;
/*     */   private float lastStateTime;
/*     */   
/*     */   public enum PlayMode
/*     */   {
/*  35 */     NORMAL,
/*  36 */     REVERSED,
/*  37 */     LOOP,
/*  38 */     LOOP_REVERSED,
/*  39 */     LOOP_PINGPONG,
/*  40 */     LOOP_RANDOM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private PlayMode playMode = PlayMode.NORMAL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation(float frameDuration, Array<? extends T> keyFrames) {
/*  57 */     this.frameDuration = frameDuration;
/*  58 */     T[] frames = (T[])new Object[keyFrames.size];
/*  59 */     for (int i = 0, n = keyFrames.size; i < n; i++) {
/*  60 */       frames[i] = (T)keyFrames.get(i);
/*     */     }
/*  62 */     setKeyFrames(frames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation(float frameDuration, Array<? extends T> keyFrames, PlayMode playMode) {
/*  70 */     this(frameDuration, keyFrames);
/*  71 */     setPlayMode(playMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Animation(float frameDuration, T... keyFrames) {
/*  79 */     this.frameDuration = frameDuration;
/*  80 */     setKeyFrames(keyFrames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getKeyFrame(float stateTime, boolean looping) {
/*  93 */     PlayMode oldPlayMode = this.playMode;
/*  94 */     if (looping && (this.playMode == PlayMode.NORMAL || this.playMode == PlayMode.REVERSED)) {
/*  95 */       if (this.playMode == PlayMode.NORMAL)
/*  96 */       { this.playMode = PlayMode.LOOP; }
/*     */       else
/*  98 */       { this.playMode = PlayMode.LOOP_REVERSED; } 
/*  99 */     } else if (!looping && this.playMode != PlayMode.NORMAL && this.playMode != PlayMode.REVERSED) {
/* 100 */       if (this.playMode == PlayMode.LOOP_REVERSED) {
/* 101 */         this.playMode = PlayMode.REVERSED;
/*     */       } else {
/* 103 */         this.playMode = PlayMode.LOOP;
/*     */       } 
/*     */     } 
/* 106 */     T frame = getKeyFrame(stateTime);
/* 107 */     this.playMode = oldPlayMode;
/* 108 */     return frame;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getKeyFrame(float stateTime) {
/* 118 */     int frameNumber = getKeyFrameIndex(stateTime);
/* 119 */     return this.keyFrames[frameNumber];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getKeyFrameIndex(float stateTime) {
/*     */     int lastFrameNumber;
/* 126 */     if (this.keyFrames.length == 1) return 0;
/*     */     
/* 128 */     int frameNumber = (int)(stateTime / this.frameDuration);
/* 129 */     switch (this.playMode) {
/*     */       case NORMAL:
/* 131 */         frameNumber = Math.min(this.keyFrames.length - 1, frameNumber);
/*     */         break;
/*     */       case LOOP:
/* 134 */         frameNumber %= this.keyFrames.length;
/*     */         break;
/*     */       case LOOP_PINGPONG:
/* 137 */         frameNumber %= this.keyFrames.length * 2 - 2;
/* 138 */         if (frameNumber >= this.keyFrames.length) frameNumber = this.keyFrames.length - 2 - frameNumber - this.keyFrames.length; 
/*     */         break;
/*     */       case LOOP_RANDOM:
/* 141 */         lastFrameNumber = (int)(this.lastStateTime / this.frameDuration);
/* 142 */         if (lastFrameNumber != frameNumber) {
/* 143 */           frameNumber = MathUtils.random(this.keyFrames.length - 1); break;
/*     */         } 
/* 145 */         frameNumber = this.lastFrameNumber;
/*     */         break;
/*     */       
/*     */       case REVERSED:
/* 149 */         frameNumber = Math.max(this.keyFrames.length - frameNumber - 1, 0);
/*     */         break;
/*     */       case LOOP_REVERSED:
/* 152 */         frameNumber %= this.keyFrames.length;
/* 153 */         frameNumber = this.keyFrames.length - frameNumber - 1;
/*     */         break;
/*     */     } 
/*     */     
/* 157 */     this.lastFrameNumber = frameNumber;
/* 158 */     this.lastStateTime = stateTime;
/*     */     
/* 160 */     return frameNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T[] getKeyFrames() {
/* 166 */     return this.keyFrames;
/*     */   }
/*     */   
/*     */   protected void setKeyFrames(T... keyFrames) {
/* 170 */     this.keyFrames = keyFrames;
/* 171 */     this.animationDuration = keyFrames.length * this.frameDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayMode getPlayMode() {
/* 176 */     return this.playMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlayMode(PlayMode playMode) {
/* 183 */     this.playMode = playMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnimationFinished(float stateTime) {
/* 190 */     int frameNumber = (int)(stateTime / this.frameDuration);
/* 191 */     return (this.keyFrames.length - 1 < frameNumber);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrameDuration(float frameDuration) {
/* 197 */     this.frameDuration = frameDuration;
/* 198 */     this.animationDuration = this.keyFrames.length * frameDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFrameDuration() {
/* 203 */     return this.frameDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAnimationDuration() {
/* 208 */     return this.animationDuration;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\Animation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */