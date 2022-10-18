/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class OrderedSet<T>
/*     */   extends ObjectSet<T>
/*     */ {
/*     */   final Array<T> items;
/*     */   OrderedSetIterator iterator1;
/*     */   OrderedSetIterator iterator2;
/*     */   
/*     */   public OrderedSet() {
/*  30 */     this.items = new Array<T>();
/*     */   }
/*     */   
/*     */   public OrderedSet(int initialCapacity, float loadFactor) {
/*  34 */     super(initialCapacity, loadFactor);
/*  35 */     this.items = new Array<T>(this.capacity);
/*     */   }
/*     */   
/*     */   public OrderedSet(int initialCapacity) {
/*  39 */     super(initialCapacity);
/*  40 */     this.items = new Array<T>(this.capacity);
/*     */   }
/*     */   
/*     */   public OrderedSet(OrderedSet set) {
/*  44 */     super(set);
/*  45 */     this.items = new Array<T>(this.capacity);
/*  46 */     this.items.addAll(set.items);
/*     */   }
/*     */   
/*     */   public boolean add(T key) {
/*  50 */     if (!super.add(key)) return false; 
/*  51 */     this.items.add(key);
/*  52 */     return true;
/*     */   }
/*     */   
/*     */   public boolean add(T key, int index) {
/*  56 */     if (!super.add(key)) {
/*  57 */       this.items.removeValue(key, true);
/*  58 */       this.items.insert(index, key);
/*  59 */       return false;
/*     */     } 
/*  61 */     this.items.insert(index, key);
/*  62 */     return true;
/*     */   }
/*     */   
/*     */   public boolean remove(T key) {
/*  66 */     this.items.removeValue(key, false);
/*  67 */     return super.remove(key);
/*     */   }
/*     */   
/*     */   public void clear(int maximumCapacity) {
/*  71 */     this.items.clear();
/*  72 */     super.clear(maximumCapacity);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  76 */     this.items.clear();
/*  77 */     super.clear();
/*     */   }
/*     */   
/*     */   public Array<T> orderedItems() {
/*  81 */     return this.items;
/*     */   }
/*     */   
/*     */   public OrderedSetIterator<T> iterator() {
/*  85 */     if (this.iterator1 == null) {
/*  86 */       this.iterator1 = new OrderedSetIterator<T>(this);
/*  87 */       this.iterator2 = new OrderedSetIterator<T>(this);
/*     */     } 
/*  89 */     if (!this.iterator1.valid) {
/*  90 */       this.iterator1.reset();
/*  91 */       this.iterator1.valid = true;
/*  92 */       this.iterator2.valid = false;
/*  93 */       return this.iterator1;
/*     */     } 
/*  95 */     this.iterator2.reset();
/*  96 */     this.iterator2.valid = true;
/*  97 */     this.iterator1.valid = false;
/*  98 */     return this.iterator2;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 102 */     if (this.size == 0) return "{}"; 
/* 103 */     T[] items = this.items.items;
/* 104 */     StringBuilder buffer = new StringBuilder(32);
/* 105 */     buffer.append('{');
/* 106 */     buffer.append(items[0]);
/* 107 */     for (int i = 1; i < this.size; i++) {
/* 108 */       buffer.append(", ");
/* 109 */       buffer.append(items[i]);
/*     */     } 
/* 111 */     buffer.append('}');
/* 112 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public String toString(String separator) {
/* 116 */     return this.items.toString(separator);
/*     */   }
/*     */   
/*     */   public static class OrderedSetIterator<T> extends ObjectSet.ObjectSetIterator<T> {
/*     */     private Array<T> items;
/*     */     
/*     */     public OrderedSetIterator(OrderedSet<T> set) {
/* 123 */       super(set);
/* 124 */       this.items = set.items;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 128 */       this.nextIndex = 0;
/* 129 */       this.hasNext = (this.set.size > 0);
/*     */     }
/*     */     
/*     */     public T next() {
/* 133 */       if (!this.hasNext) throw new NoSuchElementException(); 
/* 134 */       if (!this.valid) throw new GdxRuntimeException("#iterator() cannot be used nested."); 
/* 135 */       T key = this.items.get(this.nextIndex);
/* 136 */       this.nextIndex++;
/* 137 */       this.hasNext = (this.nextIndex < this.set.size);
/* 138 */       return key;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 142 */       if (this.nextIndex < 0) throw new IllegalStateException("next must be called before remove."); 
/* 143 */       this.nextIndex--;
/* 144 */       this.set.remove(this.items.get(this.nextIndex));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\OrderedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */