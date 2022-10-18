/*    */ package com.badlogic.gdx.graphics.glutils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.GLTexture;
/*    */ import com.badlogic.gdx.graphics.Pixmap;
/*    */ import com.badlogic.gdx.graphics.TextureData;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MipMapTextureData
/*    */   implements TextureData
/*    */ {
/*    */   TextureData[] mips;
/*    */   
/*    */   public MipMapTextureData(TextureData... mipMapData) {
/* 16 */     this.mips = new TextureData[mipMapData.length];
/* 17 */     System.arraycopy(mipMapData, 0, this.mips, 0, mipMapData.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public TextureData.TextureDataType getType() {
/* 22 */     return TextureData.TextureDataType.Custom;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPrepared() {
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void prepare() {}
/*    */ 
/*    */   
/*    */   public Pixmap consumePixmap() {
/* 35 */     throw new GdxRuntimeException("It's compressed, use the compressed method");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean disposePixmap() {
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void consumeCustomData(int target) {
/* 45 */     for (int i = 0; i < this.mips.length; i++) {
/* 46 */       GLTexture.uploadImageData(target, this.mips[i], i);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWidth() {
/* 52 */     return this.mips[0].getWidth();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 57 */     return this.mips[0].getHeight();
/*    */   }
/*    */ 
/*    */   
/*    */   public Pixmap.Format getFormat() {
/* 62 */     return this.mips[0].getFormat();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean useMipMaps() {
/* 67 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isManaged() {
/* 72 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\MipMapTextureData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */