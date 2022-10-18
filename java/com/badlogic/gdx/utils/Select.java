/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ public class Select
/*    */ {
/*    */   private static Select instance;
/*    */   private QuickSelect quickSelect;
/*    */   
/*    */   public static Select instance() {
/* 39 */     if (instance == null) instance = new Select(); 
/* 40 */     return instance;
/*    */   }
/*    */   
/*    */   public <T> T select(T[] items, Comparator<T> comp, int kthLowest, int size) {
/* 44 */     int idx = selectIndex(items, comp, kthLowest, size);
/* 45 */     return items[idx];
/*    */   }
/*    */   public <T> int selectIndex(T[] items, Comparator<T> comp, int kthLowest, int size) {
/*    */     int idx;
/* 49 */     if (size < 1)
/* 50 */       throw new GdxRuntimeException("cannot select from empty array (size < 1)"); 
/* 51 */     if (kthLowest > size) {
/* 52 */       throw new GdxRuntimeException("Kth rank is larger than size. k: " + kthLowest + ", size: " + size);
/*    */     }
/*    */ 
/*    */     
/* 56 */     if (kthLowest == 1) {
/*    */       
/* 58 */       idx = fastMin(items, comp, size);
/* 59 */     } else if (kthLowest == size) {
/*    */       
/* 61 */       idx = fastMax(items, comp, size);
/*    */     } else {
/*    */       
/* 64 */       if (this.quickSelect == null) this.quickSelect = new QuickSelect(); 
/* 65 */       idx = this.quickSelect.select(items, comp, kthLowest, size);
/*    */     } 
/* 67 */     return idx;
/*    */   }
/*    */ 
/*    */   
/*    */   private <T> int fastMin(T[] items, Comparator<T> comp, int size) {
/* 72 */     int lowestIdx = 0;
/* 73 */     for (int i = 1; i < size; i++) {
/* 74 */       int comparison = comp.compare(items[i], items[lowestIdx]);
/* 75 */       if (comparison < 0) {
/* 76 */         lowestIdx = i;
/*    */       }
/*    */     } 
/* 79 */     return lowestIdx;
/*    */   }
/*    */ 
/*    */   
/*    */   private <T> int fastMax(T[] items, Comparator<T> comp, int size) {
/* 84 */     int highestIdx = 0;
/* 85 */     for (int i = 1; i < size; i++) {
/* 86 */       int comparison = comp.compare(items[i], items[highestIdx]);
/* 87 */       if (comparison > 0) {
/* 88 */         highestIdx = i;
/*    */       }
/*    */     } 
/* 91 */     return highestIdx;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Select.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */