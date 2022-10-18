/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.TextureData;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.FloatBuffer;
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
/*     */ public class FloatTextureData
/*     */   implements TextureData
/*     */ {
/*  33 */   int width = 0;
/*  34 */   int height = 0;
/*     */   boolean isPrepared = false;
/*     */   FloatBuffer buffer;
/*     */   
/*     */   public FloatTextureData(int w, int h) {
/*  39 */     this.width = w;
/*  40 */     this.height = h;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureData.TextureDataType getType() {
/*  45 */     return TextureData.TextureDataType.Custom;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/*  50 */     return this.isPrepared;
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() {
/*  55 */     if (this.isPrepared) throw new GdxRuntimeException("Already prepared"); 
/*  56 */     this.buffer = BufferUtils.newFloatBuffer(this.width * this.height * 4);
/*  57 */     this.isPrepared = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeCustomData(int target) {
/*  62 */     if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS || Gdx.app
/*  63 */       .getType() == Application.ApplicationType.WebGL) {
/*     */       
/*  65 */       if (!Gdx.graphics.supportsExtension("OES_texture_float")) {
/*  66 */         throw new GdxRuntimeException("Extension OES_texture_float not supported!");
/*     */       }
/*     */ 
/*     */       
/*  70 */       Gdx.gl.glTexImage2D(target, 0, 6408, this.width, this.height, 0, 6408, 5126, this.buffer);
/*     */     } else {
/*     */       
/*  73 */       if (!Gdx.graphics.supportsExtension("GL_ARB_texture_float")) {
/*  74 */         throw new GdxRuntimeException("Extension GL_ARB_texture_float not supported!");
/*     */       }
/*  76 */       int GL_RGBA32F = 34836;
/*     */ 
/*     */ 
/*     */       
/*  80 */       Gdx.gl.glTexImage2D(target, 0, 34836, this.width, this.height, 0, 6408, 5126, this.buffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap consumePixmap() {
/*  86 */     throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean disposePixmap() {
/*  91 */     throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  96 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 101 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap.Format getFormat() {
/* 106 */     return Pixmap.Format.RGBA8888;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useMipMaps() {
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 116 */     return true;
/*     */   }
/*     */   
/*     */   public FloatBuffer getBuffer() {
/* 120 */     return this.buffer;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\FloatTextureData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */