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
/*    */ public class Sort
/*    */ {
/*    */   private static Sort instance;
/*    */   private TimSort timSort;
/*    */   private ComparableTimSort comparableTimSort;
/*    */   
/*    */   public <T> void sort(Array<T> a) {
/* 31 */     if (this.comparableTimSort == null) this.comparableTimSort = new ComparableTimSort(); 
/* 32 */     this.comparableTimSort.doSort((Object[])a.items, 0, a.size);
/*    */   }
/*    */   
/*    */   public <T> void sort(T[] a) {
/* 36 */     if (this.comparableTimSort == null) this.comparableTimSort = new ComparableTimSort(); 
/* 37 */     this.comparableTimSort.doSort((Object[])a, 0, a.length);
/*    */   }
/*    */   
/*    */   public <T> void sort(T[] a, int fromIndex, int toIndex) {
/* 41 */     if (this.comparableTimSort == null) this.comparableTimSort = new ComparableTimSort(); 
/* 42 */     this.comparableTimSort.doSort((Object[])a, fromIndex, toIndex);
/*    */   }
/*    */   
/*    */   public <T> void sort(Array<T> a, Comparator<? super T> c) {
/* 46 */     if (this.timSort == null) this.timSort = new TimSort(); 
/* 47 */     this.timSort.doSort((Object[])a.items, c, 0, a.size);
/*    */   }
/*    */   
/*    */   public <T> void sort(T[] a, Comparator<? super T> c) {
/* 51 */     if (this.timSort == null) this.timSort = new TimSort(); 
/* 52 */     this.timSort.doSort(a, (Comparator)c, 0, a.length);
/*    */   }
/*    */   
/*    */   public <T> void sort(T[] a, Comparator<? super T> c, int fromIndex, int toIndex) {
/* 56 */     if (this.timSort == null) this.timSort = new TimSort(); 
/* 57 */     this.timSort.doSort(a, (Comparator)c, fromIndex, toIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Sort instance() {
/* 62 */     if (instance == null) instance = new Sort(); 
/* 63 */     return instance;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Sort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */