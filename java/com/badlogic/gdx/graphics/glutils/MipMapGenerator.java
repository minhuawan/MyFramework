/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
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
/*     */ public class MipMapGenerator
/*     */ {
/*     */   private static boolean useHWMipMap = true;
/*     */   
/*     */   public static void setUseHardwareMipMap(boolean useHWMipMap) {
/*  36 */     MipMapGenerator.useHWMipMap = useHWMipMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generateMipMap(Pixmap pixmap, int textureWidth, int textureHeight) {
/*  43 */     generateMipMap(3553, pixmap, textureWidth, textureHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generateMipMap(int target, Pixmap pixmap, int textureWidth, int textureHeight) {
/*  49 */     if (!useHWMipMap) {
/*  50 */       generateMipMapCPU(target, pixmap, textureWidth, textureHeight);
/*     */       
/*     */       return;
/*     */     } 
/*  54 */     if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.WebGL || Gdx.app
/*  55 */       .getType() == Application.ApplicationType.iOS) {
/*  56 */       generateMipMapGLES20(target, pixmap);
/*     */     } else {
/*  58 */       generateMipMapDesktop(target, pixmap, textureWidth, textureHeight);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void generateMipMapGLES20(int target, Pixmap pixmap) {
/*  63 */     Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap
/*  64 */         .getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
/*  65 */     Gdx.gl20.glGenerateMipmap(target);
/*     */   }
/*     */   
/*     */   private static void generateMipMapDesktop(int target, Pixmap pixmap, int textureWidth, int textureHeight) {
/*  69 */     if (Gdx.graphics.supportsExtension("GL_ARB_framebuffer_object") || Gdx.graphics
/*  70 */       .supportsExtension("GL_EXT_framebuffer_object") || Gdx.gl30 != null) {
/*  71 */       Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap
/*  72 */           .getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
/*  73 */       Gdx.gl20.glGenerateMipmap(target);
/*     */     } else {
/*  75 */       generateMipMapCPU(target, pixmap, textureWidth, textureHeight);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void generateMipMapCPU(int target, Pixmap pixmap, int textureWidth, int textureHeight) {
/*  80 */     Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap
/*  81 */         .getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
/*  82 */     if (Gdx.gl20 == null && textureWidth != textureHeight)
/*  83 */       throw new GdxRuntimeException("texture width and height must be square when using mipmapping."); 
/*  84 */     int width = pixmap.getWidth() / 2;
/*  85 */     int height = pixmap.getHeight() / 2;
/*  86 */     int level = 1;
/*  87 */     Pixmap.Blending blending = Pixmap.getBlending();
/*  88 */     Pixmap.setBlending(Pixmap.Blending.None);
/*  89 */     while (width > 0 && height > 0) {
/*  90 */       Pixmap tmp = new Pixmap(width, height, pixmap.getFormat());
/*  91 */       tmp.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, width, height);
/*  92 */       if (level > 1) pixmap.dispose(); 
/*  93 */       pixmap = tmp;
/*     */       
/*  95 */       Gdx.gl.glTexImage2D(target, level, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap
/*  96 */           .getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
/*     */       
/*  98 */       width = pixmap.getWidth() / 2;
/*  99 */       height = pixmap.getHeight() / 2;
/* 100 */       level++;
/*     */     } 
/* 102 */     Pixmap.setBlending(blending);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\MipMapGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */