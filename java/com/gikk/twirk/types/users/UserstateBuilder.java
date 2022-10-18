/*    */ package com.gikk.twirk.types.users;
/*    */ 
/*    */ import com.gikk.twirk.types.twitchMessage.TwitchMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface UserstateBuilder
/*    */ {
/*    */   static UserstateBuilder getDefault() {
/* 13 */     return new DefaultUserstateBuilder();
/*    */   }
/*    */   
/*    */   Userstate build(TwitchMessage paramTwitchMessage);
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\type\\users\UserstateBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */