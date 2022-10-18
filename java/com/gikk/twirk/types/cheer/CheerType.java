/*    */ package com.gikk.twirk.types.cheer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CheerType
/*    */ {
/*  8 */   ANIMATED("animated"), STATIC("static");
/*    */   private final String value;
/*    */   
/*    */   CheerType(String s) {
/* 12 */     this.value = s;
/*    */   }
/*    */   
/*    */   String getValue() {
/* 16 */     return this.value;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\cheer\CheerType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */