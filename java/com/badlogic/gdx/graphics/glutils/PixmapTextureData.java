/*    */ package com.badlogic.gdx.graphics.glutils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Pixmap;
/*    */ import com.badlogic.gdx.graphics.TextureData;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*    */ public class PixmapTextureData
/*    */   implements TextureData
/*    */ {
/*    */   final Pixmap pixmap;
/*    */   final Pixmap.Format format;
/*    */   final boolean useMipMaps;
/*    */   final boolean disposePixmap;
/*    */   final boolean managed;
/*    */   
/*    */   public PixmapTextureData(Pixmap pixmap, Pixmap.Format format, boolean useMipMaps, boolean disposePixmap) {
/* 32 */     this(pixmap, format, useMipMaps, disposePixmap, false);
/*    */   }
/*    */   
/*    */   public PixmapTextureData(Pixmap pixmap, Pixmap.Format format, boolean useMipMaps, boolean disposePixmap, boolean managed) {
/* 36 */     this.pixmap = pixmap;
/* 37 */     this.format = (format == null) ? pixmap.getFormat() : format;
/* 38 */     this.useMipMaps = useMipMaps;
/* 39 */     this.disposePixmap = disposePixmap;
/* 40 */     this.managed = managed;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean disposePixmap() {
/* 45 */     return this.disposePixmap;
/*    */   }
/*    */ 
/*    */   
/*    */   public Pixmap consumePixmap() {
/* 50 */     return this.pixmap;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 55 */     return this.pixmap.getWidth();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 60 */     return this.pixmap.getHeight();
/*    */   }
/*    */ 
/*    */   
/*    */   public Pixmap.Format getFormat() {
/* 65 */     return this.format;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean useMipMaps() {
/* 70 */     return this.useMipMaps;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isManaged() {
/* 75 */     return this.managed;
/*    */   }
/*    */ 
/*    */   
/*    */   public TextureData.TextureDataType getType() {
/* 80 */     return TextureData.TextureDataType.Pixmap;
/*    */   }
/*    */ 
/*    */   
/*    */   public void consumeCustomData(int target) {
/* 85 */     throw new GdxRuntimeException("This TextureData implementation does not upload data itself");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPrepared() {
/* 90 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void prepare() {
/* 95 */     throw new GdxRuntimeException("prepare() must not be called on a PixmapTextureData instance as it is already prepared.");
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\PixmapTextureData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */