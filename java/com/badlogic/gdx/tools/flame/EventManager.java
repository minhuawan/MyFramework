/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import com.badlogic.gdx.utils.ObjectMap;
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
/*    */ public class EventManager
/*    */ {
/*    */   private static EventManager mInstance;
/* 19 */   private ObjectMap<Integer, Array<Listener>> mListeners = new ObjectMap();
/*    */ 
/*    */ 
/*    */   
/*    */   public static EventManager get() {
/* 24 */     if (mInstance == null) mInstance = new EventManager(); 
/* 25 */     return mInstance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void attach(int aEventType, Listener aListener) {
/* 30 */     boolean isNew = false;
/* 31 */     Array<Listener> listeners = (Array<Listener>)this.mListeners.get(Integer.valueOf(aEventType));
/* 32 */     if (listeners == null) {
/*    */       
/* 34 */       listeners = new Array();
/* 35 */       this.mListeners.put(Integer.valueOf(aEventType), listeners);
/* 36 */       isNew = true;
/*    */     } 
/*    */     
/* 39 */     if (isNew || !listeners.contains(aListener, true))
/*    */     {
/* 41 */       listeners.add(aListener);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void detach(int aEventType, Listener aListener) {
/* 48 */     Array<Listener> listeners = (Array<Listener>)this.mListeners.get(Integer.valueOf(aEventType));
/* 49 */     if (listeners != null) {
/*    */       
/* 51 */       listeners.removeValue(aListener, true);
/* 52 */       if (listeners.size == 0) this.mListeners.remove(Integer.valueOf(aEventType));
/*    */     
/*    */     } 
/*    */   }
/*    */   
/*    */   public void fire(int aEventType, Object aEventData) {
/* 58 */     Array<Listener> listeners = (Array<Listener>)this.mListeners.get(Integer.valueOf(aEventType));
/* 59 */     if (listeners != null)
/* 60 */       for (Listener listener : listeners)
/*    */       {
/* 62 */         listener.handle(aEventType, aEventData);
/*    */       } 
/*    */   }
/*    */   
/*    */   public void clear() {
/* 67 */     this.mListeners.clear();
/*    */   }
/*    */   
/*    */   public static interface Listener {
/*    */     void handle(int param1Int, Object param1Object);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\EventManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */