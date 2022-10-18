/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.FloatArray;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.NumberUtils;
/*     */ import com.badlogic.gdx.utils.Pools;
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
/*     */ public class BitmapFontCache
/*     */ {
/*  36 */   private static final Color tempColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*  37 */   private static final float whiteTint = Color.WHITE.toFloatBits();
/*     */   
/*     */   private final BitmapFont font;
/*     */   private boolean integer;
/*  41 */   private final Array<GlyphLayout> layouts = new Array();
/*  42 */   private final Array<GlyphLayout> pooledLayouts = new Array();
/*     */   
/*     */   private int glyphCount;
/*  45 */   private final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   
/*     */   private float x;
/*     */   
/*     */   private float y;
/*     */   
/*     */   private float currentTint;
/*     */   
/*     */   private float[][] pageVertices;
/*     */   private int[] idx;
/*     */   private IntArray[] pageGlyphIndices;
/*     */   private int[] tempGlyphCount;
/*     */   
/*     */   public BitmapFontCache(BitmapFont font) {
/*  59 */     this(font, font.usesIntegerPositions());
/*     */   }
/*     */ 
/*     */   
/*     */   public BitmapFontCache(BitmapFont font, boolean integer) {
/*  64 */     this.font = font;
/*  65 */     this.integer = integer;
/*     */     
/*  67 */     int pageCount = font.regions.size;
/*  68 */     if (pageCount == 0) throw new IllegalArgumentException("The specified font must contain at least one texture page.");
/*     */     
/*  70 */     this.pageVertices = new float[pageCount][];
/*  71 */     this.idx = new int[pageCount];
/*  72 */     if (pageCount > 1) {
/*     */       
/*  74 */       this.pageGlyphIndices = new IntArray[pageCount];
/*  75 */       for (int i = 0, n = this.pageGlyphIndices.length; i < n; i++)
/*  76 */         this.pageGlyphIndices[i] = new IntArray(); 
/*     */     } 
/*  78 */     this.tempGlyphCount = new int[pageCount];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y) {
/*  85 */     translate(x - this.x, y - this.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void translate(float xAmount, float yAmount) {
/*  92 */     if (xAmount == 0.0F && yAmount == 0.0F)
/*  93 */       return;  if (this.integer) {
/*  94 */       xAmount = Math.round(xAmount);
/*  95 */       yAmount = Math.round(yAmount);
/*     */     } 
/*  97 */     this.x += xAmount;
/*  98 */     this.y += yAmount;
/*     */     
/* 100 */     float[][] pageVertices = this.pageVertices;
/* 101 */     for (int i = 0, n = pageVertices.length; i < n; i++) {
/* 102 */       float[] vertices = pageVertices[i];
/* 103 */       for (int ii = 0, nn = this.idx[i]; ii < nn; ii += 5) {
/* 104 */         vertices[ii] = vertices[ii] + xAmount;
/* 105 */         vertices[ii + 1] = vertices[ii + 1] + yAmount;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tint(Color tint) {
/* 112 */     float newTint = tint.toFloatBits();
/* 113 */     if (this.currentTint == newTint)
/* 114 */       return;  this.currentTint = newTint;
/*     */     
/* 116 */     int[] tempGlyphCount = this.tempGlyphCount; int i, n;
/* 117 */     for (i = 0, n = tempGlyphCount.length; i < n; i++) {
/* 118 */       tempGlyphCount[i] = 0;
/*     */     }
/* 120 */     for (i = 0, n = this.layouts.size; i < n; i++) {
/* 121 */       GlyphLayout layout = (GlyphLayout)this.layouts.get(i);
/* 122 */       for (int ii = 0, nn = layout.runs.size; ii < nn; ii++) {
/* 123 */         GlyphLayout.GlyphRun run = (GlyphLayout.GlyphRun)layout.runs.get(ii);
/* 124 */         Array<BitmapFont.Glyph> glyphs = run.glyphs;
/* 125 */         float colorFloat = tempColor.set(run.color).mul(tint).toFloatBits();
/* 126 */         for (int iii = 0, nnn = glyphs.size; iii < nnn; iii++) {
/* 127 */           BitmapFont.Glyph glyph = (BitmapFont.Glyph)glyphs.get(iii);
/* 128 */           int page = glyph.page;
/* 129 */           int offset = tempGlyphCount[page] * 20 + 2;
/* 130 */           tempGlyphCount[page] = tempGlyphCount[page] + 1;
/* 131 */           float[] vertices = this.pageVertices[page];
/* 132 */           for (int v = 0; v < 20; v += 5) {
/* 133 */             vertices[offset + v] = colorFloat;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setAlphas(float alpha) {
/* 141 */     int alphaBits = (int)(254.0F * alpha) << 24;
/* 142 */     float prev = 0.0F, newColor = 0.0F;
/* 143 */     for (int j = 0, length = this.pageVertices.length; j < length; j++) {
/* 144 */       float[] vertices = this.pageVertices[j];
/* 145 */       for (int i = 2, n = this.idx[j]; i < n; i += 5) {
/* 146 */         float c = vertices[i];
/* 147 */         if (c == prev && i != 2) {
/* 148 */           vertices[i] = newColor;
/*     */         } else {
/* 150 */           prev = c;
/* 151 */           int rgba = NumberUtils.floatToIntColor(c);
/* 152 */           rgba = rgba & 0xFFFFFF | alphaBits;
/* 153 */           newColor = NumberUtils.intToFloatColor(rgba);
/* 154 */           vertices[i] = newColor;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColors(float color) {
/* 162 */     for (int j = 0, length = this.pageVertices.length; j < length; j++) {
/* 163 */       float[] vertices = this.pageVertices[j];
/* 164 */       for (int i = 2, n = this.idx[j]; i < n; i += 5) {
/* 165 */         vertices[i] = color;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setColors(Color tint) {
/* 171 */     setColors(tint.toFloatBits());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColors(float r, float g, float b, float a) {
/* 176 */     int intBits = (int)(255.0F * a) << 24 | (int)(255.0F * b) << 16 | (int)(255.0F * g) << 8 | (int)(255.0F * r);
/* 177 */     setColors(NumberUtils.intToFloatColor(intBits));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColors(Color tint, int start, int end) {
/* 183 */     setColors(tint.toFloatBits(), start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColors(float color, int start, int end) {
/* 189 */     if (this.pageVertices.length == 1) {
/* 190 */       float[] vertices = this.pageVertices[0];
/* 191 */       for (int j = start * 20 + 2, n = end * 20; j < n; j += 5) {
/* 192 */         vertices[j] = color;
/*     */       }
/*     */       return;
/*     */     } 
/* 196 */     int pageCount = this.pageVertices.length;
/* 197 */     for (int i = 0; i < pageCount; i++) {
/* 198 */       float[] vertices = this.pageVertices[i];
/* 199 */       IntArray glyphIndices = this.pageGlyphIndices[i];
/*     */       
/* 201 */       for (int j = 0, n = glyphIndices.size; j < n; j++) {
/* 202 */         int glyphIndex = glyphIndices.items[j];
/*     */ 
/*     */         
/* 205 */         if (glyphIndex >= end) {
/*     */           break;
/*     */         }
/* 208 */         if (glyphIndex >= start) {
/* 209 */           for (int off = 0; off < 20; off += 5) {
/* 210 */             vertices[off + j * 20 + 2] = color;
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 219 */     return this.color;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 224 */     this.color.set(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(float r, float g, float b, float a) {
/* 229 */     this.color.set(r, g, b, a);
/*     */   }
/*     */   
/*     */   public void draw(Batch spriteBatch) {
/* 233 */     Array<TextureRegion> regions = this.font.getRegions();
/* 234 */     for (int j = 0, n = this.pageVertices.length; j < n; j++) {
/* 235 */       if (this.idx[j] > 0) {
/* 236 */         float[] vertices = this.pageVertices[j];
/* 237 */         spriteBatch.draw(((TextureRegion)regions.get(j)).getTexture(), vertices, 0, this.idx[j]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void draw(Batch spriteBatch, int start, int end) {
/* 243 */     if (this.pageVertices.length == 1) {
/* 244 */       spriteBatch.draw(this.font.getRegion().getTexture(), this.pageVertices[0], start * 20, (end - start) * 20);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 249 */     Array<TextureRegion> regions = this.font.getRegions();
/* 250 */     for (int i = 0, pageCount = this.pageVertices.length; i < pageCount; i++) {
/* 251 */       int offset = -1, count = 0;
/*     */ 
/*     */       
/* 254 */       IntArray glyphIndices = this.pageGlyphIndices[i];
/* 255 */       for (int ii = 0, n = glyphIndices.size; ii < n; ii++) {
/* 256 */         int glyphIndex = glyphIndices.get(ii);
/*     */ 
/*     */         
/* 259 */         if (glyphIndex >= end) {
/*     */           break;
/*     */         }
/* 262 */         if (offset == -1 && glyphIndex >= start) offset = ii;
/*     */ 
/*     */         
/* 265 */         if (glyphIndex >= start) {
/* 266 */           count++;
/*     */         }
/*     */       } 
/*     */       
/* 270 */       if (offset != -1 && count != 0)
/*     */       {
/*     */         
/* 273 */         spriteBatch.draw(((TextureRegion)regions.get(i)).getTexture(), this.pageVertices[i], offset * 20, count * 20); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void draw(Batch spriteBatch, float alphaModulation) {
/* 278 */     if (alphaModulation == 1.0F) {
/* 279 */       draw(spriteBatch);
/*     */       return;
/*     */     } 
/* 282 */     Color color = getColor();
/* 283 */     float oldAlpha = color.a;
/* 284 */     color.a *= alphaModulation;
/* 285 */     setColors(color);
/* 286 */     draw(spriteBatch);
/* 287 */     color.a = oldAlpha;
/* 288 */     setColors(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 293 */     this.x = 0.0F;
/* 294 */     this.y = 0.0F;
/* 295 */     Pools.freeAll(this.pooledLayouts, true);
/* 296 */     this.pooledLayouts.clear();
/* 297 */     this.layouts.clear();
/* 298 */     for (int i = 0, n = this.idx.length; i < n; i++) {
/* 299 */       if (this.pageGlyphIndices != null) this.pageGlyphIndices[i].clear(); 
/* 300 */       this.idx[i] = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void requireGlyphs(GlyphLayout layout) {
/* 305 */     if (this.pageVertices.length == 1) {
/*     */       
/* 307 */       int newGlyphCount = 0;
/* 308 */       for (int i = 0, n = layout.runs.size; i < n; i++)
/* 309 */         newGlyphCount += ((GlyphLayout.GlyphRun)layout.runs.get(i)).glyphs.size; 
/* 310 */       requirePageGlyphs(0, newGlyphCount);
/*     */     } else {
/* 312 */       int[] tempGlyphCount = this.tempGlyphCount; int i, n;
/* 313 */       for (i = 0, n = tempGlyphCount.length; i < n; i++) {
/* 314 */         tempGlyphCount[i] = 0;
/*     */       }
/* 316 */       for (i = 0, n = layout.runs.size; i < n; i++) {
/* 317 */         Array<BitmapFont.Glyph> glyphs = ((GlyphLayout.GlyphRun)layout.runs.get(i)).glyphs;
/* 318 */         for (int ii = 0, nn = glyphs.size; ii < nn; ii++) {
/* 319 */           tempGlyphCount[((BitmapFont.Glyph)glyphs.get(ii)).page] = tempGlyphCount[((BitmapFont.Glyph)glyphs.get(ii)).page] + 1;
/*     */         }
/*     */       } 
/* 322 */       for (i = 0, n = tempGlyphCount.length; i < n; i++)
/* 323 */         requirePageGlyphs(i, tempGlyphCount[i]); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void requirePageGlyphs(int page, int glyphCount) {
/* 328 */     if (this.pageGlyphIndices != null && 
/* 329 */       glyphCount > (this.pageGlyphIndices[page]).items.length) {
/* 330 */       this.pageGlyphIndices[page].ensureCapacity(glyphCount - (this.pageGlyphIndices[page]).items.length);
/*     */     }
/*     */     
/* 333 */     int vertexCount = this.idx[page] + glyphCount * 20;
/* 334 */     float[] vertices = this.pageVertices[page];
/* 335 */     if (vertices == null) {
/* 336 */       this.pageVertices[page] = new float[vertexCount];
/* 337 */     } else if (vertices.length < vertexCount) {
/* 338 */       float[] newVertices = new float[vertexCount];
/* 339 */       System.arraycopy(vertices, 0, newVertices, 0, this.idx[page]);
/* 340 */       this.pageVertices[page] = newVertices;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addToCache(GlyphLayout layout, float x, float y) {
/* 346 */     int pageCount = this.font.regions.size;
/* 347 */     if (this.pageVertices.length < pageCount) {
/* 348 */       float[][] newPageVertices = new float[pageCount][];
/* 349 */       System.arraycopy(this.pageVertices, 0, newPageVertices, 0, this.pageVertices.length);
/* 350 */       this.pageVertices = newPageVertices;
/*     */       
/* 352 */       int[] newIdx = new int[pageCount];
/* 353 */       System.arraycopy(this.idx, 0, newIdx, 0, this.idx.length);
/* 354 */       this.idx = newIdx;
/*     */       
/* 356 */       IntArray[] newPageGlyphIndices = new IntArray[pageCount];
/* 357 */       int pageGlyphIndicesLength = 0;
/* 358 */       if (this.pageGlyphIndices != null) {
/* 359 */         pageGlyphIndicesLength = this.pageGlyphIndices.length;
/* 360 */         System.arraycopy(this.pageGlyphIndices, 0, newPageGlyphIndices, 0, this.pageGlyphIndices.length);
/*     */       } 
/* 362 */       for (int j = pageGlyphIndicesLength; j < pageCount; j++)
/* 363 */         newPageGlyphIndices[j] = new IntArray(); 
/* 364 */       this.pageGlyphIndices = newPageGlyphIndices;
/*     */       
/* 366 */       this.tempGlyphCount = new int[pageCount];
/*     */     } 
/*     */     
/* 369 */     this.layouts.add(layout);
/* 370 */     requireGlyphs(layout);
/* 371 */     for (int i = 0, n = layout.runs.size; i < n; i++) {
/* 372 */       GlyphLayout.GlyphRun run = (GlyphLayout.GlyphRun)layout.runs.get(i);
/* 373 */       Array<BitmapFont.Glyph> glyphs = run.glyphs;
/* 374 */       FloatArray xAdvances = run.xAdvances;
/* 375 */       float color = run.color.toFloatBits();
/* 376 */       float gx = x + run.x, gy = y + run.y;
/* 377 */       for (int ii = 0, nn = glyphs.size; ii < nn; ii++) {
/* 378 */         BitmapFont.Glyph glyph = (BitmapFont.Glyph)glyphs.get(ii);
/* 379 */         gx += xAdvances.get(ii);
/* 380 */         addGlyph(glyph, gx, gy, color);
/*     */       } 
/*     */     } 
/*     */     
/* 384 */     this.currentTint = whiteTint;
/*     */   }
/*     */   
/*     */   private void addGlyph(BitmapFont.Glyph glyph, float x, float y, float color) {
/* 388 */     float scaleX = this.font.data.scaleX, scaleY = this.font.data.scaleY;
/* 389 */     x += glyph.xoffset * scaleX;
/* 390 */     y += glyph.yoffset * scaleY;
/* 391 */     float width = glyph.width * scaleX, height = glyph.height * scaleY;
/* 392 */     float u = glyph.u, u2 = glyph.u2, v = glyph.v, v2 = glyph.v2;
/*     */     
/* 394 */     if (this.integer) {
/* 395 */       x = Math.round(x);
/* 396 */       y = Math.round(y);
/* 397 */       width = Math.round(width);
/* 398 */       height = Math.round(height);
/*     */     } 
/* 400 */     float x2 = x + width, y2 = y + height;
/*     */     
/* 402 */     int page = glyph.page;
/* 403 */     int idx = this.idx[page];
/* 404 */     this.idx[page] = this.idx[page] + 20;
/*     */     
/* 406 */     if (this.pageGlyphIndices != null) this.pageGlyphIndices[page].add(this.glyphCount++);
/*     */     
/* 408 */     float[] vertices = this.pageVertices[page];
/* 409 */     vertices[idx++] = x;
/* 410 */     vertices[idx++] = y;
/* 411 */     vertices[idx++] = color;
/* 412 */     vertices[idx++] = u;
/* 413 */     vertices[idx++] = v;
/*     */     
/* 415 */     vertices[idx++] = x;
/* 416 */     vertices[idx++] = y2;
/* 417 */     vertices[idx++] = color;
/* 418 */     vertices[idx++] = u;
/* 419 */     vertices[idx++] = v2;
/*     */     
/* 421 */     vertices[idx++] = x2;
/* 422 */     vertices[idx++] = y2;
/* 423 */     vertices[idx++] = color;
/* 424 */     vertices[idx++] = u2;
/* 425 */     vertices[idx++] = v2;
/*     */     
/* 427 */     vertices[idx++] = x2;
/* 428 */     vertices[idx++] = y;
/* 429 */     vertices[idx++] = color;
/* 430 */     vertices[idx++] = u2;
/* 431 */     vertices[idx] = v;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout setText(CharSequence str, float x, float y) {
/* 437 */     clear();
/* 438 */     return addText(str, x, y, 0, str.length(), 0.0F, 8, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout setText(CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap) {
/* 444 */     clear();
/* 445 */     return addText(str, x, y, 0, str.length(), targetWidth, halign, wrap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout setText(CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap) {
/* 452 */     clear();
/* 453 */     return addText(str, x, y, start, end, targetWidth, halign, wrap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout setText(CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap, String truncate) {
/* 460 */     clear();
/* 461 */     return addText(str, x, y, start, end, targetWidth, halign, wrap, truncate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(GlyphLayout layout, float x, float y) {
/* 467 */     clear();
/* 468 */     addText(layout, x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout addText(CharSequence str, float x, float y) {
/* 474 */     return addText(str, x, y, 0, str.length(), 0.0F, 8, false, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout addText(CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap) {
/* 480 */     return addText(str, x, y, 0, str.length(), targetWidth, halign, wrap, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout addText(CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap) {
/* 487 */     return addText(str, x, y, start, end, targetWidth, halign, wrap, null);
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
/*     */   public GlyphLayout addText(CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap, String truncate) {
/* 503 */     GlyphLayout layout = (GlyphLayout)Pools.obtain(GlyphLayout.class);
/* 504 */     this.pooledLayouts.add(layout);
/* 505 */     layout.setText(this.font, str, start, end, this.color, targetWidth, halign, wrap, truncate);
/* 506 */     addText(layout, x, y);
/* 507 */     return layout;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addText(GlyphLayout layout, float x, float y) {
/* 512 */     addToCache(layout, x, y + this.font.data.ascent);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX() {
/* 517 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/* 522 */     return this.y;
/*     */   }
/*     */   
/*     */   public BitmapFont getFont() {
/* 526 */     return this.font;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseIntegerPositions(boolean use) {
/* 532 */     this.integer = use;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean usesIntegerPositions() {
/* 537 */     return this.integer;
/*     */   }
/*     */   
/*     */   public float[] getVertices() {
/* 541 */     return getVertices(0);
/*     */   }
/*     */   
/*     */   public float[] getVertices(int page) {
/* 545 */     return this.pageVertices[page];
/*     */   }
/*     */   
/*     */   public int getVertexCount(int page) {
/* 549 */     return this.idx[page];
/*     */   }
/*     */   
/*     */   public Array<GlyphLayout> getLayouts() {
/* 553 */     return this.layouts;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\BitmapFontCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */