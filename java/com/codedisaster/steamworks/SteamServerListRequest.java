/*    */ package com.codedisaster.steamworks;
/*    */ 
/*    */ public class SteamServerListRequest
/*    */   extends SteamNativeHandle {
/*    */   SteamServerListRequest(long handle) {
/*  6 */     super(handle);
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 10 */     return (this.handle != 0L);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\codedisaster\steamworks\SteamServerListRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */