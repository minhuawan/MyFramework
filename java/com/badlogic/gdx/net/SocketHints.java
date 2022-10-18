/*    */ package com.badlogic.gdx.net;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SocketHints
/*    */ {
/* 26 */   public int connectTimeout = 5000;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public int performancePrefConnectionTime = 0;
/* 35 */   public int performancePrefLatency = 1;
/* 36 */   public int performancePrefBandwidth = 0;
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
/* 47 */   public int trafficClass = 20;
/*    */   
/*    */   public boolean keepAlive = true;
/*    */   
/*    */   public boolean tcpNoDelay = true;
/*    */   
/* 53 */   public int sendBufferSize = 4096;
/*    */   
/* 55 */   public int receiveBufferSize = 4096;
/*    */   
/*    */   public boolean linger = false;
/*    */   
/* 59 */   public int lingerDuration = 0;
/*    */ 
/*    */   
/* 62 */   public int socketTimeout = 0;
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\net\SocketHints.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */