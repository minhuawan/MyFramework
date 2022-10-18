/*    */ package com.google.gson.internal;
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
/*    */ 
/*    */ 
/*    */ public final class $Gson$Preconditions
/*    */ {
/*    */   private $Gson$Preconditions() {
/* 34 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public static <T> T checkNotNull(T obj) {
/* 38 */     if (obj == null) {
/* 39 */       throw new NullPointerException();
/*    */     }
/* 41 */     return obj;
/*    */   }
/*    */   
/*    */   public static void checkArgument(boolean condition) {
/* 45 */     if (!condition)
/* 46 */       throw new IllegalArgumentException(); 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\internal\$Gson$Preconditions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */