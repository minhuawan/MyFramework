/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
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
/*     */ public class Pixmap
/*     */   implements Disposable
/*     */ {
/*     */   public enum Format
/*     */   {
/*  50 */     Alpha, Intensity, LuminanceAlpha, RGB565, RGBA4444, RGB888, RGBA8888;
/*     */     
/*     */     public static int toGdx2DPixmapFormat(Format format) {
/*  53 */       if (format == Alpha) return 1; 
/*  54 */       if (format == Intensity) return 1; 
/*  55 */       if (format == LuminanceAlpha) return 2; 
/*  56 */       if (format == RGB565) return 5; 
/*  57 */       if (format == RGBA4444) return 6; 
/*  58 */       if (format == RGB888) return 3; 
/*  59 */       if (format == RGBA8888) return 4; 
/*  60 */       throw new GdxRuntimeException("Unknown Format: " + format);
/*     */     }
/*     */     
/*     */     public static Format fromGdx2DPixmapFormat(int format) {
/*  64 */       if (format == 1) return Alpha; 
/*  65 */       if (format == 2) return LuminanceAlpha; 
/*  66 */       if (format == 5) return RGB565; 
/*  67 */       if (format == 6) return RGBA4444; 
/*  68 */       if (format == 3) return RGB888; 
/*  69 */       if (format == 4) return RGBA8888; 
/*  70 */       throw new GdxRuntimeException("Unknown Gdx2DPixmap Format: " + format);
/*     */     }
/*     */     
/*     */     public static int toGlFormat(Format format) {
/*  74 */       return Gdx2DPixmap.toGlFormat(toGdx2DPixmapFormat(format));
/*     */     }
/*     */     
/*     */     public static int toGlType(Format format) {
/*  78 */       return Gdx2DPixmap.toGlType(toGdx2DPixmapFormat(format));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public enum Blending
/*     */   {
/*  85 */     None, SourceOver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Filter
/*     */   {
/*  92 */     NearestNeighbour, BiLinear;
/*     */   }
/*     */ 
/*     */   
/*  96 */   private static Blending blending = Blending.SourceOver;
/*     */   
/*     */   final Gdx2DPixmap pixmap;
/*  99 */   int color = 0;
/*     */ 
/*     */   
/*     */   private boolean disposed;
/*     */ 
/*     */   
/*     */   public static void setBlending(Blending blending) {
/* 106 */     Pixmap.blending = blending;
/* 107 */     Gdx2DPixmap.setBlend((blending == Blending.None) ? 0 : 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setFilter(Filter filter) {
/* 114 */     Gdx2DPixmap.setScale((filter == Filter.NearestNeighbour) ? 0 : 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pixmap(int width, int height, Format format) {
/* 122 */     this.pixmap = new Gdx2DPixmap(width, height, Format.toGdx2DPixmapFormat(format));
/* 123 */     setColor(0.0F, 0.0F, 0.0F, 0.0F);
/* 124 */     fill();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pixmap(byte[] encodedData, int offset, int len) {
/*     */     try {
/* 133 */       this.pixmap = new Gdx2DPixmap(encodedData, offset, len, 0);
/* 134 */     } catch (IOException e) {
/* 135 */       throw new GdxRuntimeException("Couldn't load pixmap from image data", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pixmap(FileHandle file) {
/*     */     try {
/* 145 */       byte[] bytes = file.readBytes();
/* 146 */       this.pixmap = new Gdx2DPixmap(bytes, 0, bytes.length, 0);
/* 147 */     } catch (Exception e) {
/* 148 */       throw new GdxRuntimeException("Couldn't load file: " + file, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Pixmap(Gdx2DPixmap pixmap) {
/* 155 */     this.pixmap = pixmap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(int color) {
/* 161 */     this.color = color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(float r, float g, float b, float a) {
/* 171 */     this.color = Color.rgba8888(r, g, b, a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 177 */     this.color = Color.rgba8888(color.r, color.g, color.b, color.a);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fill() {
/* 182 */     this.pixmap.clear(this.color);
/*     */   }
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
/*     */   public void drawLine(int x, int y, int x2, int y2) {
/* 199 */     this.pixmap.drawLine(x, y, x2, y2, this.color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawRectangle(int x, int y, int width, int height) {
/* 210 */     this.pixmap.drawRect(x, y, width, height, this.color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawPixmap(Pixmap pixmap, int x, int y) {
/* 219 */     drawPixmap(pixmap, x, y, 0, 0, pixmap.getWidth(), pixmap.getHeight());
/*     */   }
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
/*     */   public void drawPixmap(Pixmap pixmap, int x, int y, int srcx, int srcy, int srcWidth, int srcHeight) {
/* 232 */     this.pixmap.drawPixmap(pixmap.pixmap, srcx, srcy, x, y, srcWidth, srcHeight);
/*     */   }
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
/*     */   public void drawPixmap(Pixmap pixmap, int srcx, int srcy, int srcWidth, int srcHeight, int dstx, int dsty, int dstWidth, int dstHeight) {
/* 250 */     this.pixmap.drawPixmap(pixmap.pixmap, srcx, srcy, srcWidth, srcHeight, dstx, dsty, dstWidth, dstHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillRectangle(int x, int y, int width, int height) {
/* 261 */     this.pixmap.fillRect(x, y, width, height, this.color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawCircle(int x, int y, int radius) {
/* 270 */     this.pixmap.drawCircle(x, y, radius, this.color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillCircle(int x, int y, int radius) {
/* 279 */     this.pixmap.fillCircle(x, y, radius, this.color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
/* 291 */     this.pixmap.fillTriangle(x1, y1, x2, y2, x3, y3, this.color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPixel(int x, int y) {
/* 300 */     return this.pixmap.getPixel(x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 305 */     return this.pixmap.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 310 */     return this.pixmap.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 315 */     if (this.disposed) throw new GdxRuntimeException("Pixmap already disposed!"); 
/* 316 */     this.pixmap.dispose();
/* 317 */     this.disposed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawPixel(int x, int y) {
/* 325 */     this.pixmap.setPixel(x, y, this.color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawPixel(int x, int y, int color) {
/* 334 */     this.pixmap.setPixel(x, y, color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGLFormat() {
/* 341 */     return this.pixmap.getGLFormat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGLInternalFormat() {
/* 348 */     return this.pixmap.getGLInternalFormat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGLType() {
/* 355 */     return this.pixmap.getGLType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getPixels() {
/* 364 */     if (this.disposed) throw new GdxRuntimeException("Pixmap already disposed"); 
/* 365 */     return this.pixmap.getPixels();
/*     */   }
/*     */ 
/*     */   
/*     */   public Format getFormat() {
/* 370 */     return Format.fromGdx2DPixmapFormat(this.pixmap.getFormat());
/*     */   }
/*     */ 
/*     */   
/*     */   public static Blending getBlending() {
/* 375 */     return blending;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\Pixmap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */