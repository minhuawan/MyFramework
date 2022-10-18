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
/*    */ public class TimeScaleAction
/*    */   extends DelegateAction
/*    */ {
/*    */   private float scale;
/*    */   
/*    */   protected boolean delegate(float delta) {
/* 25 */     if (this.action == null) return true; 
/* 26 */     return this.action.act(delta * this.scale);
/*    */   }
/*    */   
/*    */   public float getScale() {
/* 30 */     return this.scale;
/*    */   }
/*    */   
/*    */   public void setScale(float scale) {
/* 34 */     this.scale = scale;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\TimeScaleAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */