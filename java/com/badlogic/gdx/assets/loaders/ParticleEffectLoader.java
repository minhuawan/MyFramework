/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
/*    */ public class ParticleEffectLoader
/*    */   extends SynchronousAssetLoader<ParticleEffect, ParticleEffectLoader.ParticleEffectParameter>
/*    */ {
/*    */   public ParticleEffectLoader(FileHandleResolver resolver) {
/* 32 */     super(resolver);
/*    */   }
/*    */ 
/*    */   
/*    */   public ParticleEffect load(AssetManager am, String fileName, FileHandle file, ParticleEffectParameter param) {
/* 37 */     ParticleEffect effect = new ParticleEffect();
/* 38 */     if (param != null && param.atlasFile != null) {
/* 39 */       effect.load(file, (TextureAtlas)am.get(param.atlasFile, TextureAtlas.class), param.atlasPrefix);
/* 40 */     } else if (param != null && param.imagesDir != null) {
/* 41 */       effect.load(file, param.imagesDir);
/*    */     } else {
/* 43 */       effect.load(file, file.parent());
/* 44 */     }  return effect;
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ParticleEffectParameter param) {
/* 49 */     Array<AssetDescriptor> deps = null;
/* 50 */     if (param != null && param.atlasFile != null) {
/* 51 */       deps = new Array();
/* 52 */       deps.add(new AssetDescriptor(param.atlasFile, TextureAtlas.class));
/*    */     } 
/* 54 */     return deps;
/*    */   }
/*    */   
/*    */   public static class ParticleEffectParameter extends AssetLoaderParameters<ParticleEffect> {
/*    */     public String atlasFile;
/*    */     public String atlasPrefix;
/*    */     public FileHandle imagesDir;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\ParticleEffectLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */