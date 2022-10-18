/*    */ package com.jcraft.jorbis;
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
/*    */ class Residue2
/*    */   extends Residue0
/*    */ {
/*    */   int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
/* 32 */     int i = 0;
/* 33 */     for (i = 0; i < ch && 
/* 34 */       nonzero[i] == 0; i++);
/*    */     
/* 36 */     if (i == ch) {
/* 37 */       return 0;
/*    */     }
/* 39 */     return _2inverse(vb, vl, in, ch);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\jcraft\jorbis\Residue2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */