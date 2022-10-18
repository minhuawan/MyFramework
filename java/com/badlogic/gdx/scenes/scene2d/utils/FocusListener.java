/*    */ package com.badlogic.gdx.scenes.scene2d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*    */ import com.badlogic.gdx.scenes.scene2d.Event;
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
/*    */ public abstract class FocusListener
/*    */   implements EventListener
/*    */ {
/*    */   public boolean handle(Event event) {
/* 27 */     if (!(event instanceof FocusEvent)) return false; 
/* 28 */     FocusEvent focusEvent = (FocusEvent)event;
/* 29 */     switch (focusEvent.getType()) {
/*    */       case keyboard:
/* 31 */         keyboardFocusChanged(focusEvent, event.getTarget(), focusEvent.isFocused());
/*    */         break;
/*    */       case scroll:
/* 34 */         scrollFocusChanged(focusEvent, event.getTarget(), focusEvent.isFocused());
/*    */         break;
/*    */     } 
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {}
/*    */ 
/*    */   
/*    */   public void scrollFocusChanged(FocusEvent event, Actor actor, boolean focused) {}
/*    */ 
/*    */   
/*    */   public static class FocusEvent
/*    */     extends Event
/*    */   {
/*    */     private boolean focused;
/*    */     
/*    */     private Type type;
/*    */     private Actor relatedActor;
/*    */     
/*    */     public void reset() {
/* 56 */       super.reset();
/* 57 */       this.relatedActor = null;
/*    */     }
/*    */     
/*    */     public boolean isFocused() {
/* 61 */       return this.focused;
/*    */     }
/*    */     
/*    */     public void setFocused(boolean focused) {
/* 65 */       this.focused = focused;
/*    */     }
/*    */     
/*    */     public Type getType() {
/* 69 */       return this.type;
/*    */     }
/*    */     
/*    */     public void setType(Type focusType) {
/* 73 */       this.type = focusType;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public Actor getRelatedActor() {
/* 79 */       return this.relatedActor;
/*    */     }
/*    */ 
/*    */     
/*    */     public void setRelatedActor(Actor relatedActor) {
/* 84 */       this.relatedActor = relatedActor;
/*    */     }
/*    */     
/*    */     public enum Type
/*    */     {
/* 89 */       keyboard, scroll; } } public enum Type { keyboard, scroll; }
/*    */ 
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\FocusListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */