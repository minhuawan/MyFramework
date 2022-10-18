/*     */ package com.badlogic.gdx.graphics.g3d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.GLTexture;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.IntBuffer;
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
/*     */ public final class DefaultTextureBinder
/*     */   implements TextureBinder
/*     */ {
/*     */   public static final int ROUNDROBIN = 0;
/*     */   public static final int WEIGHTED = 1;
/*     */   public static final int MAX_GLES_UNITS = 32;
/*     */   private final int offset;
/*     */   private final int count;
/*     */   private final int reuseWeight;
/*     */   private final GLTexture[] textures;
/*     */   private final int[] weights;
/*     */   private final int method;
/*     */   private boolean reused;
/*  50 */   private int reuseCount = 0;
/*  51 */   private int bindCount = 0; private final TextureDescriptor tempDesc;
/*     */   private int currentTexture;
/*     */   
/*     */   public DefaultTextureBinder(int method) {
/*  55 */     this(method, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultTextureBinder(int method, int offset) {
/*  60 */     this(method, offset, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultTextureBinder(int method, int offset, int count) {
/*  65 */     this(method, offset, count, 10);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getMaxTextureUnits() {
/*  82 */     IntBuffer buffer = BufferUtils.newIntBuffer(16);
/*  83 */     Gdx.gl.glGetIntegerv(34930, buffer);
/*  84 */     return buffer.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  89 */     for (int i = 0; i < this.count; i++) {
/*  90 */       this.textures[i] = null;
/*  91 */       if (this.weights != null) this.weights[i] = 0;
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void end() {
/* 102 */     Gdx.gl.glActiveTexture(33984);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int bind(TextureDescriptor textureDesc) {
/* 107 */     return bindTexture(textureDesc, false);
/*     */   }
/*     */   
/* 110 */   public DefaultTextureBinder(int method, int offset, int count, int reuseWeight) { this.tempDesc = new TextureDescriptor<GLTexture>();
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
/* 147 */     this.currentTexture = 0; int max = Math.min(getMaxTextureUnits(), 32); if (count < 0)
/*     */       count = max - offset;  if (offset < 0 || count < 0 || offset + count > max || reuseWeight < 1)
/*     */       throw new GdxRuntimeException("Illegal arguments");  this.method = method; this.offset = offset; this.count = count; this.textures = new GLTexture[count]; this.reuseWeight = reuseWeight; this.weights = (method == 1) ? new int[count] : null; }
/* 150 */   public final int bind(GLTexture texture) { this.tempDesc.set(texture, null, null, null, null); return bindTexture(this.tempDesc, false); } private final int bindTextureRoundRobin(GLTexture texture) { for (int i = 0; i < this.count; i++) {
/* 151 */       int idx = (this.currentTexture + i) % this.count;
/* 152 */       if (this.textures[idx] == texture) {
/* 153 */         this.reused = true;
/* 154 */         return idx;
/*     */       } 
/*     */     } 
/* 157 */     this.currentTexture = (this.currentTexture + 1) % this.count;
/* 158 */     this.textures[this.currentTexture] = texture;
/* 159 */     texture.bind(this.offset + this.currentTexture);
/* 160 */     return this.currentTexture; }
/*     */   private final int bindTexture(TextureDescriptor textureDesc, boolean rebind) { int idx, result; T t = textureDesc.texture; this.reused = false; switch (this.method) { case 0: result = this.offset + (idx = bindTextureRoundRobin((GLTexture)t)); break;
/*     */       case 1: result = this.offset + (idx = bindTextureWeighted((GLTexture)t)); break;
/*     */       default:
/* 164 */         return -1; }  if (this.reused) { this.reuseCount++; if (rebind) { t.bind(result); } else { Gdx.gl.glActiveTexture(33984 + result); }  } else { this.bindCount++; }  t.unsafeSetWrap(textureDesc.uWrap, textureDesc.vWrap); t.unsafeSetFilter(textureDesc.minFilter, textureDesc.magFilter); return result; } private final int bindTextureWeighted(GLTexture texture) { int result = -1;
/* 165 */     int weight = this.weights[0];
/* 166 */     int windex = 0;
/* 167 */     for (int i = 0; i < this.count; i++) {
/* 168 */       if (this.textures[i] == texture)
/* 169 */       { result = i;
/* 170 */         this.weights[i] = this.weights[i] + this.reuseWeight; }
/* 171 */       else { this.weights[i] = this.weights[i] - 1; if (this.weights[i] < 0 || this.weights[i] - 1 < weight) {
/* 172 */           weight = this.weights[i];
/* 173 */           windex = i;
/*     */         }  }
/*     */     
/* 176 */     }  if (result < 0) {
/* 177 */       this.textures[windex] = texture;
/* 178 */       this.weights[windex] = 100;
/* 179 */       texture.bind(this.offset + (result = windex));
/*     */     } else {
/* 181 */       this.reused = true;
/* 182 */     }  return result; }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getBindCount() {
/* 187 */     return this.bindCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getReuseCount() {
/* 192 */     return this.reuseCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void resetCounts() {
/* 197 */     this.bindCount = this.reuseCount = 0;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\DefaultTextureBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */