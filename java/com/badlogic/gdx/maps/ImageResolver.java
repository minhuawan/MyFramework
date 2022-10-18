/*    */ package com.badlogic.gdx.maps;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*    */ import com.badlogic.gdx.utils.ObjectMap;
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
/*    */ public interface ImageResolver
/*    */ {
/*    */   TextureRegion getImage(String paramString);
/*    */   
/*    */   public static class DirectImageResolver
/*    */     implements ImageResolver
/*    */   {
/*    */     private final ObjectMap<String, Texture> images;
/*    */     
/*    */     public DirectImageResolver(ObjectMap<String, Texture> images) {
/* 36 */       this.images = images;
/*    */     }
/*    */ 
/*    */     
/*    */     public TextureRegion getImage(String name) {
/* 41 */       return new TextureRegion((Texture)this.images.get(name));
/*    */     }
/*    */   }
/*    */   
/*    */   public static class AssetManagerImageResolver implements ImageResolver {
/*    */     private final AssetManager assetManager;
/*    */     
/*    */     public AssetManagerImageResolver(AssetManager assetManager) {
/* 49 */       this.assetManager = assetManager;
/*    */     }
/*    */ 
/*    */     
/*    */     public TextureRegion getImage(String name) {
/* 54 */       return new TextureRegion((Texture)this.assetManager.get(name, Texture.class));
/*    */     }
/*    */   }
/*    */   
/*    */   public static class TextureAtlasImageResolver implements ImageResolver {
/*    */     private final TextureAtlas atlas;
/*    */     
/*    */     public TextureAtlasImageResolver(TextureAtlas atlas) {
/* 62 */       this.atlas = atlas;
/*    */     }
/*    */ 
/*    */     
/*    */     public TextureRegion getImage(String name) {
/* 67 */       return (TextureRegion)this.atlas.findRegion(name);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\ImageResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */