/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SnapshotArray<T>
/*     */   extends Array<T>
/*     */ {
/*     */   private T[] snapshot;
/*     */   private T[] recycled;
/*     */   private int snapshots;
/*     */   
/*     */   public SnapshotArray() {}
/*     */   
/*     */   public SnapshotArray(Array<? extends T> array) {
/*  49 */     super(array);
/*     */   }
/*     */   
/*     */   public SnapshotArray(boolean ordered, int capacity, Class arrayType) {
/*  53 */     super(ordered, capacity, arrayType);
/*     */   }
/*     */   
/*     */   public SnapshotArray(boolean ordered, int capacity) {
/*  57 */     super(ordered, capacity);
/*     */   }
/*     */   
/*     */   public SnapshotArray(boolean ordered, T[] array, int startIndex, int count) {
/*  61 */     super(ordered, array, startIndex, count);
/*     */   }
/*     */   
/*     */   public SnapshotArray(Class arrayType) {
/*  65 */     super(arrayType);
/*     */   }
/*     */   
/*     */   public SnapshotArray(int capacity) {
/*  69 */     super(capacity);
/*     */   }
/*     */   
/*     */   public SnapshotArray(T[] array) {
/*  73 */     super(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public T[] begin() {
/*  78 */     modified();
/*  79 */     this.snapshot = this.items;
/*  80 */     this.snapshots++;
/*  81 */     return this.items;
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/*  86 */     this.snapshots = Math.max(0, this.snapshots - 1);
/*  87 */     if (this.snapshot == null)
/*  88 */       return;  if (this.snapshot != this.items && this.snapshots == 0) {
/*     */       
/*  90 */       this.recycled = this.snapshot;
/*  91 */       for (int i = 0, n = this.recycled.length; i < n; i++)
/*  92 */         this.recycled[i] = null; 
/*     */     } 
/*  94 */     this.snapshot = null;
/*     */   }
/*     */   
/*     */   private void modified() {
/*  98 */     if (this.snapshot == null || this.snapshot != this.items)
/*     */       return; 
/* 100 */     if (this.recycled != null && this.recycled.length >= this.size) {
/* 101 */       System.arraycopy(this.items, 0, this.recycled, 0, this.size);
/* 102 */       this.items = this.recycled;
/* 103 */       this.recycled = null;
/*     */     } else {
/* 105 */       resize(this.items.length);
/*     */     } 
/*     */   }
/*     */   public void set(int index, T value) {
/* 109 */     modified();
/* 110 */     super.set(index, value);
/*     */   }
/*     */   
/*     */   public void insert(int index, T value) {
/* 114 */     modified();
/* 115 */     super.insert(index, value);
/*     */   }
/*     */   
/*     */   public void swap(int first, int second) {
/* 119 */     modified();
/* 120 */     super.swap(first, second);
/*     */   }
/*     */   
/*     */   public boolean removeValue(T value, boolean identity) {
/* 124 */     modified();
/* 125 */     return super.removeValue(value, identity);
/*     */   }
/*     */   
/*     */   public T removeIndex(int index) {
/* 129 */     modified();
/* 130 */     return super.removeIndex(index);
/*     */   }
/*     */   
/*     */   public void removeRange(int start, int end) {
/* 134 */     modified();
/* 135 */     super.removeRange(start, end);
/*     */   }
/*     */   
/*     */   public boolean removeAll(Array<? extends T> array, boolean identity) {
/* 139 */     modified();
/* 140 */     return super.removeAll(array, identity);
/*     */   }
/*     */   
/*     */   public T pop() {
/* 144 */     modified();
/* 145 */     return super.pop();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 149 */     modified();
/* 150 */     super.clear();
/*     */   }
/*     */   
/*     */   public void sort() {
/* 154 */     modified();
/* 155 */     super.sort();
/*     */   }
/*     */   
/*     */   public void sort(Comparator<? super T> comparator) {
/* 159 */     modified();
/* 160 */     super.sort(comparator);
/*     */   }
/*     */   
/*     */   public void reverse() {
/* 164 */     modified();
/* 165 */     super.reverse();
/*     */   }
/*     */   
/*     */   public void shuffle() {
/* 169 */     modified();
/* 170 */     super.shuffle();
/*     */   }
/*     */   
/*     */   public void truncate(int newSize) {
/* 174 */     modified();
/* 175 */     super.truncate(newSize);
/*     */   }
/*     */   
/*     */   public T[] setSize(int newSize) {
/* 179 */     modified();
/* 180 */     return super.setSize(newSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> SnapshotArray<T> with(T... array) {
/* 185 */     return new SnapshotArray<T>(array);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\SnapshotArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */