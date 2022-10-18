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
/*    */ public class ScaleToAction
/*    */   extends TemporalAction
/*    */ {
/*    */   private float startX;
/*    */   private float startY;
/*    */   private float endX;
/*    */   private float endY;
/*    */   
/*    */   protected void begin() {
/* 26 */     this.startX = this.target.getScaleX();
/* 27 */     this.startY = this.target.getScaleY();
/*    */   }
/*    */   
/*    */   protected void update(float percent) {
/* 31 */     this.target.setScale(this.startX + (this.endX - this.startX) * percent, this.startY + (this.endY - this.startY) * percent);
/*    */   }
/*    */   
/*    */   public void setScale(float x, float y) {
/* 35 */     this.endX = x;
/* 36 */     this.endY = y;
/*    */   }
/*    */   
/*    */   public void setScale(float scale) {
/* 40 */     this.endX = scale;
/* 41 */     this.endY = scale;
/*    */   }
/*    */   
/*    */   public float getX() {
/* 45 */     return this.endX;
/*    */   }
/*    */   
/*    */   public void setX(float x) {
/* 49 */     this.endX = x;
/*    */   }
/*    */   
/*    */   public float getY() {
/* 53 */     return this.endY;
/*    */   }
/*    */   
/*    */   public void setY(float y) {
/* 57 */     this.endY = y;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\ScaleToAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */