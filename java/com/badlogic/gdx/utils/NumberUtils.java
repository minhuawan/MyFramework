/*    */ package com.badlogic.gdx.utils;
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
/*    */ public final class NumberUtils
/*    */ {
/*    */   public static int floatToIntBits(float value) {
/* 21 */     return Float.floatToIntBits(value);
/*    */   }
/*    */   
/*    */   public static int floatToRawIntBits(float value) {
/* 25 */     return Float.floatToRawIntBits(value);
/*    */   }
/*    */   
/*    */   public static int floatToIntColor(float value) {
/* 29 */     return Float.floatToRawIntBits(value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static float intToFloatColor(int value) {
/* 35 */     return Float.intBitsToFloat(value & 0xFEFFFFFF);
/*    */   }
/*    */   
/*    */   public static float intBitsToFloat(int value) {
/* 39 */     return Float.intBitsToFloat(value);
/*    */   }
/*    */   
/*    */   public static long doubleToLongBits(double value) {
/* 43 */     return Double.doubleToLongBits(value);
/*    */   }
/*    */   
/*    */   public static double longBitsToDouble(long value) {
/* 47 */     return Double.longBitsToDouble(value);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\NumberUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */