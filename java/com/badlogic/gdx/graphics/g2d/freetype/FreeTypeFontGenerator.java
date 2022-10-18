/*     */ package com.badlogic.gdx.graphics.g2d.freetype;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.graphics.g2d.PixmapPacker;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.BufferUtils;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public class FreeTypeFontGenerator
/*     */   implements Disposable
/*     */ {
/*     */   public static final String DEFAULT_CHARS = "\000ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$€-%+=#_&~* ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
/*     */   public static final int NO_MAXIMUM = -1;
/*  79 */   private static int maxTextureSize = 1024; final FreeType.Library library; final FreeType.Face face;
/*     */   final String name;
/*     */   
/*     */   public FreeTypeFontGenerator(FileHandle fontFile) {
/*     */     ByteBuffer buffer;
/*  84 */     this.bitmapped = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     this.name = fontFile.pathWithoutExtension();
/*  92 */     int fileSize = (int)fontFile.length();
/*     */     
/*  94 */     this.library = FreeType.initFreeType();
/*  95 */     if (this.library == null) throw new GdxRuntimeException("Couldn't initialize FreeType");
/*     */ 
/*     */     
/*  98 */     InputStream input = fontFile.read();
/*     */     try {
/* 100 */       if (fileSize == 0) {
/*     */         
/* 102 */         byte[] data = StreamUtils.copyStreamToByteArray(input, (fileSize > 0) ? (int)(fileSize * 1.5F) : 16384);
/* 103 */         buffer = BufferUtils.newUnsafeByteBuffer(data.length);
/* 104 */         BufferUtils.copy(data, 0, buffer, data.length);
/*     */       } else {
/*     */         
/* 107 */         buffer = BufferUtils.newUnsafeByteBuffer(fileSize);
/* 108 */         StreamUtils.copyStream(input, buffer);
/*     */       } 
/* 110 */     } catch (IOException ex) {
/* 111 */       throw new GdxRuntimeException(ex);
/*     */     } finally {
/* 113 */       StreamUtils.closeQuietly(input);
/*     */     } 
/*     */     
/* 116 */     this.face = this.library.newMemoryFace(buffer, 0);
/* 117 */     if (this.face == null) throw new GdxRuntimeException("Couldn't create face for font: " + fontFile);
/*     */     
/* 119 */     if (checkForBitmapFont())
/* 120 */       return;  setPixelSizes(0, 15);
/*     */   }
/*     */   boolean bitmapped; private int pixelWidth; private int pixelHeight;
/*     */   private int getLoadingFlags(FreeTypeFontParameter parameter) {
/* 124 */     int loadingFlags = FreeType.FT_LOAD_DEFAULT;
/* 125 */     switch (parameter.hinting) {
/*     */       case None:
/* 127 */         loadingFlags |= FreeType.FT_LOAD_NO_HINTING;
/*     */         break;
/*     */       case Slight:
/* 130 */         loadingFlags |= FreeType.FT_LOAD_TARGET_LIGHT;
/*     */         break;
/*     */       case Medium:
/* 133 */         loadingFlags |= FreeType.FT_LOAD_TARGET_NORMAL;
/*     */         break;
/*     */       case Full:
/* 136 */         loadingFlags |= FreeType.FT_LOAD_TARGET_MONO;
/*     */         break;
/*     */       case AutoSlight:
/* 139 */         loadingFlags |= FreeType.FT_LOAD_FORCE_AUTOHINT | FreeType.FT_LOAD_TARGET_LIGHT;
/*     */         break;
/*     */       case AutoMedium:
/* 142 */         loadingFlags |= FreeType.FT_LOAD_FORCE_AUTOHINT | FreeType.FT_LOAD_TARGET_NORMAL;
/*     */         break;
/*     */       case AutoFull:
/* 145 */         loadingFlags |= FreeType.FT_LOAD_FORCE_AUTOHINT | FreeType.FT_LOAD_TARGET_MONO;
/*     */         break;
/*     */     } 
/* 148 */     return loadingFlags;
/*     */   }
/*     */   
/*     */   private boolean loadChar(int c) {
/* 152 */     return loadChar(c, FreeType.FT_LOAD_DEFAULT | FreeType.FT_LOAD_FORCE_AUTOHINT);
/*     */   }
/*     */   
/*     */   private boolean loadChar(int c, int flags) {
/* 156 */     return this.face.loadChar(c, flags);
/*     */   }
/*     */   
/*     */   private boolean checkForBitmapFont() {
/* 160 */     int faceFlags = this.face.getFaceFlags();
/* 161 */     if ((faceFlags & FreeType.FT_FACE_FLAG_FIXED_SIZES) == FreeType.FT_FACE_FLAG_FIXED_SIZES && (faceFlags & FreeType.FT_FACE_FLAG_HORIZONTAL) == FreeType.FT_FACE_FLAG_HORIZONTAL)
/*     */     {
/* 163 */       if (loadChar(32)) {
/* 164 */         FreeType.GlyphSlot slot = this.face.getGlyph();
/* 165 */         if (slot.getFormat() == 1651078259) {
/* 166 */           this.bitmapped = true;
/*     */         }
/*     */       } 
/*     */     }
/* 170 */     return this.bitmapped;
/*     */   }
/*     */   
/*     */   public BitmapFont generateFont(FreeTypeFontParameter parameter) {
/* 174 */     return generateFont(parameter, new FreeTypeBitmapFontData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont generateFont(FreeTypeFontParameter parameter, FreeTypeBitmapFontData data) {
/* 181 */     generateData(parameter, data);
/* 182 */     if (data.regions == null && parameter.packer != null) {
/* 183 */       data.regions = new Array();
/* 184 */       parameter.packer.updateTextureRegions(data.regions, parameter.minFilter, parameter.magFilter, parameter.genMipMaps);
/*     */     } 
/* 186 */     BitmapFont font = new BitmapFont(data, data.regions, true);
/* 187 */     font.setOwnsTexture((parameter.packer == null));
/* 188 */     return font;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int scaleForPixelHeight(int height) {
/* 194 */     setPixelSizes(0, height);
/* 195 */     FreeType.SizeMetrics fontMetrics = this.face.getSize().getMetrics();
/* 196 */     int ascent = FreeType.toInt(fontMetrics.getAscender());
/* 197 */     int descent = FreeType.toInt(fontMetrics.getDescender());
/* 198 */     return height * height / (ascent - descent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int scaleForPixelWidth(int width, int numChars) {
/* 206 */     FreeType.SizeMetrics fontMetrics = this.face.getSize().getMetrics();
/* 207 */     int advance = FreeType.toInt(fontMetrics.getMaxAdvance());
/* 208 */     int ascent = FreeType.toInt(fontMetrics.getAscender());
/* 209 */     int descent = FreeType.toInt(fontMetrics.getDescender());
/* 210 */     int unscaledHeight = ascent - descent;
/* 211 */     int height = unscaledHeight * width / advance * numChars;
/* 212 */     setPixelSizes(0, height);
/* 213 */     return height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int scaleToFitSquare(int width, int height, int numChars) {
/* 222 */     return Math.min(scaleForPixelHeight(height), scaleForPixelWidth(width, numChars));
/*     */   }
/*     */   
/*     */   public class GlyphAndBitmap
/*     */   {
/*     */     public BitmapFont.Glyph glyph;
/*     */     public FreeType.Bitmap bitmap;
/*     */   }
/*     */   
/*     */   public GlyphAndBitmap generateGlyphAndBitmap(int c, int size, boolean flip) {
/*     */     FreeType.Bitmap bitmap;
/* 233 */     setPixelSizes(0, size);
/*     */     
/* 235 */     FreeType.SizeMetrics fontMetrics = this.face.getSize().getMetrics();
/* 236 */     int baseline = FreeType.toInt(fontMetrics.getAscender());
/*     */ 
/*     */ 
/*     */     
/* 240 */     if (this.face.getCharIndex(c) == 0) {
/* 241 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 245 */     if (!loadChar(c)) {
/* 246 */       throw new GdxRuntimeException("Unable to load character!");
/*     */     }
/*     */     
/* 249 */     FreeType.GlyphSlot slot = this.face.getGlyph();
/*     */ 
/*     */ 
/*     */     
/* 253 */     if (this.bitmapped) {
/* 254 */       bitmap = slot.getBitmap();
/* 255 */     } else if (!slot.renderGlyph(FreeType.FT_RENDER_MODE_NORMAL)) {
/* 256 */       bitmap = null;
/*     */     } else {
/* 258 */       bitmap = slot.getBitmap();
/*     */     } 
/*     */     
/* 261 */     FreeType.GlyphMetrics metrics = slot.getMetrics();
/*     */     
/* 263 */     BitmapFont.Glyph glyph = new BitmapFont.Glyph();
/* 264 */     if (bitmap != null) {
/* 265 */       glyph.width = bitmap.getWidth();
/* 266 */       glyph.height = bitmap.getRows();
/*     */     } else {
/* 268 */       glyph.width = 0;
/* 269 */       glyph.height = 0;
/*     */     } 
/* 271 */     glyph.xoffset = slot.getBitmapLeft();
/* 272 */     glyph.yoffset = flip ? (-slot.getBitmapTop() + baseline) : (-(glyph.height - slot.getBitmapTop()) - baseline);
/* 273 */     glyph.xadvance = FreeType.toInt(metrics.getHoriAdvance());
/* 274 */     glyph.srcX = 0;
/* 275 */     glyph.srcY = 0;
/* 276 */     glyph.id = c;
/*     */     
/* 278 */     GlyphAndBitmap result = new GlyphAndBitmap();
/* 279 */     result.glyph = glyph;
/* 280 */     result.bitmap = bitmap;
/* 281 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FreeTypeBitmapFontData generateData(int size) {
/* 287 */     FreeTypeFontParameter parameter = new FreeTypeFontParameter();
/* 288 */     parameter.size = size;
/* 289 */     return generateData(parameter);
/*     */   }
/*     */   
/*     */   public FreeTypeBitmapFontData generateData(FreeTypeFontParameter parameter) {
/* 293 */     return generateData(parameter, new FreeTypeBitmapFontData());
/*     */   }
/*     */   
/*     */   void setPixelSizes(int pixelWidth, int pixelHeight) {
/* 297 */     this.pixelWidth = pixelWidth;
/* 298 */     this.pixelHeight = pixelHeight;
/* 299 */     if (!this.bitmapped && !this.face.setPixelSizes(pixelWidth, pixelHeight)) throw new GdxRuntimeException("Couldn't set size for font");
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public FreeTypeBitmapFontData generateData(FreeTypeFontParameter parameter, FreeTypeBitmapFontData data) {
/* 305 */     parameter = (parameter == null) ? new FreeTypeFontParameter() : parameter;
/* 306 */     char[] characters = parameter.characters.toCharArray();
/* 307 */     int charactersLength = characters.length;
/* 308 */     boolean incremental = parameter.incremental;
/* 309 */     int flags = getLoadingFlags(parameter);
/*     */     
/* 311 */     setPixelSizes(0, parameter.size);
/*     */ 
/*     */     
/* 314 */     FreeType.SizeMetrics fontMetrics = this.face.getSize().getMetrics();
/* 315 */     data.flipped = parameter.flip;
/* 316 */     data.ascent = FreeType.toInt(fontMetrics.getAscender());
/* 317 */     data.descent = FreeType.toInt(fontMetrics.getDescender());
/* 318 */     data.lineHeight = FreeType.toInt(fontMetrics.getHeight());
/* 319 */     float baseLine = data.ascent;
/*     */ 
/*     */     
/* 322 */     if (this.bitmapped && data.lineHeight == 0.0F) {
/* 323 */       for (int c = 32; c < 32 + this.face.getNumGlyphs(); c++) {
/* 324 */         if (loadChar(c, flags)) {
/* 325 */           int lh = FreeType.toInt(this.face.getGlyph().getMetrics().getHeight());
/* 326 */           data.lineHeight = (lh > data.lineHeight) ? lh : data.lineHeight;
/*     */         } 
/*     */       } 
/*     */     }
/* 330 */     data.lineHeight += parameter.spaceY;
/*     */ 
/*     */     
/* 333 */     if (loadChar(32, flags) || loadChar(108, flags)) {
/* 334 */       data.spaceWidth = FreeType.toInt(this.face.getGlyph().getMetrics().getHoriAdvance());
/*     */     } else {
/* 336 */       data.spaceWidth = this.face.getMaxAdvanceWidth();
/*     */     }  char[] arrayOfChar1;
/*     */     int j;
/*     */     byte b;
/* 340 */     for (arrayOfChar1 = data.xChars, j = arrayOfChar1.length, b = 0; b < j; ) { char xChar = arrayOfChar1[b];
/* 341 */       if (!loadChar(xChar, flags)) { b++; continue; }
/* 342 */        data.xHeight = FreeType.toInt(this.face.getGlyph().getMetrics().getHeight()); }
/*     */ 
/*     */     
/* 345 */     if (data.xHeight == 0.0F) throw new GdxRuntimeException("No x-height character found in font");
/*     */ 
/*     */     
/* 348 */     for (arrayOfChar1 = data.capChars, j = arrayOfChar1.length, b = 0; b < j; ) { char capChar = arrayOfChar1[b];
/* 349 */       if (!loadChar(capChar, flags)) { b++; continue; }
/* 350 */        data.capHeight = FreeType.toInt(this.face.getGlyph().getMetrics().getHeight()); }
/*     */ 
/*     */     
/* 353 */     if (!this.bitmapped && data.capHeight == 1.0F) throw new GdxRuntimeException("No cap character found in font");
/*     */     
/* 355 */     data.ascent -= data.capHeight;
/* 356 */     data.down = -data.lineHeight;
/* 357 */     if (parameter.flip) {
/* 358 */       data.ascent = -data.ascent;
/* 359 */       data.down = -data.down;
/*     */     } 
/*     */     
/* 362 */     boolean ownsAtlas = false;
/*     */     
/* 364 */     PixmapPacker packer = parameter.packer;
/*     */     
/* 366 */     if (packer == null) {
/*     */       int size;
/*     */       
/*     */       PixmapPacker.SkylineStrategy skylineStrategy;
/* 370 */       if (incremental) {
/* 371 */         size = maxTextureSize;
/* 372 */         PixmapPacker.GuillotineStrategy guillotineStrategy = new PixmapPacker.GuillotineStrategy();
/*     */       } else {
/* 374 */         int maxGlyphHeight = (int)Math.ceil(data.lineHeight);
/* 375 */         size = MathUtils.nextPowerOfTwo((int)Math.sqrt((maxGlyphHeight * maxGlyphHeight * charactersLength)));
/* 376 */         if (maxTextureSize > 0) size = Math.min(size, maxTextureSize); 
/* 377 */         skylineStrategy = new PixmapPacker.SkylineStrategy();
/*     */       } 
/* 379 */       ownsAtlas = true;
/* 380 */       packer = new PixmapPacker(size, size, Pixmap.Format.RGBA8888, 1, false, (PixmapPacker.PackStrategy)skylineStrategy);
/* 381 */       packer.setTransparentColor(parameter.color);
/* 382 */       (packer.getTransparentColor()).a = 0.0F;
/* 383 */       if (parameter.borderWidth > 0.0F) {
/* 384 */         packer.setTransparentColor(parameter.borderColor);
/* 385 */         (packer.getTransparentColor()).a = 0.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 389 */     if (incremental) data.glyphs = new Array(charactersLength + 32);
/*     */     
/* 391 */     FreeType.Stroker stroker = null;
/* 392 */     if (parameter.borderWidth > 0.0F) {
/* 393 */       stroker = this.library.createStroker();
/* 394 */       stroker.set((int)(parameter.borderWidth * 64.0F), parameter.borderStraight ? FreeType.FT_STROKER_LINECAP_BUTT : FreeType.FT_STROKER_LINECAP_ROUND, parameter.borderStraight ? FreeType.FT_STROKER_LINEJOIN_MITER_FIXED : FreeType.FT_STROKER_LINEJOIN_ROUND, 0);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 399 */     BitmapFont.Glyph missingGlyph = createGlyph(false, data, parameter, stroker, baseLine, packer);
/* 400 */     if (missingGlyph != null && missingGlyph.width != 0 && missingGlyph.height != 0) {
/* 401 */       data.setGlyph(0, missingGlyph);
/* 402 */       if (incremental) data.glyphs.add(missingGlyph);
/*     */     
/*     */     } 
/*     */     
/* 406 */     int[] heights = new int[charactersLength];
/* 407 */     for (int i = 0, n = charactersLength; i < n; i++) {
/* 408 */       int height = loadChar(characters[i], flags) ? FreeType.toInt(this.face.getGlyph().getMetrics().getHeight()) : 0;
/* 409 */       heights[i] = height;
/*     */     } 
/* 411 */     int heightsCount = heights.length;
/* 412 */     while (heightsCount > 0) {
/* 413 */       int best = 0, maxHeight = heights[0];
/* 414 */       for (int k = 1; k < heightsCount; k++) {
/* 415 */         int height = heights[k];
/* 416 */         if (height > maxHeight) {
/* 417 */           maxHeight = height;
/* 418 */           best = k;
/*     */         } 
/*     */       } 
/*     */       
/* 422 */       char c = characters[best];
/* 423 */       BitmapFont.Glyph glyph = createGlyph(c, data, parameter, stroker, baseLine, packer);
/* 424 */       if (glyph != null) {
/* 425 */         data.setGlyph(c, glyph);
/* 426 */         if (incremental) data.glyphs.add(glyph);
/*     */       
/*     */       } 
/* 429 */       heightsCount--;
/* 430 */       heights[best] = heights[heightsCount];
/* 431 */       char tmpChar = characters[best];
/* 432 */       characters[best] = characters[heightsCount];
/* 433 */       characters[heightsCount] = tmpChar;
/*     */     } 
/*     */     
/* 436 */     if (stroker != null && !incremental) stroker.dispose();
/*     */     
/* 438 */     if (incremental) {
/* 439 */       data.generator = this;
/* 440 */       data.parameter = parameter;
/* 441 */       data.stroker = stroker;
/* 442 */       data.packer = packer;
/*     */     } 
/*     */ 
/*     */     
/* 446 */     parameter.kerning &= this.face.hasKerning();
/* 447 */     if (parameter.kerning)
/* 448 */       for (int k = 0; k < charactersLength; k++) {
/* 449 */         char firstChar = characters[k];
/* 450 */         BitmapFont.Glyph first = data.getGlyph(firstChar);
/* 451 */         if (first != null) {
/* 452 */           int firstIndex = this.face.getCharIndex(firstChar);
/* 453 */           for (int ii = k; ii < charactersLength; ii++) {
/* 454 */             char secondChar = characters[ii];
/* 455 */             BitmapFont.Glyph second = data.getGlyph(secondChar);
/* 456 */             if (second != null) {
/* 457 */               int secondIndex = this.face.getCharIndex(secondChar);
/*     */               
/* 459 */               int kerning = this.face.getKerning(firstIndex, secondIndex, 0);
/* 460 */               if (kerning != 0) first.setKerning(secondChar, FreeType.toInt(kerning));
/*     */               
/* 462 */               kerning = this.face.getKerning(secondIndex, firstIndex, 0);
/* 463 */               if (kerning != 0) second.setKerning(firstChar, FreeType.toInt(kerning));
/*     */             
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }  
/* 469 */     if (ownsAtlas) {
/* 470 */       data.regions = new Array();
/* 471 */       packer.updateTextureRegions(data.regions, parameter.minFilter, parameter.magFilter, parameter.genMipMaps);
/*     */     } 
/*     */ 
/*     */     
/* 475 */     BitmapFont.Glyph spaceGlyph = data.getGlyph(' ');
/* 476 */     if (spaceGlyph == null) {
/* 477 */       spaceGlyph = new BitmapFont.Glyph();
/* 478 */       spaceGlyph.xadvance = (int)data.spaceWidth + parameter.spaceX;
/* 479 */       spaceGlyph.id = 32;
/* 480 */       data.setGlyph(32, spaceGlyph);
/*     */     } 
/* 482 */     if (spaceGlyph.width == 0) spaceGlyph.width = (int)(spaceGlyph.xadvance + data.padRight);
/*     */     
/* 484 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BitmapFont.Glyph createGlyph(char c, FreeTypeBitmapFontData data, FreeTypeFontParameter parameter, FreeType.Stroker stroker, float baseLine, PixmapPacker packer) {
/* 491 */     boolean missing = (this.face.getCharIndex(c) == 0 && c != '\000');
/* 492 */     if (missing) return null;
/*     */     
/* 494 */     if (!loadChar(c, getLoadingFlags(parameter))) return null;
/*     */     
/* 496 */     FreeType.GlyphSlot slot = this.face.getGlyph();
/* 497 */     FreeType.Glyph mainGlyph = slot.getGlyph();
/*     */     try {
/* 499 */       mainGlyph.toBitmap(parameter.mono ? FreeType.FT_RENDER_MODE_MONO : FreeType.FT_RENDER_MODE_NORMAL);
/* 500 */     } catch (GdxRuntimeException e) {
/* 501 */       mainGlyph.dispose();
/* 502 */       Gdx.app.log("FreeTypeFontGenerator", "Couldn't render char: " + c);
/* 503 */       return null;
/*     */     } 
/* 505 */     FreeType.Bitmap mainBitmap = mainGlyph.getBitmap();
/* 506 */     Pixmap mainPixmap = mainBitmap.getPixmap(Pixmap.Format.RGBA8888, parameter.color, parameter.gamma);
/*     */     
/* 508 */     if (mainBitmap.getWidth() != 0 && mainBitmap.getRows() != 0) {
/* 509 */       int offsetX = 0, offsetY = 0;
/* 510 */       if (parameter.borderWidth > 0.0F) {
/*     */         
/* 512 */         int top = mainGlyph.getTop(), left = mainGlyph.getLeft();
/* 513 */         FreeType.Glyph borderGlyph = slot.getGlyph();
/* 514 */         borderGlyph.strokeBorder(stroker, false);
/* 515 */         borderGlyph.toBitmap(parameter.mono ? FreeType.FT_RENDER_MODE_MONO : FreeType.FT_RENDER_MODE_NORMAL);
/* 516 */         offsetX = left - borderGlyph.getLeft();
/* 517 */         offsetY = -(top - borderGlyph.getTop());
/*     */ 
/*     */         
/* 520 */         FreeType.Bitmap borderBitmap = borderGlyph.getBitmap();
/* 521 */         Pixmap borderPixmap = borderBitmap.getPixmap(Pixmap.Format.RGBA8888, parameter.borderColor, parameter.borderGamma);
/*     */ 
/*     */         
/* 524 */         for (int i = 0, n = parameter.renderCount; i < n; i++) {
/* 525 */           borderPixmap.drawPixmap(mainPixmap, offsetX, offsetY);
/*     */         }
/* 527 */         mainPixmap.dispose();
/* 528 */         mainGlyph.dispose();
/* 529 */         mainPixmap = borderPixmap;
/* 530 */         mainGlyph = borderGlyph;
/*     */       } 
/*     */       
/* 533 */       if (parameter.shadowOffsetX != 0 || parameter.shadowOffsetY != 0) {
/* 534 */         int mainW = mainPixmap.getWidth(), mainH = mainPixmap.getHeight();
/* 535 */         int shadowOffsetX = Math.max(parameter.shadowOffsetX, 0), shadowOffsetY = Math.max(parameter.shadowOffsetY, 0);
/* 536 */         int shadowW = mainW + Math.abs(parameter.shadowOffsetX), shadowH = mainH + Math.abs(parameter.shadowOffsetY);
/* 537 */         Pixmap shadowPixmap = new Pixmap(shadowW, shadowH, mainPixmap.getFormat());
/*     */         
/* 539 */         Color shadowColor = parameter.shadowColor;
/* 540 */         byte r = (byte)(int)(shadowColor.r * 255.0F), g = (byte)(int)(shadowColor.g * 255.0F), b = (byte)(int)(shadowColor.b * 255.0F);
/* 541 */         float a = shadowColor.a;
/*     */         
/* 543 */         ByteBuffer mainPixels = mainPixmap.getPixels();
/* 544 */         ByteBuffer shadowPixels = shadowPixmap.getPixels();
/* 545 */         for (int y = 0; y < mainH; y++) {
/* 546 */           int shadowRow = shadowW * (y + shadowOffsetY) + shadowOffsetX;
/* 547 */           for (int x = 0; x < mainW; x++) {
/* 548 */             int mainPixel = (mainW * y + x) * 4;
/* 549 */             byte mainA = mainPixels.get(mainPixel + 3);
/* 550 */             if (mainA != 0) {
/* 551 */               int shadowPixel = (shadowRow + x) * 4;
/* 552 */               shadowPixels.put(shadowPixel, r);
/* 553 */               shadowPixels.put(shadowPixel + 1, g);
/* 554 */               shadowPixels.put(shadowPixel + 2, b);
/* 555 */               shadowPixels.put(shadowPixel + 3, (byte)(int)((mainA & 0xFF) * a));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 560 */         for (int i = 0, n = parameter.renderCount; i < n; i++)
/* 561 */           shadowPixmap.drawPixmap(mainPixmap, Math.max(-parameter.shadowOffsetX, 0), Math.max(-parameter.shadowOffsetY, 0)); 
/* 562 */         mainPixmap.dispose();
/* 563 */         mainPixmap = shadowPixmap;
/* 564 */       } else if (parameter.borderWidth == 0.0F) {
/*     */         
/* 566 */         for (int i = 0, n = parameter.renderCount - 1; i < n; i++) {
/* 567 */           mainPixmap.drawPixmap(mainPixmap, 0, 0);
/*     */         }
/*     */       } 
/*     */     } 
/* 571 */     FreeType.GlyphMetrics metrics = slot.getMetrics();
/* 572 */     BitmapFont.Glyph glyph = new BitmapFont.Glyph();
/* 573 */     glyph.id = c;
/* 574 */     glyph.width = mainPixmap.getWidth();
/* 575 */     glyph.height = mainPixmap.getHeight();
/* 576 */     glyph.xoffset = mainGlyph.getLeft();
/* 577 */     glyph.yoffset = parameter.flip ? (-mainGlyph.getTop() + (int)baseLine) : (-(glyph.height - mainGlyph.getTop()) - (int)baseLine);
/* 578 */     glyph.xadvance = FreeType.toInt(metrics.getHoriAdvance()) + (int)parameter.borderWidth + parameter.spaceX;
/*     */     
/* 580 */     if (this.bitmapped) {
/* 581 */       mainPixmap.setColor(Color.CLEAR);
/* 582 */       mainPixmap.fill();
/* 583 */       ByteBuffer buf = mainBitmap.getBuffer();
/* 584 */       int whiteIntBits = Color.WHITE.toIntBits();
/* 585 */       int clearIntBits = Color.CLEAR.toIntBits();
/* 586 */       for (int h = 0; h < glyph.height; h++) {
/* 587 */         int idx = h * mainBitmap.getPitch();
/* 588 */         for (int w = 0; w < glyph.width + glyph.xoffset; w++) {
/* 589 */           int bit = buf.get(idx + w / 8) >>> 7 - w % 8 & 0x1;
/* 590 */           mainPixmap.drawPixel(w, h, (bit == 1) ? whiteIntBits : clearIntBits);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 596 */     Rectangle rect = packer.pack(mainPixmap);
/* 597 */     glyph.page = (packer.getPages()).size - 1;
/* 598 */     glyph.srcX = (int)rect.x;
/* 599 */     glyph.srcY = (int)rect.y;
/*     */ 
/*     */     
/* 602 */     if (parameter.incremental && data.regions != null && data.regions.size <= glyph.page) {
/* 603 */       packer.updateTextureRegions(data.regions, parameter.minFilter, parameter.magFilter, parameter.genMipMaps);
/*     */     }
/* 605 */     mainPixmap.dispose();
/* 606 */     mainGlyph.dispose();
/*     */     
/* 608 */     return glyph;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 614 */     this.face.dispose();
/* 615 */     this.library.dispose();
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
/*     */   public static void setMaxTextureSize(int texSize) {
/* 631 */     maxTextureSize = texSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getMaxTextureSize() {
/* 637 */     return maxTextureSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class FreeTypeBitmapFontData
/*     */     extends BitmapFont.BitmapFontData
/*     */     implements Disposable
/*     */   {
/*     */     Array<TextureRegion> regions;
/*     */     
/*     */     FreeTypeFontGenerator generator;
/*     */     
/*     */     FreeTypeFontGenerator.FreeTypeFontParameter parameter;
/*     */     
/*     */     FreeType.Stroker stroker;
/*     */     PixmapPacker packer;
/*     */     Array<BitmapFont.Glyph> glyphs;
/*     */     private boolean dirty;
/*     */     
/*     */     public BitmapFont.Glyph getGlyph(char ch) {
/* 657 */       BitmapFont.Glyph glyph = super.getGlyph(ch);
/* 658 */       if (glyph == null && this.generator != null) {
/* 659 */         this.generator.setPixelSizes(0, this.parameter.size);
/* 660 */         float baseline = ((this.flipped ? -this.ascent : this.ascent) + this.capHeight) / this.scaleY;
/* 661 */         glyph = this.generator.createGlyph(ch, this, this.parameter, this.stroker, baseline, this.packer);
/* 662 */         if (glyph == null) return this.missingGlyph;
/*     */         
/* 664 */         setGlyphRegion(glyph, (TextureRegion)this.regions.get(glyph.page));
/* 665 */         setGlyph(ch, glyph);
/* 666 */         this.glyphs.add(glyph);
/* 667 */         this.dirty = true;
/*     */         
/* 669 */         FreeType.Face face = this.generator.face;
/* 670 */         if (this.parameter.kerning) {
/* 671 */           int glyphIndex = face.getCharIndex(ch);
/* 672 */           for (int i = 0, n = this.glyphs.size; i < n; i++) {
/* 673 */             BitmapFont.Glyph other = (BitmapFont.Glyph)this.glyphs.get(i);
/* 674 */             int otherIndex = face.getCharIndex(other.id);
/*     */             
/* 676 */             int kerning = face.getKerning(glyphIndex, otherIndex, 0);
/* 677 */             if (kerning != 0) glyph.setKerning(other.id, FreeType.toInt(kerning));
/*     */             
/* 679 */             kerning = face.getKerning(otherIndex, glyphIndex, 0);
/* 680 */             if (kerning != 0) other.setKerning(ch, FreeType.toInt(kerning)); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 684 */       return glyph;
/*     */     }
/*     */     
/*     */     public void getGlyphs(GlyphLayout.GlyphRun run, CharSequence str, int start, int end, boolean tightBounds) {
/* 688 */       if (this.packer != null) this.packer.setPackToTexture(true); 
/* 689 */       super.getGlyphs(run, str, start, end, tightBounds);
/* 690 */       if (this.dirty) {
/* 691 */         this.dirty = false;
/* 692 */         this.packer.updateTextureRegions(this.regions, this.parameter.minFilter, this.parameter.magFilter, this.parameter.genMipMaps);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 698 */       if (this.stroker != null) this.stroker.dispose(); 
/* 699 */       if (this.packer != null) this.packer.dispose();
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Hinting
/*     */   {
/* 706 */     None,
/*     */     
/* 708 */     Slight,
/*     */     
/* 710 */     Medium,
/*     */     
/* 712 */     Full,
/*     */     
/* 714 */     AutoSlight,
/*     */     
/* 716 */     AutoMedium,
/*     */     
/* 718 */     AutoFull;
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
/*     */   public static class FreeTypeFontParameter
/*     */   {
/* 733 */     public int size = 16;
/*     */     
/*     */     public boolean mono;
/*     */     
/* 737 */     public FreeTypeFontGenerator.Hinting hinting = FreeTypeFontGenerator.Hinting.AutoMedium;
/*     */     
/* 739 */     public Color color = Color.WHITE;
/*     */     
/* 741 */     public float gamma = 1.8F;
/*     */     
/* 743 */     public int renderCount = 2;
/*     */     
/* 745 */     public float borderWidth = 0.0F;
/*     */     
/* 747 */     public Color borderColor = Color.BLACK;
/*     */     
/*     */     public boolean borderStraight = false;
/*     */     
/* 751 */     public float borderGamma = 1.8F;
/*     */     
/* 753 */     public int shadowOffsetX = 0;
/*     */     
/* 755 */     public int shadowOffsetY = 0;
/*     */     
/* 757 */     public Color shadowColor = new Color(0.0F, 0.0F, 0.0F, 0.75F);
/*     */     
/*     */     public int spaceX;
/*     */     public int spaceY;
/* 761 */     public String characters = "\000ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$€-%+=#_&~* ¡¢£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
/*     */     
/*     */     public boolean kerning = true;
/*     */     
/* 765 */     public PixmapPacker packer = null;
/*     */     
/*     */     public boolean flip = false;
/*     */     
/*     */     public boolean genMipMaps = false;
/*     */     
/* 771 */     public Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
/*     */     
/* 773 */     public Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
/*     */     public boolean incremental;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\freetype\FreeTypeFontGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */