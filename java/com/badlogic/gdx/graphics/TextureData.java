/*    */ package com.badlogic.gdx.graphics;
/*    */ 
/*    */ import com.badlogic.gdx.files.FileHandle;
/*    */ import com.badlogic.gdx.graphics.glutils.ETC1TextureData;
/*    */ import com.badlogic.gdx.graphics.glutils.FileTextureData;
/*    */ import com.badlogic.gdx.graphics.glutils.KTXTextureData;
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
/*    */ public interface TextureData
/*    */ {
/*    */   TextureDataType getType();
/*    */   
/*    */   boolean isPrepared();
/*    */   
/*    */   void prepare();
/*    */   
/*    */   Pixmap consumePixmap();
/*    */   
/*    */   boolean disposePixmap();
/*    */   
/*    */   void consumeCustomData(int paramInt);
/*    */   
/*    */   int getWidth();
/*    */   
/*    */   int getHeight();
/*    */   
/*    */   Pixmap.Format getFormat();
/*    */   
/*    */   boolean useMipMaps();
/*    */   
/*    */   boolean isManaged();
/*    */   
/*    */   public enum TextureDataType
/*    */   {
/* 44 */     Pixmap, Custom;
/*    */   }
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
/*    */   
/*    */   public static class Factory
/*    */   {
/*    */     public static TextureData loadFromFile(FileHandle file, boolean useMipMaps) {
/* 90 */       return loadFromFile(file, null, useMipMaps);
/*    */     }
/*    */     
/*    */     public static TextureData loadFromFile(FileHandle file, Pixmap.Format format, boolean useMipMaps) {
/* 94 */       if (file == null) return null; 
/* 95 */       if (file.name().endsWith(".cim")) return (TextureData)new FileTextureData(file, PixmapIO.readCIM(file), format, useMipMaps); 
/* 96 */       if (file.name().endsWith(".etc1")) return (TextureData)new ETC1TextureData(file, useMipMaps); 
/* 97 */       if (file.name().endsWith(".ktx") || file.name().endsWith(".zktx")) return (TextureData)new KTXTextureData(file, useMipMaps); 
/* 98 */       return (TextureData)new FileTextureData(file, new Pixmap(file), format, useMipMaps);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\TextureData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */