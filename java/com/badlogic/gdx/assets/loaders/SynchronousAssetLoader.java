/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.files.FileHandle;
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
/*    */ public abstract class SynchronousAssetLoader<T, P extends AssetLoaderParameters<T>>
/*    */   extends AssetLoader<T, P>
/*    */ {
/*    */   public SynchronousAssetLoader(FileHandleResolver resolver) {
/* 25 */     super(resolver);
/*    */   }
/*    */   
/*    */   public abstract T load(AssetManager paramAssetManager, String paramString, FileHandle paramFileHandle, P paramP);
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\SynchronousAssetLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */