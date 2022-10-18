/*     */ package com.badlogic.gdx.scenes.scene2d.actions;
/*     */ 
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.scenes.scene2d.Action;
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
/*     */ public abstract class TemporalAction
/*     */   extends Action
/*     */ {
/*     */   private float duration;
/*     */   private float time;
/*     */   private Interpolation interpolation;
/*     */   private boolean reverse;
/*     */   private boolean began;
/*     */   private boolean complete;
/*     */   
/*     */   public TemporalAction() {}
/*     */   
/*     */   public TemporalAction(float duration) {
/*  34 */     this.duration = duration;
/*     */   }
/*     */   
/*     */   public TemporalAction(float duration, Interpolation interpolation) {
/*  38 */     this.duration = duration;
/*  39 */     this.interpolation = interpolation;
/*     */   }
/*     */   
/*     */   public boolean act(float delta) {
/*  43 */     if (this.complete) return true; 
/*  44 */     Pool pool = getPool();
/*  45 */     setPool(null); try {
/*     */       float percent;
/*  47 */       if (!this.began) {
/*  48 */         begin();
/*  49 */         this.began = true;
/*     */       } 
/*  51 */       this.time += delta;
/*  52 */       this.complete = (this.time >= this.duration);
/*     */       
/*  54 */       if (this.complete) {
/*  55 */         percent = 1.0F;
/*     */       } else {
/*  57 */         percent = this.time / this.duration;
/*  58 */         if (this.interpolation != null) percent = this.interpolation.apply(percent); 
/*     */       } 
/*  60 */       update(this.reverse ? (1.0F - percent) : percent);
/*  61 */       if (this.complete) end(); 
/*  62 */       return this.complete;
/*     */     } finally {
/*  64 */       setPool(pool);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void begin() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void end() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void update(float paramFloat);
/*     */ 
/*     */ 
/*     */   
/*     */   public void finish() {
/*  84 */     this.time = this.duration;
/*     */   }
/*     */   
/*     */   public void restart() {
/*  88 */     this.time = 0.0F;
/*  89 */     this.began = false;
/*  90 */     this.complete = false;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  94 */     super.reset();
/*  95 */     this.reverse = false;
/*  96 */     this.interpolation = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTime() {
/* 101 */     return this.time;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTime(float time) {
/* 106 */     this.time = time;
/*     */   }
/*     */   
/*     */   public float getDuration() {
/* 110 */     return this.duration;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDuration(float duration) {
/* 115 */     this.duration = duration;
/*     */   }
/*     */   
/*     */   public Interpolation getInterpolation() {
/* 119 */     return this.interpolation;
/*     */   }
/*     */   
/*     */   public void setInterpolation(Interpolation interpolation) {
/* 123 */     this.interpolation = interpolation;
/*     */   }
/*     */   
/*     */   public boolean isReverse() {
/* 127 */     return this.reverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReverse(boolean reverse) {
/* 132 */     this.reverse = reverse;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\TemporalAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */