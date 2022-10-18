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
/*    */ public class SizeToAction
/*    */   extends TemporalAction
/*    */ {
/*    */   private float startWidth;
/*    */   private float startHeight;
/*    */   private float endWidth;
/*    */   private float endHeight;
/*    */   
/*    */   protected void begin() {
/* 26 */     this.startWidth = this.target.getWidth();
/* 27 */     this.startHeight = this.target.getHeight();
/*    */   }
/*    */   
/*    */   protected void update(float percent) {
/* 31 */     this.target.setSize(this.startWidth + (this.endWidth - this.startWidth) * percent, this.startHeight + (this.endHeight - this.startHeight) * percent);
/*    */   }
/*    */   
/*    */   public void setSize(float width, float height) {
/* 35 */     this.endWidth = width;
/* 36 */     this.endHeight = height;
/*    */   }
/*    */   
/*    */   public float getWidth() {
/* 40 */     return this.endWidth;
/*    */   }
/*    */   
/*    */   public void setWidth(float width) {
/* 44 */     this.endWidth = width;
/*    */   }
/*    */   
/*    */   public float getHeight() {
/* 48 */     return this.endHeight;
/*    */   }
/*    */   
/*    */   public void setHeight(float height) {
/* 52 */     this.endHeight = height;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\SizeToAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */