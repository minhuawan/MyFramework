/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Cubemap;
/*     */ import com.badlogic.gdx.graphics.CubemapData;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.TextureData;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FacedCubemapData
/*     */   implements CubemapData
/*     */ {
/*  23 */   protected final TextureData[] data = new TextureData[6];
/*     */ 
/*     */ 
/*     */   
/*     */   public FacedCubemapData() {
/*  28 */     this((TextureData)null, (TextureData)null, (TextureData)null, (TextureData)null, (TextureData)null, (TextureData)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FacedCubemapData(FileHandle positiveX, FileHandle negativeX, FileHandle positiveY, FileHandle negativeY, FileHandle positiveZ, FileHandle negativeZ) {
/*  34 */     this(TextureData.Factory.loadFromFile(positiveX, false), TextureData.Factory.loadFromFile(negativeX, false), 
/*  35 */         TextureData.Factory.loadFromFile(positiveY, false), TextureData.Factory.loadFromFile(negativeY, false), 
/*  36 */         TextureData.Factory.loadFromFile(positiveZ, false), 
/*  37 */         TextureData.Factory.loadFromFile(negativeZ, false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FacedCubemapData(FileHandle positiveX, FileHandle negativeX, FileHandle positiveY, FileHandle negativeY, FileHandle positiveZ, FileHandle negativeZ, boolean useMipMaps) {
/*  43 */     this(TextureData.Factory.loadFromFile(positiveX, useMipMaps), TextureData.Factory.loadFromFile(negativeX, useMipMaps), 
/*  44 */         TextureData.Factory.loadFromFile(positiveY, useMipMaps), 
/*  45 */         TextureData.Factory.loadFromFile(negativeY, useMipMaps), TextureData.Factory.loadFromFile(positiveZ, useMipMaps), 
/*  46 */         TextureData.Factory.loadFromFile(negativeZ, useMipMaps));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FacedCubemapData(Pixmap positiveX, Pixmap negativeX, Pixmap positiveY, Pixmap negativeY, Pixmap positiveZ, Pixmap negativeZ) {
/*  52 */     this(positiveX, negativeX, positiveY, negativeY, positiveZ, negativeZ, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FacedCubemapData(Pixmap positiveX, Pixmap negativeX, Pixmap positiveY, Pixmap negativeY, Pixmap positiveZ, Pixmap negativeZ, boolean useMipMaps) {
/*  58 */     this((positiveX == null) ? null : new PixmapTextureData(positiveX, null, useMipMaps, false), (negativeX == null) ? null : new PixmapTextureData(negativeX, null, useMipMaps, false), (positiveY == null) ? null : new PixmapTextureData(positiveY, null, useMipMaps, false), (negativeY == null) ? null : new PixmapTextureData(negativeY, null, useMipMaps, false), (positiveZ == null) ? null : new PixmapTextureData(positiveZ, null, useMipMaps, false), (negativeZ == null) ? null : new PixmapTextureData(negativeZ, null, useMipMaps, false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FacedCubemapData(int width, int height, int depth, Pixmap.Format format) {
/*  67 */     this(new PixmapTextureData(new Pixmap(depth, height, format), null, false, true), new PixmapTextureData(new Pixmap(depth, height, format), null, false, true), new PixmapTextureData(new Pixmap(width, depth, format), null, false, true), new PixmapTextureData(new Pixmap(width, depth, format), null, false, true), new PixmapTextureData(new Pixmap(width, height, format), null, false, true), new PixmapTextureData(new Pixmap(width, height, format), null, false, true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FacedCubemapData(TextureData positiveX, TextureData negativeX, TextureData positiveY, TextureData negativeY, TextureData positiveZ, TextureData negativeZ) {
/*  76 */     this.data[0] = positiveX;
/*  77 */     this.data[1] = negativeX;
/*  78 */     this.data[2] = positiveY;
/*  79 */     this.data[3] = negativeY;
/*  80 */     this.data[4] = positiveZ;
/*  81 */     this.data[5] = negativeZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/*  86 */     for (TextureData data : this.data) {
/*  87 */       if (!data.isManaged()) return false; 
/*  88 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(Cubemap.CubemapSide side, FileHandle file) {
/*  97 */     this.data[side.index] = TextureData.Factory.loadFromFile(file, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(Cubemap.CubemapSide side, Pixmap pixmap) {
/* 106 */     this.data[side.index] = (pixmap == null) ? null : new PixmapTextureData(pixmap, null, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 111 */     for (int i = 0; i < this.data.length; i++) {
/* 112 */       if (this.data[i] == null) return false; 
/* 113 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureData getTextureData(Cubemap.CubemapSide side) {
/* 118 */     return this.data[side.index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 123 */     int width = 0; int tmp;
/* 124 */     if (this.data[Cubemap.CubemapSide.PositiveZ.index] != null && (tmp = this.data[Cubemap.CubemapSide.PositiveZ.index].getWidth()) > width) width = tmp; 
/* 125 */     if (this.data[Cubemap.CubemapSide.NegativeZ.index] != null && (tmp = this.data[Cubemap.CubemapSide.NegativeZ.index].getWidth()) > width) width = tmp; 
/* 126 */     if (this.data[Cubemap.CubemapSide.PositiveY.index] != null && (tmp = this.data[Cubemap.CubemapSide.PositiveY.index].getWidth()) > width) width = tmp; 
/* 127 */     if (this.data[Cubemap.CubemapSide.NegativeY.index] != null && (tmp = this.data[Cubemap.CubemapSide.NegativeY.index].getWidth()) > width) width = tmp; 
/* 128 */     return width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 133 */     int height = 0; int tmp;
/* 134 */     if (this.data[Cubemap.CubemapSide.PositiveZ.index] != null && (tmp = this.data[Cubemap.CubemapSide.PositiveZ.index].getHeight()) > height)
/* 135 */       height = tmp; 
/* 136 */     if (this.data[Cubemap.CubemapSide.NegativeZ.index] != null && (tmp = this.data[Cubemap.CubemapSide.NegativeZ.index].getHeight()) > height)
/* 137 */       height = tmp; 
/* 138 */     if (this.data[Cubemap.CubemapSide.PositiveX.index] != null && (tmp = this.data[Cubemap.CubemapSide.PositiveX.index].getHeight()) > height)
/* 139 */       height = tmp; 
/* 140 */     if (this.data[Cubemap.CubemapSide.NegativeX.index] != null && (tmp = this.data[Cubemap.CubemapSide.NegativeX.index].getHeight()) > height)
/* 141 */       height = tmp; 
/* 142 */     return height;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 152 */     if (!isComplete()) throw new GdxRuntimeException("You need to complete your cubemap data before using it"); 
/* 153 */     for (int i = 0; i < this.data.length; i++) {
/* 154 */       if (!this.data[i].isPrepared()) this.data[i].prepare(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void consumeCubemapData() {
/* 159 */     for (int i = 0; i < this.data.length; i++) {
/* 160 */       if (this.data[i].getType() == TextureData.TextureDataType.Custom) {
/* 161 */         this.data[i].consumeCustomData(34069 + i);
/*     */       } else {
/* 163 */         Pixmap pixmap = this.data[i].consumePixmap();
/* 164 */         boolean disposePixmap = this.data[i].disposePixmap();
/* 165 */         if (this.data[i].getFormat() != pixmap.getFormat()) {
/* 166 */           Pixmap tmp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), this.data[i].getFormat());
/* 167 */           Pixmap.Blending blend = Pixmap.getBlending();
/* 168 */           Pixmap.setBlending(Pixmap.Blending.None);
/* 169 */           tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
/* 170 */           Pixmap.setBlending(blend);
/* 171 */           if (this.data[i].disposePixmap()) pixmap.dispose(); 
/* 172 */           pixmap = tmp;
/* 173 */           disposePixmap = true;
/*     */         } 
/* 175 */         Gdx.gl.glPixelStorei(3317, 1);
/* 176 */         Gdx.gl.glTexImage2D(34069 + i, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap
/* 177 */             .getHeight(), 0, pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
/* 178 */         if (disposePixmap) pixmap.dispose(); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\FacedCubemapData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */