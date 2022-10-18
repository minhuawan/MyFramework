/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Cubemap;
/*     */ import com.badlogic.gdx.graphics.GL20;
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
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
/*     */ public class FrameBufferCubemap
/*     */   extends GLFrameBuffer<Cubemap>
/*     */ {
/*     */   private int currentSide;
/*     */   
/*     */   public FrameBufferCubemap(Pixmap.Format format, int width, int height, boolean hasDepth) {
/*  74 */     this(format, width, height, hasDepth, false);
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
/*     */   public FrameBufferCubemap(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
/*  87 */     super(format, width, height, hasDepth, hasStencil);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Cubemap createColorTexture() {
/*  92 */     int glFormat = Pixmap.Format.toGlFormat(this.format);
/*  93 */     int glType = Pixmap.Format.toGlType(this.format);
/*  94 */     GLOnlyTextureData data = new GLOnlyTextureData(this.width, this.height, 0, glFormat, glFormat, glType);
/*  95 */     Cubemap result = new Cubemap(data, data, data, data, data, data);
/*  96 */     result.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
/*  97 */     result.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
/*  98 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void disposeColorTexture(Cubemap colorTexture) {
/* 103 */     colorTexture.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void attachFrameBufferColorTexture() {
/* 108 */     GL20 gl = Gdx.gl20;
/* 109 */     int glHandle = this.colorTexture.getTextureObjectHandle();
/* 110 */     Cubemap.CubemapSide[] sides = Cubemap.CubemapSide.values();
/* 111 */     for (Cubemap.CubemapSide side : sides) {
/* 112 */       gl.glFramebufferTexture2D(36160, 36064, side.glEnum, glHandle, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind() {
/* 121 */     this.currentSide = -1;
/* 122 */     super.bind();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean nextSide() {
/* 128 */     if (this.currentSide > 5)
/* 129 */       throw new GdxRuntimeException("No remaining sides."); 
/* 130 */     if (this.currentSide == 5) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     this.currentSide++;
/* 135 */     bindSide(getSide());
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindSide(Cubemap.CubemapSide side) {
/* 142 */     Gdx.gl20.glFramebufferTexture2D(36160, 36064, side.glEnum, this.colorTexture
/* 143 */         .getTextureObjectHandle(), 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Cubemap.CubemapSide getSide() {
/* 148 */     return (this.currentSide < 0) ? null : Cubemap.CubemapSide.values()[this.currentSide];
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\FrameBufferCubemap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */