/*    */ package com.gikk.twirk.types.usernotice;
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
/*    */ public interface UsernoticeBuilder
/*    */ {
/*    */   Usernotice build(TwitchMessage paramTwitchMessage);
/*    */   
/*    */   static UsernoticeBuilder getDefault() {
/* 22 */     return new DefaultUsernoticeBuilder();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\type\\usernotice\UsernoticeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */