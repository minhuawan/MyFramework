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
/*    */ public class LongByReference
/*    */   extends ByReference
/*    */ {
/*    */   public LongByReference() {
/* 28 */     this(0L);
/*    */   }
/*    */   
/*    */   public LongByReference(long value) {
/* 32 */     super(8);
/* 33 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(long value) {
/* 37 */     getPointer().setLong(0L, value);
/*    */   }
/*    */   
/*    */   public long getValue() {
/* 41 */     return getPointer().getLong(0L);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\ptr\LongByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */