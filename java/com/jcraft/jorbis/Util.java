/*    */ package com.jcraft.jorbis;
/*    */ 
/*    */ class Util {
/*    */   static int ilog(int v) {
/*  5 */     int ret = 0;
/*  6 */     while (v != 0) {
/*  7 */       ret++;
/*  8 */       v >>>= 1;
/*    */     } 
/* 10 */     return ret;
/*    */   }
/*    */   
/*    */   static int ilog2(int v) {
/* 14 */     int ret = 0;
/* 15 */     while (v > 1) {
/* 16 */       ret++;
/* 17 */       v >>>= 1;
/*    */     } 
/* 19 */     return ret;
/*    */   }
/*    */   
/*    */   static int icount(int v) {
/* 23 */     int ret = 0;
/* 24 */     while (v != 0) {
/* 25 */       ret += v & 0x1;
/* 26 */       v >>>= 1;
/*    */     } 
/* 28 */     return ret;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\jcraft\jorbis\Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */