/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public class Gdx2DPixmap
/*     */   implements Disposable
/*     */ {
/*     */   public static final int GDX2D_FORMAT_ALPHA = 1;
/*     */   public static final int GDX2D_FORMAT_LUMINANCE_ALPHA = 2;
/*     */   public static final int GDX2D_FORMAT_RGB888 = 3;
/*     */   public static final int GDX2D_FORMAT_RGBA8888 = 4;
/*     */   public static final int GDX2D_FORMAT_RGB565 = 5;
/*     */   public static final int GDX2D_FORMAT_RGBA4444 = 6;
/*     */   public static final int GDX2D_SCALE_NEAREST = 0;
/*     */   public static final int GDX2D_SCALE_LINEAR = 1;
/*     */   public static final int GDX2D_BLEND_NONE = 0;
/*     */   public static final int GDX2D_BLEND_SRC_OVER = 1;
/*     */   long basePtr;
/*     */   int width;
/*     */   int height;
/*     */   int format;
/*     */   ByteBuffer pixelPtr;
/*     */   
/*     */   public static int toGlFormat(int format) {
/*  44 */     switch (format) {
/*     */       case 1:
/*  46 */         return 6406;
/*     */       case 2:
/*  48 */         return 6410;
/*     */       case 3:
/*     */       case 5:
/*  51 */         return 6407;
/*     */       case 4:
/*     */       case 6:
/*  54 */         return 6408;
/*     */     } 
/*  56 */     throw new GdxRuntimeException("unknown format: " + format);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int toGlType(int format) {
/*  61 */     switch (format) {
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*  66 */         return 5121;
/*     */       case 5:
/*  68 */         return 33635;
/*     */       case 6:
/*  70 */         return 32819;
/*     */     } 
/*  72 */     throw new GdxRuntimeException("unknown format: " + format);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   long[] nativeData = new long[4];
/*     */   
/*     */   static {
/*  84 */     setBlend(1);
/*  85 */     setScale(1);
/*     */   }
/*     */   
/*     */   public Gdx2DPixmap(byte[] encodedData, int offset, int len, int requestedFormat) throws IOException {
/*  89 */     this.pixelPtr = load(this.nativeData, encodedData, offset, len);
/*  90 */     if (this.pixelPtr == null) throw new IOException("Error loading pixmap: " + getFailureReason());
/*     */     
/*  92 */     this.basePtr = this.nativeData[0];
/*  93 */     this.width = (int)this.nativeData[1];
/*  94 */     this.height = (int)this.nativeData[2];
/*  95 */     this.format = (int)this.nativeData[3];
/*     */     
/*  97 */     if (requestedFormat != 0 && requestedFormat != this.format) {
/*  98 */       convert(requestedFormat);
/*     */     }
/*     */   }
/*     */   
/*     */   public Gdx2DPixmap(InputStream in, int requestedFormat) throws IOException {
/* 103 */     ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
/* 104 */     byte[] buffer = new byte[1024];
/* 105 */     int readBytes = 0;
/*     */     
/* 107 */     while ((readBytes = in.read(buffer)) != -1) {
/* 108 */       bytes.write(buffer, 0, readBytes);
/*     */     }
/*     */     
/* 111 */     buffer = bytes.toByteArray();
/* 112 */     this.pixelPtr = load(this.nativeData, buffer, 0, buffer.length);
/* 113 */     if (this.pixelPtr == null) throw new IOException("Error loading pixmap: " + getFailureReason());
/*     */     
/* 115 */     this.basePtr = this.nativeData[0];
/* 116 */     this.width = (int)this.nativeData[1];
/* 117 */     this.height = (int)this.nativeData[2];
/* 118 */     this.format = (int)this.nativeData[3];
/*     */     
/* 120 */     if (requestedFormat != 0 && requestedFormat != this.format) {
/* 121 */       convert(requestedFormat);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Gdx2DPixmap(int width, int height, int format) throws GdxRuntimeException {
/* 127 */     this.pixelPtr = newPixmap(this.nativeData, width, height, format);
/* 128 */     if (this.pixelPtr == null) throw new GdxRuntimeException("Error loading pixmap.");
/*     */     
/* 130 */     this.basePtr = this.nativeData[0];
/* 131 */     this.width = (int)this.nativeData[1];
/* 132 */     this.height = (int)this.nativeData[2];
/* 133 */     this.format = (int)this.nativeData[3];
/*     */   }
/*     */   
/*     */   public Gdx2DPixmap(ByteBuffer pixelPtr, long[] nativeData) {
/* 137 */     this.pixelPtr = pixelPtr;
/* 138 */     this.basePtr = nativeData[0];
/* 139 */     this.width = (int)nativeData[1];
/* 140 */     this.height = (int)nativeData[2];
/* 141 */     this.format = (int)nativeData[3];
/*     */   }
/*     */   
/*     */   private void convert(int requestedFormat) {
/* 145 */     Gdx2DPixmap pixmap = new Gdx2DPixmap(this.width, this.height, requestedFormat);
/* 146 */     pixmap.drawPixmap(this, 0, 0, 0, 0, this.width, this.height);
/* 147 */     dispose();
/* 148 */     this.basePtr = pixmap.basePtr;
/* 149 */     this.format = pixmap.format;
/* 150 */     this.height = pixmap.height;
/* 151 */     this.nativeData = pixmap.nativeData;
/* 152 */     this.pixelPtr = pixmap.pixelPtr;
/* 153 */     this.width = pixmap.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 158 */     free(this.basePtr);
/*     */   }
/*     */   
/*     */   public void clear(int color) {
/* 162 */     clear(this.basePtr, color);
/*     */   }
/*     */   
/*     */   public void setPixel(int x, int y, int color) {
/* 166 */     setPixel(this.basePtr, x, y, color);
/*     */   }
/*     */   
/*     */   public int getPixel(int x, int y) {
/* 170 */     return getPixel(this.basePtr, x, y);
/*     */   }
/*     */   
/*     */   public void drawLine(int x, int y, int x2, int y2, int color) {
/* 174 */     drawLine(this.basePtr, x, y, x2, y2, color);
/*     */   }
/*     */   
/*     */   public void drawRect(int x, int y, int width, int height, int color) {
/* 178 */     drawRect(this.basePtr, x, y, width, height, color);
/*     */   }
/*     */   
/*     */   public void drawCircle(int x, int y, int radius, int color) {
/* 182 */     drawCircle(this.basePtr, x, y, radius, color);
/*     */   }
/*     */   
/*     */   public void fillRect(int x, int y, int width, int height, int color) {
/* 186 */     fillRect(this.basePtr, x, y, width, height, color);
/*     */   }
/*     */   
/*     */   public void fillCircle(int x, int y, int radius, int color) {
/* 190 */     fillCircle(this.basePtr, x, y, radius, color);
/*     */   }
/*     */   
/*     */   public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
/* 194 */     fillTriangle(this.basePtr, x1, y1, x2, y2, x3, y3, color);
/*     */   }
/*     */   
/*     */   public void drawPixmap(Gdx2DPixmap src, int srcX, int srcY, int dstX, int dstY, int width, int height) {
/* 198 */     drawPixmap(src.basePtr, this.basePtr, srcX, srcY, width, height, dstX, dstY, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawPixmap(Gdx2DPixmap src, int srcX, int srcY, int srcWidth, int srcHeight, int dstX, int dstY, int dstWidth, int dstHeight) {
/* 203 */     drawPixmap(src.basePtr, this.basePtr, srcX, srcY, srcWidth, srcHeight, dstX, dstY, dstWidth, dstHeight);
/*     */   }
/*     */   
/*     */   public static Gdx2DPixmap newPixmap(InputStream in, int requestedFormat) {
/*     */     try {
/* 208 */       return new Gdx2DPixmap(in, requestedFormat);
/* 209 */     } catch (IOException e) {
/* 210 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Gdx2DPixmap newPixmap(int width, int height, int format) {
/*     */     try {
/* 216 */       return new Gdx2DPixmap(width, height, format);
/* 217 */     } catch (IllegalArgumentException e) {
/* 218 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ByteBuffer getPixels() {
/* 223 */     return this.pixelPtr;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 227 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 231 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getFormat() {
/* 235 */     return this.format;
/*     */   }
/*     */   
/*     */   public int getGLInternalFormat() {
/* 239 */     return toGlFormat(this.format);
/*     */   }
/*     */   
/*     */   public int getGLFormat() {
/* 243 */     return getGLInternalFormat();
/*     */   }
/*     */   
/*     */   public int getGLType() {
/* 247 */     return toGlType(this.format);
/*     */   }
/*     */   
/*     */   public String getFormatString() {
/* 251 */     switch (this.format) {
/*     */       case 1:
/* 253 */         return "alpha";
/*     */       case 2:
/* 255 */         return "luminance alpha";
/*     */       case 3:
/* 257 */         return "rgb888";
/*     */       case 4:
/* 259 */         return "rgba8888";
/*     */       case 5:
/* 261 */         return "rgb565";
/*     */       case 6:
/* 263 */         return "rgba4444";
/*     */     } 
/* 265 */     return "unknown";
/*     */   }
/*     */   
/*     */   private static native ByteBuffer load(long[] paramArrayOflong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */   
/*     */   private static native ByteBuffer newPixmap(long[] paramArrayOflong, int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native void free(long paramLong);
/*     */   
/*     */   private static native void clear(long paramLong, int paramInt);
/*     */   
/*     */   private static native void setPixel(long paramLong, int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   private static native int getPixel(long paramLong, int paramInt1, int paramInt2);
/*     */   
/*     */   private static native void drawLine(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */   
/*     */   private static native void drawRect(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */   
/*     */   private static native void drawCircle(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native void fillRect(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*     */   
/*     */   private static native void fillCircle(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   private static native void fillTriangle(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
/*     */   
/*     */   private static native void drawPixmap(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8);
/*     */   
/*     */   public static native void setBlend(int paramInt);
/*     */   
/*     */   public static native void setScale(int paramInt);
/*     */   
/*     */   public static native String getFailureReason();
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\Gdx2DPixmap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */