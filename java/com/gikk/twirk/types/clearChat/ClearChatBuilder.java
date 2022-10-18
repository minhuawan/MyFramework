/*    */ package com.gikk.twirk.types.clearChat;
/*    */ 
/*    */ import com.gikk.twirk.types.twitchMessage.TwitchMessage;
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
/*    */ public interface ClearChatBuilder
/*    */ {
/*    */   ClearChat build(TwitchMessage paramTwitchMessage);
/*    */   
/*    */   static ClearChatBuilder getDefault() {
/* 22 */     return new DefaultClearChatBuilder();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\clearChat\ClearChatBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */