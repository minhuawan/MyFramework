/*     */ package com.badlogic.gdx.graphics.g3d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.ModelInstance;
/*     */ import com.badlogic.gdx.graphics.g3d.model.Animation;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.Pool;
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
/*     */ public class AnimationController
/*     */   extends BaseAnimationController
/*     */ {
/*     */   public static class AnimationDesc
/*     */   {
/*     */     public AnimationController.AnimationListener listener;
/*     */     public Animation animation;
/*     */     public float speed;
/*     */     public float time;
/*     */     public float offset;
/*     */     public float duration;
/*     */     public int loopCount;
/*     */     
/*     */     protected float update(float delta) {
/*  76 */       if (this.loopCount != 0 && this.animation != null) {
/*     */         int loops;
/*  78 */         float diff = this.speed * delta;
/*  79 */         if (!MathUtils.isZero(this.duration)) {
/*  80 */           this.time += diff;
/*  81 */           loops = (int)Math.abs(this.time / this.duration);
/*  82 */           if (this.time < 0.0F) {
/*  83 */             loops++;
/*  84 */             while (this.time < 0.0F)
/*  85 */               this.time += this.duration; 
/*     */           } 
/*  87 */           this.time = Math.abs(this.time % this.duration);
/*     */         } else {
/*  89 */           loops = 1;
/*  90 */         }  for (int i = 0; i < loops; i++) {
/*  91 */           if (this.loopCount > 0) this.loopCount--; 
/*  92 */           if (this.loopCount != 0 && this.listener != null) this.listener.onLoop(this); 
/*  93 */           if (this.loopCount == 0) {
/*  94 */             float result = (loops - 1 - i) * this.duration + ((diff < 0.0F) ? (this.duration - this.time) : this.time);
/*  95 */             this.time = (diff < 0.0F) ? 0.0F : this.duration;
/*  96 */             if (this.listener != null) this.listener.onEnd(this); 
/*  97 */             return result;
/*     */           } 
/*     */         } 
/* 100 */         return 0.0F;
/*     */       } 
/* 102 */       return delta;
/*     */     }
/*     */   }
/*     */   
/* 106 */   protected final Pool<AnimationDesc> animationPool = new Pool<AnimationDesc>()
/*     */     {
/*     */       protected AnimationController.AnimationDesc newObject() {
/* 109 */         return new AnimationController.AnimationDesc();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public AnimationDesc current;
/*     */   
/*     */   public AnimationDesc queued;
/*     */   
/*     */   public float queuedTransitionTime;
/*     */   
/*     */   public AnimationDesc previous;
/*     */   public float transitionCurrentTime;
/*     */   public float transitionTargetTime;
/*     */   public boolean inAction;
/*     */   public boolean paused;
/*     */   public boolean allowSameAnimation;
/*     */   
/*     */   public static interface AnimationListener
/*     */   {
/*     */     void onEnd(AnimationController.AnimationDesc param1AnimationDesc);
/*     */     
/*     */     void onLoop(AnimationController.AnimationDesc param1AnimationDesc);
/*     */   }
/*     */   
/*     */   private boolean justChangedAnimation = false;
/*     */   
/*     */   public AnimationController(ModelInstance target) {
/* 137 */     super(target);
/*     */   }
/*     */ 
/*     */   
/*     */   private AnimationDesc obtain(Animation anim, float offset, float duration, int loopCount, float speed, AnimationListener listener) {
/* 142 */     if (anim == null) return null; 
/* 143 */     AnimationDesc result = (AnimationDesc)this.animationPool.obtain();
/* 144 */     result.animation = anim;
/* 145 */     result.listener = listener;
/* 146 */     result.loopCount = loopCount;
/* 147 */     result.speed = speed;
/* 148 */     result.offset = offset;
/* 149 */     result.duration = (duration < 0.0F) ? (anim.duration - offset) : duration;
/* 150 */     result.time = (speed < 0.0F) ? result.duration : 0.0F;
/* 151 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private AnimationDesc obtain(String id, float offset, float duration, int loopCount, float speed, AnimationListener listener) {
/* 156 */     if (id == null) return null; 
/* 157 */     Animation anim = this.target.getAnimation(id);
/* 158 */     if (anim == null) throw new GdxRuntimeException("Unknown animation: " + id); 
/* 159 */     return obtain(anim, offset, duration, loopCount, speed, listener);
/*     */   }
/*     */   
/*     */   private AnimationDesc obtain(AnimationDesc anim) {
/* 163 */     return obtain(anim.animation, anim.offset, anim.duration, anim.loopCount, anim.speed, anim.listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(float delta) {
/* 169 */     if (this.paused)
/* 170 */       return;  if (this.previous != null && (this.transitionCurrentTime += delta) >= this.transitionTargetTime) {
/* 171 */       removeAnimation(this.previous.animation);
/* 172 */       this.justChangedAnimation = true;
/* 173 */       this.animationPool.free(this.previous);
/* 174 */       this.previous = null;
/*     */     } 
/* 176 */     if (this.justChangedAnimation) {
/* 177 */       this.target.calculateTransforms();
/* 178 */       this.justChangedAnimation = false;
/*     */     } 
/* 180 */     if (this.current == null || this.current.loopCount == 0 || this.current.animation == null)
/* 181 */       return;  float remain = this.current.update(delta);
/* 182 */     if (remain != 0.0F && this.queued != null) {
/* 183 */       this.inAction = false;
/* 184 */       animate(this.queued, this.queuedTransitionTime);
/* 185 */       this.queued = null;
/* 186 */       update(remain);
/*     */       return;
/*     */     } 
/* 189 */     if (this.previous != null) {
/* 190 */       applyAnimations(this.previous.animation, this.previous.offset + this.previous.time, this.current.animation, this.current.offset + this.current.time, this.transitionCurrentTime / this.transitionTargetTime);
/*     */     } else {
/*     */       
/* 193 */       applyAnimation(this.current.animation, this.current.offset + this.current.time);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc setAnimation(String id) {
/* 201 */     return setAnimation(id, 1, 1.0F, (AnimationListener)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc setAnimation(String id, int loopCount) {
/* 211 */     return setAnimation(id, loopCount, 1.0F, (AnimationListener)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc setAnimation(String id, AnimationListener listener) {
/* 220 */     return setAnimation(id, 1, 1.0F, listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc setAnimation(String id, int loopCount, AnimationListener listener) {
/* 231 */     return setAnimation(id, loopCount, 1.0F, listener);
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
/*     */   
/*     */   public AnimationDesc setAnimation(String id, int loopCount, float speed, AnimationListener listener) {
/* 245 */     return setAnimation(id, 0.0F, -1.0F, loopCount, speed, listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc setAnimation(String id, float offset, float duration, int loopCount, float speed, AnimationListener listener) {
/* 262 */     return setAnimation(obtain(id, offset, duration, loopCount, speed, listener));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnimationDesc setAnimation(Animation anim, float offset, float duration, int loopCount, float speed, AnimationListener listener) {
/* 268 */     return setAnimation(obtain(anim, offset, duration, loopCount, speed, listener));
/*     */   }
/*     */ 
/*     */   
/*     */   protected AnimationDesc setAnimation(AnimationDesc anim) {
/* 273 */     if (this.current == null) {
/* 274 */       this.current = anim;
/*     */     } else {
/* 276 */       if (!this.allowSameAnimation && anim != null && this.current.animation == anim.animation) {
/* 277 */         anim.time = this.current.time;
/*     */       } else {
/* 279 */         removeAnimation(this.current.animation);
/* 280 */       }  this.animationPool.free(this.current);
/* 281 */       this.current = anim;
/*     */     } 
/* 283 */     this.justChangedAnimation = true;
/* 284 */     return anim;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc animate(String id, float transitionTime) {
/* 293 */     return animate(id, 1, 1.0F, (AnimationListener)null, transitionTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc animate(String id, AnimationListener listener, float transitionTime) {
/* 303 */     return animate(id, 1, 1.0F, listener, transitionTime);
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
/*     */   public AnimationDesc animate(String id, int loopCount, AnimationListener listener, float transitionTime) {
/* 315 */     return animate(id, loopCount, 1.0F, listener, transitionTime);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc animate(String id, int loopCount, float speed, AnimationListener listener, float transitionTime) {
/* 331 */     return animate(id, 0.0F, -1.0F, loopCount, speed, listener, transitionTime);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc animate(String id, float offset, float duration, int loopCount, float speed, AnimationListener listener, float transitionTime) {
/* 349 */     return animate(obtain(id, offset, duration, loopCount, speed, listener), transitionTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnimationDesc animate(Animation anim, float offset, float duration, int loopCount, float speed, AnimationListener listener, float transitionTime) {
/* 355 */     return animate(obtain(anim, offset, duration, loopCount, speed, listener), transitionTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AnimationDesc animate(AnimationDesc anim, float transitionTime) {
/* 360 */     if (this.current == null) {
/* 361 */       this.current = anim;
/* 362 */     } else if (this.inAction) {
/* 363 */       queue(anim, transitionTime);
/* 364 */     } else if (!this.allowSameAnimation && anim != null && this.current.animation == anim.animation) {
/* 365 */       anim.time = this.current.time;
/* 366 */       this.animationPool.free(this.current);
/* 367 */       this.current = anim;
/*     */     } else {
/* 369 */       if (this.previous != null) {
/* 370 */         removeAnimation(this.previous.animation);
/* 371 */         this.animationPool.free(this.previous);
/*     */       } 
/* 373 */       this.previous = this.current;
/* 374 */       this.current = anim;
/* 375 */       this.transitionCurrentTime = 0.0F;
/* 376 */       this.transitionTargetTime = transitionTime;
/*     */     } 
/* 378 */     return anim;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc queue(String id, int loopCount, float speed, AnimationListener listener, float transitionTime) {
/* 394 */     return queue(id, 0.0F, -1.0F, loopCount, speed, listener, transitionTime);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc queue(String id, float offset, float duration, int loopCount, float speed, AnimationListener listener, float transitionTime) {
/* 413 */     return queue(obtain(id, offset, duration, loopCount, speed, listener), transitionTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnimationDesc queue(Animation anim, float offset, float duration, int loopCount, float speed, AnimationListener listener, float transitionTime) {
/* 419 */     return queue(obtain(anim, offset, duration, loopCount, speed, listener), transitionTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AnimationDesc queue(AnimationDesc anim, float transitionTime) {
/* 424 */     if (this.current == null || this.current.loopCount == 0) {
/* 425 */       animate(anim, transitionTime);
/*     */     } else {
/* 427 */       if (this.queued != null) this.animationPool.free(this.queued); 
/* 428 */       this.queued = anim;
/* 429 */       this.queuedTransitionTime = transitionTime;
/* 430 */       if (this.current.loopCount < 0) this.current.loopCount = 1; 
/*     */     } 
/* 432 */     return anim;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc action(String id, int loopCount, float speed, AnimationListener listener, float transitionTime) {
/* 448 */     return action(id, 0.0F, -1.0F, loopCount, speed, listener, transitionTime);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationDesc action(String id, float offset, float duration, int loopCount, float speed, AnimationListener listener, float transitionTime) {
/* 466 */     return action(obtain(id, offset, duration, loopCount, speed, listener), transitionTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnimationDesc action(Animation anim, float offset, float duration, int loopCount, float speed, AnimationListener listener, float transitionTime) {
/* 472 */     return action(obtain(anim, offset, duration, loopCount, speed, listener), transitionTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected AnimationDesc action(AnimationDesc anim, float transitionTime) {
/* 477 */     if (anim.loopCount < 0) throw new GdxRuntimeException("An action cannot be continuous"); 
/* 478 */     if (this.current == null || this.current.loopCount == 0) {
/* 479 */       animate(anim, transitionTime);
/*     */     } else {
/* 481 */       AnimationDesc toQueue = this.inAction ? null : obtain(this.current);
/* 482 */       this.inAction = false;
/* 483 */       animate(anim, transitionTime);
/* 484 */       this.inAction = true;
/* 485 */       if (toQueue != null) queue(toQueue, transitionTime); 
/*     */     } 
/* 487 */     return anim;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\AnimationController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */