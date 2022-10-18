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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AsynchronousAssetLoader<T, P extends AssetLoaderParameters<T>>
/*    */   extends AssetLoader<T, P>
/*    */ {
/*    */   public AsynchronousAssetLoader(FileHandleResolver resolver) {
/* 32 */     super(resolver);
/*    */   }
/*    */   
/*    */   public abstract void loadAsync(AssetManager paramAssetManager, String paramString, FileHandle paramFileHandle, P paramP);
/*    */   
/*    */   public abstract T loadSync(AssetManager paramAssetManager, String paramString, FileHandle paramFileHandle, P paramP);
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\AsynchronousAssetLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */