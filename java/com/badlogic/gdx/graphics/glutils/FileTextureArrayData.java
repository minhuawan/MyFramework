/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.TextureArrayData;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileTextureArrayData
/*     */   implements TextureArrayData
/*     */ {
/*     */   private TextureData[] textureDatas;
/*     */   private boolean prepared;
/*     */   private Pixmap.Format format;
/*     */   private int depth;
/*     */   boolean useMipMaps;
/*     */   
/*     */   public FileTextureArrayData(Pixmap.Format format, boolean useMipMaps, FileHandle[] files) {
/*  37 */     this.format = format;
/*  38 */     this.useMipMaps = useMipMaps;
/*  39 */     this.depth = files.length;
/*  40 */     this.textureDatas = new TextureData[files.length];
/*  41 */     for (int i = 0; i < files.length; i++) {
/*  42 */       this.textureDatas[i] = TextureData.Factory.loadFromFile(files[i], format, useMipMaps);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/*  48 */     return this.prepared;
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() {
/*  53 */     int width = -1;
/*  54 */     int height = -1;
/*  55 */     for (TextureData data : this.textureDatas) {
/*  56 */       data.prepare();
/*  57 */       if (width == -1) {
/*  58 */         width = data.getWidth();
/*  59 */         height = data.getHeight();
/*     */       
/*     */       }
/*  62 */       else if (width != data.getWidth() || height != data.getHeight()) {
/*  63 */         throw new GdxRuntimeException("Error whilst preparing TextureArray: TextureArray Textures must have equal dimensions.");
/*     */       } 
/*     */     } 
/*  66 */     this.prepared = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeTextureArrayData() {
/*  71 */     for (int i = 0; i < this.textureDatas.length; i++) {
/*  72 */       if (this.textureDatas[i].getType() == TextureData.TextureDataType.Custom) {
/*  73 */         this.textureDatas[i].consumeCustomData(35866);
/*     */       } else {
/*  75 */         TextureData texData = this.textureDatas[i];
/*  76 */         Pixmap pixmap = texData.consumePixmap();
/*  77 */         boolean disposePixmap = texData.disposePixmap();
/*  78 */         if (texData.getFormat() != pixmap.getFormat()) {
/*  79 */           Pixmap temp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), texData.getFormat());
/*  80 */           Pixmap.Blending blendmode = Pixmap.getBlending();
/*  81 */           Pixmap.setBlending(Pixmap.Blending.None);
/*  82 */           temp.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
/*  83 */           Pixmap.setBlending(blendmode);
/*  84 */           if (texData.disposePixmap()) {
/*  85 */             pixmap.dispose();
/*     */           }
/*  87 */           pixmap = temp;
/*  88 */           disposePixmap = true;
/*     */         } 
/*  90 */         Gdx.gl30.glTexSubImage3D(35866, 0, 0, 0, i, pixmap.getWidth(), pixmap.getHeight(), 1, pixmap.getGLInternalFormat(), pixmap.getGLType(), pixmap.getPixels());
/*  91 */         if (disposePixmap) pixmap.dispose();
/*     */       
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  98 */     return this.textureDatas[0].getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 103 */     return this.textureDatas[0].getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/* 108 */     return this.depth;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInternalFormat() {
/* 113 */     return Pixmap.Format.toGlFormat(this.format);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGLType() {
/* 118 */     return Pixmap.Format.toGlType(this.format);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 123 */     for (TextureData data : this.textureDatas) {
/* 124 */       if (!data.isManaged()) {
/* 125 */         return false;
/*     */       }
/*     */     } 
/* 128 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\FileTextureArrayData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */