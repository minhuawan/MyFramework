/*     */ package com.badlogic.gdx.net;
/*     */ 
/*     */ import com.badlogic.gdx.Net;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NetJavaSocketImpl
/*     */   implements Socket
/*     */ {
/*     */   private Socket socket;
/*     */   
/*     */   public NetJavaSocketImpl(Net.Protocol protocol, String host, int port, SocketHints hints) {
/*     */     try {
/*  40 */       this.socket = new Socket();
/*  41 */       applyHints(hints);
/*     */ 
/*     */       
/*  44 */       InetSocketAddress address = new InetSocketAddress(host, port);
/*  45 */       if (hints != null) {
/*  46 */         this.socket.connect(address, hints.connectTimeout);
/*     */       } else {
/*  48 */         this.socket.connect(address);
/*     */       } 
/*  50 */     } catch (Exception e) {
/*  51 */       throw new GdxRuntimeException("Error making a socket connection to " + host + ":" + port, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public NetJavaSocketImpl(Socket socket, SocketHints hints) {
/*  56 */     this.socket = socket;
/*  57 */     applyHints(hints);
/*     */   }
/*     */   
/*     */   private void applyHints(SocketHints hints) {
/*  61 */     if (hints != null) {
/*     */       try {
/*  63 */         this.socket.setPerformancePreferences(hints.performancePrefConnectionTime, hints.performancePrefLatency, hints.performancePrefBandwidth);
/*     */         
/*  65 */         this.socket.setTrafficClass(hints.trafficClass);
/*  66 */         this.socket.setTcpNoDelay(hints.tcpNoDelay);
/*  67 */         this.socket.setKeepAlive(hints.keepAlive);
/*  68 */         this.socket.setSendBufferSize(hints.sendBufferSize);
/*  69 */         this.socket.setReceiveBufferSize(hints.receiveBufferSize);
/*  70 */         this.socket.setSoLinger(hints.linger, hints.lingerDuration);
/*  71 */         this.socket.setSoTimeout(hints.socketTimeout);
/*  72 */       } catch (Exception e) {
/*  73 */         throw new GdxRuntimeException("Error setting socket hints.", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/*  80 */     if (this.socket != null) {
/*  81 */       return this.socket.isConnected();
/*     */     }
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() {
/*     */     try {
/*  90 */       return this.socket.getInputStream();
/*  91 */     } catch (Exception e) {
/*  92 */       throw new GdxRuntimeException("Error getting input stream from socket.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() {
/*     */     try {
/*  99 */       return this.socket.getOutputStream();
/* 100 */     } catch (Exception e) {
/* 101 */       throw new GdxRuntimeException("Error getting output stream from socket.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRemoteAddress() {
/* 107 */     return this.socket.getRemoteSocketAddress().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 112 */     if (this.socket != null)
/*     */       try {
/* 114 */         this.socket.close();
/* 115 */         this.socket = null;
/* 116 */       } catch (Exception e) {
/* 117 */         throw new GdxRuntimeException("Error closing socket.", e);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\net\NetJavaSocketImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */