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
/*    */ public class MoveToAction
/*    */   extends TemporalAction
/*    */ {
/*    */   private float startX;
/*    */   private float startY;
/*    */   private float endX;
/*    */   private float endY;
/* 26 */   private int alignment = 12;
/*    */   
/*    */   protected void begin() {
/* 29 */     this.startX = this.target.getX(this.alignment);
/* 30 */     this.startY = this.target.getY(this.alignment);
/*    */   }
/*    */   
/*    */   protected void update(float percent) {
/* 34 */     this.target.setPosition(this.startX + (this.endX - this.startX) * percent, this.startY + (this.endY - this.startY) * percent, this.alignment);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 38 */     super.reset();
/* 39 */     this.alignment = 12;
/*    */   }
/*    */   
/*    */   public void setPosition(float x, float y) {
/* 43 */     this.endX = x;
/* 44 */     this.endY = y;
/*    */   }
/*    */   
/*    */   public void setPosition(float x, float y, int alignment) {
/* 48 */     this.endX = x;
/* 49 */     this.endY = y;
/* 50 */     this.alignment = alignment;
/*    */   }
/*    */   
/*    */   public float getX() {
/* 54 */     return this.endX;
/*    */   }
/*    */   
/*    */   public void setX(float x) {
/* 58 */     this.endX = x;
/*    */   }
/*    */   
/*    */   public float getY() {
/* 62 */     return this.endY;
/*    */   }
/*    */   
/*    */   public void setY(float y) {
/* 66 */     this.endY = y;
/*    */   }
/*    */   
/*    */   public int getAlignment() {
/* 70 */     return this.alignment;
/*    */   }
/*    */   
/*    */   public void setAlignment(int alignment) {
/* 74 */     this.alignment = alignment;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\MoveToAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */