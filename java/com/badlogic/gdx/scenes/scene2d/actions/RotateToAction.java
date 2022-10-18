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
/*    */ public class RotateToAction
/*    */   extends TemporalAction
/*    */ {
/*    */   private float start;
/*    */   private float end;
/*    */   
/*    */   protected void begin() {
/* 25 */     this.start = this.target.getRotation();
/*    */   }
/*    */   
/*    */   protected void update(float percent) {
/* 29 */     this.target.setRotation(this.start + (this.end - this.start) * percent);
/*    */   }
/*    */   
/*    */   public float getRotation() {
/* 33 */     return this.end;
/*    */   }
/*    */   
/*    */   public void setRotation(float rotation) {
/* 37 */     this.end = rotation;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\RotateToAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */