/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ETC1TextureData
/*     */   implements TextureData
/*     */ {
/*     */   FileHandle file;
/*     */   ETC1.ETC1Data data;
/*     */   boolean useMipMaps;
/*  32 */   int width = 0;
/*  33 */   int height = 0;
/*     */   boolean isPrepared = false;
/*     */   
/*     */   public ETC1TextureData(FileHandle file) {
/*  37 */     this(file, false);
/*     */   }
/*     */   
/*     */   public ETC1TextureData(FileHandle file, boolean useMipMaps) {
/*  41 */     this.file = file;
/*  42 */     this.useMipMaps = useMipMaps;
/*     */   }
/*     */   
/*     */   public ETC1TextureData(ETC1.ETC1Data encodedImage, boolean useMipMaps) {
/*  46 */     this.data = encodedImage;
/*  47 */     this.useMipMaps = useMipMaps;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureData.TextureDataType getType() {
/*  52 */     return TextureData.TextureDataType.Custom;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrepared() {
/*  57 */     return this.isPrepared;
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() {
/*  62 */     if (this.isPrepared) throw new GdxRuntimeException("Already prepared"); 
/*  63 */     if (this.file == null && this.data == null) throw new GdxRuntimeException("Can only load once from ETC1Data"); 
/*  64 */     if (this.file != null) {
/*  65 */       this.data = new ETC1.ETC1Data(this.file);
/*     */     }
/*  67 */     this.width = this.data.width;
/*  68 */     this.height = this.data.height;
/*  69 */     this.isPrepared = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeCustomData(int target) {
/*  74 */     if (!this.isPrepared) throw new GdxRuntimeException("Call prepare() before calling consumeCompressedData()");
/*     */     
/*  76 */     if (!Gdx.graphics.supportsExtension("GL_OES_compressed_ETC1_RGB8_texture")) {
/*  77 */       Pixmap pixmap = ETC1.decodeImage(this.data, Pixmap.Format.RGB565);
/*  78 */       Gdx.gl.glTexImage2D(target, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0, pixmap
/*  79 */           .getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
/*  80 */       if (this.useMipMaps) MipMapGenerator.generateMipMap(target, pixmap, pixmap.getWidth(), pixmap.getHeight()); 
/*  81 */       pixmap.dispose();
/*  82 */       this.useMipMaps = false;
/*     */     } else {
/*  84 */       Gdx.gl.glCompressedTexImage2D(target, 0, ETC1.ETC1_RGB8_OES, this.width, this.height, 0, this.data.compressedData.capacity() - this.data.dataOffset, this.data.compressedData);
/*     */       
/*  86 */       if (useMipMaps()) Gdx.gl20.glGenerateMipmap(3553); 
/*     */     } 
/*  88 */     this.data.dispose();
/*  89 */     this.data = null;
/*  90 */     this.isPrepared = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap consumePixmap() {
/*  95 */     throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean disposePixmap() {
/* 100 */     throw new GdxRuntimeException("This TextureData implementation does not return a Pixmap");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 105 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 110 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap.Format getFormat() {
/* 115 */     return Pixmap.Format.RGB565;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useMipMaps() {
/* 120 */     return this.useMipMaps;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 125 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\ETC1TextureData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */