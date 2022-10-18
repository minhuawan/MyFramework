/*    */ package com.badlogic.gdx.backends.lwjgl;
/*    */ 
/*    */ import com.badlogic.gdx.Files;
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*    */ public final class LwjglFileHandle
/*    */   extends FileHandle
/*    */ {
/*    */   public LwjglFileHandle(String fileName, Files.FileType type) {
/* 29 */     super(fileName, type);
/*    */   }
/*    */   
/*    */   public LwjglFileHandle(File file, Files.FileType type) {
/* 33 */     super(file, type);
/*    */   }
/*    */   
/*    */   public FileHandle child(String name) {
/* 37 */     if (this.file.getPath().length() == 0) return new LwjglFileHandle(new File(name), this.type); 
/* 38 */     return new LwjglFileHandle(new File(this.file, name), this.type);
/*    */   }
/*    */   
/*    */   public FileHandle sibling(String name) {
/* 42 */     if (this.file.getPath().length() == 0) throw new GdxRuntimeException("Cannot get the sibling of the root."); 
/* 43 */     return new LwjglFileHandle(new File(this.file.getParent(), name), this.type);
/*    */   }
/*    */   
/*    */   public FileHandle parent() {
/* 47 */     File parent = this.file.getParentFile();
/* 48 */     if (parent == null)
/* 49 */       if (this.type == Files.FileType.Absolute) {
/* 50 */         parent = new File("/");
/*    */       } else {
/* 52 */         parent = new File("");
/*    */       }  
/* 54 */     return new LwjglFileHandle(parent, this.type);
/*    */   }
/*    */   
/*    */   public File file() {
/* 58 */     if (this.type == Files.FileType.External) return new File(LwjglFiles.externalPath, this.file.getPath()); 
/* 59 */     if (this.type == Files.FileType.Local) return new File(LwjglFiles.localPath, this.file.getPath()); 
/* 60 */     return this.file;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglFileHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */