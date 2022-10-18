/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.nio.CharBuffer;
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
/*     */ class NativeString
/*     */   implements CharSequence, Comparable
/*     */ {
/*     */   static final String WIDE_STRING = "--WIDE-STRING--";
/*     */   private Pointer pointer;
/*     */   private String encoding;
/*     */   
/*     */   private class StringMemory
/*     */     extends Memory
/*     */   {
/*     */     public StringMemory(long size) {
/*  42 */       super(size);
/*     */     }
/*     */     public String toString() {
/*  45 */       return NativeString.this.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NativeString(String string) {
/*  53 */     this(string, Native.getDefaultStringEncoding());
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
/*     */   public NativeString(String string, boolean wide) {
/*  65 */     this(string, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NativeString(WString string) {
/*  72 */     this(string.toString(), "--WIDE-STRING--");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NativeString(String string, String encoding) {
/*  79 */     if (string == null) {
/*  80 */       throw new NullPointerException("String must not be null");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     this.encoding = encoding;
/*  86 */     if ("--WIDE-STRING--".equals(this.encoding)) {
/*  87 */       int len = (string.length() + 1) * Native.WCHAR_SIZE;
/*  88 */       this.pointer = new StringMemory(len);
/*  89 */       this.pointer.setWideString(0L, string);
/*     */     } else {
/*  91 */       byte[] data = Native.getBytes(string, encoding);
/*  92 */       this.pointer = new StringMemory((data.length + 1));
/*  93 */       this.pointer.write(0L, data, 0, data.length);
/*  94 */       this.pointer.setByte(data.length, (byte)0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 100 */     return toString().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 105 */     if (other instanceof CharSequence) {
/* 106 */       return (compareTo(other) == 0);
/*     */     }
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     boolean wide = "--WIDE-STRING--".equals(this.encoding);
/* 114 */     String s = wide ? "const wchar_t*" : "const char*";
/* 115 */     s = s + "(" + (wide ? this.pointer.getWideString(0L) : this.pointer.getString(0L, this.encoding)) + ")";
/* 116 */     return s;
/*     */   }
/*     */   
/*     */   public Pointer getPointer() {
/* 120 */     return this.pointer;
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 125 */     return toString().charAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 130 */     return toString().length();
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 135 */     return CharBuffer.wrap(toString()).subSequence(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Object other) {
/* 140 */     if (other == null) {
/* 141 */       return 1;
/*     */     }
/* 143 */     return toString().compareTo(other.toString());
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\NativeString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */