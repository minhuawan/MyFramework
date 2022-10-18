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
/*    */ public class SizeByAction
/*    */   extends RelativeTemporalAction
/*    */ {
/*    */   private float amountWidth;
/*    */   private float amountHeight;
/*    */   
/*    */   protected void updateRelative(float percentDelta) {
/* 25 */     this.target.sizeBy(this.amountWidth * percentDelta, this.amountHeight * percentDelta);
/*    */   }
/*    */   
/*    */   public void setAmount(float width, float height) {
/* 29 */     this.amountWidth = width;
/* 30 */     this.amountHeight = height;
/*    */   }
/*    */   
/*    */   public float getAmountWidth() {
/* 34 */     return this.amountWidth;
/*    */   }
/*    */   
/*    */   public void setAmountWidth(float width) {
/* 38 */     this.amountWidth = width;
/*    */   }
/*    */   
/*    */   public float getAmountHeight() {
/* 42 */     return this.amountHeight;
/*    */   }
/*    */   
/*    */   public void setAmountHeight(float height) {
/* 46 */     this.amountHeight = height;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\SizeByAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */