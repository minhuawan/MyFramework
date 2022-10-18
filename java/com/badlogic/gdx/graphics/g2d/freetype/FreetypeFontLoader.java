/*    */ package com.badlogic.gdx.graphics.g2d.freetype;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
/*    */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FreetypeFontLoader
/*    */   extends AsynchronousAssetLoader<BitmapFont, FreetypeFontLoader.FreeTypeFontLoaderParameter>
/*    */ {
/*    */   public FreetypeFontLoader(FileHandleResolver resolver) {
/* 22 */     super(resolver);
/*    */   }
/*    */   
/*    */   public static class FreeTypeFontLoaderParameter
/*    */     extends AssetLoaderParameters<BitmapFont>
/*    */   {
/*    */     public String fontFileName;
/* 29 */     public FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
/*    */   }
/*    */ 
/*    */   
/*    */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, FreeTypeFontLoaderParameter parameter) {
/* 34 */     if (parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
/*    */   
/*    */   }
/*    */   
/*    */   public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, FreeTypeFontLoaderParameter parameter) {
/* 39 */     if (parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!"); 
/* 40 */     FreeTypeFontGenerator generator = (FreeTypeFontGenerator)manager.get(parameter.fontFileName + ".gen", FreeTypeFontGenerator.class);
/* 41 */     BitmapFont font = generator.generateFont(parameter.fontParameters);
/* 42 */     return font;
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, FreeTypeFontLoaderParameter parameter) {
/* 47 */     Array<AssetDescriptor> deps = new Array();
/* 48 */     deps.add(new AssetDescriptor(parameter.fontFileName + ".gen", FreeTypeFontGenerator.class));
/* 49 */     return deps;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\freetype\FreetypeFontLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */