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
/*    */ public class FloatByReference
/*    */   extends ByReference
/*    */ {
/*    */   public FloatByReference() {
/* 28 */     this(0.0F);
/*    */   }
/*    */   
/*    */   public FloatByReference(float value) {
/* 32 */     super(4);
/* 33 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(float value) {
/* 37 */     getPointer().setFloat(0L, value);
/*    */   }
/*    */   
/*    */   public float getValue() {
/* 41 */     return getPointer().getFloat(0L);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\ptr\FloatByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */