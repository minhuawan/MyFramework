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
/*    */ public class FloatAction
/*    */   extends TemporalAction
/*    */ {
/*    */   private float start;
/*    */   private float end;
/*    */   private float value;
/*    */   
/*    */   public FloatAction() {
/* 27 */     this.start = 0.0F;
/* 28 */     this.end = 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public FloatAction(float start, float end) {
/* 33 */     this.start = start;
/* 34 */     this.end = end;
/*    */   }
/*    */   
/*    */   protected void begin() {
/* 38 */     this.value = this.start;
/*    */   }
/*    */   
/*    */   protected void update(float percent) {
/* 42 */     this.value = this.start + (this.end - this.start) * percent;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getValue() {
/* 47 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(float value) {
/* 52 */     this.value = value;
/*    */   }
/*    */   
/*    */   public float getStart() {
/* 56 */     return this.start;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setStart(float start) {
/* 61 */     this.start = start;
/*    */   }
/*    */   
/*    */   public float getEnd() {
/* 65 */     return this.end;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEnd(float end) {
/* 70 */     this.end = end;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\FloatAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */