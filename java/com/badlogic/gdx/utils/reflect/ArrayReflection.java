/*    */ package com.badlogic.gdx.utils.reflect;
/*    */ 
/*    */ import java.lang.reflect.Array;
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
/*    */ public final class ArrayReflection
/*    */ {
/*    */   public static Object newInstance(Class<?> c, int size) {
/* 25 */     return Array.newInstance(c, size);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getLength(Object array) {
/* 30 */     return Array.getLength(array);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Object get(Object array, int index) {
/* 35 */     return Array.get(array, index);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void set(Object array, int index, Object value) {
/* 40 */     Array.set(array, index, value);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\reflect\ArrayReflection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */