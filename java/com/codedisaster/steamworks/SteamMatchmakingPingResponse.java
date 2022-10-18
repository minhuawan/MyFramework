/*   */ package com.codedisaster.steamworks;
/*   */ 
/*   */ public abstract class SteamMatchmakingPingResponse
/*   */   extends SteamInterface {
/*   */   protected SteamMatchmakingPingResponse() {
/* 6 */     super(-1L);
/* 7 */     this.callback = createProxy(this);
/*   */   }
/*   */   
/*   */   private static native long createProxy(SteamMatchmakingPingResponse paramSteamMatchmakingPingResponse);
/*   */   
/*   */   public abstract void serverFailedToRespond();
/*   */   
/*   */   public abstract void serverResponded(SteamMatchmakingGameServerItem paramSteamMatchmakingGameServerItem);
/*   */ }


/* Location:              D:\files\slayTheSpire-export\!\com\codedisaster\steamworks\SteamMatchmakingPingResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */