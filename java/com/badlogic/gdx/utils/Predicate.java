/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.util.Iterator;
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
/*     */ public interface Predicate<T>
/*     */ {
/*     */   boolean evaluate(T paramT);
/*     */   
/*     */   public static class PredicateIterator<T>
/*     */     implements Iterator<T>
/*     */   {
/*     */     public Iterator<T> iterator;
/*     */     public Predicate<T> predicate;
/*     */     public boolean end = false;
/*     */     public boolean peeked = false;
/*  35 */     public T next = null;
/*     */     
/*     */     public PredicateIterator(Iterable<T> iterable, Predicate<T> predicate) {
/*  38 */       this(iterable.iterator(), predicate);
/*     */     }
/*     */     
/*     */     public PredicateIterator(Iterator<T> iterator, Predicate<T> predicate) {
/*  42 */       set(iterator, predicate);
/*     */     }
/*     */     
/*     */     public void set(Iterable<T> iterable, Predicate<T> predicate) {
/*  46 */       set(iterable.iterator(), predicate);
/*     */     }
/*     */     
/*     */     public void set(Iterator<T> iterator, Predicate<T> predicate) {
/*  50 */       this.iterator = iterator;
/*  51 */       this.predicate = predicate;
/*  52 */       this.end = this.peeked = false;
/*  53 */       this.next = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  58 */       if (this.end) return false; 
/*  59 */       if (this.next != null) return true; 
/*  60 */       this.peeked = true;
/*  61 */       while (this.iterator.hasNext()) {
/*  62 */         T n = this.iterator.next();
/*  63 */         if (this.predicate.evaluate(n)) {
/*  64 */           this.next = n;
/*  65 */           return true;
/*     */         } 
/*     */       } 
/*  68 */       this.end = true;
/*  69 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/*  74 */       if (this.next == null && !hasNext()) return null; 
/*  75 */       T result = this.next;
/*  76 */       this.next = null;
/*  77 */       this.peeked = false;
/*  78 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  83 */       if (this.peeked) throw new GdxRuntimeException("Cannot remove between a call to hasNext() and next()."); 
/*  84 */       this.iterator.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PredicateIterable<T> implements Iterable<T> {
/*     */     public Iterable<T> iterable;
/*     */     public Predicate<T> predicate;
/*  91 */     public Predicate.PredicateIterator<T> iterator = null;
/*     */     
/*     */     public PredicateIterable(Iterable<T> iterable, Predicate<T> predicate) {
/*  94 */       set(iterable, predicate);
/*     */     }
/*     */     
/*     */     public void set(Iterable<T> iterable, Predicate<T> predicate) {
/*  98 */       this.iterable = iterable;
/*  99 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 106 */       if (this.iterator == null) {
/* 107 */         this.iterator = new Predicate.PredicateIterator<T>(this.iterable.iterator(), this.predicate);
/*     */       } else {
/* 109 */         this.iterator.set(this.iterable.iterator(), this.predicate);
/* 110 */       }  return this.iterator;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Predicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */