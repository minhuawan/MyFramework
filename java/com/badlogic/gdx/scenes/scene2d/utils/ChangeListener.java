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
/*    */ public abstract class ChangeListener
/*    */   implements EventListener
/*    */ {
/*    */   public boolean handle(Event event) {
/* 27 */     if (!(event instanceof ChangeEvent)) return false; 
/* 28 */     changed((ChangeEvent)event, event.getTarget());
/* 29 */     return false;
/*    */   }
/*    */   
/*    */   public abstract void changed(ChangeEvent paramChangeEvent, Actor paramActor);
/*    */   
/*    */   public static class ChangeEvent extends Event {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\ChangeListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */