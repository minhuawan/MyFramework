/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Action;
/*    */ import com.badlogic.gdx.utils.Pool;
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
/*    */ 
/*    */ public class SequenceAction
/*    */   extends ParallelAction
/*    */ {
/*    */   private int index;
/*    */   
/*    */   public SequenceAction() {}
/*    */   
/*    */   public SequenceAction(Action action1) {
/* 31 */     addAction(action1);
/*    */   }
/*    */   
/*    */   public SequenceAction(Action action1, Action action2) {
/* 35 */     addAction(action1);
/* 36 */     addAction(action2);
/*    */   }
/*    */   
/*    */   public SequenceAction(Action action1, Action action2, Action action3) {
/* 40 */     addAction(action1);
/* 41 */     addAction(action2);
/* 42 */     addAction(action3);
/*    */   }
/*    */   
/*    */   public SequenceAction(Action action1, Action action2, Action action3, Action action4) {
/* 46 */     addAction(action1);
/* 47 */     addAction(action2);
/* 48 */     addAction(action3);
/* 49 */     addAction(action4);
/*    */   }
/*    */   
/*    */   public SequenceAction(Action action1, Action action2, Action action3, Action action4, Action action5) {
/* 53 */     addAction(action1);
/* 54 */     addAction(action2);
/* 55 */     addAction(action3);
/* 56 */     addAction(action4);
/* 57 */     addAction(action5);
/*    */   }
/*    */   
/*    */   public boolean act(float delta) {
/* 61 */     if (this.index >= this.actions.size) return true; 
/* 62 */     Pool pool = getPool();
/* 63 */     setPool(null);
/*    */     try {
/* 65 */       if (((Action)this.actions.get(this.index)).act(delta)) {
/* 66 */         if (this.actor == null) return true; 
/* 67 */         this.index++;
/* 68 */         if (this.index >= this.actions.size) return true; 
/*    */       } 
/* 70 */       return false;
/*    */     } finally {
/* 72 */       setPool(pool);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void restart() {
/* 77 */     super.restart();
/* 78 */     this.index = 0;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\SequenceAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */