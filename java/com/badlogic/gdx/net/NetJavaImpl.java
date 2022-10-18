/*     */ package com.badlogic.gdx.net;
/*     */ 
/*     */ import com.badlogic.gdx.Net;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import com.badlogic.gdx.utils.async.AsyncExecutor;
/*     */ import com.badlogic.gdx.utils.async.AsyncTask;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
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
/*     */ public class NetJavaImpl
/*     */ {
/*     */   static class HttpClientResponse
/*     */     implements Net.HttpResponse
/*     */   {
/*     */     private final HttpURLConnection connection;
/*     */     private HttpStatus status;
/*     */     
/*     */     public HttpClientResponse(HttpURLConnection connection) throws IOException {
/*  49 */       this.connection = connection;
/*     */       try {
/*  51 */         this.status = new HttpStatus(connection.getResponseCode());
/*  52 */       } catch (IOException e) {
/*  53 */         this.status = new HttpStatus(-1);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getResult() {
/*  59 */       InputStream input = getInputStream();
/*     */ 
/*     */       
/*  62 */       if (input == null) {
/*  63 */         return StreamUtils.EMPTY_BYTES;
/*     */       }
/*     */       
/*     */       try {
/*  67 */         return StreamUtils.copyStreamToByteArray(input, this.connection.getContentLength());
/*  68 */       } catch (IOException e) {
/*  69 */         return StreamUtils.EMPTY_BYTES;
/*     */       } finally {
/*  71 */         StreamUtils.closeQuietly(input);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String getResultAsString() {
/*  77 */       InputStream input = getInputStream();
/*     */ 
/*     */       
/*  80 */       if (input == null) {
/*  81 */         return "";
/*     */       }
/*     */       
/*     */       try {
/*  85 */         return StreamUtils.copyStreamToString(input, this.connection.getContentLength());
/*  86 */       } catch (IOException e) {
/*  87 */         return "";
/*     */       } finally {
/*  89 */         StreamUtils.closeQuietly(input);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getResultAsStream() {
/*  95 */       return getInputStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpStatus getStatus() {
/* 100 */       return this.status;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getHeader(String name) {
/* 105 */       return this.connection.getHeaderField(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, List<String>> getHeaders() {
/* 110 */       return this.connection.getHeaderFields();
/*     */     }
/*     */     
/*     */     private InputStream getInputStream() {
/*     */       try {
/* 115 */         return this.connection.getInputStream();
/* 116 */       } catch (IOException e) {
/* 117 */         return this.connection.getErrorStream();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   private final AsyncExecutor asyncExecutor = new AsyncExecutor(1);
/* 128 */   final ObjectMap<Net.HttpRequest, HttpURLConnection> connections = new ObjectMap();
/* 129 */   final ObjectMap<Net.HttpRequest, Net.HttpResponseListener> listeners = new ObjectMap();
/*     */ 
/*     */   
/*     */   public void sendHttpRequest(final Net.HttpRequest httpRequest, final Net.HttpResponseListener httpResponseListener) {
/* 133 */     if (httpRequest.getUrl() == null) {
/* 134 */       httpResponseListener.failed((Throwable)new GdxRuntimeException("can't process a HTTP request without URL set"));
/*     */       return;
/*     */     } 
/*     */     try {
/*     */       URL url;
/* 139 */       String method = httpRequest.getMethod();
/*     */ 
/*     */       
/* 142 */       if (method.equalsIgnoreCase("GET")) {
/* 143 */         String queryString = "";
/* 144 */         String value = httpRequest.getContent();
/* 145 */         if (value != null && !"".equals(value)) queryString = "?" + value; 
/* 146 */         url = new URL(httpRequest.getUrl() + queryString);
/*     */       } else {
/* 148 */         url = new URL(httpRequest.getUrl());
/*     */       } 
/*     */       
/* 151 */       final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/*     */       
/* 153 */       final boolean doingOutPut = (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT"));
/* 154 */       connection.setDoOutput(doingOutPut);
/* 155 */       connection.setDoInput(true);
/* 156 */       connection.setRequestMethod(method);
/* 157 */       HttpURLConnection.setFollowRedirects(httpRequest.getFollowRedirects());
/*     */       
/* 159 */       putIntoConnectionsAndListeners(httpRequest, httpResponseListener, connection);
/*     */ 
/*     */       
/* 162 */       for (Map.Entry<String, String> header : (Iterable<Map.Entry<String, String>>)httpRequest.getHeaders().entrySet()) {
/* 163 */         connection.addRequestProperty(header.getKey(), header.getValue());
/*     */       }
/*     */       
/* 166 */       connection.setConnectTimeout(httpRequest.getTimeOut());
/* 167 */       connection.setReadTimeout(httpRequest.getTimeOut());
/*     */       
/* 169 */       this.asyncExecutor.submit(new AsyncTask<Void>()
/*     */           {
/*     */             public Void call() throws Exception
/*     */             {
/*     */               try {
/* 174 */                 if (doingOutPut) {
/*     */                   
/* 176 */                   String contentAsString = httpRequest.getContent();
/* 177 */                   if (contentAsString != null) {
/* 178 */                     OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
/*     */                     try {
/* 180 */                       writer.write(contentAsString);
/*     */                     } finally {
/* 182 */                       StreamUtils.closeQuietly(writer);
/*     */                     } 
/*     */                   } else {
/* 185 */                     InputStream contentAsStream = httpRequest.getContentStream();
/* 186 */                     if (contentAsStream != null) {
/* 187 */                       OutputStream os = connection.getOutputStream();
/*     */                       try {
/* 189 */                         StreamUtils.copyStream(contentAsStream, os);
/*     */                       } finally {
/* 191 */                         StreamUtils.closeQuietly(os);
/*     */                       } 
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */                 
/* 197 */                 connection.connect();
/*     */                 
/* 199 */                 NetJavaImpl.HttpClientResponse clientResponse = new NetJavaImpl.HttpClientResponse(connection);
/*     */                 try {
/* 201 */                   Net.HttpResponseListener listener = NetJavaImpl.this.getFromListeners(httpRequest);
/*     */                   
/* 203 */                   if (listener != null) {
/* 204 */                     listener.handleHttpResponse(clientResponse);
/*     */                   }
/* 206 */                   NetJavaImpl.this.removeFromConnectionsAndListeners(httpRequest);
/*     */                 } finally {
/* 208 */                   connection.disconnect();
/*     */                 } 
/* 210 */               } catch (Exception e) {
/* 211 */                 connection.disconnect();
/*     */                 try {
/* 213 */                   httpResponseListener.failed(e);
/*     */                 } finally {
/* 215 */                   NetJavaImpl.this.removeFromConnectionsAndListeners(httpRequest);
/*     */                 } 
/*     */               } 
/*     */               
/* 219 */               return null;
/*     */             }
/*     */           });
/* 222 */     } catch (Exception e) {
/*     */       try {
/* 224 */         httpResponseListener.failed(e);
/*     */       } finally {
/* 226 */         removeFromConnectionsAndListeners(httpRequest);
/*     */       } 
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cancelHttpRequest(Net.HttpRequest httpRequest) {
/* 233 */     Net.HttpResponseListener httpResponseListener = getFromListeners(httpRequest);
/*     */     
/* 235 */     if (httpResponseListener != null) {
/* 236 */       httpResponseListener.cancelled();
/* 237 */       removeFromConnectionsAndListeners(httpRequest);
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized void removeFromConnectionsAndListeners(Net.HttpRequest httpRequest) {
/* 242 */     this.connections.remove(httpRequest);
/* 243 */     this.listeners.remove(httpRequest);
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void putIntoConnectionsAndListeners(Net.HttpRequest httpRequest, Net.HttpResponseListener httpResponseListener, HttpURLConnection connection) {
/* 248 */     this.connections.put(httpRequest, connection);
/* 249 */     this.listeners.put(httpRequest, httpResponseListener);
/*     */   }
/*     */   
/*     */   synchronized Net.HttpResponseListener getFromListeners(Net.HttpRequest httpRequest) {
/* 253 */     Net.HttpResponseListener httpResponseListener = (Net.HttpResponseListener)this.listeners.get(httpRequest);
/* 254 */     return httpResponseListener;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\net\NetJavaImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */