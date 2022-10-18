/*    */ package com.badlogic.gdx.backends.headless;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.Net;
/*    */ import com.badlogic.gdx.net.NetJavaImpl;
/*    */ import com.badlogic.gdx.net.NetJavaServerSocketImpl;
/*    */ import com.badlogic.gdx.net.NetJavaSocketImpl;
/*    */ import com.badlogic.gdx.net.ServerSocket;
/*    */ import com.badlogic.gdx.net.ServerSocketHints;
/*    */ import com.badlogic.gdx.net.Socket;
/*    */ import com.badlogic.gdx.net.SocketHints;
/*    */ import java.awt.Desktop;
/*    */ import java.awt.GraphicsEnvironment;
/*    */ import java.net.URI;
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
/*    */ public class HeadlessNet
/*    */   implements Net
/*    */ {
/* 38 */   NetJavaImpl netJavaImpl = new NetJavaImpl();
/*    */ 
/*    */   
/*    */   public void sendHttpRequest(Net.HttpRequest httpRequest, Net.HttpResponseListener httpResponseListener) {
/* 42 */     this.netJavaImpl.sendHttpRequest(httpRequest, httpResponseListener);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancelHttpRequest(Net.HttpRequest httpRequest) {
/* 47 */     this.netJavaImpl.cancelHttpRequest(httpRequest);
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerSocket newServerSocket(Net.Protocol protocol, String hostname, int port, ServerSocketHints hints) {
/* 52 */     return (ServerSocket)new NetJavaServerSocketImpl(protocol, hostname, port, hints);
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerSocket newServerSocket(Net.Protocol protocol, int port, ServerSocketHints hints) {
/* 57 */     return (ServerSocket)new NetJavaServerSocketImpl(protocol, port, hints);
/*    */   }
/*    */ 
/*    */   
/*    */   public Socket newClientSocket(Net.Protocol protocol, String host, int port, SocketHints hints) {
/* 62 */     return (Socket)new NetJavaSocketImpl(protocol, host, port, hints);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean openURI(String URI) {
/* 67 */     boolean result = false;
/*    */     try {
/* 69 */       if (!GraphicsEnvironment.isHeadless() && Desktop.isDesktopSupported()) {
/* 70 */         if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
/* 71 */           Desktop.getDesktop().browse(URI.create(URI));
/* 72 */           result = true;
/*    */         } 
/*    */       } else {
/* 75 */         Gdx.app.error("HeadlessNet", "Opening URIs on this environment is not supported. Ignoring.");
/*    */       } 
/* 77 */     } catch (Throwable t) {
/* 78 */       Gdx.app.error("HeadlessNet", "Failed to open URI. ", t);
/*    */     } 
/* 80 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\headless\HeadlessNet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */