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
/*     */ public class DelayedRemovalArray<T>
/*     */   extends Array<T>
/*     */ {
/*     */   private int iterating;
/*  30 */   private IntArray remove = new IntArray(0);
/*     */ 
/*     */   
/*     */   public DelayedRemovalArray() {}
/*     */ 
/*     */   
/*     */   public DelayedRemovalArray(Array<? extends T> array) {
/*  37 */     super(array);
/*     */   }
/*     */   
/*     */   public DelayedRemovalArray(boolean ordered, int capacity, Class arrayType) {
/*  41 */     super(ordered, capacity, arrayType);
/*     */   }
/*     */   
/*     */   public DelayedRemovalArray(boolean ordered, int capacity) {
/*  45 */     super(ordered, capacity);
/*     */   }
/*     */   
/*     */   public DelayedRemovalArray(boolean ordered, T[] array, int startIndex, int count) {
/*  49 */     super(ordered, array, startIndex, count);
/*     */   }
/*     */   
/*     */   public DelayedRemovalArray(Class arrayType) {
/*  53 */     super(arrayType);
/*     */   }
/*     */   
/*     */   public DelayedRemovalArray(int capacity) {
/*  57 */     super(capacity);
/*     */   }
/*     */   
/*     */   public DelayedRemovalArray(T[] array) {
/*  61 */     super(array);
/*     */   }
/*     */   
/*     */   public void begin() {
/*  65 */     this.iterating++;
/*     */   }
/*     */   
/*     */   public void end() {
/*  69 */     if (this.iterating == 0) throw new IllegalStateException("begin must be called before end."); 
/*  70 */     this.iterating--;
/*  71 */     if (this.iterating == 0)
/*  72 */       for (int i = 0, n = this.remove.size; i < n; i++) {
/*  73 */         removeIndex(this.remove.pop());
/*     */       } 
/*     */   }
/*     */   
/*     */   private void remove(int index) {
/*  78 */     for (int i = 0, n = this.remove.size; i < n; i++) {
/*  79 */       int removeIndex = this.remove.get(i);
/*  80 */       if (index == removeIndex)
/*  81 */         return;  if (index < removeIndex) {
/*  82 */         this.remove.insert(i, index);
/*     */         return;
/*     */       } 
/*     */     } 
/*  86 */     this.remove.add(index);
/*     */   }
/*     */   
/*     */   public boolean removeValue(T value, boolean identity) {
/*  90 */     if (this.iterating > 0) {
/*  91 */       int index = indexOf(value, identity);
/*  92 */       if (index == -1) return false; 
/*  93 */       remove(index);
/*  94 */       return true;
/*     */     } 
/*  96 */     return super.removeValue(value, identity);
/*     */   }
/*     */   
/*     */   public T removeIndex(int index) {
/* 100 */     if (this.iterating > 0) {
/* 101 */       remove(index);
/* 102 */       return get(index);
/*     */     } 
/* 104 */     return super.removeIndex(index);
/*     */   }
/*     */   
/*     */   public void removeRange(int start, int end) {
/* 108 */     if (this.iterating > 0) {
/* 109 */       for (int i = end; i >= start; i--)
/* 110 */         remove(i); 
/*     */     } else {
/* 112 */       super.removeRange(start, end);
/*     */     } 
/*     */   }
/*     */   public void set(int index, T value) {
/* 116 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 117 */     super.set(index, value);
/*     */   }
/*     */   
/*     */   public void insert(int index, T value) {
/* 121 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 122 */     super.insert(index, value);
/*     */   }
/*     */   
/*     */   public void swap(int first, int second) {
/* 126 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 127 */     super.swap(first, second);
/*     */   }
/*     */   
/*     */   public T pop() {
/* 131 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 132 */     return super.pop();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 136 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 137 */     super.clear();
/*     */   }
/*     */   
/*     */   public void sort() {
/* 141 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 142 */     super.sort();
/*     */   }
/*     */   
/*     */   public void sort(Comparator<? super T> comparator) {
/* 146 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 147 */     super.sort(comparator);
/*     */   }
/*     */   
/*     */   public void reverse() {
/* 151 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 152 */     super.reverse();
/*     */   }
/*     */   
/*     */   public void shuffle() {
/* 156 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 157 */     super.shuffle();
/*     */   }
/*     */   
/*     */   public void truncate(int newSize) {
/* 161 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 162 */     super.truncate(newSize);
/*     */   }
/*     */   
/*     */   public T[] setSize(int newSize) {
/* 166 */     if (this.iterating > 0) throw new IllegalStateException("Invalid between begin/end."); 
/* 167 */     return super.setSize(newSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> DelayedRemovalArray<T> with(T... array) {
/* 172 */     return new DelayedRemovalArray<T>(array);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\DelayedRemovalArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */