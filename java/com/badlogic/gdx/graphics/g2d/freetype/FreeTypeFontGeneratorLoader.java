/*    */ package com.badlogic.gdx.graphics.g2d.freetype;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*    */ import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
/*    */ import com.badlogic.gdx.files.FileHandle;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FreeTypeFontGeneratorLoader
/*    */   extends SynchronousAssetLoader<FreeTypeFontGenerator, FreeTypeFontGeneratorLoader.FreeTypeFontGeneratorParameters>
/*    */ {
/*    */   public FreeTypeFontGeneratorLoader(FileHandleResolver resolver) {
/* 38 */     super(resolver);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FreeTypeFontGenerator load(AssetManager assetManager, String fileName, FileHandle file, FreeTypeFontGeneratorParameters parameter) {
/* 44 */     FreeTypeFontGenerator generator = null;
/* 45 */     if (file.extension().equals("gen")) {
/* 46 */       generator = new FreeTypeFontGenerator(file.sibling(file.nameWithoutExtension()));
/*    */     } else {
/* 48 */       generator = new FreeTypeFontGenerator(file);
/*    */     } 
/* 50 */     return generator;
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, FreeTypeFontGeneratorParameters parameter) {
/* 55 */     return null;
/*    */   }
/*    */   
/*    */   public static class FreeTypeFontGeneratorParameters extends AssetLoaderParameters<FreeTypeFontGenerator> {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\freetype\FreeTypeFontGeneratorLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */