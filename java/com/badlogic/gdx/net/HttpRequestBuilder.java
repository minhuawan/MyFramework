/*     */ package com.badlogic.gdx.net;
/*     */ 
/*     */ import com.badlogic.gdx.Net;
/*     */ import com.badlogic.gdx.utils.Base64Coder;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.Pools;
/*     */ import java.io.InputStream;
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
/*     */ public class HttpRequestBuilder
/*     */ {
/*  37 */   public static String baseUrl = "";
/*     */ 
/*     */   
/*  40 */   public static int defaultTimeout = 1000;
/*     */ 
/*     */   
/*  43 */   public static Json json = new Json();
/*     */   
/*     */   private Net.HttpRequest httpRequest;
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder newRequest() {
/*  49 */     if (this.httpRequest != null) {
/*  50 */       throw new IllegalStateException("A new request has already been started. Call HttpRequestBuilder.build() first.");
/*     */     }
/*     */     
/*  53 */     this.httpRequest = (Net.HttpRequest)Pools.obtain(Net.HttpRequest.class);
/*  54 */     this.httpRequest.setTimeOut(defaultTimeout);
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder method(String httpMethod) {
/*  60 */     validate();
/*  61 */     this.httpRequest.setMethod(httpMethod);
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder url(String url) {
/*  69 */     validate();
/*  70 */     this.httpRequest.setUrl(baseUrl + url);
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder timeout(int timeOut) {
/*  78 */     validate();
/*  79 */     this.httpRequest.setTimeOut(timeOut);
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder followRedirects(boolean followRedirects) {
/*  85 */     validate();
/*  86 */     this.httpRequest.setFollowRedirects(followRedirects);
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder includeCredentials(boolean includeCredentials) {
/*  92 */     validate();
/*  93 */     this.httpRequest.setIncludeCredentials(includeCredentials);
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder header(String name, String value) {
/*  99 */     validate();
/* 100 */     this.httpRequest.setHeader(name, value);
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder content(String content) {
/* 106 */     validate();
/* 107 */     this.httpRequest.setContent(content);
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder content(InputStream contentStream, long contentLength) {
/* 113 */     validate();
/* 114 */     this.httpRequest.setContent(contentStream, contentLength);
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder formEncodedContent(Map<String, String> content) {
/* 120 */     validate();
/* 121 */     this.httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
/* 122 */     String formEncodedContent = HttpParametersUtils.convertHttpParameters(content);
/* 123 */     this.httpRequest.setContent(formEncodedContent);
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder jsonContent(Object content) {
/* 129 */     validate();
/* 130 */     this.httpRequest.setHeader("Content-Type", "application/json");
/* 131 */     String jsonContent = json.toJson(content);
/* 132 */     this.httpRequest.setContent(jsonContent);
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequestBuilder basicAuthentication(String username, String password) {
/* 138 */     validate();
/* 139 */     this.httpRequest.setHeader("Authorization", "Basic " + Base64Coder.encodeString(username + ":" + password));
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Net.HttpRequest build() {
/* 146 */     validate();
/* 147 */     Net.HttpRequest request = this.httpRequest;
/* 148 */     this.httpRequest = null;
/* 149 */     return request;
/*     */   }
/*     */   
/*     */   private void validate() {
/* 153 */     if (this.httpRequest == null)
/* 154 */       throw new IllegalStateException("A new request has not been started yet. Call HttpRequestBuilder.newRequest() first."); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\net\HttpRequestBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */