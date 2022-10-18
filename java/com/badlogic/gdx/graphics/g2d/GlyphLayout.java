/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Colors;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.FloatArray;
/*     */ import com.badlogic.gdx.utils.Pool;
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
/*     */ public class GlyphLayout
/*     */   implements Pool.Poolable
/*     */ {
/*  35 */   public final Array<GlyphRun> runs = new Array();
/*     */   public float width;
/*     */   public float height;
/*  38 */   private final Array<Color> colorStack = new Array(4);
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout() {}
/*     */ 
/*     */   
/*     */   public GlyphLayout(BitmapFont font, CharSequence str) {
/*  46 */     setText(font, str);
/*     */   }
/*     */ 
/*     */   
/*     */   public GlyphLayout(BitmapFont font, CharSequence str, Color color, float targetWidth, int halign, boolean wrap) {
/*  51 */     setText(font, str, color, targetWidth, halign, wrap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout(BitmapFont font, CharSequence str, int start, int end, Color color, float targetWidth, int halign, boolean wrap, String truncate) {
/*  57 */     setText(font, str, start, end, color, targetWidth, halign, wrap, truncate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(BitmapFont font, CharSequence str) {
/*  63 */     setText(font, str, 0, str.length(), font.getColor(), 0.0F, 8, false, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(BitmapFont font, CharSequence str, Color color, float targetWidth, int halign, boolean wrap) {
/*  69 */     setText(font, str, 0, str.length(), color, targetWidth, halign, wrap, null);
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
/*     */   public void setText(BitmapFont font, CharSequence str, int start, int end, Color color, float targetWidth, int halign, boolean wrap, String truncate) {
/*  82 */     if (truncate != null) {
/*  83 */       wrap = true;
/*  84 */     } else if (targetWidth <= font.data.spaceWidth) {
/*  85 */       wrap = false;
/*     */     } 
/*  87 */     BitmapFont.BitmapFontData fontData = font.data;
/*  88 */     boolean markupEnabled = fontData.markupEnabled;
/*     */     
/*  90 */     Pool<GlyphRun> glyphRunPool = Pools.get(GlyphRun.class);
/*  91 */     Array<GlyphRun> runs = this.runs;
/*  92 */     glyphRunPool.freeAll(runs);
/*  93 */     runs.clear();
/*     */     
/*  95 */     float x = 0.0F, y = 0.0F, width = 0.0F;
/*  96 */     int lines = 0, blankLines = 0;
/*     */     
/*  98 */     Array<Color> colorStack = this.colorStack;
/*  99 */     Color nextColor = color;
/* 100 */     colorStack.add(color);
/* 101 */     Pool<Color> colorPool = Pools.get(Color.class);
/*     */     
/* 103 */     int runStart = start;
/*     */ 
/*     */     
/*     */     label101: while (true) {
/* 107 */       int runEnd = -1;
/* 108 */       boolean newline = false, colorRun = false;
/* 109 */       if (start == end) {
/* 110 */         if (runStart == end)
/* 111 */           break;  runEnd = end;
/*     */       } else {
/* 113 */         switch (str.charAt(start++)) {
/*     */           
/*     */           case '\n':
/* 116 */             runEnd = start - 1;
/* 117 */             newline = true;
/*     */             break;
/*     */           
/*     */           case '[':
/* 121 */             if (markupEnabled) {
/* 122 */               int length = parseColorMarkup(str, start, end, colorPool);
/* 123 */               if (length >= 0) {
/* 124 */                 runEnd = start - 1;
/* 125 */                 start += length + 1;
/* 126 */                 nextColor = (Color)colorStack.peek();
/* 127 */                 colorRun = true; break;
/* 128 */               }  if (length == -2) {
/* 129 */                 start++;
/*     */                 continue;
/*     */               } 
/*     */             } 
/*     */             break;
/*     */         } 
/*     */       
/*     */       } 
/* 137 */       if (runEnd != -1) {
/* 138 */         if (runEnd != runStart) {
/*     */           
/* 140 */           GlyphRun run = (GlyphRun)glyphRunPool.obtain();
/* 141 */           run.color.set(color);
/* 142 */           run.x = x;
/* 143 */           run.y = y;
/* 144 */           fontData.getGlyphs(run, str, runStart, runEnd, colorRun);
/* 145 */           if (run.glyphs.size == 0) {
/* 146 */             glyphRunPool.free(run);
/*     */           } else {
/* 148 */             runs.add(run);
/*     */ 
/*     */             
/* 151 */             float[] xAdvances = run.xAdvances.items;
/* 152 */             for (int j = 0, k = run.xAdvances.size; j < k; j++) {
/* 153 */               float xAdvance = xAdvances[j];
/* 154 */               x += xAdvance;
/*     */ 
/*     */               
/* 157 */               if (wrap && x > targetWidth && j > 1 && x - xAdvance + (((BitmapFont.Glyph)run.glyphs
/* 158 */                 .get(j - 1)).xoffset + ((BitmapFont.Glyph)run.glyphs.get(j - 1)).width) * fontData.scaleX - 1.0E-4F > targetWidth) {
/*     */                 GlyphRun next;
/*     */                 
/* 161 */                 if (truncate != null) {
/* 162 */                   truncate(fontData, run, targetWidth, truncate, j, glyphRunPool);
/* 163 */                   x = run.x + run.width;
/*     */                   
/*     */                   break label101;
/*     */                 } 
/* 167 */                 int wrapIndex = fontData.getWrapIndex(run.glyphs, j);
/* 168 */                 if ((run.x == 0.0F && wrapIndex == 0) || wrapIndex >= run.glyphs.size)
/*     */                 {
/* 170 */                   wrapIndex = j - 1;
/*     */                 }
/*     */                 
/* 173 */                 if (wrapIndex == 0) {
/* 174 */                   next = run;
/*     */                 } else {
/* 176 */                   next = wrap(fontData, run, glyphRunPool, wrapIndex, j);
/* 177 */                   runs.add(next);
/*     */                 } 
/*     */ 
/*     */                 
/* 181 */                 width = Math.max(width, run.x + run.width);
/* 182 */                 x = 0.0F;
/* 183 */                 y += fontData.down;
/* 184 */                 lines++;
/* 185 */                 next.x = 0.0F;
/* 186 */                 next.y = y;
/* 187 */                 j = -1;
/* 188 */                 k = next.xAdvances.size;
/* 189 */                 xAdvances = next.xAdvances.items;
/* 190 */                 run = next;
/*     */               } else {
/* 192 */                 run.width += xAdvance;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 197 */         if (newline) {
/*     */           
/* 199 */           width = Math.max(width, x);
/* 200 */           x = 0.0F;
/* 201 */           float down = fontData.down;
/* 202 */           if (runEnd == runStart) {
/* 203 */             down *= fontData.blankLineScale;
/* 204 */             blankLines++;
/*     */           } else {
/* 206 */             lines++;
/* 207 */           }  y += down;
/*     */         } 
/*     */         
/* 210 */         runStart = start;
/* 211 */         color = nextColor;
/*     */       } 
/*     */     } 
/* 214 */     width = Math.max(width, x);
/*     */     
/* 216 */     for (int i = 1, n = colorStack.size; i < n; i++)
/* 217 */       colorPool.free(colorStack.get(i)); 
/* 218 */     colorStack.clear();
/*     */ 
/*     */     
/* 221 */     if ((halign & 0x8) == 0) {
/* 222 */       boolean center = ((halign & 0x1) != 0);
/* 223 */       float lineWidth = 0.0F, lineY = -2.14748365E9F;
/* 224 */       int lineStart = 0, j = runs.size;
/* 225 */       for (int k = 0; k < j; k++) {
/* 226 */         GlyphRun run = (GlyphRun)runs.get(k);
/* 227 */         if (run.y != lineY) {
/* 228 */           lineY = run.y;
/* 229 */           float f = targetWidth - lineWidth;
/* 230 */           if (center) f /= 2.0F; 
/* 231 */           while (lineStart < k)
/* 232 */             ((GlyphRun)runs.get(lineStart++)).x += f; 
/* 233 */           lineWidth = 0.0F;
/*     */         } 
/* 235 */         lineWidth += run.width;
/*     */       } 
/* 237 */       float shift = targetWidth - lineWidth;
/* 238 */       if (center) shift /= 2.0F; 
/* 239 */       while (lineStart < j) {
/* 240 */         ((GlyphRun)runs.get(lineStart++)).x += shift;
/*     */       }
/*     */     } 
/* 243 */     this.width = width;
/* 244 */     this.height = fontData.capHeight + lines * fontData.lineHeight + blankLines * fontData.lineHeight * fontData.blankLineScale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void truncate(BitmapFont.BitmapFontData fontData, GlyphRun run, float targetWidth, String truncate, int widthIndex, Pool<GlyphRun> glyphRunPool) {
/* 251 */     GlyphRun truncateRun = (GlyphRun)glyphRunPool.obtain();
/* 252 */     fontData.getGlyphs(truncateRun, truncate, 0, truncate.length(), true);
/* 253 */     float truncateWidth = 0.0F;
/* 254 */     for (int i = 1, n = truncateRun.xAdvances.size; i < n; i++)
/* 255 */       truncateWidth += truncateRun.xAdvances.get(i); 
/* 256 */     targetWidth -= truncateWidth;
/*     */ 
/*     */     
/* 259 */     int count = 0;
/* 260 */     float width = run.x;
/* 261 */     while (count < run.xAdvances.size) {
/* 262 */       float xAdvance = run.xAdvances.get(count);
/* 263 */       width += xAdvance;
/* 264 */       if (width > targetWidth) {
/* 265 */         run.width = width - run.x - xAdvance;
/*     */         break;
/*     */       } 
/* 268 */       count++;
/*     */     } 
/*     */     
/* 271 */     if (count > 1) {
/*     */       
/* 273 */       run.glyphs.truncate(count - 1);
/* 274 */       run.xAdvances.truncate(count);
/* 275 */       adjustLastGlyph(fontData, run);
/* 276 */       if (truncateRun.xAdvances.size > 0) run.xAdvances.addAll(truncateRun.xAdvances, 1, truncateRun.xAdvances.size - 1);
/*     */     
/*     */     } else {
/* 279 */       run.glyphs.clear();
/* 280 */       run.xAdvances.clear();
/* 281 */       run.xAdvances.addAll(truncateRun.xAdvances);
/* 282 */       if (truncateRun.xAdvances.size > 0) run.width += truncateRun.xAdvances.get(0); 
/*     */     } 
/* 284 */     run.glyphs.addAll(truncateRun.glyphs);
/* 285 */     run.width += truncateWidth;
/*     */     
/* 287 */     glyphRunPool.free(truncateRun);
/*     */   }
/*     */   
/*     */   private GlyphRun wrap(BitmapFont.BitmapFontData fontData, GlyphRun first, Pool<GlyphRun> glyphRunPool, int wrapIndex, int widthIndex) {
/* 291 */     GlyphRun second = (GlyphRun)glyphRunPool.obtain();
/* 292 */     second.color.set(first.color);
/* 293 */     int glyphCount = first.glyphs.size;
/*     */ 
/*     */     
/* 296 */     while (widthIndex < wrapIndex) {
/* 297 */       first.width += first.xAdvances.get(widthIndex++);
/*     */     }
/*     */     
/* 300 */     while (widthIndex > wrapIndex + 1) {
/* 301 */       first.width -= first.xAdvances.get(--widthIndex);
/*     */     }
/*     */ 
/*     */     
/* 305 */     if (wrapIndex < glyphCount) {
/* 306 */       Array<BitmapFont.Glyph> glyphs1 = second.glyphs;
/* 307 */       Array<BitmapFont.Glyph> glyphs2 = first.glyphs;
/* 308 */       glyphs1.addAll(glyphs2, 0, wrapIndex);
/* 309 */       glyphs2.removeRange(0, wrapIndex - 1);
/* 310 */       first.glyphs = glyphs1;
/* 311 */       second.glyphs = glyphs2;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 316 */       FloatArray xAdvances1 = second.xAdvances;
/* 317 */       FloatArray xAdvances2 = first.xAdvances;
/* 318 */       xAdvances1.addAll(xAdvances2, 0, wrapIndex + 1);
/* 319 */       xAdvances2.removeRange(1, wrapIndex);
/* 320 */       xAdvances2.set(0, -((BitmapFont.Glyph)glyphs2.first()).xoffset * fontData.scaleX - fontData.padLeft);
/* 321 */       first.xAdvances = xAdvances1;
/* 322 */       second.xAdvances = xAdvances2;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     if (wrapIndex == 0) {
/*     */       
/* 331 */       glyphRunPool.free(first);
/* 332 */       this.runs.pop();
/*     */     } else {
/* 334 */       adjustLastGlyph(fontData, first);
/*     */     } 
/* 336 */     return second;
/*     */   }
/*     */ 
/*     */   
/*     */   private void adjustLastGlyph(BitmapFont.BitmapFontData fontData, GlyphRun run) {
/* 341 */     BitmapFont.Glyph last = (BitmapFont.Glyph)run.glyphs.peek();
/* 342 */     if (fontData.isWhitespace((char)last.id))
/* 343 */       return;  float width = (last.xoffset + last.width) * fontData.scaleX - fontData.padRight;
/* 344 */     run.width += width - run.xAdvances.peek();
/* 345 */     run.xAdvances.set(run.xAdvances.size - 1, width);
/*     */   }
/*     */   private int parseColorMarkup(CharSequence str, int start, int end, Pool<Color> colorPool) {
/*     */     int colorInt;
/* 349 */     if (start == end) return -1; 
/* 350 */     switch (str.charAt(start)) {
/*     */       
/*     */       case '#':
/* 353 */         colorInt = 0;
/* 354 */         for (i = start + 1; i < end; i++) {
/* 355 */           char ch = str.charAt(i);
/* 356 */           if (ch == ']') {
/* 357 */             if (i < start + 2 || i > start + 9)
/* 358 */               break;  if (i - start <= 7) {
/* 359 */               for (int ii = 0, nn = 9 - i - start; ii < nn; ii++)
/* 360 */                 colorInt <<= 4; 
/* 361 */               colorInt |= 0xFF;
/*     */             } 
/* 363 */             Color color = (Color)colorPool.obtain();
/* 364 */             this.colorStack.add(color);
/* 365 */             Color.rgba8888ToColor(color, colorInt);
/* 366 */             return i - start;
/*     */           } 
/* 368 */           if (ch >= '0' && ch <= '9') {
/* 369 */             colorInt = colorInt * 16 + ch - 48;
/* 370 */           } else if (ch >= 'a' && ch <= 'f') {
/* 371 */             colorInt = colorInt * 16 + ch - 87;
/* 372 */           } else if (ch >= 'A' && ch <= 'F') {
/* 373 */             colorInt = colorInt * 16 + ch - 55;
/*     */           } else {
/*     */             break;
/*     */           } 
/* 377 */         }  return -1;
/*     */       case '[':
/* 379 */         return -2;
/*     */       case ']':
/* 381 */         if (this.colorStack.size > 1) colorPool.free(this.colorStack.pop()); 
/* 382 */         return 0;
/*     */     } 
/*     */     
/* 385 */     int colorStart = start;
/* 386 */     for (int i = start + 1; i < end; ) {
/* 387 */       char ch = str.charAt(i);
/* 388 */       if (ch != ']') { i++; continue; }
/* 389 */        Color namedColor = Colors.get(str.subSequence(colorStart, i).toString());
/* 390 */       if (namedColor == null) return -1; 
/* 391 */       Color color = (Color)colorPool.obtain();
/* 392 */       this.colorStack.add(color);
/* 393 */       color.set(namedColor);
/* 394 */       return i - start;
/*     */     } 
/* 396 */     return -1;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 400 */     Pools.get(GlyphRun.class).freeAll(this.runs);
/* 401 */     this.runs.clear();
/*     */     
/* 403 */     this.width = 0.0F;
/* 404 */     this.height = 0.0F;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 408 */     if (this.runs.size == 0) return ""; 
/* 409 */     StringBuilder buffer = new StringBuilder(128);
/* 410 */     buffer.append(this.width);
/* 411 */     buffer.append('x');
/* 412 */     buffer.append(this.height);
/* 413 */     buffer.append('\n');
/* 414 */     for (int i = 0, n = this.runs.size; i < n; i++) {
/* 415 */       buffer.append(((GlyphRun)this.runs.get(i)).toString());
/* 416 */       buffer.append('\n');
/*     */     } 
/* 418 */     buffer.setLength(buffer.length() - 1);
/* 419 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   public static class GlyphRun
/*     */     implements Pool.Poolable
/*     */   {
/* 425 */     public Array<BitmapFont.Glyph> glyphs = new Array();
/*     */ 
/*     */     
/* 428 */     public FloatArray xAdvances = new FloatArray(); public float x; public float y;
/*     */     public float width;
/* 430 */     public final Color color = new Color();
/*     */     
/*     */     public void reset() {
/* 433 */       this.glyphs.clear();
/* 434 */       this.xAdvances.clear();
/* 435 */       this.width = 0.0F;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 439 */       StringBuilder buffer = new StringBuilder(this.glyphs.size);
/* 440 */       Array<BitmapFont.Glyph> glyphs = this.glyphs;
/* 441 */       for (int i = 0, n = glyphs.size; i < n; i++) {
/* 442 */         BitmapFont.Glyph g = (BitmapFont.Glyph)glyphs.get(i);
/* 443 */         buffer.append((char)g.id);
/*     */       } 
/* 445 */       buffer.append(", #");
/* 446 */       buffer.append(this.color);
/* 447 */       buffer.append(", ");
/* 448 */       buffer.append(this.x);
/* 449 */       buffer.append(", ");
/* 450 */       buffer.append(this.y);
/* 451 */       buffer.append(", ");
/* 452 */       buffer.append(this.width);
/* 453 */       return buffer.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\GlyphLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */