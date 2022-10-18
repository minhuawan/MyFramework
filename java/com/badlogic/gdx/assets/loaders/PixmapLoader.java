/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.Pixmap;
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
/*    */ public class PixmapLoader
/*    */   extends AsynchronousAssetLoader<Pixmap, PixmapLoader.PixmapParameter>
/*    */ {
/*    */   Pixmap pixmap;
/*    */   
/*    */   public PixmapLoader(FileHandleResolver resolver) {
/* 30 */     super(resolver);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, PixmapParameter parameter) {
/* 37 */     this.pixmap = null;
/* 38 */     this.pixmap = new Pixmap(file);
/*    */   }
/*    */ 
/*    */   
/*    */   public Pixmap loadSync(AssetManager manager, String fileName, FileHandle file, PixmapParameter parameter) {
/* 43 */     Pixmap pixmap = this.pixmap;
/* 44 */     this.pixmap = null;
/* 45 */     return pixmap;
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, PixmapParameter parameter) {
/* 50 */     return null;
/*    */   }
/*    */   
/*    */   public static class PixmapParameter extends AssetLoaderParameters<Pixmap> {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\PixmapLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */