/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.glutils.MipMapGenerator;
/*     */ import com.badlogic.gdx.utils.Disposable;
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
/*     */ public abstract class GLTexture
/*     */   implements Disposable
/*     */ {
/*     */   public final int glTarget;
/*     */   protected int glHandle;
/*  34 */   protected Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
/*  35 */   protected Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
/*  36 */   protected Texture.TextureWrap uWrap = Texture.TextureWrap.ClampToEdge;
/*  37 */   protected Texture.TextureWrap vWrap = Texture.TextureWrap.ClampToEdge;
/*     */ 
/*     */   
/*     */   public abstract int getWidth();
/*     */ 
/*     */   
/*     */   public abstract int getHeight();
/*     */ 
/*     */   
/*     */   public abstract int getDepth();
/*     */ 
/*     */   
/*     */   public GLTexture(int glTarget) {
/*  50 */     this(glTarget, Gdx.gl.glGenTexture());
/*     */   }
/*     */   
/*     */   public GLTexture(int glTarget, int glHandle) {
/*  54 */     this.glTarget = glTarget;
/*  55 */     this.glHandle = glHandle;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean isManaged();
/*     */ 
/*     */   
/*     */   protected abstract void reload();
/*     */ 
/*     */   
/*     */   public void bind() {
/*  66 */     Gdx.gl.glBindTexture(this.glTarget, this.glHandle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind(int unit) {
/*  72 */     Gdx.gl.glActiveTexture(33984 + unit);
/*  73 */     Gdx.gl.glBindTexture(this.glTarget, this.glHandle);
/*     */   }
/*     */ 
/*     */   
/*     */   public Texture.TextureFilter getMinFilter() {
/*  78 */     return this.minFilter;
/*     */   }
/*     */ 
/*     */   
/*     */   public Texture.TextureFilter getMagFilter() {
/*  83 */     return this.magFilter;
/*     */   }
/*     */ 
/*     */   
/*     */   public Texture.TextureWrap getUWrap() {
/*  88 */     return this.uWrap;
/*     */   }
/*     */ 
/*     */   
/*     */   public Texture.TextureWrap getVWrap() {
/*  93 */     return this.vWrap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTextureObjectHandle() {
/*  98 */     return this.glHandle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unsafeSetWrap(Texture.TextureWrap u, Texture.TextureWrap v) {
/* 105 */     unsafeSetWrap(u, v, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unsafeSetWrap(Texture.TextureWrap u, Texture.TextureWrap v, boolean force) {
/* 113 */     if (u != null && (force || this.uWrap != u)) {
/* 114 */       Gdx.gl.glTexParameterf(this.glTarget, 10242, u.getGLEnum());
/* 115 */       this.uWrap = u;
/*     */     } 
/* 117 */     if (v != null && (force || this.vWrap != v)) {
/* 118 */       Gdx.gl.glTexParameterf(this.glTarget, 10243, v.getGLEnum());
/* 119 */       this.vWrap = v;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWrap(Texture.TextureWrap u, Texture.TextureWrap v) {
/* 127 */     this.uWrap = u;
/* 128 */     this.vWrap = v;
/* 129 */     bind();
/* 130 */     Gdx.gl.glTexParameterf(this.glTarget, 10242, u.getGLEnum());
/* 131 */     Gdx.gl.glTexParameterf(this.glTarget, 10243, v.getGLEnum());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unsafeSetFilter(Texture.TextureFilter minFilter, Texture.TextureFilter magFilter) {
/* 138 */     unsafeSetFilter(minFilter, magFilter, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unsafeSetFilter(Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, boolean force) {
/* 146 */     if (minFilter != null && (force || this.minFilter != minFilter)) {
/* 147 */       Gdx.gl.glTexParameterf(this.glTarget, 10241, minFilter.getGLEnum());
/* 148 */       this.minFilter = minFilter;
/*     */     } 
/* 150 */     if (magFilter != null && (force || this.magFilter != magFilter)) {
/* 151 */       Gdx.gl.glTexParameterf(this.glTarget, 10240, magFilter.getGLEnum());
/* 152 */       this.magFilter = magFilter;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilter(Texture.TextureFilter minFilter, Texture.TextureFilter magFilter) {
/* 160 */     this.minFilter = minFilter;
/* 161 */     this.magFilter = magFilter;
/* 162 */     bind();
/* 163 */     Gdx.gl.glTexParameterf(this.glTarget, 10241, minFilter.getGLEnum());
/* 164 */     Gdx.gl.glTexParameterf(this.glTarget, 10240, magFilter.getGLEnum());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void delete() {
/* 169 */     if (this.glHandle != 0) {
/* 170 */       Gdx.gl.glDeleteTexture(this.glHandle);
/* 171 */       this.glHandle = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 177 */     delete();
/*     */   }
/*     */   
/*     */   protected static void uploadImageData(int target, TextureData data) {
/* 181 */     uploadImageData(target, data, 0);
/*     */   }
/*     */   
/*     */   public static void uploadImageData(int target, TextureData data, int miplevel) {
/* 185 */     if (data == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 190 */     if (!data.isPrepared()) data.prepare();
/*     */     
/* 192 */     TextureData.TextureDataType type = data.getType();
/* 193 */     if (type == TextureData.TextureDataType.Custom) {
/* 194 */       data.consumeCustomData(target);
/*     */       
/*     */       return;
/*     */     } 
/* 198 */     Pixmap pixmap = data.consumePixmap();
/* 199 */     boolean disposePixmap = data.disposePixmap();
/* 200 */     if (data.getFormat() != pixmap.getFormat()) {
/* 201 */       Pixmap tmp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), data.getFormat());
/* 202 */       Pixmap.Blending blend = Pixmap.getBlending();
/* 203 */       Pixmap.setBlending(Pixmap.Blending.None);
/* 204 */       tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
/* 205 */       Pixmap.setBlending(blend);
/* 206 */       if (data.disposePixmap()) {
/* 207 */         pixmap.dispose();
/*     */       }
/* 209 */       pixmap = tmp;
/* 210 */       disposePixmap = true;
/*     */     } 
/*     */     
/* 213 */     Gdx.gl.glPixelStorei(3317, 1);
/* 214 */     if (data.useMipMaps()) {
/* 215 */       MipMapGenerator.generateMipMap(target, pixmap, pixmap.getWidth(), pixmap.getHeight());
/*     */     } else {
/* 217 */       Gdx.gl.glTexImage2D(target, miplevel, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap
/* 218 */           .getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
/*     */     } 
/* 220 */     if (disposePixmap) pixmap.dispose(); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\GLTexture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */