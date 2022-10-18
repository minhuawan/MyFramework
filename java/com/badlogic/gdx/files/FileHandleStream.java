/*    */ package com.badlogic.gdx.files;
/*    */ 
/*    */ import com.badlogic.gdx.Files;
/*    */ import java.io.File;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
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
/*    */ public abstract class FileHandleStream
/*    */   extends FileHandle
/*    */ {
/*    */   public FileHandleStream(String path) {
/* 31 */     super(new File(path), Files.FileType.Absolute);
/*    */   }
/*    */   
/*    */   public boolean isDirectory() {
/* 35 */     return false;
/*    */   }
/*    */   
/*    */   public long length() {
/* 39 */     return 0L;
/*    */   }
/*    */   
/*    */   public boolean exists() {
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   public FileHandle child(String name) {
/* 47 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public FileHandle sibling(String name) {
/* 51 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public FileHandle parent() {
/* 55 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public InputStream read() {
/* 59 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public OutputStream write(boolean overwrite) {
/* 63 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public FileHandle[] list() {
/* 67 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public void mkdirs() {
/* 71 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public boolean delete() {
/* 75 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public boolean deleteDirectory() {
/* 79 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public void copyTo(FileHandle dest) {
/* 83 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public void moveTo(FileHandle dest) {
/* 87 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\files\FileHandleStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */