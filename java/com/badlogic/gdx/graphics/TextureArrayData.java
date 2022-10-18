/*    */ package com.badlogic.gdx.graphics;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.glutils.FileTextureArrayData;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TextureArrayData
/*    */ {
/*    */   boolean isPrepared();
/*    */   
/*    */   void prepare();
/*    */   
/*    */   void consumeTextureArrayData();
/*    */   
/*    */   int getWidth();
/*    */   
/*    */   int getHeight();
/*    */   
/*    */   int getDepth();
/*    */   
/*    */   boolean isManaged();
/*    */   
/*    */   int getInternalFormat();
/*    */   
/*    */   int getGLType();
/*    */   
/*    */   public static class Factory
/*    */   {
/*    */     public static TextureArrayData loadFromFiles(Pixmap.Format format, boolean useMipMaps, FileHandle... files) {
/* 68 */       return (TextureArrayData)new FileTextureArrayData(format, useMipMaps, files);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\TextureArrayData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */