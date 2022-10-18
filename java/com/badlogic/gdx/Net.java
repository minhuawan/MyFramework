/*     */ package com.badlogic.gdx;
/*     */ 
/*     */ import com.badlogic.gdx.net.HttpStatus;
/*     */ import com.badlogic.gdx.net.ServerSocket;
/*     */ import com.badlogic.gdx.net.ServerSocketHints;
/*     */ import com.badlogic.gdx.net.Socket;
/*     */ import com.badlogic.gdx.net.SocketHints;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public interface Net
/*     */ {
/*     */   void sendHttpRequest(HttpRequest paramHttpRequest, HttpResponseListener paramHttpResponseListener);
/*     */   
/*     */   void cancelHttpRequest(HttpRequest paramHttpRequest);
/*     */   
/*     */   ServerSocket newServerSocket(Protocol paramProtocol, String paramString, int paramInt, ServerSocketHints paramServerSocketHints);
/*     */   
/*     */   ServerSocket newServerSocket(Protocol paramProtocol, int paramInt, ServerSocketHints paramServerSocketHints);
/*     */   
/*     */   Socket newClientSocket(Protocol paramProtocol, String paramString, int paramInt, SocketHints paramSocketHints);
/*     */   
/*     */   boolean openURI(String paramString);
/*     */   
/*     */   public static interface HttpResponse
/*     */   {
/*     */     byte[] getResult();
/*     */     
/*     */     String getResultAsString();
/*     */     
/*     */     InputStream getResultAsStream();
/*     */     
/*     */     HttpStatus getStatus();
/*     */     
/*     */     String getHeader(String param1String);
/*     */     
/*     */     Map<String, List<String>> getHeaders();
/*     */   }
/*     */   
/*     */   public static interface HttpMethods
/*     */   {
/*     */     public static final String GET = "GET";
/*     */     public static final String POST = "POST";
/*     */     public static final String PUT = "PUT";
/*     */     public static final String DELETE = "DELETE";
/*     */   }
/*     */   
/*     */   public static class HttpRequest
/*     */     implements Pool.Poolable
/*     */   {
/*     */     private String httpMethod;
/*     */     private String url;
/*     */     private Map<String, String> headers;
/* 147 */     private int timeOut = 0;
/*     */     
/*     */     private String content;
/*     */     
/*     */     private InputStream contentStream;
/*     */     
/*     */     private long contentLength;
/*     */     private boolean followRedirects = true;
/*     */     private boolean includeCredentials = false;
/*     */     
/*     */     public HttpRequest() {
/* 158 */       this.headers = new HashMap<String, String>();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HttpRequest(String httpMethod) {
/* 164 */       this();
/* 165 */       this.httpMethod = httpMethod;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setUrl(String url) {
/* 171 */       this.url = url;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setHeader(String name, String value) {
/* 178 */       this.headers.put(name, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setContent(String content) {
/* 186 */       this.content = content;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setContent(InputStream contentStream, long contentLength) {
/* 192 */       this.contentStream = contentStream;
/* 193 */       this.contentLength = contentLength;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTimeOut(int timeOut) {
/* 200 */       this.timeOut = timeOut;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFollowRedirects(boolean followRedirects) throws IllegalArgumentException {
/* 208 */       if (followRedirects == true || Gdx.app.getType() != Application.ApplicationType.WebGL) {
/* 209 */         this.followRedirects = followRedirects;
/*     */       } else {
/* 211 */         throw new IllegalArgumentException("Following redirects can't be disabled using the GWT/WebGL backend!");
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setIncludeCredentials(boolean includeCredentials) {
/* 218 */       this.includeCredentials = includeCredentials;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setMethod(String httpMethod) {
/* 223 */       this.httpMethod = httpMethod;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getTimeOut() {
/* 229 */       return this.timeOut;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMethod() {
/* 234 */       return this.httpMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getUrl() {
/* 239 */       return this.url;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getContent() {
/* 244 */       return this.content;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getContentStream() {
/* 249 */       return this.contentStream;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getContentLength() {
/* 254 */       return this.contentLength;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, String> getHeaders() {
/* 259 */       return this.headers;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getFollowRedirects() {
/* 264 */       return this.followRedirects;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getIncludeCredentials() {
/* 269 */       return this.includeCredentials;
/*     */     }
/*     */ 
/*     */     
/*     */     public void reset() {
/* 274 */       this.httpMethod = null;
/* 275 */       this.url = null;
/* 276 */       this.headers.clear();
/* 277 */       this.timeOut = 0;
/*     */       
/* 279 */       this.content = null;
/* 280 */       this.contentStream = null;
/* 281 */       this.contentLength = 0L;
/*     */       
/* 283 */       this.followRedirects = true;
/*     */     }
/*     */   }
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
/*     */   public static interface HttpResponseListener
/*     */   {
/*     */     void handleHttpResponse(Net.HttpResponse param1HttpResponse);
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
/*     */     void failed(Throwable param1Throwable);
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
/*     */     void cancelled();
/*     */   }
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
/*     */   public enum Protocol
/*     */   {
/* 334 */     TCP;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\Net.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */