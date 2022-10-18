/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Action;
/*    */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*    */ import com.badlogic.gdx.scenes.scene2d.Event;
/*    */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*    */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*    */ 
/*    */ 
/*    */ public abstract class EventAction<T extends Event>
/*    */   extends Action
/*    */ {
/*    */   final Class<? extends T> eventClass;
/*    */   boolean result;
/*    */   boolean active;
/*    */   
/* 17 */   private final EventListener listener = new EventListener() {
/*    */       public boolean handle(Event event) {
/* 19 */         if (!EventAction.this.active || !ClassReflection.isInstance(EventAction.this.eventClass, event)) return false; 
/* 20 */         EventAction.this.result = EventAction.this.handle(event);
/* 21 */         return EventAction.this.result;
/*    */       }
/*    */     };
/*    */   
/*    */   public EventAction(Class<? extends T> eventClass) {
/* 26 */     this.eventClass = eventClass;
/*    */   }
/*    */   
/*    */   public void restart() {
/* 30 */     this.result = false;
/* 31 */     this.active = false;
/*    */   }
/*    */   
/*    */   public void setTarget(Actor newTarget) {
/* 35 */     if (this.target != null) this.target.removeListener(this.listener); 
/* 36 */     super.setTarget(newTarget);
/* 37 */     if (newTarget != null) newTarget.addListener(this.listener);
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract boolean handle(T paramT);
/*    */   
/*    */   public boolean act(float delta) {
/* 45 */     this.active = true;
/* 46 */     return this.result;
/*    */   }
/*    */   
/*    */   public boolean isActive() {
/* 50 */     return this.active;
/*    */   }
/*    */   
/*    */   public void setActive(boolean active) {
/* 54 */     this.active = active;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\EventAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */