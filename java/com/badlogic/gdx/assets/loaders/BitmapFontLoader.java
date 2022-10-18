/*     */ package com.badlogic.gdx.assets.loaders;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class BitmapFontLoader
/*     */   extends AsynchronousAssetLoader<BitmapFont, BitmapFontLoader.BitmapFontParameter>
/*     */ {
/*     */   BitmapFont.BitmapFontData data;
/*     */   
/*     */   public BitmapFontLoader(FileHandleResolver resolver) {
/*  41 */     super(resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, BitmapFontParameter parameter) {
/*  48 */     Array<AssetDescriptor> deps = new Array();
/*  49 */     if (parameter != null && parameter.bitmapFontData != null) {
/*  50 */       this.data = parameter.bitmapFontData;
/*  51 */       return deps;
/*     */     } 
/*     */     
/*  54 */     this.data = new BitmapFont.BitmapFontData(file, (parameter != null) ? parameter.flip : false);
/*  55 */     if (parameter != null && parameter.atlasName != null) {
/*  56 */       deps.add(new AssetDescriptor(parameter.atlasName, TextureAtlas.class));
/*     */     } else {
/*  58 */       for (int i = 0; i < (this.data.getImagePaths()).length; i++) {
/*  59 */         String path = this.data.getImagePath(i);
/*  60 */         FileHandle resolved = resolve(path);
/*     */         
/*  62 */         TextureLoader.TextureParameter textureParams = new TextureLoader.TextureParameter();
/*     */         
/*  64 */         if (parameter != null) {
/*  65 */           textureParams.genMipMaps = parameter.genMipMaps;
/*  66 */           textureParams.minFilter = parameter.minFilter;
/*  67 */           textureParams.magFilter = parameter.magFilter;
/*     */         } 
/*     */         
/*  70 */         AssetDescriptor descriptor = new AssetDescriptor(resolved, Texture.class, textureParams);
/*  71 */         deps.add(descriptor);
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     return deps;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter) {}
/*     */ 
/*     */   
/*     */   public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter) {
/*  84 */     if (parameter != null && parameter.atlasName != null) {
/*  85 */       TextureAtlas atlas = (TextureAtlas)manager.get(parameter.atlasName, TextureAtlas.class);
/*  86 */       String name = file.sibling(this.data.imagePaths[0]).nameWithoutExtension().toString();
/*  87 */       TextureAtlas.AtlasRegion region = atlas.findRegion(name);
/*     */       
/*  89 */       if (region == null)
/*  90 */         throw new GdxRuntimeException("Could not find font region " + name + " in atlas " + parameter.atlasName); 
/*  91 */       return new BitmapFont(file, (TextureRegion)region);
/*     */     } 
/*  93 */     int n = (this.data.getImagePaths()).length;
/*  94 */     Array<TextureRegion> regs = new Array(n);
/*  95 */     for (int i = 0; i < n; i++) {
/*  96 */       regs.add(new TextureRegion((Texture)manager.get(this.data.getImagePath(i), Texture.class)));
/*     */     }
/*  98 */     return new BitmapFont(this.data, regs, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BitmapFontParameter
/*     */     extends AssetLoaderParameters<BitmapFont>
/*     */   {
/*     */     public boolean flip = false;
/*     */ 
/*     */     
/*     */     public boolean genMipMaps = false;
/*     */ 
/*     */     
/* 113 */     public Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
/*     */ 
/*     */     
/* 116 */     public Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
/*     */ 
/*     */ 
/*     */     
/* 120 */     public BitmapFont.BitmapFontData bitmapFontData = null;
/*     */ 
/*     */ 
/*     */     
/* 124 */     public String atlasName = null;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\BitmapFontLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */