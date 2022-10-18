/*     */ package com.badlogic.gdx.scenes.scene2d;
/*     */ 
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
/*     */ public abstract class Action
/*     */   implements Pool.Poolable
/*     */ {
/*     */   protected Actor actor;
/*     */   protected Actor target;
/*     */   private Pool pool;
/*     */   
/*     */   public abstract boolean act(float paramFloat);
/*     */   
/*     */   public void restart() {}
/*     */   
/*     */   public void setActor(Actor actor) {
/*  56 */     this.actor = actor;
/*  57 */     if (this.target == null) setTarget(actor); 
/*  58 */     if (actor == null && 
/*  59 */       this.pool != null) {
/*  60 */       this.pool.free(this);
/*  61 */       this.pool = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Actor getActor() {
/*  68 */     return this.actor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(Actor target) {
/*  74 */     this.target = target;
/*     */   }
/*     */ 
/*     */   
/*     */   public Actor getTarget() {
/*  79 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/*  89 */     this.actor = null;
/*  90 */     this.target = null;
/*  91 */     this.pool = null;
/*  92 */     restart();
/*     */   }
/*     */   
/*     */   public Pool getPool() {
/*  96 */     return this.pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPool(Pool pool) {
/* 103 */     this.pool = pool;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 107 */     String name = getClass().getName();
/* 108 */     int dotIndex = name.lastIndexOf('.');
/* 109 */     if (dotIndex != -1) name = name.substring(dotIndex + 1); 
/* 110 */     if (name.endsWith("Action")) name = name.substring(0, name.length() - 6); 
/* 111 */     return name;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\Action.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */