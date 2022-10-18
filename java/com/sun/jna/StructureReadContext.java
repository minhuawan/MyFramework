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
/*    */ public class StructureReadContext
/*    */   extends FromNativeContext
/*    */ {
/*    */   private Structure structure;
/*    */   private Field field;
/*    */   
/*    */   StructureReadContext(Structure struct, Field field) {
/* 36 */     super(field.getType());
/* 37 */     this.structure = struct;
/* 38 */     this.field = field;
/*    */   }
/*    */   public Structure getStructure() {
/* 41 */     return this.structure;
/*    */   } public Field getField() {
/* 43 */     return this.field;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\StructureReadContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */