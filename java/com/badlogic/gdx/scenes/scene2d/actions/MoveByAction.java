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
/*    */ public class MoveByAction
/*    */   extends RelativeTemporalAction
/*    */ {
/*    */   private float amountX;
/*    */   private float amountY;
/*    */   
/*    */   protected void updateRelative(float percentDelta) {
/* 25 */     this.target.moveBy(this.amountX * percentDelta, this.amountY * percentDelta);
/*    */   }
/*    */   
/*    */   public void setAmount(float x, float y) {
/* 29 */     this.amountX = x;
/* 30 */     this.amountY = y;
/*    */   }
/*    */   
/*    */   public float getAmountX() {
/* 34 */     return this.amountX;
/*    */   }
/*    */   
/*    */   public void setAmountX(float x) {
/* 38 */     this.amountX = x;
/*    */   }
/*    */   
/*    */   public float getAmountY() {
/* 42 */     return this.amountY;
/*    */   }
/*    */   
/*    */   public void setAmountY(float y) {
/* 46 */     this.amountY = y;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\MoveByAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */