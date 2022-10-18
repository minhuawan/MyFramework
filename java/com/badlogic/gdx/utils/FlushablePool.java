/*    */ package com.badlogic.gdx.utils;
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
/*    */ public abstract class FlushablePool<T>
/*    */   extends Pool<T>
/*    */ {
/* 23 */   protected Array<T> obtained = new Array<T>();
/*    */ 
/*    */   
/*    */   public FlushablePool() {}
/*    */ 
/*    */   
/*    */   public FlushablePool(int initialCapacity) {
/* 30 */     super(initialCapacity);
/*    */   }
/*    */   
/*    */   public FlushablePool(int initialCapacity, int max) {
/* 34 */     super(initialCapacity, max);
/*    */   }
/*    */ 
/*    */   
/*    */   public T obtain() {
/* 39 */     T result = super.obtain();
/* 40 */     this.obtained.add(result);
/* 41 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() {
/* 46 */     super.freeAll(this.obtained);
/* 47 */     this.obtained.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public void free(T object) {
/* 52 */     this.obtained.removeValue(object, true);
/* 53 */     super.free(object);
/*    */   }
/*    */ 
/*    */   
/*    */   public void freeAll(Array<T> objects) {
/* 58 */     this.obtained.removeAll(objects, true);
/* 59 */     super.freeAll(objects);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\FlushablePool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */