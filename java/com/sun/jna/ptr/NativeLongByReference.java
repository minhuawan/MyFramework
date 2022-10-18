/*    */ package com.sun.jna.ptr;
/*    */ 
/*    */ import com.sun.jna.NativeLong;
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
/*    */ public class NativeLongByReference
/*    */   extends ByReference
/*    */ {
/*    */   public NativeLongByReference() {
/* 30 */     this(new NativeLong(0L));
/*    */   }
/*    */   
/*    */   public NativeLongByReference(NativeLong value) {
/* 34 */     super(NativeLong.SIZE);
/* 35 */     setValue(value);
/*    */   }
/*    */   
/*    */   public void setValue(NativeLong value) {
/* 39 */     getPointer().setNativeLong(0L, value);
/*    */   }
/*    */   
/*    */   public NativeLong getValue() {
/* 43 */     return getPointer().getNativeLong(0L);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\ptr\NativeLongByReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */