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
/*    */ public class ShortByReference
/*    */   extends ByReference
/*    */ {
/*    */   public ShortByReference() {
/* 29 */     this((short)0);
/*    */   }
/*    */   
/*    */   public ShortByReference(short value) {
/* 33 */     super(2);
/* 34 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(short value) {
/* 38 */     getPointer().setShort(0L, value);
/*    */   }
/*    */   
/*    */   public short getValue() {
/* 42 */     return getPointer().getShort(0L);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\ptr\ShortByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */