/*    */ package com.gikk.twirk.types.mode;
/*    */ 
/*    */ class ModeImpl implements Mode {
/*    */   private final String user;
/*    */   private final Mode.MODE_EVENT event;
/*    */   private final String rawLine;
/*    */   
/*    */   ModeImpl(DefaultModeBuilder builder) {
/*  9 */     this.event = builder.event;
/* 10 */     this.user = builder.user;
/* 11 */     this.rawLine = builder.rawLine;
/*    */   }
/*    */ 
/*    */   
/*    */   public Mode.MODE_EVENT getEvent() {
/* 16 */     return this.event;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUser() {
/* 21 */     return this.user;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRaw() {
/* 26 */     return this.rawLine;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\mode\ModeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */