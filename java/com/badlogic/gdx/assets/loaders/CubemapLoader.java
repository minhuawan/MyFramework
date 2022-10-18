/*     */ package com.badlogic.gdx.assets.loaders;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Cubemap;
/*     */ import com.badlogic.gdx.graphics.CubemapData;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.glutils.KTXTextureData;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class CubemapLoader
/*     */   extends AsynchronousAssetLoader<Cubemap, CubemapLoader.CubemapParameter>
/*     */ {
/*     */   public static class CubemapLoaderInfo
/*     */   {
/*     */     String filename;
/*     */     CubemapData data;
/*     */     Cubemap cubemap;
/*     */   }
/*  49 */   CubemapLoaderInfo info = new CubemapLoaderInfo();
/*     */   
/*     */   public CubemapLoader(FileHandleResolver resolver) {
/*  52 */     super(resolver);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, CubemapParameter parameter) {
/*  57 */     this.info.filename = fileName;
/*  58 */     if (parameter == null || parameter.cubemapData == null) {
/*  59 */       Pixmap pixmap = null;
/*  60 */       Pixmap.Format format = null;
/*  61 */       boolean genMipMaps = false;
/*  62 */       this.info.cubemap = null;
/*     */       
/*  64 */       if (parameter != null) {
/*  65 */         format = parameter.format;
/*  66 */         this.info.cubemap = parameter.cubemap;
/*     */       } 
/*     */       
/*  69 */       if (fileName.contains(".ktx") || fileName.contains(".zktx")) {
/*  70 */         this.info.data = (CubemapData)new KTXTextureData(file, genMipMaps);
/*     */       }
/*     */     } else {
/*  73 */       this.info.data = parameter.cubemapData;
/*  74 */       this.info.cubemap = parameter.cubemap;
/*     */     } 
/*  76 */     if (!this.info.data.isPrepared()) this.info.data.prepare();
/*     */   
/*     */   }
/*     */   
/*     */   public Cubemap loadSync(AssetManager manager, String fileName, FileHandle file, CubemapParameter parameter) {
/*  81 */     if (this.info == null) return null; 
/*  82 */     Cubemap cubemap = this.info.cubemap;
/*  83 */     if (cubemap != null) {
/*  84 */       cubemap.load(this.info.data);
/*     */     } else {
/*  86 */       cubemap = new Cubemap(this.info.data);
/*     */     } 
/*  88 */     if (parameter != null) {
/*  89 */       cubemap.setFilter(parameter.minFilter, parameter.magFilter);
/*  90 */       cubemap.setWrap(parameter.wrapU, parameter.wrapV);
/*     */     } 
/*  92 */     return cubemap;
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CubemapParameter parameter) {
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   public static class CubemapParameter
/*     */     extends AssetLoaderParameters<Cubemap> {
/* 102 */     public Pixmap.Format format = null;
/*     */     
/* 104 */     public Cubemap cubemap = null;
/*     */     
/* 106 */     public CubemapData cubemapData = null;
/* 107 */     public Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
/* 108 */     public Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
/* 109 */     public Texture.TextureWrap wrapU = Texture.TextureWrap.ClampToEdge;
/* 110 */     public Texture.TextureWrap wrapV = Texture.TextureWrap.ClampToEdge;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\CubemapLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */