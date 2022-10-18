/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.TextureData;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class GLOnlyTextureData
/*     */   implements TextureData
/*     */ {
/*  31 */   int width = 0;
/*  32 */   int height = 0;
/*     */   
/*     */   boolean isPrepared = false;
/*     */   
/*  36 */   int mipLevel = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   int internalFormat;
/*     */ 
/*     */ 
/*     */   
/*     */   int format;
/*     */ 
/*     */   
/*     */   int type;
/*     */ 
/*     */ 
/*     */   
/*     */   public GLOnlyTextureData(int width, int height, int mipMapLevel, int internalFormat, int format, int type) {
/*  52 */     this.width = width;
/*  53 */     this.height = height;
/*  54 */     this.mipLevel = mipMapLevel;
/*  55 */     this.internalFormat = internalFormat;
/*  56 */     this.format = format;
/*  57 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureData.TextureDataType getType() {
/*  62 */     return TextureData.TextureDataType.Custom;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/*  67 */     return this.isPrepared;
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() {
/*  72 */     if (this.isPrepared) throw new GdxRuntimeException("Already prepared"); 
/*  73 */     this.isPrepared = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeCustomData(int target) {
/*  78 */     Gdx.gl.glTexImage2D(target, this.mipLevel, this.internalFormat, this.width, this.height, 0, this.format, this.type, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap consumePixmap() {
/*  83 */     throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean disposePixmap() {
/*  88 */     throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  93 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/*  98 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap.Format getFormat() {
/* 103 */     return Pixmap.Format.RGBA8888;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useMipMaps() {
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 113 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\GLOnlyTextureData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */