/*     */ package com.sun.jna;
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
/*     */ public abstract class PointerType
/*     */   implements NativeMapped
/*     */ {
/*     */   private Pointer pointer;
/*     */   
/*     */   protected PointerType() {
/*  36 */     this.pointer = Pointer.NULL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PointerType(Pointer p) {
/*  43 */     this.pointer = p;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> nativeType() {
/*  49 */     return Pointer.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object toNative() {
/*  55 */     return getPointer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pointer getPointer() {
/*  62 */     return this.pointer;
/*     */   }
/*     */   
/*     */   public void setPointer(Pointer p) {
/*  66 */     this.pointer = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object fromNative(Object nativeValue, FromNativeContext context) {
/*  78 */     if (nativeValue == null) {
/*  79 */       return null;
/*     */     }
/*     */     try {
/*  82 */       PointerType pt = (PointerType)getClass().newInstance();
/*  83 */       pt.pointer = (Pointer)nativeValue;
/*  84 */       return pt;
/*     */     }
/*  86 */     catch (InstantiationException e) {
/*  87 */       throw new IllegalArgumentException("Can't instantiate " + getClass());
/*     */     }
/*  89 */     catch (IllegalAccessException e) {
/*  90 */       throw new IllegalArgumentException("Not allowed to instantiate " + getClass());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return (this.pointer != null) ? this.pointer.hashCode() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 107 */     if (o == this) {
/* 108 */       return true;
/*     */     }
/* 110 */     if (o instanceof PointerType) {
/* 111 */       Pointer p = ((PointerType)o).getPointer();
/* 112 */       if (this.pointer == null) {
/* 113 */         return (p == null);
/*     */       }
/* 115 */       return this.pointer.equals(p);
/*     */     } 
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return (this.pointer == null) ? "NULL" : (this.pointer.toString() + " (" + super.toString() + ")");
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\PointerType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */