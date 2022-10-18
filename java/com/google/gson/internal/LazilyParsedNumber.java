/*    */ package com.google.gson.internal;
/*    */ 
/*    */ import java.io.ObjectStreamException;
/*    */ import java.math.BigDecimal;
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
/*    */ public final class LazilyParsedNumber
/*    */   extends Number
/*    */ {
/*    */   private final String value;
/*    */   
/*    */   public LazilyParsedNumber(String value) {
/* 31 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public int intValue() {
/*    */     try {
/* 37 */       return Integer.parseInt(this.value);
/* 38 */     } catch (NumberFormatException e) {
/*    */       try {
/* 40 */         return (int)Long.parseLong(this.value);
/* 41 */       } catch (NumberFormatException nfe) {
/* 42 */         return (new BigDecimal(this.value)).intValue();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public long longValue() {
/*    */     try {
/* 50 */       return Long.parseLong(this.value);
/* 51 */     } catch (NumberFormatException e) {
/* 52 */       return (new BigDecimal(this.value)).longValue();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public float floatValue() {
/* 58 */     return Float.parseFloat(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public double doubleValue() {
/* 63 */     return Double.parseDouble(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Object writeReplace() throws ObjectStreamException {
/* 77 */     return new BigDecimal(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 82 */     return this.value.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 87 */     if (this == obj) {
/* 88 */       return true;
/*    */     }
/* 90 */     if (obj instanceof LazilyParsedNumber) {
/* 91 */       LazilyParsedNumber other = (LazilyParsedNumber)obj;
/* 92 */       return (this.value == other.value || this.value.equals(other.value));
/*    */     } 
/* 94 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\internal\LazilyParsedNumber.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */