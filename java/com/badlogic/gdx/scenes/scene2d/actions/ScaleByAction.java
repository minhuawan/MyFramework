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
/*    */ public class ScaleByAction
/*    */   extends RelativeTemporalAction
/*    */ {
/*    */   private float amountX;
/*    */   private float amountY;
/*    */   
/*    */   protected void updateRelative(float percentDelta) {
/* 25 */     this.target.scaleBy(this.amountX * percentDelta, this.amountY * percentDelta);
/*    */   }
/*    */   
/*    */   public void setAmount(float x, float y) {
/* 29 */     this.amountX = x;
/* 30 */     this.amountY = y;
/*    */   }
/*    */   
/*    */   public void setAmount(float scale) {
/* 34 */     this.amountX = scale;
/* 35 */     this.amountY = scale;
/*    */   }
/*    */   
/*    */   public float getAmountX() {
/* 39 */     return this.amountX;
/*    */   }
/*    */   
/*    */   public void setAmountX(float x) {
/* 43 */     this.amountX = x;
/*    */   }
/*    */   
/*    */   public float getAmountY() {
/* 47 */     return this.amountY;
/*    */   }
/*    */   
/*    */   public void setAmountY(float y) {
/* 51 */     this.amountY = y;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\ScaleByAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */