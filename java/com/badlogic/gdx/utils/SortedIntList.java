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
/*     */ 
/*     */ public class SortedIntList<E>
/*     */   implements Iterable<SortedIntList.Node<E>>
/*     */ {
/*  23 */   private NodePool<E> nodePool = new NodePool<E>();
/*     */   private Iterator iterator;
/*  25 */   int size = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Node<E> first;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E insert(int index, E value) {
/*  39 */     if (this.first != null) {
/*  40 */       Node<E> c = this.first;
/*     */       
/*  42 */       while (c.n != null && c.n.index <= index) {
/*  43 */         c = c.n;
/*     */       }
/*     */       
/*  46 */       if (index > c.index) {
/*  47 */         c.n = this.nodePool.obtain(c, c.n, value, index);
/*  48 */         if (c.n.n != null) {
/*  49 */           c.n.n.p = c.n;
/*     */         }
/*  51 */         this.size++;
/*     */       
/*     */       }
/*  54 */       else if (index < c.index) {
/*  55 */         Node<E> newFirst = this.nodePool.obtain(null, this.first, value, index);
/*  56 */         this.first.p = newFirst;
/*  57 */         this.first = newFirst;
/*  58 */         this.size++;
/*     */       }
/*     */       else {
/*     */         
/*  62 */         c.value = value;
/*     */       } 
/*     */     } else {
/*  65 */       this.first = this.nodePool.obtain(null, null, value, index);
/*  66 */       this.size++;
/*     */     } 
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  76 */     E match = null;
/*  77 */     if (this.first != null) {
/*  78 */       Node<E> c = this.first;
/*  79 */       while (c.n != null && c.index < index) {
/*  80 */         c = c.n;
/*     */       }
/*  82 */       if (c.index == index) {
/*  83 */         match = c.value;
/*     */       }
/*     */     } 
/*  86 */     return match;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  91 */     for (; this.first != null; this.first = this.first.n) {
/*  92 */       this.nodePool.free(this.first);
/*     */     }
/*  94 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  99 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public java.util.Iterator<Node<E>> iterator() {
/* 107 */     if (this.iterator == null) {
/* 108 */       this.iterator = new Iterator();
/*     */     }
/* 110 */     return this.iterator.reset();
/*     */   }
/*     */   
/*     */   class Iterator
/*     */     implements java.util.Iterator<Node<E>> {
/*     */     private SortedIntList.Node<E> position;
/*     */     private SortedIntList.Node<E> previousPosition;
/*     */     
/*     */     public boolean hasNext() {
/* 119 */       return (this.position != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedIntList.Node<E> next() {
/* 124 */       this.previousPosition = this.position;
/* 125 */       this.position = this.position.n;
/* 126 */       return this.previousPosition;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 132 */       if (this.previousPosition != null) {
/*     */         
/* 134 */         if (this.previousPosition == SortedIntList.this.first) {
/* 135 */           SortedIntList.this.first = this.position;
/*     */         }
/*     */         else {
/*     */           
/* 139 */           this.previousPosition.p.n = this.position;
/* 140 */           if (this.position != null) {
/* 141 */             this.position.p = this.previousPosition.p;
/*     */           }
/*     */         } 
/* 144 */         SortedIntList.this.size--;
/*     */       } 
/*     */     }
/*     */     
/*     */     public Iterator reset() {
/* 149 */       this.position = SortedIntList.this.first;
/* 150 */       this.previousPosition = null;
/* 151 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Node<E>
/*     */   {
/*     */     protected Node<E> p;
/*     */     
/*     */     protected Node<E> n;
/*     */     public E value;
/*     */     public int index;
/*     */   }
/*     */   
/*     */   static class NodePool<E>
/*     */     extends Pool<Node<E>>
/*     */   {
/*     */     protected SortedIntList.Node<E> newObject() {
/* 169 */       return new SortedIntList.Node<E>();
/*     */     }
/*     */     
/*     */     public SortedIntList.Node<E> obtain(SortedIntList.Node<E> p, SortedIntList.Node<E> n, E value, int index) {
/* 173 */       SortedIntList.Node<E> newNode = (SortedIntList.Node<E>)obtain();
/* 174 */       newNode.p = p;
/* 175 */       newNode.n = n;
/* 176 */       newNode.value = value;
/* 177 */       newNode.index = index;
/* 178 */       return newNode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\SortedIntList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */