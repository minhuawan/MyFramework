/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.audio.Music;
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
/*    */ public class MusicLoader
/*    */   extends AsynchronousAssetLoader<Music, MusicLoader.MusicParameter>
/*    */ {
/*    */   private Music music;
/*    */   
/*    */   public MusicLoader(FileHandleResolver resolver) {
/* 34 */     super(resolver);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Music getLoadedMusic() {
/* 43 */     return this.music;
/*    */   }
/*    */ 
/*    */   
/*    */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, MusicParameter parameter) {
/* 48 */     this.music = Gdx.audio.newMusic(file);
/*    */   }
/*    */ 
/*    */   
/*    */   public Music loadSync(AssetManager manager, String fileName, FileHandle file, MusicParameter parameter) {
/* 53 */     Music music = this.music;
/* 54 */     this.music = null;
/* 55 */     return music;
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MusicParameter parameter) {
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public static class MusicParameter extends AssetLoaderParameters<Music> {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\MusicLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */