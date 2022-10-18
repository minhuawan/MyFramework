/*    */ package com.gikk.twirk.types.hostTarget;
/*    */ 
/*    */ import com.gikk.twirk.types.twitchMessage.TwitchMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface HostTargetBuilder
/*    */ {
/*    */   static HostTargetBuilder getDefault() {
/* 13 */     return new DefaultHostTargetBuilder();
/*    */   }
/*    */   
/*    */   HostTarget build(TwitchMessage paramTwitchMessage);
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\hostTarget\HostTargetBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */