/*     */ package com.badlogic.gdx.tools.hiero.unicodefont;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
/*     */ import com.badlogic.gdx.tools.hiero.HieroSettings;
/*     */ import com.badlogic.gdx.tools.hiero.unicodefont.effects.Effect;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontFormatException;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.lwjgl.opengl.GL11;
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
/*     */ public class UnicodeFont
/*     */ {
/*     */   private static final int DISPLAY_LIST_CACHE_SIZE = 200;
/*     */   private static final int MAX_GLYPH_CODE = 1114111;
/*     */   private static final int PAGE_SIZE = 512;
/*     */   private static final int PAGES = 2175;
/*     */   private Font font;
/*     */   private FontMetrics metrics;
/*     */   private String ttfFileRef;
/*     */   private int ascent;
/*     */   private int descent;
/*     */   private int leading;
/*     */   private int spaceWidth;
/*  69 */   private final Glyph[][] glyphs = new Glyph[2175][];
/*  70 */   private final List<GlyphPage> glyphPages = new ArrayList<GlyphPage>();
/*  71 */   private final List<Glyph> queuedGlyphs = new ArrayList<Glyph>(256); private int paddingTop; private int paddingLeft; private int paddingBottom; private int paddingRight;
/*  72 */   private final List<Effect> effects = new ArrayList<Effect>(); private int paddingAdvanceX;
/*     */   private int paddingAdvanceY;
/*     */   private Glyph missingGlyph;
/*  75 */   private int glyphPageWidth = 512; private int glyphPageHeight = 512; RenderType renderType; BitmapFont bitmapFont; private FreeTypeFontGenerator generator; private BitmapFontCache cache; private GlyphLayout layout;
/*     */   private boolean mono;
/*     */   private float gamma;
/*     */   private static final int X = 0;
/*     */   private static final int Y = 1;
/*     */   private static final int U = 3;
/*     */   private static final int V = 4;
/*     */   private static final int X2 = 10;
/*     */   private static final int Y2 = 11;
/*     */   private static final int U2 = 13;
/*     */   private static final int V2 = 14;
/*     */   
/*     */   public UnicodeFont(String ttfFileRef, String hieroFileRef) {
/*  88 */     this(ttfFileRef, new HieroSettings(hieroFileRef));
/*     */   }
/*     */ 
/*     */   
/*     */   public UnicodeFont(String ttfFileRef, HieroSettings settings) {
/*  93 */     this.ttfFileRef = ttfFileRef;
/*  94 */     Font font = createFont(ttfFileRef);
/*  95 */     initializeFont(font, settings.getFontSize(), settings.isBold(), settings.isItalic());
/*  96 */     loadSettings(settings);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnicodeFont(String ttfFileRef, int size, boolean bold, boolean italic) {
/* 101 */     this.ttfFileRef = ttfFileRef;
/* 102 */     initializeFont(createFont(ttfFileRef), size, bold, italic);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UnicodeFont(Font font, String hieroFileRef) {
/* 108 */     this(font, new HieroSettings(hieroFileRef));
/*     */   }
/*     */ 
/*     */   
/*     */   public UnicodeFont(Font font, HieroSettings settings) {
/* 113 */     initializeFont(font, settings.getFontSize(), settings.isBold(), settings.isItalic());
/* 114 */     loadSettings(settings);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnicodeFont(Font font) {
/* 119 */     initializeFont(font, font.getSize(), font.isBold(), font.isItalic());
/*     */   }
/*     */ 
/*     */   
/*     */   public UnicodeFont(Font font, int size, boolean bold, boolean italic) {
/* 124 */     initializeFont(font, size, bold, italic);
/*     */   }
/*     */   
/*     */   private void initializeFont(Font baseFont, int size, boolean bold, boolean italic) {
/* 128 */     Map<TextAttribute, ?> attributes = baseFont.getAttributes();
/* 129 */     attributes.put(TextAttribute.SIZE, new Float(size));
/* 130 */     attributes.put(TextAttribute.WEIGHT, bold ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
/* 131 */     attributes.put(TextAttribute.POSTURE, italic ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
/*     */     try {
/* 133 */       attributes.put(TextAttribute.class.getDeclaredField("KERNING").get(null), TextAttribute.class
/* 134 */           .getDeclaredField("KERNING_ON").get(null));
/* 135 */     } catch (Throwable throwable) {}
/*     */     
/* 137 */     this.font = baseFont.deriveFont((Map)attributes);
/*     */     
/* 139 */     this.metrics = GlyphPage.scratchGraphics.getFontMetrics(this.font);
/* 140 */     this.ascent = this.metrics.getAscent();
/* 141 */     this.descent = this.metrics.getDescent();
/* 142 */     this.leading = this.metrics.getLeading();
/*     */ 
/*     */     
/* 145 */     char[] chars = " ".toCharArray();
/* 146 */     GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
/* 147 */     this.spaceWidth = (vector.getGlyphLogicalBounds(0).getBounds()).width;
/*     */   }
/*     */   
/*     */   private void loadSettings(HieroSettings settings) {
/* 151 */     this.paddingTop = settings.getPaddingTop();
/* 152 */     this.paddingLeft = settings.getPaddingLeft();
/* 153 */     this.paddingBottom = settings.getPaddingBottom();
/* 154 */     this.paddingRight = settings.getPaddingRight();
/* 155 */     this.paddingAdvanceX = settings.getPaddingAdvanceX();
/* 156 */     this.paddingAdvanceY = settings.getPaddingAdvanceY();
/* 157 */     this.glyphPageWidth = settings.getGlyphPageWidth();
/* 158 */     this.glyphPageHeight = settings.getGlyphPageHeight();
/* 159 */     this.effects.addAll(settings.getEffects());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGlyphs(int startCodePoint, int endCodePoint) {
/* 168 */     for (int codePoint = startCodePoint; codePoint <= endCodePoint; codePoint++) {
/* 169 */       addGlyphs(new String(Character.toChars(codePoint)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addGlyphs(String text) {
/* 175 */     if (text == null) throw new IllegalArgumentException("text cannot be null.");
/*     */     
/* 177 */     char[] chars = text.toCharArray();
/* 178 */     GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
/* 179 */     for (int i = 0, n = vector.getNumGlyphs(); i < n; i++) {
/* 180 */       int codePoint = text.codePointAt(vector.getGlyphCharIndex(i));
/* 181 */       Rectangle bounds = getGlyphBounds(vector, i, codePoint);
/* 182 */       getGlyph(vector.getGlyphCode(i), codePoint, bounds, vector, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAsciiGlyphs() {
/* 189 */     addGlyphs(32, 255);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNeheGlyphs() {
/* 195 */     addGlyphs(32, 128);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean loadGlyphs() {
/* 202 */     return loadGlyphs(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean loadGlyphs(int maxGlyphsToLoad) {
/* 208 */     if (this.queuedGlyphs.isEmpty()) return false;
/*     */     
/* 210 */     if (this.effects.isEmpty()) {
/* 211 */       throw new IllegalStateException("The UnicodeFont must have at least one effect before any glyphs can be loaded.");
/*     */     }
/* 213 */     for (Iterator<Glyph> iterator = this.queuedGlyphs.iterator(); iterator.hasNext(); ) {
/* 214 */       Glyph glyph = iterator.next();
/* 215 */       int codePoint = glyph.getCodePoint();
/*     */ 
/*     */       
/* 218 */       if (glyph.isMissing()) {
/* 219 */         if (this.missingGlyph != null) {
/* 220 */           if (glyph != this.missingGlyph) iterator.remove(); 
/*     */           continue;
/*     */         } 
/* 223 */         this.missingGlyph = glyph;
/*     */       } 
/*     */     } 
/*     */     
/* 227 */     Collections.sort(this.queuedGlyphs, heightComparator);
/*     */ 
/*     */     
/* 230 */     for (Iterator<GlyphPage> iter = this.glyphPages.iterator(); iter.hasNext(); ) {
/* 231 */       GlyphPage glyphPage = iter.next();
/* 232 */       maxGlyphsToLoad -= glyphPage.loadGlyphs(this.queuedGlyphs, maxGlyphsToLoad);
/* 233 */       if (maxGlyphsToLoad == 0 || this.queuedGlyphs.isEmpty()) return true;
/*     */     
/*     */     } 
/*     */     
/* 237 */     while (!this.queuedGlyphs.isEmpty()) {
/* 238 */       GlyphPage glyphPage = new GlyphPage(this, this.glyphPageWidth, this.glyphPageHeight);
/* 239 */       this.glyphPages.add(glyphPage);
/* 240 */       maxGlyphsToLoad -= glyphPage.loadGlyphs(this.queuedGlyphs, maxGlyphsToLoad);
/* 241 */       if (maxGlyphsToLoad == 0) return true;
/*     */     
/*     */     } 
/* 244 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 250 */     for (Iterator<GlyphPage> iter = this.glyphPages.iterator(); iter.hasNext(); ) {
/* 251 */       GlyphPage page = iter.next();
/* 252 */       page.getTexture().dispose();
/*     */     } 
/* 254 */     if (this.bitmapFont != null) {
/* 255 */       this.bitmapFont.dispose();
/* 256 */       this.generator.dispose();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawString(float x, float y, String text, Color color, int startIndex, int endIndex) {
/* 261 */     if (text == null) throw new IllegalArgumentException("text cannot be null."); 
/* 262 */     if (text.length() == 0)
/* 263 */       return;  if (color == null) throw new IllegalArgumentException("color cannot be null.");
/*     */     
/* 265 */     x -= this.paddingLeft;
/* 266 */     y -= this.paddingTop;
/*     */     
/* 268 */     GL11.glColor4f(color.r, color.g, color.b, color.a);
/* 269 */     GL11.glTranslatef(x, y, 0.0F);
/*     */     
/* 271 */     if (this.renderType == RenderType.FreeType && this.bitmapFont != null) {
/* 272 */       drawBitmap(text, startIndex, endIndex);
/*     */     } else {
/* 274 */       drawUnicode(text, startIndex, endIndex);
/*     */     } 
/* 276 */     GL11.glTranslatef(-x, -y, 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawBitmap(String text, int startIndex, int endIndex) {
/* 283 */     BitmapFont.BitmapFontData data = this.bitmapFont.getData();
/* 284 */     int padY = this.paddingTop + this.paddingBottom + this.paddingAdvanceY;
/* 285 */     data.setLineHeight(data.lineHeight + padY);
/* 286 */     this.layout.setText(this.bitmapFont, text);
/* 287 */     data.setLineHeight(data.lineHeight - padY);
/* 288 */     for (GlyphLayout.GlyphRun run : this.layout.runs) {
/* 289 */       for (int j = 0, k = run.xAdvances.size; j < k; j++)
/* 290 */         run.xAdvances.incr(j, (this.paddingAdvanceX + this.paddingLeft + this.paddingRight)); 
/* 291 */     }  this.cache.setText(this.layout, this.paddingLeft, this.paddingRight);
/*     */     
/* 293 */     Array<TextureRegion> regions = this.bitmapFont.getRegions();
/* 294 */     for (int i = 0, n = regions.size; i < n; i++) {
/* 295 */       ((TextureRegion)regions.get(i)).getTexture().bind();
/* 296 */       GL11.glBegin(7);
/* 297 */       float[] vertices = this.cache.getVertices(i);
/* 298 */       for (int ii = 0, nn = vertices.length; ii < nn; ii += 20) {
/* 299 */         GL11.glTexCoord2f(vertices[ii + 3], vertices[ii + 4]);
/* 300 */         GL11.glVertex3f(vertices[ii + 0], vertices[ii + 1], 0.0F);
/* 301 */         GL11.glTexCoord2f(vertices[ii + 3], vertices[ii + 14]);
/* 302 */         GL11.glVertex3f(vertices[ii + 0], vertices[ii + 11], 0.0F);
/* 303 */         GL11.glTexCoord2f(vertices[ii + 13], vertices[ii + 14]);
/* 304 */         GL11.glVertex3f(vertices[ii + 10], vertices[ii + 11], 0.0F);
/* 305 */         GL11.glTexCoord2f(vertices[ii + 13], vertices[ii + 4]);
/* 306 */         GL11.glVertex3f(vertices[ii + 10], vertices[ii + 1], 0.0F);
/*     */       } 
/* 308 */       GL11.glEnd();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void drawUnicode(String text, int startIndex, int endIndex) {
/* 313 */     char[] chars = text.substring(0, endIndex).toCharArray();
/* 314 */     GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
/*     */     
/* 316 */     int maxWidth = 0, totalHeight = 0, lines = 0;
/* 317 */     int extraX = 0, extraY = this.ascent;
/* 318 */     boolean startNewLine = false;
/* 319 */     Texture lastBind = null;
/* 320 */     int offsetX = 0;
/* 321 */     for (int glyphIndex = 0, n = vector.getNumGlyphs(); glyphIndex < n; glyphIndex++) {
/* 322 */       int charIndex = vector.getGlyphCharIndex(glyphIndex);
/* 323 */       if (charIndex >= startIndex) {
/* 324 */         if (charIndex > endIndex)
/*     */           break; 
/* 326 */         int codePoint = text.codePointAt(charIndex);
/*     */         
/* 328 */         Rectangle bounds = getGlyphBounds(vector, glyphIndex, codePoint);
/* 329 */         bounds.x += offsetX;
/* 330 */         Glyph glyph = getGlyph(vector.getGlyphCode(glyphIndex), codePoint, bounds, vector, glyphIndex);
/*     */         
/* 332 */         if (startNewLine && codePoint != 10) {
/* 333 */           extraX = -bounds.x;
/* 334 */           startNewLine = false;
/*     */         } 
/*     */         
/* 337 */         if (glyph.getTexture() == null && this.missingGlyph != null && glyph.isMissing()) glyph = this.missingGlyph; 
/* 338 */         if (glyph.getTexture() != null) {
/*     */           
/* 340 */           Texture texture = glyph.getTexture();
/* 341 */           if (lastBind != null && lastBind != texture) {
/* 342 */             GL11.glEnd();
/* 343 */             lastBind = null;
/*     */           } 
/* 345 */           if (lastBind == null) {
/* 346 */             texture.bind();
/* 347 */             GL11.glBegin(7);
/* 348 */             lastBind = texture;
/*     */           } 
/* 350 */           int glyphX = bounds.x + extraX;
/* 351 */           int glyphY = bounds.y + extraY;
/* 352 */           GL11.glTexCoord2f(glyph.getU(), glyph.getV());
/* 353 */           GL11.glVertex3f(glyphX, glyphY, 0.0F);
/* 354 */           GL11.glTexCoord2f(glyph.getU(), glyph.getV2());
/* 355 */           GL11.glVertex3f(glyphX, (glyphY + glyph.getHeight()), 0.0F);
/* 356 */           GL11.glTexCoord2f(glyph.getU2(), glyph.getV2());
/* 357 */           GL11.glVertex3f((glyphX + glyph.getWidth()), (glyphY + glyph.getHeight()), 0.0F);
/* 358 */           GL11.glTexCoord2f(glyph.getU2(), glyph.getV());
/* 359 */           GL11.glVertex3f((glyphX + glyph.getWidth()), glyphY, 0.0F);
/*     */         } 
/*     */         
/* 362 */         if (glyphIndex > 0) extraX += this.paddingRight + this.paddingLeft + this.paddingAdvanceX; 
/* 363 */         maxWidth = Math.max(maxWidth, bounds.x + extraX + bounds.width);
/* 364 */         totalHeight = Math.max(totalHeight, this.ascent + bounds.y + bounds.height);
/*     */         
/* 366 */         if (codePoint == 10)
/* 367 */         { startNewLine = true;
/* 368 */           extraY += getLineHeight();
/* 369 */           lines++;
/* 370 */           totalHeight = 0; }
/* 371 */         else if (this.renderType == RenderType.Native) { offsetX += bounds.width; } 
/*     */       } 
/* 373 */     }  if (lastBind != null) GL11.glEnd(); 
/*     */   }
/*     */   
/*     */   public void drawString(float x, float y, String text) {
/* 377 */     drawString(x, y, text, Color.WHITE);
/*     */   }
/*     */   
/*     */   public void drawString(float x, float y, String text, Color col) {
/* 381 */     drawString(x, y, text, col, 0, text.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public Glyph getGlyph(int glyphCode, int codePoint, Rectangle bounds, GlyphVector vector, int index) {
/* 386 */     if (glyphCode < 0 || glyphCode >= 1114111)
/*     */     {
/* 388 */       return new Glyph(codePoint, bounds, vector, index, this) {
/*     */           public boolean isMissing() {
/* 390 */             return true;
/*     */           }
/*     */         };
/*     */     }
/* 394 */     int pageIndex = glyphCode / 512;
/* 395 */     int glyphIndex = glyphCode & 0x1FF;
/* 396 */     Glyph glyph = null;
/* 397 */     Glyph[] page = this.glyphs[pageIndex];
/* 398 */     if (page != null) {
/* 399 */       glyph = page[glyphIndex];
/* 400 */       if (glyph != null) return glyph; 
/*     */     } else {
/* 402 */       page = this.glyphs[pageIndex] = new Glyph[512];
/*     */     } 
/* 404 */     glyph = page[glyphIndex] = new Glyph(codePoint, bounds, vector, index, this);
/* 405 */     this.queuedGlyphs.add(glyph);
/* 406 */     return glyph;
/*     */   }
/*     */ 
/*     */   
/*     */   private Rectangle getGlyphBounds(GlyphVector vector, int index, int codePoint) {
/* 411 */     Rectangle bounds = vector.getGlyphPixelBounds(index, GlyphPage.renderContext, 0.0F, 0.0F);
/* 412 */     if (this.renderType == RenderType.Native)
/* 413 */       if (bounds.width == 0 || bounds.height == 0) {
/* 414 */         bounds = new Rectangle();
/*     */       } else {
/* 416 */         bounds = this.metrics.getStringBounds("" + (char)codePoint, GlyphPage.scratchGraphics).getBounds();
/*     */       }  
/* 418 */     if (codePoint == 32) bounds.width = this.spaceWidth; 
/* 419 */     return bounds;
/*     */   }
/*     */   
/*     */   public int getSpaceWidth() {
/* 423 */     return this.spaceWidth;
/*     */   }
/*     */   
/*     */   public int getWidth(String text) {
/* 427 */     if (text == null) throw new IllegalArgumentException("text cannot be null."); 
/* 428 */     if (text.length() == 0) return 0;
/*     */     
/* 430 */     char[] chars = text.toCharArray();
/* 431 */     GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
/*     */     
/* 433 */     int width = 0;
/* 434 */     int extraX = 0;
/* 435 */     boolean startNewLine = false;
/* 436 */     for (int glyphIndex = 0, n = vector.getNumGlyphs(); glyphIndex < n; glyphIndex++) {
/* 437 */       int charIndex = vector.getGlyphCharIndex(glyphIndex);
/* 438 */       int codePoint = text.codePointAt(charIndex);
/* 439 */       Rectangle bounds = getGlyphBounds(vector, glyphIndex, codePoint);
/*     */       
/* 441 */       if (startNewLine && codePoint != 10) extraX = -bounds.x;
/*     */       
/* 443 */       if (glyphIndex > 0) extraX += this.paddingLeft + this.paddingRight + this.paddingAdvanceX; 
/* 444 */       width = Math.max(width, bounds.x + extraX + bounds.width);
/*     */       
/* 446 */       if (codePoint == 10) startNewLine = true;
/*     */     
/*     */     } 
/* 449 */     return width;
/*     */   }
/*     */   
/*     */   public int getHeight(String text) {
/* 453 */     if (text == null) throw new IllegalArgumentException("text cannot be null."); 
/* 454 */     if (text.length() == 0) return 0;
/*     */     
/* 456 */     char[] chars = text.toCharArray();
/* 457 */     GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
/*     */     
/* 459 */     int lines = 0, height = 0;
/* 460 */     for (int i = 0, n = vector.getNumGlyphs(); i < n; i++) {
/* 461 */       int charIndex = vector.getGlyphCharIndex(i);
/* 462 */       int codePoint = text.codePointAt(charIndex);
/* 463 */       if (codePoint != 32) {
/* 464 */         Rectangle bounds = getGlyphBounds(vector, i, codePoint);
/*     */         
/* 466 */         height = Math.max(height, this.ascent + bounds.y + bounds.height);
/*     */         
/* 468 */         if (codePoint == 10) {
/* 469 */           lines++;
/* 470 */           height = 0;
/*     */         } 
/*     */       } 
/* 473 */     }  return lines * getLineHeight() + height;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getYOffset(String text) {
/* 478 */     if (text == null) throw new IllegalArgumentException("text cannot be null.");
/*     */     
/* 480 */     if (this.renderType == RenderType.FreeType && this.bitmapFont != null) return (int)this.bitmapFont.getAscent();
/*     */     
/* 482 */     int index = text.indexOf('\n');
/* 483 */     if (index != -1) text = text.substring(0, index); 
/* 484 */     char[] chars = text.toCharArray();
/* 485 */     GlyphVector vector = this.font.layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
/* 486 */     int yOffset = this.ascent + (vector.getPixelBounds(null, 0.0F, 0.0F)).y;
/*     */     
/* 488 */     return yOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Font getFont() {
/* 493 */     return this.font;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingTop() {
/* 498 */     return this.paddingTop;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingTop(int paddingTop) {
/* 503 */     this.paddingTop = paddingTop;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingLeft() {
/* 508 */     return this.paddingLeft;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingLeft(int paddingLeft) {
/* 513 */     this.paddingLeft = paddingLeft;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingBottom() {
/* 518 */     return this.paddingBottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingBottom(int paddingBottom) {
/* 523 */     this.paddingBottom = paddingBottom;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingRight() {
/* 528 */     return this.paddingRight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaddingRight(int paddingRight) {
/* 533 */     this.paddingRight = paddingRight;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingAdvanceX() {
/* 538 */     return this.paddingAdvanceX;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPaddingAdvanceX(int paddingAdvanceX) {
/* 544 */     this.paddingAdvanceX = paddingAdvanceX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPaddingAdvanceY() {
/* 549 */     return this.paddingAdvanceY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPaddingAdvanceY(int paddingAdvanceY) {
/* 555 */     this.paddingAdvanceY = paddingAdvanceY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineHeight() {
/* 561 */     return this.descent + this.ascent + this.leading + this.paddingTop + this.paddingBottom + this.paddingAdvanceY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAscent() {
/* 566 */     return this.ascent;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDescent() {
/* 571 */     return this.descent;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLeading() {
/* 576 */     return this.leading;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlyphPageWidth() {
/* 581 */     return this.glyphPageWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGlyphPageWidth(int glyphPageWidth) {
/* 586 */     this.glyphPageWidth = glyphPageWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGlyphPageHeight() {
/* 591 */     return this.glyphPageHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGlyphPageHeight(int glyphPageHeight) {
/* 596 */     this.glyphPageHeight = glyphPageHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public List getGlyphPages() {
/* 601 */     return this.glyphPages;
/*     */   }
/*     */ 
/*     */   
/*     */   public List getEffects() {
/* 606 */     return this.effects;
/*     */   }
/*     */   
/*     */   public boolean getMono() {
/* 610 */     return this.mono;
/*     */   }
/*     */   
/*     */   public void setMono(boolean mono) {
/* 614 */     this.mono = mono;
/*     */   }
/*     */   
/*     */   public float getGamma() {
/* 618 */     return this.gamma;
/*     */   }
/*     */   
/*     */   public void setGamma(float gamma) {
/* 622 */     this.gamma = gamma;
/*     */   }
/*     */   
/*     */   public RenderType getRenderType() {
/* 626 */     return this.renderType;
/*     */   }
/*     */   
/*     */   public void setRenderType(RenderType renderType) {
/* 630 */     this.renderType = renderType;
/*     */     
/* 632 */     if (renderType != RenderType.FreeType) {
/* 633 */       if (this.bitmapFont != null) {
/* 634 */         this.bitmapFont.dispose();
/* 635 */         this.generator.dispose();
/*     */       } 
/*     */     } else {
/* 638 */       String fontFile = getFontFile();
/* 639 */       if (fontFile != null) {
/* 640 */         this.generator = new FreeTypeFontGenerator(Gdx.files.absolute(fontFile));
/* 641 */         FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
/* 642 */         param.size = this.font.getSize();
/* 643 */         param.incremental = true;
/* 644 */         param.flip = true;
/* 645 */         param.mono = this.mono;
/* 646 */         param.gamma = this.gamma;
/* 647 */         this.bitmapFont = this.generator.generateFont(param);
/* 648 */         if ((this.bitmapFont.getData()).missingGlyph == null)
/* 649 */           (this.bitmapFont.getData()).missingGlyph = this.bitmapFont.getData().getGlyph('�'); 
/* 650 */         this.cache = this.bitmapFont.newFontCache();
/* 651 */         this.layout = new GlyphLayout();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFontFile() {
/* 659 */     if (this.ttfFileRef == null) {
/*     */       try {
/*     */         Object font2D;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 666 */           font2D = Class.forName("sun.font.FontUtilities").getDeclaredMethod("getFont2D", new Class[] { Font.class }).invoke(null, new Object[] { this.font });
/* 667 */         } catch (Throwable ignored) {
/*     */           
/* 669 */           font2D = Class.forName("sun.font.FontManager").getDeclaredMethod("getFont2D", new Class[] { Font.class }).invoke(null, new Object[] { this.font });
/*     */         } 
/* 671 */         Field platNameField = Class.forName("sun.font.PhysicalFont").getDeclaredField("platName");
/* 672 */         platNameField.setAccessible(true);
/* 673 */         this.ttfFileRef = (String)platNameField.get(font2D);
/* 674 */       } catch (Throwable throwable) {}
/*     */       
/* 676 */       if (this.ttfFileRef == null) this.ttfFileRef = ""; 
/*     */     } 
/* 678 */     if (this.ttfFileRef.length() == 0) return null; 
/* 679 */     return this.ttfFileRef;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Font createFont(String ttfFileRef) {
/*     */     try {
/* 685 */       return Font.createFont(0, Gdx.files.absolute(ttfFileRef).read());
/* 686 */     } catch (FontFormatException ex) {
/* 687 */       throw new GdxRuntimeException("Invalid font: " + ttfFileRef, ex);
/* 688 */     } catch (IOException ex) {
/* 689 */       throw new GdxRuntimeException("Error reading font: " + ttfFileRef, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 694 */   private static final Comparator heightComparator = new Comparator() {
/*     */       public int compare(Object o1, Object o2) {
/* 696 */         return ((Glyph)o2).getHeight() - ((Glyph)o1).getHeight();
/*     */       }
/*     */     };
/*     */   
/*     */   public enum RenderType {
/* 701 */     Java, Native, FreeType;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\UnicodeFont.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */