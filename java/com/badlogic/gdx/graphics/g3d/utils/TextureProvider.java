/*    */ package com.badlogic.gdx.graphics.g3d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.graphics.Texture;
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
/*    */ public interface TextureProvider
/*    */ {
/*    */   Texture load(String paramString);
/*    */   
/*    */   public static class FileTextureProvider
/*    */     implements TextureProvider
/*    */   {
/*    */     private Texture.TextureFilter minFilter;
/*    */     private Texture.TextureFilter magFilter;
/*    */     private Texture.TextureWrap uWrap;
/*    */     private Texture.TextureWrap vWrap;
/*    */     private boolean useMipMaps;
/*    */     
/*    */     public FileTextureProvider() {
/* 36 */       this.minFilter = this.magFilter = Texture.TextureFilter.Linear;
/* 37 */       this.uWrap = this.vWrap = Texture.TextureWrap.Repeat;
/* 38 */       this.useMipMaps = false;
/*    */     }
/*    */ 
/*    */     
/*    */     public FileTextureProvider(Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, Texture.TextureWrap uWrap, Texture.TextureWrap vWrap, boolean useMipMaps) {
/* 43 */       this.minFilter = minFilter;
/* 44 */       this.magFilter = magFilter;
/* 45 */       this.uWrap = uWrap;
/* 46 */       this.vWrap = vWrap;
/* 47 */       this.useMipMaps = useMipMaps;
/*    */     }
/*    */ 
/*    */     
/*    */     public Texture load(String fileName) {
/* 52 */       Texture result = new Texture(Gdx.files.internal(fileName), this.useMipMaps);
/* 53 */       result.setFilter(this.minFilter, this.magFilter);
/* 54 */       result.setWrap(this.uWrap, this.vWrap);
/* 55 */       return result;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class AssetTextureProvider implements TextureProvider {
/*    */     public final AssetManager assetManager;
/*    */     
/*    */     public AssetTextureProvider(AssetManager assetManager) {
/* 63 */       this.assetManager = assetManager;
/*    */     }
/*    */ 
/*    */     
/*    */     public Texture load(String fileName) {
/* 68 */       return (Texture)this.assetManager.get(fileName, Texture.class);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\TextureProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */