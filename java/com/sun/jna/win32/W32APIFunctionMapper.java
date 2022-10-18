/*    */ package com.sun.jna.win32;
/*    */ 
/*    */ import com.sun.jna.FunctionMapper;
/*    */ import com.sun.jna.NativeLibrary;
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
/*    */ 
/*    */ public class W32APIFunctionMapper
/*    */   implements FunctionMapper
/*    */ {
/* 33 */   public static final FunctionMapper UNICODE = new W32APIFunctionMapper(true);
/* 34 */   public static final FunctionMapper ASCII = new W32APIFunctionMapper(false);
/*    */   
/*    */   protected W32APIFunctionMapper(boolean unicode) {
/* 37 */     this.suffix = unicode ? "W" : "A";
/*    */   }
/*    */   private final String suffix;
/*    */   
/*    */   public String getFunctionName(NativeLibrary library, Method method) {
/* 42 */     String name = method.getName();
/* 43 */     if (!name.endsWith("W") && !name.endsWith("A")) {
/*    */       try {
/* 45 */         name = library.getFunction(name + this.suffix, 63).getName();
/*    */       }
/* 47 */       catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
/*    */     }
/*    */ 
/*    */     
/* 51 */     return name;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\win32\W32APIFunctionMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */