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
/*     */ public class Event
/*     */   implements Pool.Poolable
/*     */ {
/*     */   private Stage stage;
/*     */   private Actor targetActor;
/*     */   private Actor listenerActor;
/*     */   private boolean capture;
/*     */   private boolean bubbles = true;
/*     */   private boolean handled;
/*     */   private boolean stopped;
/*     */   private boolean cancelled;
/*     */   
/*     */   public void handle() {
/*  48 */     this.handled = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {
/*  55 */     this.cancelled = true;
/*  56 */     this.stopped = true;
/*  57 */     this.handled = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  63 */     this.stopped = true;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  67 */     this.stage = null;
/*  68 */     this.targetActor = null;
/*  69 */     this.listenerActor = null;
/*  70 */     this.capture = false;
/*  71 */     this.bubbles = true;
/*  72 */     this.handled = false;
/*  73 */     this.stopped = false;
/*  74 */     this.cancelled = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Actor getTarget() {
/*  79 */     return this.targetActor;
/*     */   }
/*     */   
/*     */   public void setTarget(Actor targetActor) {
/*  83 */     this.targetActor = targetActor;
/*     */   }
/*     */ 
/*     */   
/*     */   public Actor getListenerActor() {
/*  88 */     return this.listenerActor;
/*     */   }
/*     */   
/*     */   public void setListenerActor(Actor listenerActor) {
/*  92 */     this.listenerActor = listenerActor;
/*     */   }
/*     */   
/*     */   public boolean getBubbles() {
/*  96 */     return this.bubbles;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBubbles(boolean bubbles) {
/* 102 */     this.bubbles = bubbles;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHandled() {
/* 107 */     return this.handled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStopped() {
/* 112 */     return this.stopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/* 117 */     return this.cancelled;
/*     */   }
/*     */   
/*     */   public void setCapture(boolean capture) {
/* 121 */     this.capture = capture;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCapture() {
/* 127 */     return this.capture;
/*     */   }
/*     */   
/*     */   public void setStage(Stage stage) {
/* 131 */     this.stage = stage;
/*     */   }
/*     */ 
/*     */   
/*     */   public Stage getStage() {
/* 136 */     return this.stage;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\Event.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */