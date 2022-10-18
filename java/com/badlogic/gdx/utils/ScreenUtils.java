/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.math.MathUtils;
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
/*     */ public final class ScreenUtils
/*     */ {
/*     */   public static TextureRegion getFrameBufferTexture() {
/*  40 */     int w = Gdx.graphics.getBackBufferWidth();
/*  41 */     int h = Gdx.graphics.getBackBufferHeight();
/*  42 */     return getFrameBufferTexture(0, 0, w, h);
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
/*     */   
/*     */   public static TextureRegion getFrameBufferTexture(int x, int y, int w, int h) {
/*  56 */     int potW = MathUtils.nextPowerOfTwo(w);
/*  57 */     int potH = MathUtils.nextPowerOfTwo(h);
/*     */     
/*  59 */     Pixmap pixmap = getFrameBufferPixmap(x, y, w, h);
/*  60 */     Pixmap potPixmap = new Pixmap(potW, potH, Pixmap.Format.RGBA8888);
/*  61 */     potPixmap.drawPixmap(pixmap, 0, 0);
/*  62 */     Texture texture = new Texture(potPixmap);
/*  63 */     TextureRegion textureRegion = new TextureRegion(texture, 0, h, w, -h);
/*  64 */     potPixmap.dispose();
/*  65 */     pixmap.dispose();
/*     */     
/*  67 */     return textureRegion;
/*     */   }
/*     */   
/*     */   public static Pixmap getFrameBufferPixmap(int x, int y, int w, int h) {
/*  71 */     Gdx.gl.glPixelStorei(3333, 1);
/*     */     
/*  73 */     Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
/*  74 */     ByteBuffer pixels = pixmap.getPixels();
/*  75 */     Gdx.gl.glReadPixels(x, y, w, h, 6408, 5121, pixels);
/*     */     
/*  77 */     return pixmap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getFrameBufferPixels(boolean flipY) {
/*  87 */     int w = Gdx.graphics.getBackBufferWidth();
/*  88 */     int h = Gdx.graphics.getBackBufferHeight();
/*  89 */     return getFrameBufferPixels(0, 0, w, h, flipY);
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
/*     */   public static byte[] getFrameBufferPixels(int x, int y, int w, int h, boolean flipY) {
/* 101 */     Gdx.gl.glPixelStorei(3333, 1);
/* 102 */     ByteBuffer pixels = BufferUtils.newByteBuffer(w * h * 4);
/* 103 */     Gdx.gl.glReadPixels(x, y, w, h, 6408, 5121, pixels);
/* 104 */     int numBytes = w * h * 4;
/* 105 */     byte[] lines = new byte[numBytes];
/* 106 */     if (flipY) {
/* 107 */       int numBytesPerLine = w * 4;
/* 108 */       for (int i = 0; i < h; i++) {
/* 109 */         pixels.position((h - i - 1) * numBytesPerLine);
/* 110 */         pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
/*     */       } 
/*     */     } else {
/* 113 */       pixels.clear();
/* 114 */       pixels.get(lines);
/*     */     } 
/* 116 */     return lines;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\ScreenUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */