/*    */ package com.badlogic.gdx.net;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLEncoder;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ public class HttpParametersUtils
/*    */ {
/* 30 */   public static String defaultEncoding = "UTF-8";
/* 31 */   public static String nameValueSeparator = "=";
/* 32 */   public static String parameterSeparator = "&";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String convertHttpParameters(Map<String, String> parameters) {
/* 38 */     Set<String> keySet = parameters.keySet();
/* 39 */     StringBuffer convertedParameters = new StringBuffer();
/* 40 */     for (String name : keySet) {
/* 41 */       convertedParameters.append(encode(name, defaultEncoding));
/* 42 */       convertedParameters.append(nameValueSeparator);
/* 43 */       convertedParameters.append(encode(parameters.get(name), defaultEncoding));
/* 44 */       convertedParameters.append(parameterSeparator);
/*    */     } 
/* 46 */     if (convertedParameters.length() > 0) convertedParameters.deleteCharAt(convertedParameters.length() - 1); 
/* 47 */     return convertedParameters.toString();
/*    */   }
/*    */   
/*    */   private static String encode(String content, String encoding) {
/*    */     try {
/* 52 */       return URLEncoder.encode(content, encoding);
/* 53 */     } catch (UnsupportedEncodingException e) {
/* 54 */       throw new IllegalArgumentException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\net\HttpParametersUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */