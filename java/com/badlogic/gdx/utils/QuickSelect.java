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
/*    */ public class QuickSelect<T>
/*    */ {
/*    */   private T[] array;
/*    */   private Comparator<? super T> comp;
/*    */   
/*    */   public int select(T[] items, Comparator<T> comp, int n, int size) {
/* 30 */     this.array = items;
/* 31 */     this.comp = comp;
/* 32 */     return recursiveSelect(0, size - 1, n);
/*    */   }
/*    */   
/*    */   private int partition(int left, int right, int pivot) {
/* 36 */     T pivotValue = this.array[pivot];
/* 37 */     swap(right, pivot);
/* 38 */     int storage = left;
/* 39 */     for (int i = left; i < right; i++) {
/* 40 */       if (this.comp.compare(this.array[i], pivotValue) < 0) {
/* 41 */         swap(storage, i);
/* 42 */         storage++;
/*    */       } 
/*    */     } 
/* 45 */     swap(right, storage);
/* 46 */     return storage;
/*    */   }
/*    */   private int recursiveSelect(int left, int right, int k) {
/*    */     int result;
/* 50 */     if (left == right) return left; 
/* 51 */     int pivotIndex = medianOfThreePivot(left, right);
/* 52 */     int pivotNewIndex = partition(left, right, pivotIndex);
/* 53 */     int pivotDist = pivotNewIndex - left + 1;
/*    */     
/* 55 */     if (pivotDist == k) {
/* 56 */       result = pivotNewIndex;
/* 57 */     } else if (k < pivotDist) {
/* 58 */       result = recursiveSelect(left, pivotNewIndex - 1, k);
/*    */     } else {
/* 60 */       result = recursiveSelect(pivotNewIndex + 1, right, k - pivotDist);
/*    */     } 
/* 62 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   private int medianOfThreePivot(int leftIdx, int rightIdx) {
/* 67 */     T left = this.array[leftIdx];
/* 68 */     int midIdx = (leftIdx + rightIdx) / 2;
/* 69 */     T mid = this.array[midIdx];
/* 70 */     T right = this.array[rightIdx];
/*    */ 
/*    */ 
/*    */     
/* 74 */     if (this.comp.compare(left, mid) > 0) {
/* 75 */       if (this.comp.compare(mid, right) > 0)
/* 76 */         return midIdx; 
/* 77 */       if (this.comp.compare(left, right) > 0) {
/* 78 */         return rightIdx;
/*    */       }
/* 80 */       return leftIdx;
/*    */     } 
/*    */     
/* 83 */     if (this.comp.compare(left, right) > 0)
/* 84 */       return leftIdx; 
/* 85 */     if (this.comp.compare(mid, right) > 0) {
/* 86 */       return rightIdx;
/*    */     }
/* 88 */     return midIdx;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void swap(int left, int right) {
/* 94 */     T tmp = this.array[left];
/* 95 */     this.array[left] = this.array[right];
/* 96 */     this.array[right] = tmp;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\QuickSelect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */