/*    */ package com.badlogic.gdx.assets;
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
/*    */ public class RefCountedContainer
/*    */ {
/*    */   Object object;
/* 21 */   int refCount = 1;
/*    */   
/*    */   public RefCountedContainer(Object object) {
/* 24 */     if (object == null) throw new IllegalArgumentException("Object must not be null"); 
/* 25 */     this.object = object;
/*    */   }
/*    */   
/*    */   public void incRefCount() {
/* 29 */     this.refCount++;
/*    */   }
/*    */   
/*    */   public void decRefCount() {
/* 33 */     this.refCount--;
/*    */   }
/*    */   
/*    */   public int getRefCount() {
/* 37 */     return this.refCount;
/*    */   }
/*    */   
/*    */   public void setRefCount(int refCount) {
/* 41 */     this.refCount = refCount;
/*    */   }
/*    */   
/*    */   public <T> T getObject(Class<T> type) {
/* 45 */     return (T)this.object;
/*    */   }
/*    */   
/*    */   public void setObject(Object asset) {
/* 49 */     this.object = asset;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\RefCountedContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */