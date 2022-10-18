/*    */ package com.badlogic.gdx.net;
/*    */ 
/*    */ import com.badlogic.gdx.Net;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.ServerSocket;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NetJavaServerSocketImpl
/*    */   implements ServerSocket
/*    */ {
/*    */   private Net.Protocol protocol;
/*    */   private ServerSocket server;
/*    */   
/*    */   public NetJavaServerSocketImpl(Net.Protocol protocol, int port, ServerSocketHints hints) {
/* 41 */     this(protocol, null, port, hints);
/*    */   }
/*    */   
/*    */   public NetJavaServerSocketImpl(Net.Protocol protocol, String hostname, int port, ServerSocketHints hints) {
/* 45 */     this.protocol = protocol;
/*    */     
/*    */     try {
/*    */       InetSocketAddress address;
/*    */       
/* 50 */       this.server = new ServerSocket();
/* 51 */       if (hints != null) {
/* 52 */         this.server.setPerformancePreferences(hints.performancePrefConnectionTime, hints.performancePrefLatency, hints.performancePrefBandwidth);
/*    */         
/* 54 */         this.server.setReuseAddress(hints.reuseAddress);
/* 55 */         this.server.setSoTimeout(hints.acceptTimeout);
/* 56 */         this.server.setReceiveBufferSize(hints.receiveBufferSize);
/*    */       } 
/*    */ 
/*    */ 
/*    */       
/* 61 */       if (hostname != null) {
/* 62 */         address = new InetSocketAddress(hostname, port);
/*    */       } else {
/* 64 */         address = new InetSocketAddress(port);
/*    */       } 
/*    */       
/* 67 */       if (hints != null) {
/* 68 */         this.server.bind(address, hints.backlog);
/*    */       } else {
/* 70 */         this.server.bind(address);
/*    */       } 
/* 72 */     } catch (Exception e) {
/* 73 */       throw new GdxRuntimeException("Cannot create a server socket at port " + port + ".", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Net.Protocol getProtocol() {
/* 79 */     return this.protocol;
/*    */   }
/*    */ 
/*    */   
/*    */   public Socket accept(SocketHints hints) {
/*    */     try {
/* 85 */       return new NetJavaSocketImpl(this.server.accept(), hints);
/* 86 */     } catch (Exception e) {
/* 87 */       throw new GdxRuntimeException("Error accepting socket.", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 93 */     if (this.server != null)
/*    */       try {
/* 95 */         this.server.close();
/* 96 */         this.server = null;
/* 97 */       } catch (Exception e) {
/* 98 */         throw new GdxRuntimeException("Error closing server.", e);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\net\NetJavaServerSocketImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */