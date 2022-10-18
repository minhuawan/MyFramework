/*     */ package com.badlogic.gdx.utils;
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
/*     */ public class PooledLinkedList<T>
/*     */ {
/*     */   private Item<T> head;
/*     */   private Item<T> tail;
/*     */   private Item<T> iter;
/*     */   private Item<T> curr;
/*     */   
/*     */   static final class Item<T>
/*     */   {
/*     */     public T payload;
/*     */     public Item<T> next;
/*     */     public Item<T> prev;
/*     */   }
/*  32 */   private int size = 0;
/*     */   
/*     */   private final Pool<Item<T>> pool;
/*     */   
/*     */   public PooledLinkedList(int maxPoolSize) {
/*  37 */     this.pool = (Pool)new Pool<Item<Item<T>>>(16, maxPoolSize)
/*     */       {
/*     */         protected PooledLinkedList.Item<T> newObject() {
/*  40 */           return new PooledLinkedList.Item<T>();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(T object) {
/*  47 */     Item<T> item = this.pool.obtain();
/*  48 */     item.payload = object;
/*  49 */     item.next = null;
/*  50 */     item.prev = null;
/*     */     
/*  52 */     if (this.head == null) {
/*  53 */       this.head = item;
/*  54 */       this.tail = item;
/*  55 */       this.size++;
/*     */       
/*     */       return;
/*     */     } 
/*  59 */     item.prev = this.tail;
/*  60 */     this.tail.next = item;
/*  61 */     this.tail = item;
/*  62 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  67 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public void iter() {
/*  72 */     this.iter = this.head;
/*     */   }
/*     */ 
/*     */   
/*     */   public void iterReverse() {
/*  77 */     this.iter = this.tail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T next() {
/*  84 */     if (this.iter == null) return null;
/*     */     
/*  86 */     T payload = this.iter.payload;
/*  87 */     this.curr = this.iter;
/*  88 */     this.iter = this.iter.next;
/*  89 */     return payload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T previous() {
/*  96 */     if (this.iter == null) return null;
/*     */     
/*  98 */     T payload = this.iter.payload;
/*  99 */     this.curr = this.iter;
/* 100 */     this.iter = this.iter.prev;
/* 101 */     return payload;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 106 */     if (this.curr == null)
/*     */       return; 
/* 108 */     this.size--;
/* 109 */     this.pool.free(this.curr);
/*     */     
/* 111 */     Item<T> c = this.curr;
/* 112 */     Item<T> n = this.curr.next;
/* 113 */     Item<T> p = this.curr.prev;
/* 114 */     this.curr = null;
/*     */     
/* 116 */     if (this.size == 0) {
/* 117 */       this.head = null;
/* 118 */       this.tail = null;
/*     */       
/*     */       return;
/*     */     } 
/* 122 */     if (c == this.head) {
/* 123 */       n.prev = null;
/* 124 */       this.head = n;
/*     */       
/*     */       return;
/*     */     } 
/* 128 */     if (c == this.tail) {
/* 129 */       p.next = null;
/* 130 */       this.tail = p;
/*     */       
/*     */       return;
/*     */     } 
/* 134 */     p.next = n;
/* 135 */     n.prev = p;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 139 */     iter();
/* 140 */     T v = null;
/* 141 */     while ((v = next()) != null)
/* 142 */       remove(); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\PooledLinkedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */