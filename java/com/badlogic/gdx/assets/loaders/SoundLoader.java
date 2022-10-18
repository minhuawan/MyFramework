/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.audio.Sound;
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
/*    */ public class SoundLoader
/*    */   extends AsynchronousAssetLoader<Sound, SoundLoader.SoundParameter>
/*    */ {
/*    */   private Sound sound;
/*    */   
/*    */   public SoundLoader(FileHandleResolver resolver) {
/* 34 */     super(resolver);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Sound getLoadedSound() {
/* 43 */     return this.sound;
/*    */   }
/*    */ 
/*    */   
/*    */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, SoundParameter parameter) {
/* 48 */     this.sound = Gdx.audio.newSound(file);
/*    */   }
/*    */ 
/*    */   
/*    */   public Sound loadSync(AssetManager manager, String fileName, FileHandle file, SoundParameter parameter) {
/* 53 */     Sound sound = this.sound;
/* 54 */     this.sound = null;
/* 55 */     return sound;
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SoundParameter parameter) {
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public static class SoundParameter extends AssetLoaderParameters<Sound> {}
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\SoundLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */