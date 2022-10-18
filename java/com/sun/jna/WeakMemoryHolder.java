/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.IdentityHashMap;
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
/*    */ public class WeakMemoryHolder
/*    */ {
/* 40 */   ReferenceQueue<Object> referenceQueue = new ReferenceQueue();
/* 41 */   IdentityHashMap<Reference<Object>, Memory> backingMap = new IdentityHashMap<Reference<Object>, Memory>();
/*    */   
/*    */   public synchronized void put(Object o, Memory m) {
/* 44 */     clean();
/* 45 */     Reference<Object> reference = new WeakReference(o, this.referenceQueue);
/* 46 */     this.backingMap.put(reference, m);
/*    */   }
/*    */   
/*    */   public synchronized void clean() {
/* 50 */     for (Reference<?> ref = this.referenceQueue.poll(); ref != null; ref = this.referenceQueue.poll())
/* 51 */       this.backingMap.remove(ref); 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\WeakMemoryHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */