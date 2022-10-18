/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Action;
/*    */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ public class AfterAction
/*    */   extends DelegateAction
/*    */ {
/* 26 */   private Array<Action> waitForActions = new Array(false, 4);
/*    */   
/*    */   public void setTarget(Actor target) {
/* 29 */     if (target != null) this.waitForActions.addAll(target.getActions()); 
/* 30 */     super.setTarget(target);
/*    */   }
/*    */   
/*    */   public void restart() {
/* 34 */     super.restart();
/* 35 */     this.waitForActions.clear();
/*    */   }
/*    */   
/*    */   protected boolean delegate(float delta) {
/* 39 */     Array<Action> currentActions = this.target.getActions();
/* 40 */     if (currentActions.size == 1) this.waitForActions.clear(); 
/* 41 */     for (int i = this.waitForActions.size - 1; i >= 0; i--) {
/* 42 */       Action action = (Action)this.waitForActions.get(i);
/* 43 */       int index = currentActions.indexOf(action, true);
/* 44 */       if (index == -1) this.waitForActions.removeIndex(i); 
/*    */     } 
/* 46 */     if (this.waitForActions.size > 0) return false; 
/* 47 */     return this.action.act(delta);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\AfterAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */