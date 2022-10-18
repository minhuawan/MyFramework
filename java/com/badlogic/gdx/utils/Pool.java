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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Pool<T>
/*    */ {
/*    */   public final int max;
/*    */   public int peak;
/*    */   private final Array<T> freeObjects;
/*    */   
/*    */   public Pool() {
/* 32 */     this(16, 2147483647);
/*    */   }
/*    */ 
/*    */   
/*    */   public Pool(int initialCapacity) {
/* 37 */     this(initialCapacity, 2147483647);
/*    */   }
/*    */ 
/*    */   
/*    */   public Pool(int initialCapacity, int max) {
/* 42 */     this.freeObjects = new Array<T>(false, initialCapacity);
/* 43 */     this.max = max;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract T newObject();
/*    */ 
/*    */   
/*    */   public T obtain() {
/* 51 */     return (this.freeObjects.size == 0) ? newObject() : this.freeObjects.pop();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void free(T object) {
/* 57 */     if (object == null) throw new IllegalArgumentException("object cannot be null."); 
/* 58 */     if (this.freeObjects.size < this.max) {
/* 59 */       this.freeObjects.add(object);
/* 60 */       this.peak = Math.max(this.peak, this.freeObjects.size);
/*    */     } 
/* 62 */     reset(object);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void reset(T object) {
/* 68 */     if (object instanceof Poolable) ((Poolable)object).reset();
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public void freeAll(Array<T> objects) {
/* 74 */     if (objects == null) throw new IllegalArgumentException("objects cannot be null."); 
/* 75 */     Array<T> freeObjects = this.freeObjects;
/* 76 */     int max = this.max;
/* 77 */     for (int i = 0; i < objects.size; i++) {
/* 78 */       T object = objects.get(i);
/* 79 */       if (object != null) {
/* 80 */         if (freeObjects.size < max) freeObjects.add(object); 
/* 81 */         reset(object);
/*    */       } 
/* 83 */     }  this.peak = Math.max(this.peak, freeObjects.size);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 88 */     this.freeObjects.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFree() {
/* 93 */     return this.freeObjects.size;
/*    */   }
/*    */   
/*    */   public static interface Poolable {
/*    */     void reset();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Pool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */