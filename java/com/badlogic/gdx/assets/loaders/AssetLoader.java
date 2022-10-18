/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
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
/*    */ public abstract class AssetLoader<T, P extends AssetLoaderParameters<T>>
/*    */ {
/*    */   private FileHandleResolver resolver;
/*    */   
/*    */   public AssetLoader(FileHandleResolver resolver) {
/* 36 */     this.resolver = resolver;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FileHandle resolve(String fileName) {
/* 42 */     return this.resolver.resolve(fileName);
/*    */   }
/*    */   
/*    */   public abstract Array<AssetDescriptor> getDependencies(String paramString, FileHandle paramFileHandle, P paramP);
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\AssetLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */