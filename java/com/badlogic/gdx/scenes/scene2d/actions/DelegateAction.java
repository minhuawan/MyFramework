/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Action;
/*    */ import com.badlogic.gdx.scenes.scene2d.Actor;
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
/*    */ public abstract class DelegateAction
/*    */   extends Action
/*    */ {
/*    */   protected Action action;
/*    */   
/*    */   public void setAction(Action action) {
/* 30 */     this.action = action;
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 34 */     return this.action;
/*    */   }
/*    */   
/*    */   protected abstract boolean delegate(float paramFloat);
/*    */   
/*    */   public final boolean act(float delta) {
/* 40 */     Pool pool = getPool();
/* 41 */     setPool(null);
/*    */     try {
/* 43 */       return delegate(delta);
/*    */     } finally {
/* 45 */       setPool(pool);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void restart() {
/* 50 */     if (this.action != null) this.action.restart(); 
/*    */   }
/*    */   
/*    */   public void reset() {
/* 54 */     super.reset();
/* 55 */     this.action = null;
/*    */   }
/*    */   
/*    */   public void setActor(Actor actor) {
/* 59 */     if (this.action != null) this.action.setActor(actor); 
/* 60 */     super.setActor(actor);
/*    */   }
/*    */   
/*    */   public void setTarget(Actor target) {
/* 64 */     if (this.action != null) this.action.setTarget(target); 
/* 65 */     super.setTarget(target);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 69 */     return super.toString() + ((this.action == null) ? "" : ("(" + this.action + ")"));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\DelegateAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */