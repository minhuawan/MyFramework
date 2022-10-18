/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Action;
/*    */ import com.badlogic.gdx.scenes.scene2d.EventListener;
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
/*    */ public class RemoveListenerAction
/*    */   extends Action
/*    */ {
/*    */   private EventListener listener;
/*    */   private boolean capture;
/*    */   
/*    */   public boolean act(float delta) {
/* 30 */     if (this.capture) {
/* 31 */       this.target.removeCaptureListener(this.listener);
/*    */     } else {
/* 33 */       this.target.removeListener(this.listener);
/* 34 */     }  return true;
/*    */   }
/*    */   
/*    */   public EventListener getListener() {
/* 38 */     return this.listener;
/*    */   }
/*    */   
/*    */   public void setListener(EventListener listener) {
/* 42 */     this.listener = listener;
/*    */   }
/*    */   
/*    */   public boolean getCapture() {
/* 46 */     return this.capture;
/*    */   }
/*    */   
/*    */   public void setCapture(boolean capture) {
/* 50 */     this.capture = capture;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 54 */     super.reset();
/* 55 */     this.listener = null;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\RemoveListenerAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */