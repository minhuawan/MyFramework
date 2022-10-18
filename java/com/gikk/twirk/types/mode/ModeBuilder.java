/*    */ package com.gikk.twirk.types.mode;
/*    */ 
/*    */ import com.gikk.twirk.types.twitchMessage.TwitchMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ModeBuilder
/*    */ {
/*    */   static ModeBuilder getDefault() {
/* 13 */     return new DefaultModeBuilder();
/*    */   }
/*    */   
/*    */   Mode build(TwitchMessage paramTwitchMessage);
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\mode\ModeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */