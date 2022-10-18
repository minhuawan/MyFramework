/*    */ package com.gikk.twirk.enums;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum NOTICE_EVENT
/*    */ {
/*    */   private final String msgID;
/* 24 */   ALREADY_BANNED("already_banned"),
/*    */   
/* 26 */   ALREADY_EMOTES_ONLY_OFF("already_emote_only_off"),
/*    */   
/* 28 */   ALREADY_EMOTES_ONLY_ON("already_emote_only_on"),
/*    */   
/* 30 */   ALREADY_R9K_OFF("already_r9k_off"),
/*    */   
/* 32 */   ALREADY_R9K_ON("already_r9k_on"),
/*    */   
/* 34 */   ALREADY_SUBS_OFF("already_subs_off"),
/*    */   
/* 36 */   ALREADY_SUBS_ON("already_subs_on"),
/*    */   
/* 38 */   BAD_HOST_HOSTING("bad_host_hosting"),
/*    */   
/* 40 */   BAD_UNBAN_NO_BAN("bad_unban_no_ban"),
/*    */   
/* 42 */   BAN_SUCCESS("ban_success"),
/*    */   
/* 44 */   EMOTE_ONLY_OFF("emote_only_off"),
/*    */   
/* 46 */   EMOTE_ONLY_ON("emote_only_on"),
/*    */   
/* 48 */   HOST_OFF("host_off"),
/*    */   
/* 50 */   HOST_ON("host_on"),
/*    */   
/* 52 */   HOST_REMAINING("hosts_remaining"),
/*    */   
/* 54 */   MESSAGE_CHANNEL_SUSPENDED("msg_channel_suspended"),
/*    */   
/* 56 */   R9K_OFF("r9k_off"),
/*    */   
/* 58 */   R9K_ON("r9k_on"),
/*    */   
/* 60 */   SLOW_OFF("slow_off"),
/*    */   
/* 62 */   SLOW_ON("slow_on"),
/*    */   
/* 64 */   SUBSCRIBER_MODE_OFF("subs_off"),
/*    */   
/* 66 */   SUBSCRIBER_MODE_ON("subs_on"),
/*    */   
/* 68 */   TIMEOUT_SUCCESS("timeout_success"),
/*    */   
/* 70 */   UNBAN_SUCCESS("unban_success"),
/*    */   
/* 72 */   UNRECOGNIZED_COMMAND("unrecognized_cmd"),
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   OTHER("");
/*    */   private static final Map<String, NOTICE_EVENT> mapping;
/*    */   
/*    */   NOTICE_EVENT(String msgID) {
/* 82 */     this.msgID = msgID;
/*    */   }
/*    */   static {
/* 85 */     mapping = new HashMap<>();
/*    */     
/* 87 */     for (NOTICE_EVENT n : values()) {
/* 88 */       mapping.put(n.msgID, n);
/*    */     }
/*    */   }
/*    */   
/*    */   public static NOTICE_EVENT of(String msgID) {
/* 93 */     return mapping.getOrDefault(msgID, OTHER);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\gikk\twirk\enums\NOTICE_EVENT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */