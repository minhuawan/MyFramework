/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*    */ public class AtomicQueue<T>
/*    */ {
/* 32 */   private final AtomicInteger writeIndex = new AtomicInteger();
/* 33 */   private final AtomicInteger readIndex = new AtomicInteger();
/*    */   private final AtomicReferenceArray<T> queue;
/*    */   
/*    */   public AtomicQueue(int capacity) {
/* 37 */     this.queue = new AtomicReferenceArray<T>(capacity);
/*    */   }
/*    */   
/*    */   private int next(int idx) {
/* 41 */     return (idx + 1) % this.queue.length();
/*    */   }
/*    */   
/*    */   public boolean put(T value) {
/* 45 */     int write = this.writeIndex.get();
/* 46 */     int read = this.readIndex.get();
/* 47 */     int next = next(write);
/* 48 */     if (next == read) return false; 
/* 49 */     this.queue.set(write, value);
/* 50 */     this.writeIndex.set(next);
/* 51 */     return true;
/*    */   }
/*    */   
/*    */   public T poll() {
/* 55 */     int read = this.readIndex.get();
/* 56 */     int write = this.writeIndex.get();
/* 57 */     if (read == write) return null; 
/* 58 */     T value = this.queue.get(read);
/* 59 */     this.readIndex.set(next(read));
/* 60 */     return value;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\AtomicQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */