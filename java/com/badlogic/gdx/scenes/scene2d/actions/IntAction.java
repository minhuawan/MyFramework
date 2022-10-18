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
/*    */ public class IntAction
/*    */   extends TemporalAction
/*    */ {
/*    */   private int start;
/*    */   private int end;
/*    */   private int value;
/*    */   
/*    */   public IntAction() {
/* 27 */     this.start = 0;
/* 28 */     this.end = 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public IntAction(int start, int end) {
/* 33 */     this.start = start;
/* 34 */     this.end = end;
/*    */   }
/*    */   
/*    */   protected void begin() {
/* 38 */     this.value = this.start;
/*    */   }
/*    */   
/*    */   protected void update(float percent) {
/* 42 */     this.value = (int)(this.start + (this.end - this.start) * percent);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getValue() {
/* 47 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(int value) {
/* 52 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int getStart() {
/* 56 */     return this.start;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setStart(int start) {
/* 61 */     this.start = start;
/*    */   }
/*    */   
/*    */   public int getEnd() {
/* 65 */     return this.end;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEnd(int end) {
/* 70 */     this.end = end;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\IntAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */