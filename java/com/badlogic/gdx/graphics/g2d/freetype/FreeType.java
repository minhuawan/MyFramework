/*     */ package com.badlogic.gdx.graphics.g2d.freetype;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.LongMap;
/*     */ import com.badlogic.gdx.utils.SharedLibraryLoader;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
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
/*     */ public class FreeType
/*     */ {
/*     */   private static class Pointer
/*     */   {
/*     */     long address;
/*     */     
/*     */     Pointer(long address) {
/*  55 */       this.address = address;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Library extends Pointer implements Disposable {
/*  60 */     LongMap<ByteBuffer> fontData = new LongMap();
/*     */     
/*     */     Library(long address) {
/*  63 */       super(address);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  68 */       doneFreeType(this.address);
/*  69 */       for (ByteBuffer buffer : this.fontData.values()) {
/*  70 */         BufferUtils.disposeUnsafeByteBuffer(buffer);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private static native void doneFreeType(long param1Long);
/*     */ 
/*     */     
/*     */     public FreeType.Face newFace(FileHandle font, int faceIndex) {
/*  79 */       byte[] data = font.readBytes();
/*  80 */       return newMemoryFace(data, data.length, faceIndex);
/*     */     }
/*     */     
/*     */     public FreeType.Face newMemoryFace(byte[] data, int dataSize, int faceIndex) {
/*  84 */       ByteBuffer buffer = BufferUtils.newUnsafeByteBuffer(data.length);
/*  85 */       BufferUtils.copy(data, 0, buffer, data.length);
/*  86 */       return newMemoryFace(buffer, faceIndex);
/*     */     }
/*     */     
/*     */     public FreeType.Face newMemoryFace(ByteBuffer buffer, int faceIndex) {
/*  90 */       long face = newMemoryFace(this.address, buffer, buffer.remaining(), faceIndex);
/*  91 */       if (face == 0L) {
/*  92 */         BufferUtils.disposeUnsafeByteBuffer(buffer);
/*  93 */         throw new GdxRuntimeException("Couldn't load font, FreeType error code: " + FreeType.getLastErrorCode());
/*     */       } 
/*     */       
/*  96 */       this.fontData.put(face, buffer);
/*  97 */       return new FreeType.Face(face, this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static native long newMemoryFace(long param1Long, ByteBuffer param1ByteBuffer, int param1Int1, int param1Int2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FreeType.Stroker createStroker() {
/* 112 */       long stroker = strokerNew(this.address);
/* 113 */       if (stroker == 0L) throw new GdxRuntimeException("Couldn't create FreeType stroker, FreeType error code: " + FreeType.getLastErrorCode()); 
/* 114 */       return new FreeType.Stroker(stroker);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static native long strokerNew(long param1Long);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Face
/*     */     extends Pointer
/*     */     implements Disposable
/*     */   {
/*     */     FreeType.Library library;
/*     */ 
/*     */     
/*     */     public Face(long address, FreeType.Library library) {
/* 132 */       super(address);
/* 133 */       this.library = library;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 138 */       doneFace(this.address);
/* 139 */       ByteBuffer buffer = (ByteBuffer)this.library.fontData.get(this.address);
/* 140 */       if (buffer != null) {
/* 141 */         this.library.fontData.remove(this.address);
/* 142 */         BufferUtils.disposeUnsafeByteBuffer(buffer);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static native void doneFace(long param1Long);
/*     */ 
/*     */     
/*     */     public int getFaceFlags() {
/* 151 */       return getFaceFlags(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getFaceFlags(long param1Long);
/*     */ 
/*     */     
/*     */     public int getStyleFlags() {
/* 159 */       return getStyleFlags(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getStyleFlags(long param1Long);
/*     */ 
/*     */     
/*     */     public int getNumGlyphs() {
/* 167 */       return getNumGlyphs(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getNumGlyphs(long param1Long);
/*     */ 
/*     */     
/*     */     public int getAscender() {
/* 175 */       return getAscender(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getAscender(long param1Long);
/*     */ 
/*     */     
/*     */     public int getDescender() {
/* 183 */       return getDescender(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getDescender(long param1Long);
/*     */ 
/*     */     
/*     */     public int getHeight() {
/* 191 */       return getHeight(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getHeight(long param1Long);
/*     */ 
/*     */     
/*     */     public int getMaxAdvanceWidth() {
/* 199 */       return getMaxAdvanceWidth(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getMaxAdvanceWidth(long param1Long);
/*     */ 
/*     */     
/*     */     public int getMaxAdvanceHeight() {
/* 207 */       return getMaxAdvanceHeight(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getMaxAdvanceHeight(long param1Long);
/*     */ 
/*     */     
/*     */     public int getUnderlinePosition() {
/* 215 */       return getUnderlinePosition(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getUnderlinePosition(long param1Long);
/*     */ 
/*     */     
/*     */     public int getUnderlineThickness() {
/* 223 */       return getUnderlineThickness(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getUnderlineThickness(long param1Long);
/*     */ 
/*     */     
/*     */     public boolean selectSize(int strikeIndex) {
/* 231 */       return selectSize(this.address, strikeIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native boolean selectSize(long param1Long, int param1Int);
/*     */ 
/*     */     
/*     */     public boolean setCharSize(int charWidth, int charHeight, int horzResolution, int vertResolution) {
/* 239 */       return setCharSize(this.address, charWidth, charHeight, horzResolution, vertResolution);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native boolean setCharSize(long param1Long, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
/*     */ 
/*     */     
/*     */     public boolean setPixelSizes(int pixelWidth, int pixelHeight) {
/* 247 */       return setPixelSizes(this.address, pixelWidth, pixelHeight);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native boolean setPixelSizes(long param1Long, int param1Int1, int param1Int2);
/*     */ 
/*     */     
/*     */     public boolean loadGlyph(int glyphIndex, int loadFlags) {
/* 255 */       return loadGlyph(this.address, glyphIndex, loadFlags);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native boolean loadGlyph(long param1Long, int param1Int1, int param1Int2);
/*     */ 
/*     */     
/*     */     public boolean loadChar(int charCode, int loadFlags) {
/* 263 */       return loadChar(this.address, charCode, loadFlags);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native boolean loadChar(long param1Long, int param1Int1, int param1Int2);
/*     */ 
/*     */     
/*     */     public FreeType.GlyphSlot getGlyph() {
/* 271 */       return new FreeType.GlyphSlot(getGlyph(this.address));
/*     */     }
/*     */ 
/*     */     
/*     */     private static native long getGlyph(long param1Long);
/*     */ 
/*     */     
/*     */     public FreeType.Size getSize() {
/* 279 */       return new FreeType.Size(getSize(this.address));
/*     */     }
/*     */ 
/*     */     
/*     */     private static native long getSize(long param1Long);
/*     */ 
/*     */     
/*     */     public boolean hasKerning() {
/* 287 */       return hasKerning(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native boolean hasKerning(long param1Long);
/*     */ 
/*     */     
/*     */     public int getKerning(int leftGlyph, int rightGlyph, int kernMode) {
/* 295 */       return getKerning(this.address, leftGlyph, rightGlyph, kernMode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static native int getKerning(long param1Long, int param1Int1, int param1Int2, int param1Int3);
/*     */ 
/*     */ 
/*     */     
/*     */     public int getCharIndex(int charCode) {
/* 306 */       return getCharIndex(this.address, charCode);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getCharIndex(long param1Long, int param1Int);
/*     */   }
/*     */   
/*     */   public static class Size
/*     */     extends Pointer
/*     */   {
/*     */     Size(long address) {
/* 317 */       super(address);
/*     */     }
/*     */     
/*     */     public FreeType.SizeMetrics getMetrics() {
/* 321 */       return new FreeType.SizeMetrics(getMetrics(this.address));
/*     */     }
/*     */     
/*     */     private static native long getMetrics(long param1Long);
/*     */   }
/*     */   
/*     */   public static class SizeMetrics
/*     */     extends Pointer
/*     */   {
/*     */     SizeMetrics(long address) {
/* 331 */       super(address);
/*     */     }
/*     */     
/*     */     public int getXppem() {
/* 335 */       return getXppem(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getXppem(long param1Long);
/*     */ 
/*     */     
/*     */     public int getYppem() {
/* 343 */       return getYppem(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getYppem(long param1Long);
/*     */ 
/*     */     
/*     */     public int getXScale() {
/* 351 */       return getXscale(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getXscale(long param1Long);
/*     */ 
/*     */     
/*     */     public int getYscale() {
/* 359 */       return getYscale(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getYscale(long param1Long);
/*     */ 
/*     */     
/*     */     public int getAscender() {
/* 367 */       return getAscender(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getAscender(long param1Long);
/*     */ 
/*     */     
/*     */     public int getDescender() {
/* 375 */       return getDescender(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getDescender(long param1Long);
/*     */ 
/*     */     
/*     */     public int getHeight() {
/* 383 */       return getHeight(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getHeight(long param1Long);
/*     */ 
/*     */     
/*     */     public int getMaxAdvance() {
/* 391 */       return getMaxAdvance(this.address);
/*     */     }
/*     */     
/*     */     private static native int getMaxAdvance(long param1Long);
/*     */   }
/*     */   
/*     */   public static class GlyphSlot
/*     */     extends Pointer
/*     */   {
/*     */     GlyphSlot(long address) {
/* 401 */       super(address);
/*     */     }
/*     */     
/*     */     public FreeType.GlyphMetrics getMetrics() {
/* 405 */       return new FreeType.GlyphMetrics(getMetrics(this.address));
/*     */     }
/*     */ 
/*     */     
/*     */     private static native long getMetrics(long param1Long);
/*     */ 
/*     */     
/*     */     public int getLinearHoriAdvance() {
/* 413 */       return getLinearHoriAdvance(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getLinearHoriAdvance(long param1Long);
/*     */ 
/*     */     
/*     */     public int getLinearVertAdvance() {
/* 421 */       return getLinearVertAdvance(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getLinearVertAdvance(long param1Long);
/*     */ 
/*     */     
/*     */     public int getAdvanceX() {
/* 429 */       return getAdvanceX(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getAdvanceX(long param1Long);
/*     */ 
/*     */     
/*     */     public int getAdvanceY() {
/* 437 */       return getAdvanceY(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getAdvanceY(long param1Long);
/*     */ 
/*     */     
/*     */     public int getFormat() {
/* 445 */       return getFormat(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getFormat(long param1Long);
/*     */ 
/*     */     
/*     */     public FreeType.Bitmap getBitmap() {
/* 453 */       return new FreeType.Bitmap(getBitmap(this.address));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static native long getBitmap(long param1Long);
/*     */ 
/*     */     
/*     */     public int getBitmapLeft() {
/* 462 */       return getBitmapLeft(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getBitmapLeft(long param1Long);
/*     */ 
/*     */     
/*     */     public int getBitmapTop() {
/* 470 */       return getBitmapTop(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getBitmapTop(long param1Long);
/*     */ 
/*     */     
/*     */     public boolean renderGlyph(int renderMode) {
/* 478 */       return renderGlyph(this.address, renderMode);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native boolean renderGlyph(long param1Long, int param1Int);
/*     */ 
/*     */     
/*     */     public FreeType.Glyph getGlyph() {
/* 486 */       long glyph = getGlyph(this.address);
/* 487 */       if (glyph == 0L) throw new GdxRuntimeException("Couldn't get glyph, FreeType error code: " + FreeType.getLastErrorCode()); 
/* 488 */       return new FreeType.Glyph(glyph);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static native long getGlyph(long param1Long);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Glyph
/*     */     extends Pointer
/*     */     implements Disposable
/*     */   {
/*     */     private boolean rendered;
/*     */ 
/*     */     
/*     */     Glyph(long address) {
/* 506 */       super(address);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 511 */       done(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native void done(long param1Long);
/*     */ 
/*     */     
/*     */     public void strokeBorder(FreeType.Stroker stroker, boolean inside) {
/* 519 */       this.address = strokeBorder(this.address, stroker.address, inside);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static native long strokeBorder(long param1Long1, long param1Long2, boolean param1Boolean);
/*     */ 
/*     */ 
/*     */     
/*     */     public void toBitmap(int renderMode) {
/* 529 */       long bitmap = toBitmap(this.address, renderMode);
/* 530 */       if (bitmap == 0L) throw new GdxRuntimeException("Couldn't render glyph, FreeType error code: " + FreeType.getLastErrorCode()); 
/* 531 */       this.address = bitmap;
/* 532 */       this.rendered = true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static native long toBitmap(long param1Long, int param1Int);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FreeType.Bitmap getBitmap() {
/* 546 */       if (!this.rendered) {
/* 547 */         throw new GdxRuntimeException("Glyph is not yet rendered");
/*     */       }
/* 549 */       return new FreeType.Bitmap(getBitmap(this.address));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static native long getBitmap(long param1Long);
/*     */ 
/*     */     
/*     */     public int getLeft() {
/* 558 */       if (!this.rendered) {
/* 559 */         throw new GdxRuntimeException("Glyph is not yet rendered");
/*     */       }
/* 561 */       return getLeft(this.address);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static native int getLeft(long param1Long);
/*     */ 
/*     */     
/*     */     public int getTop() {
/* 570 */       if (!this.rendered) {
/* 571 */         throw new GdxRuntimeException("Glyph is not yet rendered");
/*     */       }
/* 573 */       return getTop(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getTop(long param1Long);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Bitmap
/*     */     extends Pointer
/*     */   {
/*     */     Bitmap(long address) {
/* 585 */       super(address);
/*     */     }
/*     */     
/*     */     public int getRows() {
/* 589 */       return getRows(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getRows(long param1Long);
/*     */ 
/*     */     
/*     */     public int getWidth() {
/* 597 */       return getWidth(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getWidth(long param1Long);
/*     */ 
/*     */     
/*     */     public int getPitch() {
/* 605 */       return getPitch(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getPitch(long param1Long);
/*     */ 
/*     */     
/*     */     public ByteBuffer getBuffer() {
/* 613 */       if (getRows() == 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 619 */         return BufferUtils.newByteBuffer(1); } 
/* 620 */       return getBuffer(this.address);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static native ByteBuffer getBuffer(long param1Long);
/*     */ 
/*     */     
/*     */     public Pixmap getPixmap(Pixmap.Format format, Color color, float gamma) {
/*     */       Pixmap pixmap;
/* 630 */       int width = getWidth(), rows = getRows();
/* 631 */       ByteBuffer src = getBuffer();
/*     */       
/* 633 */       int pixelMode = getPixelMode();
/* 634 */       int rowBytes = Math.abs(getPitch());
/* 635 */       if (color == Color.WHITE && pixelMode == FreeType.FT_PIXEL_MODE_GRAY && rowBytes == width && gamma == 1.0F) {
/* 636 */         pixmap = new Pixmap(width, rows, Pixmap.Format.Alpha);
/* 637 */         BufferUtils.copy(src, pixmap.getPixels(), pixmap.getPixels().capacity());
/*     */       } else {
/* 639 */         pixmap = new Pixmap(width, rows, Pixmap.Format.RGBA8888);
/* 640 */         int rgba = Color.rgba8888(color);
/* 641 */         byte[] srcRow = new byte[rowBytes];
/* 642 */         int[] dstRow = new int[width];
/* 643 */         IntBuffer dst = pixmap.getPixels().asIntBuffer();
/* 644 */         if (pixelMode == FreeType.FT_PIXEL_MODE_MONO) {
/*     */           
/* 646 */           for (int y = 0; y < rows; y++) {
/* 647 */             src.get(srcRow);
/* 648 */             for (int i = 0, x = 0; x < width; i++, x += 8) {
/* 649 */               byte b = srcRow[i];
/* 650 */               for (int ii = 0, n = Math.min(8, width - x); ii < n; ii++) {
/* 651 */                 if ((b & 1 << 7 - ii) != 0) {
/* 652 */                   dstRow[x + ii] = rgba;
/*     */                 } else {
/* 654 */                   dstRow[x + ii] = 0;
/*     */                 } 
/*     */               } 
/* 657 */             }  dst.put(dstRow);
/*     */           } 
/*     */         } else {
/*     */           
/* 661 */           int rgb = rgba & 0xFFFFFF00;
/* 662 */           int a = rgba & 0xFF;
/* 663 */           for (int y = 0; y < rows; y++) {
/* 664 */             src.get(srcRow);
/* 665 */             for (int x = 0; x < width; x++) {
/* 666 */               float alpha = (srcRow[x] & 0xFF) / 255.0F;
/* 667 */               alpha = (float)Math.pow(alpha, gamma);
/* 668 */               dstRow[x] = rgb | (int)(a * alpha);
/*     */             } 
/* 670 */             dst.put(dstRow);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 675 */       Pixmap converted = pixmap;
/* 676 */       if (format != pixmap.getFormat()) {
/* 677 */         converted = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), format);
/* 678 */         Pixmap.Blending blending = Pixmap.getBlending();
/* 679 */         Pixmap.setBlending(Pixmap.Blending.None);
/* 680 */         converted.drawPixmap(pixmap, 0, 0);
/* 681 */         Pixmap.setBlending(blending);
/* 682 */         pixmap.dispose();
/*     */       } 
/* 684 */       return converted;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getNumGray() {
/* 689 */       return getNumGray(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getNumGray(long param1Long);
/*     */ 
/*     */     
/*     */     public int getPixelMode() {
/* 697 */       return getPixelMode(this.address);
/*     */     }
/*     */     
/*     */     private static native int getPixelMode(long param1Long);
/*     */   }
/*     */   
/*     */   public static class GlyphMetrics
/*     */     extends Pointer
/*     */   {
/*     */     GlyphMetrics(long address) {
/* 707 */       super(address);
/*     */     }
/*     */     
/*     */     public int getWidth() {
/* 711 */       return getWidth(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getWidth(long param1Long);
/*     */ 
/*     */     
/*     */     public int getHeight() {
/* 719 */       return getHeight(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getHeight(long param1Long);
/*     */ 
/*     */     
/*     */     public int getHoriBearingX() {
/* 727 */       return getHoriBearingX(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getHoriBearingX(long param1Long);
/*     */ 
/*     */     
/*     */     public int getHoriBearingY() {
/* 735 */       return getHoriBearingY(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getHoriBearingY(long param1Long);
/*     */ 
/*     */     
/*     */     public int getHoriAdvance() {
/* 743 */       return getHoriAdvance(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getHoriAdvance(long param1Long);
/*     */ 
/*     */     
/*     */     public int getVertBearingX() {
/* 751 */       return getVertBearingX(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getVertBearingX(long param1Long);
/*     */ 
/*     */     
/*     */     public int getVertBearingY() {
/* 759 */       return getVertBearingY(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native int getVertBearingY(long param1Long);
/*     */ 
/*     */     
/*     */     public int getVertAdvance() {
/* 767 */       return getVertAdvance(this.address);
/*     */     }
/*     */     
/*     */     private static native int getVertAdvance(long param1Long);
/*     */   }
/*     */   
/*     */   public static class Stroker
/*     */     extends Pointer
/*     */     implements Disposable {
/*     */     Stroker(long address) {
/* 777 */       super(address);
/*     */     }
/*     */     
/*     */     public void set(int radius, int lineCap, int lineJoin, int miterLimit) {
/* 781 */       set(this.address, radius, lineCap, lineJoin, miterLimit);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static native void set(long param1Long, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 790 */       done(this.address);
/*     */     }
/*     */ 
/*     */     
/*     */     private static native void done(long param1Long);
/*     */   }
/*     */ 
/*     */   
/* 798 */   public static int FT_PIXEL_MODE_NONE = 0;
/* 799 */   public static int FT_PIXEL_MODE_MONO = 1;
/* 800 */   public static int FT_PIXEL_MODE_GRAY = 2;
/* 801 */   public static int FT_PIXEL_MODE_GRAY2 = 3;
/* 802 */   public static int FT_PIXEL_MODE_GRAY4 = 4;
/* 803 */   public static int FT_PIXEL_MODE_LCD = 5;
/* 804 */   public static int FT_PIXEL_MODE_LCD_V = 6;
/*     */   
/*     */   private static int encode(char a, char b, char c, char d) {
/* 807 */     return a << 24 | b << 16 | c << 8 | d;
/*     */   }
/*     */   static native int getLastErrorCode();
/* 810 */   public static int FT_ENCODING_NONE = 0;
/* 811 */   public static int FT_ENCODING_MS_SYMBOL = encode('s', 'y', 'm', 'b');
/* 812 */   public static int FT_ENCODING_UNICODE = encode('u', 'n', 'i', 'c');
/* 813 */   public static int FT_ENCODING_SJIS = encode('s', 'j', 'i', 's');
/* 814 */   public static int FT_ENCODING_GB2312 = encode('g', 'b', ' ', ' ');
/* 815 */   public static int FT_ENCODING_BIG5 = encode('b', 'i', 'g', '5');
/* 816 */   public static int FT_ENCODING_WANSUNG = encode('w', 'a', 'n', 's');
/* 817 */   public static int FT_ENCODING_JOHAB = encode('j', 'o', 'h', 'a');
/* 818 */   public static int FT_ENCODING_ADOBE_STANDARD = encode('A', 'D', 'O', 'B');
/* 819 */   public static int FT_ENCODING_ADOBE_EXPERT = encode('A', 'D', 'B', 'E');
/* 820 */   public static int FT_ENCODING_ADOBE_CUSTOM = encode('A', 'D', 'B', 'C');
/* 821 */   public static int FT_ENCODING_ADOBE_LATIN_1 = encode('l', 'a', 't', '1');
/* 822 */   public static int FT_ENCODING_OLD_LATIN_2 = encode('l', 'a', 't', '2');
/* 823 */   public static int FT_ENCODING_APPLE_ROMAN = encode('a', 'r', 'm', 'n');
/*     */   
/* 825 */   public static int FT_FACE_FLAG_SCALABLE = 1;
/* 826 */   public static int FT_FACE_FLAG_FIXED_SIZES = 2;
/* 827 */   public static int FT_FACE_FLAG_FIXED_WIDTH = 4;
/* 828 */   public static int FT_FACE_FLAG_SFNT = 8;
/* 829 */   public static int FT_FACE_FLAG_HORIZONTAL = 16;
/* 830 */   public static int FT_FACE_FLAG_VERTICAL = 32;
/* 831 */   public static int FT_FACE_FLAG_KERNING = 64;
/* 832 */   public static int FT_FACE_FLAG_FAST_GLYPHS = 128;
/* 833 */   public static int FT_FACE_FLAG_MULTIPLE_MASTERS = 256;
/* 834 */   public static int FT_FACE_FLAG_GLYPH_NAMES = 512;
/* 835 */   public static int FT_FACE_FLAG_EXTERNAL_STREAM = 1024;
/* 836 */   public static int FT_FACE_FLAG_HINTER = 2048;
/* 837 */   public static int FT_FACE_FLAG_CID_KEYED = 4096;
/* 838 */   public static int FT_FACE_FLAG_TRICKY = 8192;
/*     */   
/* 840 */   public static int FT_STYLE_FLAG_ITALIC = 1;
/* 841 */   public static int FT_STYLE_FLAG_BOLD = 2;
/*     */   
/* 843 */   public static int FT_LOAD_DEFAULT = 0;
/* 844 */   public static int FT_LOAD_NO_SCALE = 1;
/* 845 */   public static int FT_LOAD_NO_HINTING = 2;
/* 846 */   public static int FT_LOAD_RENDER = 4;
/* 847 */   public static int FT_LOAD_NO_BITMAP = 8;
/* 848 */   public static int FT_LOAD_VERTICAL_LAYOUT = 16;
/* 849 */   public static int FT_LOAD_FORCE_AUTOHINT = 32;
/* 850 */   public static int FT_LOAD_CROP_BITMAP = 64;
/* 851 */   public static int FT_LOAD_PEDANTIC = 128;
/* 852 */   public static int FT_LOAD_IGNORE_GLOBAL_ADVANCE_WIDTH = 512;
/* 853 */   public static int FT_LOAD_NO_RECURSE = 1024;
/* 854 */   public static int FT_LOAD_IGNORE_TRANSFORM = 2048;
/* 855 */   public static int FT_LOAD_MONOCHROME = 4096;
/* 856 */   public static int FT_LOAD_LINEAR_DESIGN = 8192;
/* 857 */   public static int FT_LOAD_NO_AUTOHINT = 32768;
/*     */   
/* 859 */   public static int FT_LOAD_TARGET_NORMAL = 0;
/* 860 */   public static int FT_LOAD_TARGET_LIGHT = 65536;
/* 861 */   public static int FT_LOAD_TARGET_MONO = 131072;
/* 862 */   public static int FT_LOAD_TARGET_LCD = 196608;
/* 863 */   public static int FT_LOAD_TARGET_LCD_V = 262144;
/*     */   
/* 865 */   public static int FT_RENDER_MODE_NORMAL = 0;
/* 866 */   public static int FT_RENDER_MODE_LIGHT = 1;
/* 867 */   public static int FT_RENDER_MODE_MONO = 2;
/* 868 */   public static int FT_RENDER_MODE_LCD = 3;
/* 869 */   public static int FT_RENDER_MODE_LCD_V = 4;
/* 870 */   public static int FT_RENDER_MODE_MAX = 5;
/*     */   
/* 872 */   public static int FT_KERNING_DEFAULT = 0;
/* 873 */   public static int FT_KERNING_UNFITTED = 1;
/* 874 */   public static int FT_KERNING_UNSCALED = 2;
/*     */   
/* 876 */   public static int FT_STROKER_LINECAP_BUTT = 0;
/* 877 */   public static int FT_STROKER_LINECAP_ROUND = 1;
/* 878 */   public static int FT_STROKER_LINECAP_SQUARE = 2;
/*     */   
/* 880 */   public static int FT_STROKER_LINEJOIN_ROUND = 0;
/* 881 */   public static int FT_STROKER_LINEJOIN_BEVEL = 1;
/* 882 */   public static int FT_STROKER_LINEJOIN_MITER_VARIABLE = 2;
/* 883 */   public static int FT_STROKER_LINEJOIN_MITER = FT_STROKER_LINEJOIN_MITER_VARIABLE;
/* 884 */   public static int FT_STROKER_LINEJOIN_MITER_FIXED = 3;
/*     */   
/*     */   public static Library initFreeType() {
/* 887 */     (new SharedLibraryLoader()).load("gdx-freetype");
/* 888 */     long address = initFreeTypeJni();
/* 889 */     if (address == 0L) throw new GdxRuntimeException("Couldn't initialize FreeType library, FreeType error code: " + getLastErrorCode()); 
/* 890 */     return new Library(address);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static native long initFreeTypeJni();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toInt(int value) {
/* 904 */     return (value + 63 & 0xFFFFFFC0) >> 6;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\freetype\FreeType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */