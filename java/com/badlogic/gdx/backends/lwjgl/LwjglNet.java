/*    */ package com.badlogic.gdx.backends.lwjgl;
/*    */ 
/*    */ import com.badlogic.gdx.Net;
/*    */ import com.badlogic.gdx.net.NetJavaImpl;
/*    */ import com.badlogic.gdx.net.NetJavaServerSocketImpl;
/*    */ import com.badlogic.gdx.net.NetJavaSocketImpl;
/*    */ import com.badlogic.gdx.net.ServerSocket;
/*    */ import com.badlogic.gdx.net.ServerSocketHints;
/*    */ import com.badlogic.gdx.net.Socket;
/*    */ import com.badlogic.gdx.net.SocketHints;
/*    */ import org.lwjgl.Sys;
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
/*    */ public class LwjglNet
/*    */   implements Net
/*    */ {
/* 37 */   NetJavaImpl netJavaImpl = new NetJavaImpl();
/*    */ 
/*    */   
/*    */   public void sendHttpRequest(Net.HttpRequest httpRequest, Net.HttpResponseListener httpResponseListener) {
/* 41 */     this.netJavaImpl.sendHttpRequest(httpRequest, httpResponseListener);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancelHttpRequest(Net.HttpRequest httpRequest) {
/* 46 */     this.netJavaImpl.cancelHttpRequest(httpRequest);
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerSocket newServerSocket(Net.Protocol protocol, String ipAddress, int port, ServerSocketHints hints) {
/* 51 */     return (ServerSocket)new NetJavaServerSocketImpl(protocol, ipAddress, port, hints);
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerSocket newServerSocket(Net.Protocol protocol, int port, ServerSocketHints hints) {
/* 56 */     return (ServerSocket)new NetJavaServerSocketImpl(protocol, port, hints);
/*    */   }
/*    */ 
/*    */   
/*    */   public Socket newClientSocket(Net.Protocol protocol, String host, int port, SocketHints hints) {
/* 61 */     return (Socket)new NetJavaSocketImpl(protocol, host, port, hints);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean openURI(String URI) {
/* 66 */     return Sys.openURL(URI);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglNet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */