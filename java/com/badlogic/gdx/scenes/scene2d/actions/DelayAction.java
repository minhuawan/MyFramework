/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DelayAction
/*    */   extends DelegateAction
/*    */ {
/*    */   private float duration;
/*    */   private float time;
/*    */   
/*    */   public DelayAction() {}
/*    */   
/*    */   public DelayAction(float duration) {
/* 28 */     this.duration = duration;
/*    */   }
/*    */   
/*    */   protected boolean delegate(float delta) {
/* 32 */     if (this.time < this.duration) {
/* 33 */       this.time += delta;
/* 34 */       if (this.time < this.duration) return false; 
/* 35 */       delta = this.time - this.duration;
/*    */     } 
/* 37 */     if (this.action == null) return true; 
/* 38 */     return this.action.act(delta);
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish() {
/* 43 */     this.time = this.duration;
/*    */   }
/*    */   
/*    */   public void restart() {
/* 47 */     super.restart();
/* 48 */     this.time = 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getTime() {
/* 53 */     return this.time;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTime(float time) {
/* 58 */     this.time = time;
/*    */   }
/*    */   
/*    */   public float getDuration() {
/* 62 */     return this.duration;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDuration(float duration) {
/* 67 */     this.duration = duration;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\DelayAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */