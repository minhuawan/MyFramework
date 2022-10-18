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
/*    */ public class RepeatAction
/*    */   extends DelegateAction
/*    */ {
/*    */   public static final int FOREVER = -1;
/*    */   private int repeatCount;
/*    */   private int executedCount;
/*    */   private boolean finished;
/*    */   
/*    */   protected boolean delegate(float delta) {
/* 28 */     if (this.executedCount == this.repeatCount) return true; 
/* 29 */     if (this.action.act(delta)) {
/* 30 */       if (this.finished) return true; 
/* 31 */       if (this.repeatCount > 0) this.executedCount++; 
/* 32 */       if (this.executedCount == this.repeatCount) return true; 
/* 33 */       if (this.action != null) this.action.restart(); 
/*    */     } 
/* 35 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void finish() {
/* 40 */     this.finished = true;
/*    */   }
/*    */   
/*    */   public void restart() {
/* 44 */     super.restart();
/* 45 */     this.executedCount = 0;
/* 46 */     this.finished = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCount(int count) {
/* 51 */     this.repeatCount = count;
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 55 */     return this.repeatCount;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\RepeatAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */