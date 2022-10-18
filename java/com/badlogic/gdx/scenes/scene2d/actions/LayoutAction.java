/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Action;
/*    */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*    */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*    */ public class LayoutAction
/*    */   extends Action
/*    */ {
/*    */   private boolean enabled;
/*    */   
/*    */   public void setTarget(Actor actor) {
/* 31 */     if (actor != null && !(actor instanceof Layout)) throw new GdxRuntimeException("Actor must implement layout: " + actor); 
/* 32 */     super.setTarget(actor);
/*    */   }
/*    */   
/*    */   public boolean act(float delta) {
/* 36 */     ((Layout)this.target).setLayoutEnabled(this.enabled);
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 41 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public void setLayoutEnabled(boolean enabled) {
/* 45 */     this.enabled = enabled;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\LayoutAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */