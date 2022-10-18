/*    */ package com.sun.jna.ptr;
/*    */ 
/*    */ import com.sun.jna.Pointer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PointerByReference
/*    */   extends ByReference
/*    */ {
/*    */   public PointerByReference() {
/* 35 */     this(null);
/*    */   }
/*    */   
/*    */   public PointerByReference(Pointer value) {
/* 39 */     super(Pointer.SIZE);
/* 40 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(Pointer value) {
/* 44 */     getPointer().setPointer(0L, value);
/*    */   }
/*    */   
/*    */   public Pointer getValue() {
/* 48 */     return getPointer().getPointer(0L);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\ptr\PointerByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */