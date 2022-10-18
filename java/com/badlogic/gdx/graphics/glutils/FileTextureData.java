/*     */ package com.badlogic.gdx.graphics.glutils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.PixmapIO;
/*     */ import com.badlogic.gdx.graphics.TextureData;
/*     */ import com.badlogic.gdx.math.MathUtils;
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
/*     */ public class FileTextureData
/*     */   implements TextureData
/*     */ {
/*     */   public static boolean copyToPOT;
/*     */   final FileHandle file;
/*  32 */   int width = 0;
/*  33 */   int height = 0;
/*     */   Pixmap.Format format;
/*     */   Pixmap pixmap;
/*     */   boolean useMipMaps;
/*     */   boolean isPrepared = false;
/*     */   
/*     */   public FileTextureData(FileHandle file, Pixmap preloadedPixmap, Pixmap.Format format, boolean useMipMaps) {
/*  40 */     this.file = file;
/*  41 */     this.pixmap = preloadedPixmap;
/*  42 */     this.format = format;
/*  43 */     this.useMipMaps = useMipMaps;
/*  44 */     if (this.pixmap != null) {
/*  45 */       this.pixmap = ensurePot(this.pixmap);
/*  46 */       this.width = this.pixmap.getWidth();
/*  47 */       this.height = this.pixmap.getHeight();
/*  48 */       if (format == null) this.format = this.pixmap.getFormat();
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isPrepared() {
/*  54 */     return this.isPrepared;
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepare() {
/*  59 */     if (this.isPrepared) throw new GdxRuntimeException("Already prepared"); 
/*  60 */     if (this.pixmap == null) {
/*  61 */       if (this.file.extension().equals("cim")) {
/*  62 */         this.pixmap = PixmapIO.readCIM(this.file);
/*     */       } else {
/*  64 */         this.pixmap = ensurePot(new Pixmap(this.file));
/*  65 */       }  this.width = this.pixmap.getWidth();
/*  66 */       this.height = this.pixmap.getHeight();
/*  67 */       if (this.format == null) this.format = this.pixmap.getFormat(); 
/*     */     } 
/*  69 */     this.isPrepared = true;
/*     */   }
/*     */   
/*     */   private Pixmap ensurePot(Pixmap pixmap) {
/*  73 */     if (Gdx.gl20 == null && copyToPOT) {
/*  74 */       int pixmapWidth = pixmap.getWidth();
/*  75 */       int pixmapHeight = pixmap.getHeight();
/*  76 */       int potWidth = MathUtils.nextPowerOfTwo(pixmapWidth);
/*  77 */       int potHeight = MathUtils.nextPowerOfTwo(pixmapHeight);
/*  78 */       if (pixmapWidth != potWidth || pixmapHeight != potHeight) {
/*  79 */         Pixmap tmp = new Pixmap(potWidth, potHeight, pixmap.getFormat());
/*  80 */         tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmapWidth, pixmapHeight);
/*  81 */         pixmap.dispose();
/*  82 */         return tmp;
/*     */       } 
/*     */     } 
/*  85 */     return pixmap;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap consumePixmap() {
/*  90 */     if (!this.isPrepared) throw new GdxRuntimeException("Call prepare() before calling getPixmap()"); 
/*  91 */     this.isPrepared = false;
/*  92 */     Pixmap pixmap = this.pixmap;
/*  93 */     this.pixmap = null;
/*  94 */     return pixmap;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean disposePixmap() {
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 104 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 109 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public Pixmap.Format getFormat() {
/* 114 */     return this.format;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useMipMaps() {
/* 119 */     return this.useMipMaps;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 124 */     return true;
/*     */   }
/*     */   
/*     */   public FileHandle getFileHandle() {
/* 128 */     return this.file;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureData.TextureDataType getType() {
/* 133 */     return TextureData.TextureDataType.Pixmap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeCustomData(int target) {
/* 138 */     throw new GdxRuntimeException("This TextureData implementation does not upload data itself");
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\glutils\FileTextureData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */