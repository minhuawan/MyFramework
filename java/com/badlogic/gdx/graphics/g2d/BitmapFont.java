/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.FloatArray;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class BitmapFont
/*     */   implements Disposable
/*     */ {
/*     */   private static final int LOG2_PAGE_SIZE = 9;
/*     */   private static final int PAGE_SIZE = 512;
/*     */   private static final int PAGES = 128;
/*     */   final BitmapFontData data;
/*     */   Array<TextureRegion> regions;
/*     */   private final BitmapFontCache cache;
/*     */   private boolean flipped;
/*     */   boolean integer;
/*     */   private boolean ownsTexture;
/*     */   
/*     */   public BitmapFont() {
/*  72 */     this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"), Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), false, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(boolean flip) {
/*  80 */     this(Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.fnt"), Gdx.files.classpath("com/badlogic/gdx/utils/arial-15.png"), flip, true);
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
/*     */   public BitmapFont(FileHandle fontFile, TextureRegion region) {
/*  93 */     this(fontFile, region, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile, TextureRegion region, boolean flip) {
/* 103 */     this(new BitmapFontData(fontFile, flip), region, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile) {
/* 109 */     this(fontFile, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile, boolean flip) {
/* 116 */     this(new BitmapFontData(fontFile, flip), (TextureRegion)null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile, FileHandle imageFile, boolean flip) {
/* 123 */     this(fontFile, imageFile, flip, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(FileHandle fontFile, FileHandle imageFile, boolean flip, boolean integer) {
/* 131 */     this(new BitmapFontData(fontFile, flip), new TextureRegion(new Texture(imageFile, false)), integer);
/* 132 */     this.ownsTexture = true;
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
/*     */   public BitmapFont(BitmapFontData data, TextureRegion region, boolean integer) {
/* 144 */     this(data, (region != null) ? Array.with((Object[])new TextureRegion[] { region }, ) : null, integer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFont(BitmapFontData data, Array<TextureRegion> pageRegions, boolean integer) {
/* 152 */     this.flipped = data.flipped;
/* 153 */     this.data = data;
/* 154 */     this.integer = integer;
/*     */     
/* 156 */     if (pageRegions == null || pageRegions.size == 0) {
/*     */       
/* 158 */       int n = data.imagePaths.length;
/* 159 */       this.regions = new Array(n);
/* 160 */       for (int i = 0; i < n; i++) {
/*     */         FileHandle file;
/* 162 */         if (data.fontFile == null) {
/* 163 */           file = Gdx.files.internal(data.imagePaths[i]);
/*     */         } else {
/* 165 */           file = Gdx.files.getFileHandle(data.imagePaths[i], data.fontFile.type());
/* 166 */         }  this.regions.add(new TextureRegion(new Texture(file, false)));
/*     */       } 
/* 168 */       this.ownsTexture = true;
/*     */     } else {
/* 170 */       this.regions = pageRegions;
/* 171 */       this.ownsTexture = false;
/*     */     } 
/*     */     
/* 174 */     this.cache = newFontCache();
/*     */     
/* 176 */     load(data);
/*     */   }
/*     */   
/*     */   protected void load(BitmapFontData data) {
/* 180 */     for (Glyph[] page : data.glyphs) {
/* 181 */       if (page != null)
/* 182 */         for (Glyph glyph : page) {
/* 183 */           if (glyph != null) data.setGlyphRegion(glyph, (TextureRegion)this.regions.get(glyph.page)); 
/*     */         }  
/* 185 */     }  if (data.missingGlyph != null) data.setGlyphRegion(data.missingGlyph, (TextureRegion)this.regions.get(data.missingGlyph.page));
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public GlyphLayout draw(Batch batch, CharSequence str, float x, float y) {
/* 191 */     this.cache.clear();
/* 192 */     GlyphLayout layout = this.cache.addText(str, x, y);
/* 193 */     this.cache.draw(batch);
/* 194 */     return layout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap) {
/* 200 */     this.cache.clear();
/* 201 */     GlyphLayout layout = this.cache.addText(str, x, y, targetWidth, halign, wrap);
/* 202 */     this.cache.draw(batch);
/* 203 */     return layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap) {
/* 210 */     this.cache.clear();
/* 211 */     GlyphLayout layout = this.cache.addText(str, x, y, start, end, targetWidth, halign, wrap);
/* 212 */     this.cache.draw(batch);
/* 213 */     return layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int start, int end, float targetWidth, int halign, boolean wrap, String truncate) {
/* 220 */     this.cache.clear();
/* 221 */     GlyphLayout layout = this.cache.addText(str, x, y, start, end, targetWidth, halign, wrap, truncate);
/* 222 */     this.cache.draw(batch);
/* 223 */     return layout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, GlyphLayout layout, float x, float y) {
/* 229 */     this.cache.clear();
/* 230 */     this.cache.addText(layout, x, y);
/* 231 */     this.cache.draw(batch);
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 236 */     return this.cache.getColor();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 241 */     this.cache.getColor().set(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(float r, float g, float b, float a) {
/* 246 */     this.cache.getColor().set(r, g, b, a);
/*     */   }
/*     */   
/*     */   public float getScaleX() {
/* 250 */     return this.data.scaleX;
/*     */   }
/*     */   
/*     */   public float getScaleY() {
/* 254 */     return this.data.scaleY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion getRegion() {
/* 261 */     return (TextureRegion)this.regions.first();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<TextureRegion> getRegions() {
/* 267 */     return this.regions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion getRegion(int index) {
/* 273 */     return (TextureRegion)this.regions.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLineHeight() {
/* 278 */     return this.data.lineHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSpaceWidth() {
/* 283 */     return this.data.spaceWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getXHeight() {
/* 288 */     return this.data.xHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCapHeight() {
/* 294 */     return this.data.capHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAscent() {
/* 299 */     return this.data.ascent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDescent() {
/* 305 */     return this.data.descent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlipped() {
/* 310 */     return this.flipped;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 315 */     if (this.ownsTexture) {
/* 316 */       for (int i = 0; i < this.regions.size; i++) {
/* 317 */         ((TextureRegion)this.regions.get(i)).getTexture().dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFixedWidthGlyphs(CharSequence glyphs) {
/* 324 */     BitmapFontData data = this.data;
/* 325 */     int maxAdvance = 0; int index, end;
/* 326 */     for (index = 0, end = glyphs.length(); index < end; index++) {
/* 327 */       Glyph g = data.getGlyph(glyphs.charAt(index));
/* 328 */       if (g != null && g.xadvance > maxAdvance) maxAdvance = g.xadvance; 
/*     */     } 
/* 330 */     for (index = 0, end = glyphs.length(); index < end; index++) {
/* 331 */       Glyph g = data.getGlyph(glyphs.charAt(index));
/* 332 */       if (g != null) {
/* 333 */         g.xoffset += Math.round(((maxAdvance - g.xadvance) / 2));
/* 334 */         g.xadvance = maxAdvance;
/* 335 */         g.kerning = (byte[][])null;
/* 336 */         g.fixedWidth = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setUseIntegerPositions(boolean integer) {
/* 342 */     this.integer = integer;
/* 343 */     this.cache.setUseIntegerPositions(integer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean usesIntegerPositions() {
/* 348 */     return this.integer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFontCache getCache() {
/* 355 */     return this.cache;
/*     */   }
/*     */ 
/*     */   
/*     */   public BitmapFontData getData() {
/* 360 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ownsTexture() {
/* 365 */     return this.ownsTexture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOwnsTexture(boolean ownsTexture) {
/* 372 */     this.ownsTexture = ownsTexture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitmapFontCache newFontCache() {
/* 381 */     return new BitmapFontCache(this, this.integer);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 385 */     if (this.data.fontFile != null) return this.data.fontFile.nameWithoutExtension(); 
/* 386 */     return super.toString();
/*     */   }
/*     */   public static class Glyph { public int id;
/*     */     public int srcX;
/*     */     public int srcY;
/*     */     public int width;
/*     */     public int height;
/*     */     public float u;
/*     */     public float v;
/*     */     public float u2;
/*     */     public float v2;
/*     */     public int xoffset;
/*     */     public int yoffset;
/*     */     public int xadvance;
/*     */     public byte[][] kerning;
/*     */     public boolean fixedWidth;
/* 402 */     public int page = 0;
/*     */     
/*     */     public int getKerning(char ch) {
/* 405 */       if (this.kerning != null) {
/* 406 */         byte[] page = this.kerning[ch >>> 9];
/* 407 */         if (page != null) return page[ch & 0x1FF]; 
/*     */       } 
/* 409 */       return 0;
/*     */     }
/*     */     
/*     */     public void setKerning(int ch, int value) {
/* 413 */       if (this.kerning == null) this.kerning = new byte[128][]; 
/* 414 */       byte[] page = this.kerning[ch >>> 9];
/* 415 */       if (page == null) this.kerning[ch >>> 9] = page = new byte[512]; 
/* 416 */       page[ch & 0x1FF] = (byte)value;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 420 */       return Character.toString((char)this.id);
/*     */     } }
/*     */ 
/*     */   
/*     */   static int indexOf(CharSequence text, char ch, int start) {
/* 425 */     int n = text.length();
/* 426 */     for (; start < n; start++) {
/* 427 */       if (text.charAt(start) == ch) return start; 
/* 428 */     }  return n;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class BitmapFontData
/*     */   {
/*     */     public String[] imagePaths;
/*     */     public FileHandle fontFile;
/*     */     public boolean flipped;
/*     */     public float padTop;
/*     */     public float padRight;
/*     */     public float padBottom;
/*     */     public float padLeft;
/*     */     public float lineHeight;
/* 442 */     public float capHeight = 1.0F;
/*     */ 
/*     */     
/*     */     public float ascent;
/*     */     
/*     */     public float descent;
/*     */     
/*     */     public float down;
/*     */     
/* 451 */     public float blankLineScale = 1.0F;
/* 452 */     public float scaleX = 1.0F; public float scaleY = 1.0F;
/*     */     
/*     */     public boolean markupEnabled;
/*     */     
/*     */     public float cursorX;
/*     */     
/* 458 */     public final BitmapFont.Glyph[][] glyphs = new BitmapFont.Glyph[128][];
/*     */ 
/*     */     
/*     */     public BitmapFont.Glyph missingGlyph;
/*     */     
/*     */     public float spaceWidth;
/*     */     
/* 465 */     public float xHeight = 1.0F;
/*     */     
/*     */     public char[] breakChars;
/*     */     
/* 469 */     public char[] xChars = new char[] { 'x', 'e', 'a', 'o', 'n', 's', 'r', 'c', 'u', 'm', 'v', 'w', 'z' };
/* 470 */     public char[] capChars = new char[] { 'M', 'N', 'B', 'D', 'C', 'E', 'F', 'K', 'A', 'G', 'H', 'I', 'J', 'L', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
/*     */ 
/*     */ 
/*     */     
/*     */     public BitmapFontData() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public BitmapFontData(FileHandle fontFile, boolean flip) {
/* 479 */       this.fontFile = fontFile;
/* 480 */       this.flipped = flip;
/* 481 */       load(fontFile, flip);
/*     */     }
/*     */     
/*     */     public void load(FileHandle fontFile, boolean flip) {
/* 485 */       if (this.imagePaths != null) throw new IllegalStateException("Already loaded.");
/*     */       
/* 487 */       BufferedReader reader = new BufferedReader(new InputStreamReader(fontFile.read()), 512);
/*     */       try {
/* 489 */         String line = reader.readLine();
/* 490 */         if (line == null) throw new GdxRuntimeException("File is empty.");
/*     */         
/* 492 */         line = line.substring(line.indexOf("padding=") + 8);
/* 493 */         String[] padding = line.substring(0, line.indexOf(' ')).split(",", 4);
/* 494 */         if (padding.length != 4) throw new GdxRuntimeException("Invalid padding."); 
/* 495 */         this.padTop = Integer.parseInt(padding[0]);
/* 496 */         this.padRight = Integer.parseInt(padding[1]);
/* 497 */         this.padBottom = Integer.parseInt(padding[2]);
/* 498 */         this.padLeft = Integer.parseInt(padding[3]);
/* 499 */         float padY = this.padTop + this.padBottom;
/*     */         
/* 501 */         line = reader.readLine();
/* 502 */         if (line == null) throw new GdxRuntimeException("Missing common header."); 
/* 503 */         String[] common = line.split(" ", 7);
/*     */ 
/*     */         
/* 506 */         if (common.length < 3) throw new GdxRuntimeException("Invalid common header.");
/*     */         
/* 508 */         if (!common[1].startsWith("lineHeight=")) throw new GdxRuntimeException("Missing: lineHeight"); 
/* 509 */         this.lineHeight = Integer.parseInt(common[1].substring(11));
/*     */         
/* 511 */         if (!common[2].startsWith("base=")) throw new GdxRuntimeException("Missing: base"); 
/* 512 */         float baseLine = Integer.parseInt(common[2].substring(5));
/*     */         
/* 514 */         int pageCount = 1;
/* 515 */         if (common.length >= 6 && common[5] != null && common[5].startsWith("pages=")) {
/*     */           try {
/* 517 */             pageCount = Math.max(1, Integer.parseInt(common[5].substring(6)));
/* 518 */           } catch (NumberFormatException numberFormatException) {}
/*     */         }
/*     */ 
/*     */         
/* 522 */         this.imagePaths = new String[pageCount];
/*     */ 
/*     */         
/* 525 */         for (int p = 0; p < pageCount; p++) {
/*     */           
/* 527 */           line = reader.readLine();
/* 528 */           if (line == null) throw new GdxRuntimeException("Missing additional page definitions.");
/*     */ 
/*     */           
/* 531 */           Matcher matcher = Pattern.compile(".*id=(\\d+)").matcher(line);
/* 532 */           if (matcher.matches()) {
/* 533 */             String id = matcher.group(1);
/*     */             try {
/* 535 */               int pageID = Integer.parseInt(id.substring(3));
/* 536 */               if (pageID != p) throw new GdxRuntimeException("Page IDs must be indices starting at 0: " + id.substring(3)); 
/* 537 */             } catch (NumberFormatException ex) {
/* 538 */               throw new GdxRuntimeException("Invalid page id: " + id.substring(3), ex);
/*     */             } 
/*     */           } 
/*     */           
/* 542 */           matcher = Pattern.compile(".*file=\"?([^\"]*+)\"?").matcher(line);
/* 543 */           if (!matcher.matches()) throw new GdxRuntimeException("Missing: file"); 
/* 544 */           String fileName = matcher.group(1);
/*     */           
/* 546 */           this.imagePaths[p] = fontFile.parent().child(fileName).path().replaceAll("\\\\", "/");
/*     */         } 
/* 548 */         this.descent = 0.0F;
/*     */         
/*     */         while (true) {
/* 551 */           line = reader.readLine();
/* 552 */           if (line == null || 
/* 553 */             line.startsWith("kernings "))
/* 554 */             break;  if (!line.startsWith("char "))
/*     */             continue; 
/* 556 */           BitmapFont.Glyph glyph = new BitmapFont.Glyph();
/*     */           
/* 558 */           StringTokenizer tokens = new StringTokenizer(line, " =");
/* 559 */           tokens.nextToken();
/* 560 */           tokens.nextToken();
/* 561 */           int ch = Integer.parseInt(tokens.nextToken());
/* 562 */           if (ch <= 0) {
/* 563 */             this.missingGlyph = glyph;
/* 564 */           } else if (ch <= 65535) {
/* 565 */             setGlyph(ch, glyph);
/*     */           } else {
/*     */             continue;
/* 568 */           }  glyph.id = ch;
/* 569 */           tokens.nextToken();
/* 570 */           glyph.srcX = Integer.parseInt(tokens.nextToken());
/* 571 */           tokens.nextToken();
/* 572 */           glyph.srcY = Integer.parseInt(tokens.nextToken());
/* 573 */           tokens.nextToken();
/* 574 */           glyph.width = Integer.parseInt(tokens.nextToken());
/* 575 */           tokens.nextToken();
/* 576 */           glyph.height = Integer.parseInt(tokens.nextToken());
/* 577 */           tokens.nextToken();
/* 578 */           glyph.xoffset = Integer.parseInt(tokens.nextToken());
/* 579 */           tokens.nextToken();
/* 580 */           if (flip) {
/* 581 */             glyph.yoffset = Integer.parseInt(tokens.nextToken());
/*     */           } else {
/* 583 */             glyph.yoffset = -(glyph.height + Integer.parseInt(tokens.nextToken()));
/* 584 */           }  tokens.nextToken();
/* 585 */           glyph.xadvance = Integer.parseInt(tokens.nextToken());
/*     */ 
/*     */           
/* 588 */           if (tokens.hasMoreTokens()) tokens.nextToken(); 
/* 589 */           if (tokens.hasMoreTokens()) {
/*     */             try {
/* 591 */               glyph.page = Integer.parseInt(tokens.nextToken());
/* 592 */             } catch (NumberFormatException numberFormatException) {}
/*     */           }
/*     */ 
/*     */           
/* 596 */           if (glyph.width > 0 && glyph.height > 0) this.descent = Math.min(baseLine + glyph.yoffset, this.descent); 
/*     */         } 
/* 598 */         this.descent += this.padBottom;
/*     */         
/*     */         while (true) {
/* 601 */           line = reader.readLine();
/* 602 */           if (line == null || 
/* 603 */             !line.startsWith("kerning "))
/*     */             break; 
/* 605 */           StringTokenizer tokens = new StringTokenizer(line, " =");
/* 606 */           tokens.nextToken();
/* 607 */           tokens.nextToken();
/* 608 */           int first = Integer.parseInt(tokens.nextToken());
/* 609 */           tokens.nextToken();
/* 610 */           int second = Integer.parseInt(tokens.nextToken());
/* 611 */           if (first < 0 || first > 65535 || second < 0 || second > 65535)
/* 612 */             continue;  BitmapFont.Glyph glyph = getGlyph((char)first);
/* 613 */           tokens.nextToken();
/* 614 */           int amount = Integer.parseInt(tokens.nextToken());
/* 615 */           if (glyph != null) {
/* 616 */             glyph.setKerning(second, amount);
/*     */           }
/*     */         } 
/*     */         
/* 620 */         BitmapFont.Glyph spaceGlyph = getGlyph(' ');
/* 621 */         if (spaceGlyph == null) {
/* 622 */           spaceGlyph = new BitmapFont.Glyph();
/* 623 */           spaceGlyph.id = 32;
/* 624 */           BitmapFont.Glyph xadvanceGlyph = getGlyph('l');
/* 625 */           if (xadvanceGlyph == null) xadvanceGlyph = getFirstGlyph(); 
/* 626 */           spaceGlyph.xadvance = xadvanceGlyph.xadvance;
/* 627 */           setGlyph(32, spaceGlyph);
/*     */         } 
/* 629 */         if (spaceGlyph.width == 0) {
/* 630 */           spaceGlyph.width = (int)(this.padLeft + spaceGlyph.xadvance + this.padRight);
/* 631 */           spaceGlyph.xoffset = (int)-this.padLeft;
/*     */         } 
/* 633 */         this.spaceWidth = spaceGlyph.width;
/*     */         
/* 635 */         BitmapFont.Glyph xGlyph = null;
/* 636 */         for (char xChar : this.xChars) {
/* 637 */           xGlyph = getGlyph(xChar);
/* 638 */           if (xGlyph != null)
/*     */             break; 
/* 640 */         }  if (xGlyph == null) xGlyph = getFirstGlyph(); 
/* 641 */         this.xHeight = xGlyph.height - padY;
/*     */         
/* 643 */         BitmapFont.Glyph capGlyph = null;
/* 644 */         for (char capChar : this.capChars) {
/* 645 */           capGlyph = getGlyph(capChar);
/* 646 */           if (capGlyph != null)
/*     */             break; 
/* 648 */         }  if (capGlyph == null) {
/* 649 */           for (BitmapFont.Glyph[] page : this.glyphs) {
/* 650 */             if (page != null)
/* 651 */               for (BitmapFont.Glyph glyph : page) {
/* 652 */                 if (glyph != null && glyph.height != 0 && glyph.width != 0)
/* 653 */                   this.capHeight = Math.max(this.capHeight, glyph.height); 
/*     */               }  
/*     */           } 
/*     */         } else {
/* 657 */           this.capHeight = capGlyph.height;
/* 658 */         }  this.capHeight -= padY;
/*     */         
/* 660 */         this.ascent = baseLine - this.capHeight;
/* 661 */         this.down = -this.lineHeight;
/* 662 */         if (flip) {
/* 663 */           this.ascent = -this.ascent;
/* 664 */           this.down = -this.down;
/*     */         } 
/* 666 */       } catch (Exception ex) {
/* 667 */         throw new GdxRuntimeException("Error loading font file: " + fontFile, ex);
/*     */       } finally {
/* 669 */         StreamUtils.closeQuietly(reader);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setGlyphRegion(BitmapFont.Glyph glyph, TextureRegion region) {
/* 674 */       Texture texture = region.getTexture();
/* 675 */       float invTexWidth = 1.0F / texture.getWidth();
/* 676 */       float invTexHeight = 1.0F / texture.getHeight();
/*     */       
/* 678 */       float offsetX = 0.0F, offsetY = 0.0F;
/* 679 */       float u = region.u;
/* 680 */       float v = region.v;
/* 681 */       float regionWidth = region.getRegionWidth();
/* 682 */       float regionHeight = region.getRegionHeight();
/* 683 */       if (region instanceof TextureAtlas.AtlasRegion) {
/*     */         
/* 685 */         TextureAtlas.AtlasRegion atlasRegion = (TextureAtlas.AtlasRegion)region;
/* 686 */         offsetX = atlasRegion.offsetX;
/* 687 */         offsetY = (atlasRegion.originalHeight - atlasRegion.packedHeight) - atlasRegion.offsetY;
/*     */       } 
/*     */       
/* 690 */       float x = glyph.srcX;
/* 691 */       float x2 = (glyph.srcX + glyph.width);
/* 692 */       float y = glyph.srcY;
/* 693 */       float y2 = (glyph.srcY + glyph.height);
/*     */ 
/*     */       
/* 696 */       if (offsetX > 0.0F) {
/* 697 */         x -= offsetX;
/* 698 */         if (x < 0.0F) {
/* 699 */           glyph.width = (int)(glyph.width + x);
/* 700 */           glyph.xoffset = (int)(glyph.xoffset - x);
/* 701 */           x = 0.0F;
/*     */         } 
/* 703 */         x2 -= offsetX;
/* 704 */         if (x2 > regionWidth) {
/* 705 */           glyph.width = (int)(glyph.width - x2 - regionWidth);
/* 706 */           x2 = regionWidth;
/*     */         } 
/*     */       } 
/* 709 */       if (offsetY > 0.0F) {
/* 710 */         y -= offsetY;
/* 711 */         if (y < 0.0F) {
/* 712 */           glyph.height = (int)(glyph.height + y);
/* 713 */           y = 0.0F;
/*     */         } 
/* 715 */         y2 -= offsetY;
/* 716 */         if (y2 > regionHeight) {
/* 717 */           float amount = y2 - regionHeight;
/* 718 */           glyph.height = (int)(glyph.height - amount);
/* 719 */           glyph.yoffset = (int)(glyph.yoffset + amount);
/* 720 */           y2 = regionHeight;
/*     */         } 
/*     */       } 
/*     */       
/* 724 */       glyph.u = u + x * invTexWidth;
/* 725 */       glyph.u2 = u + x2 * invTexWidth;
/* 726 */       if (this.flipped) {
/* 727 */         glyph.v = v + y * invTexHeight;
/* 728 */         glyph.v2 = v + y2 * invTexHeight;
/*     */       } else {
/* 730 */         glyph.v2 = v + y * invTexHeight;
/* 731 */         glyph.v = v + y2 * invTexHeight;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void setLineHeight(float height) {
/* 737 */       this.lineHeight = height * this.scaleY;
/* 738 */       this.down = this.flipped ? this.lineHeight : -this.lineHeight;
/*     */     }
/*     */     
/*     */     public void setGlyph(int ch, BitmapFont.Glyph glyph) {
/* 742 */       BitmapFont.Glyph[] page = this.glyphs[ch / 512];
/* 743 */       if (page == null) this.glyphs[ch / 512] = page = new BitmapFont.Glyph[512]; 
/* 744 */       page[ch & 0x1FF] = glyph;
/*     */     }
/*     */     
/*     */     public BitmapFont.Glyph getFirstGlyph() {
/* 748 */       for (BitmapFont.Glyph[] page : this.glyphs) {
/* 749 */         if (page != null) {
/* 750 */           BitmapFont.Glyph[] arrayOfGlyph; int i; byte b; for (arrayOfGlyph = page, i = arrayOfGlyph.length, b = 0; b < i; ) { BitmapFont.Glyph glyph = arrayOfGlyph[b];
/* 751 */             if (glyph == null || glyph.height == 0 || glyph.width == 0) { b++; continue; }
/* 752 */              return glyph; }
/*     */         
/*     */         } 
/* 755 */       }  throw new GdxRuntimeException("No glyphs found.");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasGlyph(char ch) {
/* 760 */       if (this.missingGlyph != null) return true; 
/* 761 */       return (getGlyph(ch) != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BitmapFont.Glyph getGlyph(char ch) {
/* 768 */       BitmapFont.Glyph[] page = this.glyphs[ch / 512];
/* 769 */       if (page != null) return page[ch & 0x1FF]; 
/* 770 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void getGlyphs(GlyphLayout.GlyphRun run, CharSequence str, int start, int end, boolean tightBounds) {
/* 780 */       boolean markupEnabled = this.markupEnabled;
/* 781 */       float scaleX = this.scaleX;
/* 782 */       BitmapFont.Glyph missingGlyph = this.missingGlyph;
/* 783 */       Array<BitmapFont.Glyph> glyphs = run.glyphs;
/* 784 */       FloatArray xAdvances = run.xAdvances;
/*     */ 
/*     */       
/* 787 */       glyphs.ensureCapacity(end - start);
/* 788 */       xAdvances.ensureCapacity(end - start + 1);
/*     */       
/* 790 */       BitmapFont.Glyph lastGlyph = null;
/* 791 */       while (start < end) {
/* 792 */         char ch = str.charAt(start++);
/* 793 */         BitmapFont.Glyph glyph = getGlyph(ch);
/* 794 */         if (glyph == null) {
/* 795 */           if (missingGlyph == null)
/* 796 */             continue;  glyph = missingGlyph;
/*     */         } 
/*     */         
/* 799 */         glyphs.add(glyph);
/*     */         
/* 801 */         if (lastGlyph == null) {
/* 802 */           xAdvances.add((!tightBounds || glyph.fixedWidth) ? 0.0F : (-glyph.xoffset * scaleX - this.padLeft));
/*     */         } else {
/* 804 */           xAdvances.add((lastGlyph.xadvance + lastGlyph.getKerning(ch)) * scaleX);
/* 805 */         }  lastGlyph = glyph;
/*     */ 
/*     */         
/* 808 */         if (markupEnabled && ch == '[' && start < end && str.charAt(start) == '[') start++; 
/*     */       } 
/* 810 */       if (lastGlyph != null) {
/* 811 */         float lastGlyphWidth = (!tightBounds || lastGlyph.fixedWidth) ? lastGlyph.xadvance : ((lastGlyph.xoffset + lastGlyph.width) - this.padRight);
/*     */         
/* 813 */         xAdvances.add(lastGlyphWidth * scaleX);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getWrapIndex(Array<BitmapFont.Glyph> glyphs, int start) {
/* 820 */       int i = start - 1;
/* 821 */       for (; i >= 1 && 
/* 822 */         isWhitespace((char)((BitmapFont.Glyph)glyphs.get(i)).id); i--);
/* 823 */       for (; i >= 1; i--) {
/* 824 */         char ch = (char)((BitmapFont.Glyph)glyphs.get(i)).id;
/* 825 */         if (isWhitespace(ch) || isBreakChar(ch)) return i + 1; 
/*     */       } 
/* 827 */       return 0;
/*     */     }
/*     */     
/*     */     public boolean isBreakChar(char c) {
/* 831 */       if (this.breakChars == null) return false; 
/* 832 */       for (char br : this.breakChars) {
/* 833 */         if (c == br) return true; 
/* 834 */       }  return false;
/*     */     }
/*     */     
/*     */     public boolean isWhitespace(char c) {
/* 838 */       switch (c) {
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\r':
/*     */         case ' ':
/* 843 */           return true;
/*     */       } 
/* 845 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getImagePath(int index) {
/* 851 */       return this.imagePaths[index];
/*     */     }
/*     */     
/*     */     public String[] getImagePaths() {
/* 855 */       return this.imagePaths;
/*     */     }
/*     */     
/*     */     public FileHandle getFontFile() {
/* 859 */       return this.fontFile;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setScale(float scaleX, float scaleY) {
/* 868 */       if (scaleX == 0.0F) throw new IllegalArgumentException("scaleX cannot be 0."); 
/* 869 */       if (scaleY == 0.0F) throw new IllegalArgumentException("scaleY cannot be 0."); 
/* 870 */       float x = scaleX / this.scaleX;
/* 871 */       float y = scaleY / this.scaleY;
/* 872 */       this.lineHeight *= y;
/* 873 */       this.spaceWidth *= x;
/* 874 */       this.xHeight *= y;
/* 875 */       this.capHeight *= y;
/* 876 */       this.ascent *= y;
/* 877 */       this.descent *= y;
/* 878 */       this.down *= y;
/* 879 */       this.padTop *= y;
/* 880 */       this.padLeft *= y;
/* 881 */       this.padBottom *= y;
/* 882 */       this.padRight *= y;
/* 883 */       this.scaleX = scaleX;
/* 884 */       this.scaleY = scaleY;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setScale(float scaleXY) {
/* 891 */       setScale(scaleXY, scaleXY);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void scale(float amount) {
/* 898 */       setScale(this.scaleX + amount, this.scaleY + amount);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\BitmapFont.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */