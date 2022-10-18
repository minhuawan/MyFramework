/*     */ package com.badlogic.gdx.graphics.g3d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
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
/*     */ public class RenderContext
/*     */ {
/*     */   public final TextureBinder textureBinder;
/*     */   private boolean blending;
/*     */   private int blendSFactor;
/*     */   private int blendDFactor;
/*     */   private int depthFunc;
/*     */   private float depthRangeNear;
/*     */   private float depthRangeFar;
/*     */   private boolean depthMask;
/*     */   private int cullFace;
/*     */   
/*     */   public RenderContext(TextureBinder textures) {
/*  39 */     this.textureBinder = textures;
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  44 */     Gdx.gl.glDisable(2929);
/*  45 */     this.depthFunc = 0;
/*  46 */     Gdx.gl.glDepthMask(true);
/*  47 */     this.depthMask = true;
/*  48 */     Gdx.gl.glDisable(3042);
/*  49 */     this.blending = false;
/*  50 */     Gdx.gl.glDisable(2884);
/*  51 */     this.cullFace = this.blendSFactor = this.blendDFactor = 0;
/*  52 */     this.textureBinder.begin();
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/*  57 */     if (this.depthFunc != 0) Gdx.gl.glDisable(2929); 
/*  58 */     if (!this.depthMask) Gdx.gl.glDepthMask(true); 
/*  59 */     if (this.blending) Gdx.gl.glDisable(3042); 
/*  60 */     if (this.cullFace > 0) Gdx.gl.glDisable(2884); 
/*  61 */     this.textureBinder.end();
/*     */   }
/*     */   
/*     */   public void setDepthMask(boolean depthMask) {
/*  65 */     if (this.depthMask != depthMask) Gdx.gl.glDepthMask(this.depthMask = depthMask); 
/*     */   }
/*     */   
/*     */   public void setDepthTest(int depthFunction) {
/*  69 */     setDepthTest(depthFunction, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void setDepthTest(int depthFunction, float depthRangeNear, float depthRangeFar) {
/*  73 */     boolean wasEnabled = (this.depthFunc != 0);
/*  74 */     boolean enabled = (depthFunction != 0);
/*  75 */     if (this.depthFunc != depthFunction) {
/*  76 */       this.depthFunc = depthFunction;
/*  77 */       if (enabled) {
/*  78 */         Gdx.gl.glEnable(2929);
/*  79 */         Gdx.gl.glDepthFunc(depthFunction);
/*     */       } else {
/*  81 */         Gdx.gl.glDisable(2929);
/*     */       } 
/*  83 */     }  if (enabled) {
/*  84 */       if (!wasEnabled || this.depthFunc != depthFunction) Gdx.gl.glDepthFunc(this.depthFunc = depthFunction); 
/*  85 */       if (!wasEnabled || this.depthRangeNear != depthRangeNear || this.depthRangeFar != depthRangeFar)
/*  86 */         Gdx.gl.glDepthRangef(this.depthRangeNear = depthRangeNear, this.depthRangeFar = depthRangeFar); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setBlending(boolean enabled, int sFactor, int dFactor) {
/*  91 */     if (enabled != this.blending) {
/*  92 */       this.blending = enabled;
/*  93 */       if (enabled) {
/*  94 */         Gdx.gl.glEnable(3042);
/*     */       } else {
/*  96 */         Gdx.gl.glDisable(3042);
/*     */       } 
/*  98 */     }  if (enabled && (this.blendSFactor != sFactor || this.blendDFactor != dFactor)) {
/*  99 */       Gdx.gl.glBlendFunc(sFactor, dFactor);
/* 100 */       this.blendSFactor = sFactor;
/* 101 */       this.blendDFactor = dFactor;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setCullFace(int face) {
/* 106 */     if (face != this.cullFace) {
/* 107 */       this.cullFace = face;
/* 108 */       if (face == 1028 || face == 1029 || face == 1032) {
/* 109 */         Gdx.gl.glEnable(2884);
/* 110 */         Gdx.gl.glCullFace(face);
/*     */       } else {
/* 112 */         Gdx.gl.glDisable(2884);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\RenderContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */