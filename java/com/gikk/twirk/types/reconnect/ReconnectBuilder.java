/*    */ package com.gikk.twirk.types.reconnect;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ReconnectBuilder
/*    */ {
/*    */   static ReconnectBuilder getDefault() {
/* 12 */     return new DefaultReconnectBuilder();
/*    */   }
/*    */   
/*    */   Reconnect build();
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\reconnect\ReconnectBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */