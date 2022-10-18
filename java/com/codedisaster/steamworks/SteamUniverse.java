/*    */ package com.codedisaster.steamworks;
/*    */ public enum SteamUniverse {
/*    */   private final int value;
/*  4 */   Invalid(0),
/*  5 */   Public(1),
/*  6 */   Beta(2),
/*  7 */   Internal(3),
/*  8 */   Dev(4); private static final SteamUniverse[] values;
/*    */   
/*    */   static {
/* 11 */     values = values();
/*    */   }
/*    */   SteamUniverse(int value) {
/* 14 */     this.value = value;
/*    */   }
/*    */   
/*    */   static SteamUniverse byValue(int value) {
/* 18 */     for (SteamUniverse type : values) {
/* 19 */       if (type.value == value) {
/* 20 */         return type;
/*    */       }
/*    */     } 
/* 23 */     return Invalid;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\codedisaster\steamworks\SteamUniverse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */