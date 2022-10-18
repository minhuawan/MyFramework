/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*    */ import com.badlogic.gdx.scenes.scene2d.ui.Skin;
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SkinLoader
/*    */   extends AsynchronousAssetLoader<Skin, SkinLoader.SkinParameter>
/*    */ {
/*    */   public SkinLoader(FileHandleResolver resolver) {
/* 40 */     super(resolver);
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkinParameter parameter) {
/* 45 */     Array<AssetDescriptor> deps = new Array();
/* 46 */     if (parameter == null || parameter.textureAtlasPath == null)
/* 47 */     { deps.add(new AssetDescriptor(file.pathWithoutExtension() + ".atlas", TextureAtlas.class)); }
/* 48 */     else if (parameter.textureAtlasPath != null) { deps.add(new AssetDescriptor(parameter.textureAtlasPath, TextureAtlas.class)); }
/* 49 */      return deps;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {}
/*    */ 
/*    */   
/*    */   public Skin loadSync(AssetManager manager, String fileName, FileHandle file, SkinParameter parameter) {
/* 58 */     String textureAtlasPath = file.pathWithoutExtension() + ".atlas";
/* 59 */     ObjectMap<String, Object> resources = null;
/* 60 */     if (parameter != null) {
/* 61 */       if (parameter.textureAtlasPath != null) {
/* 62 */         textureAtlasPath = parameter.textureAtlasPath;
/*    */       }
/* 64 */       if (parameter.resources != null) {
/* 65 */         resources = parameter.resources;
/*    */       }
/*    */     } 
/* 68 */     TextureAtlas atlas = (TextureAtlas)manager.get(textureAtlasPath, TextureAtlas.class);
/* 69 */     Skin skin = new Skin(atlas);
/* 70 */     if (resources != null) {
/* 71 */       for (ObjectMap.Entries<ObjectMap.Entry<String, Object>> entries = resources.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<String, Object> entry = entries.next();
/* 72 */         skin.add((String)entry.key, entry.value); }
/*    */     
/*    */     }
/* 75 */     skin.load(file);
/* 76 */     return skin;
/*    */   }
/*    */   
/*    */   public static class SkinParameter extends AssetLoaderParameters<Skin> {
/*    */     public final String textureAtlasPath;
/*    */     public final ObjectMap<String, Object> resources;
/*    */     
/*    */     public SkinParameter() {
/* 84 */       this(null, null);
/*    */     }
/*    */     
/*    */     public SkinParameter(ObjectMap<String, Object> resources) {
/* 88 */       this(null, resources);
/*    */     }
/*    */     
/*    */     public SkinParameter(String textureAtlasPath) {
/* 92 */       this(textureAtlasPath, null);
/*    */     }
/*    */     
/*    */     public SkinParameter(String textureAtlasPath, ObjectMap<String, Object> resources) {
/* 96 */       this.textureAtlasPath = textureAtlasPath;
/* 97 */       this.resources = resources;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\SkinLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */