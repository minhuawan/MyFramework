/*    */ package com.badlogic.gdx.backends.headless;
/*    */ 
/*    */ import com.badlogic.gdx.Files;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import java.io.File;
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
/*    */ public final class HeadlessFiles
/*    */   implements Files
/*    */ {
/* 27 */   public static final String externalPath = System.getProperty("user.home") + File.separator;
/* 28 */   public static final String localPath = (new File("")).getAbsolutePath() + File.separator;
/*    */ 
/*    */   
/*    */   public FileHandle getFileHandle(String fileName, Files.FileType type) {
/* 32 */     return new HeadlessFileHandle(fileName, type);
/*    */   }
/*    */ 
/*    */   
/*    */   public FileHandle classpath(String path) {
/* 37 */     return new HeadlessFileHandle(path, Files.FileType.Classpath);
/*    */   }
/*    */ 
/*    */   
/*    */   public FileHandle internal(String path) {
/* 42 */     return new HeadlessFileHandle(path, Files.FileType.Internal);
/*    */   }
/*    */ 
/*    */   
/*    */   public FileHandle external(String path) {
/* 47 */     return new HeadlessFileHandle(path, Files.FileType.External);
/*    */   }
/*    */ 
/*    */   
/*    */   public FileHandle absolute(String path) {
/* 52 */     return new HeadlessFileHandle(path, Files.FileType.Absolute);
/*    */   }
/*    */ 
/*    */   
/*    */   public FileHandle local(String path) {
/* 57 */     return new HeadlessFileHandle(path, Files.FileType.Local);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getExternalStoragePath() {
/* 62 */     return externalPath;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isExternalStorageAvailable() {
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLocalStoragePath() {
/* 72 */     return localPath;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isLocalStorageAvailable() {
/* 77 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\headless\HeadlessFiles.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */