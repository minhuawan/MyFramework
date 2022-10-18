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
/*    */ public class ByteByReference
/*    */   extends ByReference
/*    */ {
/*    */   public ByteByReference() {
/* 29 */     this((byte)0);
/*    */   }
/*    */   
/*    */   public ByteByReference(byte value) {
/* 33 */     super(1);
/* 34 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(byte value) {
/* 38 */     getPointer().setByte(0L, value);
/*    */   }
/*    */   
/*    */   public byte getValue() {
/* 42 */     return getPointer().getByte(0L);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\ptr\ByteByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */