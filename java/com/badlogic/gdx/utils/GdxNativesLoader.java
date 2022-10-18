/*    */ package com.badlogic.gdx.utils;
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
/*    */ public class GdxNativesLoader
/*    */ {
/*    */   public static boolean disableNativesLoading = false;
/*    */   private static boolean nativesLoaded;
/*    */   
/*    */   public static synchronized void load() {
/* 28 */     if (nativesLoaded)
/* 29 */       return;  nativesLoaded = true;
/*    */     
/* 31 */     if (disableNativesLoading)
/*    */       return; 
/* 33 */     (new SharedLibraryLoader()).load("gdx");
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\GdxNativesLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */