/*    */ package com.gikk.twirk.types.cheer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CheerSize
/*    */ {
/*  8 */   TINY(1), SMALL(2), MEDIUM(3), LARGE(4);
/*    */   private final int value;
/*    */   
/*    */   CheerSize(int i) {
/* 12 */     this.value = i;
/*    */   }
/*    */   
/*    */   int getValue() {
/* 16 */     return this.value;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\cheer\CheerSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */