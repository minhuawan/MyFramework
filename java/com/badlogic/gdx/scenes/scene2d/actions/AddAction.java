/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Action;
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
/*    */ public class AddAction
/*    */   extends Action
/*    */ {
/*    */   private Action action;
/*    */   
/*    */   public boolean act(float delta) {
/* 28 */     this.target.addAction(this.action);
/* 29 */     return true;
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 33 */     return this.action;
/*    */   }
/*    */   
/*    */   public void setAction(Action action) {
/* 37 */     this.action = action;
/*    */   }
/*    */   
/*    */   public void restart() {
/* 41 */     if (this.action != null) this.action.restart(); 
/*    */   }
/*    */   
/*    */   public void reset() {
/* 45 */     super.reset();
/* 46 */     this.action = null;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\AddAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */