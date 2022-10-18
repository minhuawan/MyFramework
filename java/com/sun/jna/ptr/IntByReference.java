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
/*    */ 
/*    */ public class IntByReference
/*    */   extends ByReference
/*    */ {
/*    */   public IntByReference() {
/* 29 */     this(0);
/*    */   }
/*    */   
/*    */   public IntByReference(int value) {
/* 33 */     super(4);
/* 34 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(int value) {
/* 38 */     getPointer().setInt(0L, value);
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 42 */     return getPointer().getInt(0L);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\ptr\IntByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */