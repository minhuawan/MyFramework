/*    */ package com.badlogic.gdx.backends.lwjgl;
/*    */ 
/*    */ import com.badlogic.gdx.utils.GdxNativesLoader;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ import com.badlogic.gdx.utils.SharedLibraryLoader;
/*    */ import java.io.File;
/*    */ import java.lang.reflect.Method;
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
/*    */ public final class LwjglNativesLoader
/*    */ {
/*    */   public static boolean load = true;
/*    */   
/*    */   static {
/* 33 */     System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
/*    */ 
/*    */     
/*    */     try {
/* 37 */       Method method = Class.forName("javax.jnlp.ServiceManager").getDeclaredMethod("lookup", new Class[] { String.class });
/* 38 */       method.invoke(null, new Object[] { "javax.jnlp.PersistenceService" });
/* 39 */       load = false;
/* 40 */     } catch (Throwable ex) {
/* 41 */       load = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void load() {
/* 47 */     GdxNativesLoader.load();
/* 48 */     if (GdxNativesLoader.disableNativesLoading)
/* 49 */       return;  if (!load)
/*    */       return; 
/* 51 */     SharedLibraryLoader loader = new SharedLibraryLoader();
/* 52 */     File nativesDir = null;
/*    */     try {
/* 54 */       if (SharedLibraryLoader.isWindows) {
/* 55 */         nativesDir = loader.extractFile(SharedLibraryLoader.is64Bit ? "lwjgl64.dll" : "lwjgl.dll", null).getParentFile();
/* 56 */         if (!LwjglApplicationConfiguration.disableAudio)
/* 57 */           loader.extractFileTo(SharedLibraryLoader.is64Bit ? "OpenAL64.dll" : "OpenAL32.dll", nativesDir); 
/* 58 */       } else if (SharedLibraryLoader.isMac) {
/* 59 */         nativesDir = loader.extractFile("liblwjgl.dylib", null).getParentFile();
/* 60 */         if (!LwjglApplicationConfiguration.disableAudio) loader.extractFileTo("openal.dylib", nativesDir); 
/* 61 */       } else if (SharedLibraryLoader.isLinux) {
/* 62 */         nativesDir = loader.extractFile(SharedLibraryLoader.is64Bit ? "liblwjgl64.so" : "liblwjgl.so", null).getParentFile();
/* 63 */         if (!LwjglApplicationConfiguration.disableAudio)
/* 64 */           loader.extractFileTo(SharedLibraryLoader.is64Bit ? "libopenal64.so" : "libopenal.so", nativesDir); 
/*    */       } 
/* 66 */     } catch (Throwable ex) {
/* 67 */       throw new GdxRuntimeException("Unable to extract LWJGL natives.", ex);
/*    */     } 
/* 69 */     System.setProperty("org.lwjgl.librarypath", nativesDir.getAbsolutePath());
/* 70 */     load = false;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglNativesLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */