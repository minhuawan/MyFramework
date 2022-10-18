/*     */ package com.badlogic.gdx.assets.loaders;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.TextureData;
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
/*     */ 
/*     */ public class TextureLoader
/*     */   extends AsynchronousAssetLoader<Texture, TextureLoader.TextureParameter>
/*     */ {
/*     */   public static class TextureLoaderInfo
/*     */   {
/*     */     String filename;
/*     */     TextureData data;
/*     */     Texture texture;
/*     */   }
/*  48 */   TextureLoaderInfo info = new TextureLoaderInfo();
/*     */   
/*     */   public TextureLoader(FileHandleResolver resolver) {
/*  51 */     super(resolver);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextureParameter parameter) {
/*  56 */     this.info.filename = fileName;
/*  57 */     if (parameter == null || parameter.textureData == null) {
/*  58 */       Pixmap pixmap = null;
/*  59 */       Pixmap.Format format = null;
/*  60 */       boolean genMipMaps = false;
/*  61 */       this.info.texture = null;
/*     */       
/*  63 */       if (parameter != null) {
/*  64 */         format = parameter.format;
/*  65 */         genMipMaps = parameter.genMipMaps;
/*  66 */         this.info.texture = parameter.texture;
/*     */       } 
/*     */       
/*  69 */       this.info.data = TextureData.Factory.loadFromFile(file, format, genMipMaps);
/*     */     } else {
/*  71 */       this.info.data = parameter.textureData;
/*  72 */       this.info.texture = parameter.texture;
/*     */     } 
/*  74 */     if (!this.info.data.isPrepared()) this.info.data.prepare();
/*     */   
/*     */   }
/*     */   
/*     */   public Texture loadSync(AssetManager manager, String fileName, FileHandle file, TextureParameter parameter) {
/*  79 */     if (this.info == null) return null; 
/*  80 */     Texture texture = this.info.texture;
/*  81 */     if (texture != null) {
/*  82 */       texture.load(this.info.data);
/*     */     } else {
/*  84 */       texture = new Texture(this.info.data);
/*     */     } 
/*  86 */     if (parameter != null) {
/*  87 */       texture.setFilter(parameter.minFilter, parameter.magFilter);
/*  88 */       texture.setWrap(parameter.wrapU, parameter.wrapV);
/*     */     } 
/*  90 */     return texture;
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureParameter parameter) {
/*  95 */     return null;
/*     */   }
/*     */   
/*     */   public static class TextureParameter
/*     */     extends AssetLoaderParameters<Texture> {
/* 100 */     public Pixmap.Format format = null;
/*     */     
/*     */     public boolean genMipMaps = false;
/*     */     
/* 104 */     public Texture texture = null;
/*     */     
/* 106 */     public TextureData textureData = null;
/* 107 */     public Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
/* 108 */     public Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
/* 109 */     public Texture.TextureWrap wrapU = Texture.TextureWrap.ClampToEdge;
/* 110 */     public Texture.TextureWrap wrapV = Texture.TextureWrap.ClampToEdge;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\TextureLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */