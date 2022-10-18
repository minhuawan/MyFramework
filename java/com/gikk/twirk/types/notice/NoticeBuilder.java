/*    */ package com.gikk.twirk.types.notice;
/*    */ 
/*    */ import com.gikk.twirk.types.twitchMessage.TwitchMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface NoticeBuilder
/*    */ {
/*    */   static NoticeBuilder getDefault() {
/* 13 */     return new DefaultNoticeBuilder();
/*    */   }
/*    */   
/*    */   Notice build(TwitchMessage paramTwitchMessage);
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\types\notice\NoticeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */