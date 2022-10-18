/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.ByteBuffer;
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
/*     */ public final class StreamUtils
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 4096;
/*  31 */   public static final byte[] EMPTY_BYTES = new byte[0];
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyStream(InputStream input, OutputStream output) throws IOException {
/*  36 */     copyStream(input, output, new byte[4096]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyStream(InputStream input, OutputStream output, int bufferSize) throws IOException {
/*  42 */     copyStream(input, output, new byte[bufferSize]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyStream(InputStream input, OutputStream output, byte[] buffer) throws IOException {
/*     */     int bytesRead;
/*  49 */     while ((bytesRead = input.read(buffer)) != -1) {
/*  50 */       output.write(buffer, 0, bytesRead);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyStream(InputStream input, ByteBuffer output) throws IOException {
/*  57 */     copyStream(input, output, new byte[4096]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyStream(InputStream input, ByteBuffer output, int bufferSize) throws IOException {
/*  63 */     copyStream(input, output, new byte[bufferSize]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int copyStream(InputStream input, ByteBuffer output, byte[] buffer) throws IOException {
/*  72 */     int startPosition = output.position(), total = 0; int bytesRead;
/*  73 */     while ((bytesRead = input.read(buffer)) != -1) {
/*  74 */       BufferUtils.copy(buffer, 0, output, bytesRead);
/*  75 */       total += bytesRead;
/*  76 */       output.position(startPosition + total);
/*     */     } 
/*  78 */     output.position(startPosition);
/*  79 */     return total;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] copyStreamToByteArray(InputStream input) throws IOException {
/*  84 */     return copyStreamToByteArray(input, input.available());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] copyStreamToByteArray(InputStream input, int estimatedSize) throws IOException {
/*  90 */     ByteArrayOutputStream baos = new OptimizedByteArrayOutputStream(Math.max(0, estimatedSize));
/*  91 */     copyStream(input, baos);
/*  92 */     return baos.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String copyStreamToString(InputStream input) throws IOException {
/*  98 */     return copyStreamToString(input, input.available(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String copyStreamToString(InputStream input, int estimatedSize) throws IOException {
/* 103 */     return copyStreamToString(input, estimatedSize, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String copyStreamToString(InputStream input, int estimatedSize, String charset) throws IOException {
/* 110 */     InputStreamReader reader = (charset == null) ? new InputStreamReader(input) : new InputStreamReader(input, charset);
/* 111 */     StringWriter writer = new StringWriter(Math.max(0, estimatedSize));
/* 112 */     char[] buffer = new char[4096];
/*     */     int charsRead;
/* 114 */     while ((charsRead = reader.read(buffer)) != -1) {
/* 115 */       writer.write(buffer, 0, charsRead);
/*     */     }
/* 117 */     return writer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void closeQuietly(Closeable c) {
/* 122 */     if (c != null) {
/*     */       try {
/* 124 */         c.close();
/* 125 */       } catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static class OptimizedByteArrayOutputStream
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     public OptimizedByteArrayOutputStream(int initialSize) {
/* 133 */       super(initialSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized byte[] toByteArray() {
/* 138 */       if (this.count == this.buf.length) return this.buf; 
/* 139 */       return super.toByteArray();
/*     */     }
/*     */     
/*     */     public byte[] getBuffer() {
/* 143 */       return this.buf;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\StreamUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */