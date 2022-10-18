/*    */ package com.badlogic.gdx.assets.loaders;
/*    */ 
/*    */ import com.badlogic.gdx.assets.AssetDescriptor;
/*    */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*    */ import com.badlogic.gdx.assets.AssetManager;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import com.badlogic.gdx.utils.I18NBundle;
/*    */ import java.util.Locale;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class I18NBundleLoader
/*    */   extends AsynchronousAssetLoader<I18NBundle, I18NBundleLoader.I18NBundleParameter>
/*    */ {
/*    */   I18NBundle bundle;
/*    */   
/*    */   public I18NBundleLoader(FileHandleResolver resolver) {
/* 48 */     super(resolver);
/*    */   }
/*    */ 
/*    */   
/*    */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, I18NBundleParameter parameter) {
/*    */     Locale locale;
/*    */     String encoding;
/* 55 */     this.bundle = null;
/*    */ 
/*    */     
/* 58 */     if (parameter == null) {
/* 59 */       locale = Locale.getDefault();
/* 60 */       encoding = null;
/*    */     } else {
/* 62 */       locale = (parameter.locale == null) ? Locale.getDefault() : parameter.locale;
/* 63 */       encoding = parameter.encoding;
/*    */     } 
/* 65 */     if (encoding == null) {
/* 66 */       this.bundle = I18NBundle.createBundle(file, locale);
/*    */     } else {
/* 68 */       this.bundle = I18NBundle.createBundle(file, locale, encoding);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public I18NBundle loadSync(AssetManager manager, String fileName, FileHandle file, I18NBundleParameter parameter) {
/* 74 */     I18NBundle bundle = this.bundle;
/* 75 */     this.bundle = null;
/* 76 */     return bundle;
/*    */   }
/*    */ 
/*    */   
/*    */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, I18NBundleParameter parameter) {
/* 81 */     return null;
/*    */   }
/*    */   
/*    */   public static class I18NBundleParameter extends AssetLoaderParameters<I18NBundle> {
/*    */     public final Locale locale;
/*    */     public final String encoding;
/*    */     
/*    */     public I18NBundleParameter() {
/* 89 */       this(null, null);
/*    */     }
/*    */     
/*    */     public I18NBundleParameter(Locale locale) {
/* 93 */       this(locale, null);
/*    */     }
/*    */     
/*    */     public I18NBundleParameter(Locale locale, String encoding) {
/* 97 */       this.locale = locale;
/* 98 */       this.encoding = encoding;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\I18NBundleLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */