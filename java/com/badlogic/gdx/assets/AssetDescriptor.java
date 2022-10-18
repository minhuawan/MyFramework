/*    */ package com.badlogic.gdx.assets;
/*    */ 
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
/*    */ public class AssetDescriptor<T>
/*    */ {
/*    */   public final String fileName;
/*    */   public final Class<T> type;
/*    */   public final AssetLoaderParameters params;
/*    */   public FileHandle file;
/*    */   
/*    */   public AssetDescriptor(String fileName, Class<T> assetType) {
/* 32 */     this(fileName, assetType, (AssetLoaderParameters<T>)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public AssetDescriptor(FileHandle file, Class<T> assetType) {
/* 37 */     this(file, assetType, (AssetLoaderParameters<T>)null);
/*    */   }
/*    */   
/*    */   public AssetDescriptor(String fileName, Class<T> assetType, AssetLoaderParameters<T> params) {
/* 41 */     this.fileName = fileName.replaceAll("\\\\", "/");
/* 42 */     this.type = assetType;
/* 43 */     this.params = params;
/*    */   }
/*    */ 
/*    */   
/*    */   public AssetDescriptor(FileHandle file, Class<T> assetType, AssetLoaderParameters<T> params) {
/* 48 */     this.fileName = file.path().replaceAll("\\\\", "/");
/* 49 */     this.file = file;
/* 50 */     this.type = assetType;
/* 51 */     this.params = params;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     StringBuffer buffer = new StringBuffer();
/* 57 */     buffer.append(this.fileName);
/* 58 */     buffer.append(", ");
/* 59 */     buffer.append(this.type.getName());
/* 60 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\AssetDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */