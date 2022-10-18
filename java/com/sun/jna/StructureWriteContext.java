/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.lang.reflect.Field;
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
/*    */ public class StructureWriteContext
/*    */   extends ToNativeContext
/*    */ {
/*    */   private Structure struct;
/*    */   private Field field;
/*    */   
/*    */   StructureWriteContext(Structure struct, Field field) {
/* 36 */     this.struct = struct;
/* 37 */     this.field = field;
/*    */   }
/*    */   public Structure getStructure() {
/* 40 */     return this.struct;
/*    */   }
/*    */   public Field getField() {
/* 43 */     return this.field;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\StructureWriteContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */