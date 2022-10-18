/*    */ package com.sun.jna.ptr;
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
/*    */ 
/*    */ 
/*    */ public class DoubleByReference
/*    */   extends ByReference
/*    */ {
/*    */   public DoubleByReference() {
/* 28 */     this(0.0D);
/*    */   }
/*    */   
/*    */   public DoubleByReference(double value) {
/* 32 */     super(8);
/* 33 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(double value) {
/* 37 */     getPointer().setDouble(0L, value);
/*    */   }
/*    */   
/*    */   public double getValue() {
/* 41 */     return getPointer().getDouble(0L);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\ptr\DoubleByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */