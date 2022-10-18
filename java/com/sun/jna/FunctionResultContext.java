/*    */ package com.sun.jna;
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
/*    */ public class FunctionResultContext
/*    */   extends FromNativeContext
/*    */ {
/*    */   private Function function;
/*    */   private Object[] args;
/*    */   
/*    */   FunctionResultContext(Class<?> resultClass, Function function, Object[] args) {
/* 32 */     super(resultClass);
/* 33 */     this.function = function;
/* 34 */     this.args = args;
/*    */   }
/*    */ 
/*    */   
/*    */   public Function getFunction() {
/* 39 */     return this.function;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object[] getArguments() {
/* 44 */     return this.args;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\FunctionResultContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */