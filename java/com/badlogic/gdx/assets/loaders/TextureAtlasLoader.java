/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TextureAtlasLoader
/*    */   extends SynchronousAssetLoader<TextureAtlas, TextureAtlasLoader.TextureAtlasParameter>
/*    */ {
/*    */   TextureAtlas.TextureAtlasData data;
/*    */   
/*    */   public TextureAtlasLoader(FileHandleResolver resolver) {
/* 36 */     super(resolver);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TextureAtlas load(AssetManager assetManager, String fileName, FileHandle file, TextureAtlasParameter parameter) {
/* 43 */     for (TextureAtlas.TextureAtlasData.Page page : this.data.getPages()) {
/* 44 */       Texture texture = (Texture)assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
/* 45 */       page.texture = texture;
/*    */     } 
/*    */     
/* 48 */     return new TextureAtlas(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle atlasFile, TextureAtlasParameter parameter) {
/* 53 */     FileHandle imgDir = atlasFile.parent();
/*    */     
/* 55 */     if (parameter != null) {
/* 56 */       this.data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, parameter.flip);
/*    */     } else {
/* 58 */       this.data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, false);
/*    */     } 
/*    */     
/* 61 */     Array<AssetDescriptor> dependencies = new Array();
/* 62 */     for (TextureAtlas.TextureAtlasData.Page page : this.data.getPages()) {
/* 63 */       TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
/* 64 */       params.format = page.format;
/* 65 */       params.genMipMaps = page.useMipMaps;
/* 66 */       params.minFilter = page.minFilter;
/* 67 */       params.magFilter = page.magFilter;
/* 68 */       dependencies.add(new AssetDescriptor(page.textureFile, Texture.class, params));
/*    */     } 
/* 70 */     return dependencies;
/*    */   }
/*    */   
/*    */   public static class TextureAtlasParameter
/*    */     extends AssetLoaderParameters<TextureAtlas>
/*    */   {
/*    */     public boolean flip = false;
/*    */     
/*    */     public TextureAtlasParameter() {}
/*    */     
/*    */     public TextureAtlasParameter(boolean flip) {
/* 81 */       this.flip = flip;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\TextureAtlasLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */