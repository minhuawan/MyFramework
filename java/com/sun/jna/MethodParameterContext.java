/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class MethodParameterContext
/*    */   extends FunctionParameterContext
/*    */ {
/*    */   private Method method;
/*    */   
/*    */   MethodParameterContext(Function f, Object[] args, int index, Method m) {
/* 33 */     super(f, args, index);
/* 34 */     this.method = m;
/*    */   }
/*    */   public Method getMethod() {
/* 37 */     return this.method;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\MethodParameterContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */