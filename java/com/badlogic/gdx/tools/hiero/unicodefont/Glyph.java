/*     */ package com.badlogic.gdx.tools.hiero.unicodefont;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.GlyphMetrics;
/*     */ import java.awt.font.GlyphVector;
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
/*     */ public class Glyph
/*     */ {
/*     */   private int codePoint;
/*     */   private short width;
/*     */   private short height;
/*     */   private short yOffset;
/*     */   private boolean isMissing;
/*     */   private Shape shape;
/*     */   float u;
/*     */   float v;
/*     */   float u2;
/*     */   float v2;
/*     */   private int xOffset;
/*     */   private int xAdvance;
/*     */   Texture texture;
/*     */   
/*     */   Glyph(int codePoint, Rectangle bounds, GlyphVector vector, int index, UnicodeFont unicodeFont) {
/*  42 */     this.codePoint = codePoint;
/*     */     
/*  44 */     int padTop = unicodeFont.getPaddingTop(), padBottom = unicodeFont.getPaddingBottom();
/*  45 */     int padLeft = unicodeFont.getPaddingLeft(), padRight = unicodeFont.getPaddingRight();
/*     */     
/*  47 */     if (unicodeFont.renderType == UnicodeFont.RenderType.FreeType && unicodeFont.bitmapFont != null) {
/*  48 */       BitmapFont.Glyph g = unicodeFont.bitmapFont.getData().getGlyph((char)codePoint);
/*  49 */       if (g == null) {
/*  50 */         this.isMissing = true;
/*     */       } else {
/*  52 */         boolean empty = (g.width == 0 || g.height == 0);
/*  53 */         this.width = empty ? 0 : (short)(g.width + padLeft + padRight);
/*  54 */         this.height = empty ? 0 : (short)(g.height + padTop + padBottom);
/*  55 */         this.yOffset = (short)(g.yoffset - padTop);
/*  56 */         this.xOffset = g.xoffset - unicodeFont.getPaddingLeft();
/*  57 */         this
/*  58 */           .xAdvance = g.xadvance + unicodeFont.getPaddingAdvanceX() + unicodeFont.getPaddingLeft() + unicodeFont.getPaddingRight();
/*  59 */         this.isMissing = (codePoint == 0);
/*     */       } 
/*     */     } else {
/*     */       
/*  63 */       GlyphMetrics metrics = vector.getGlyphMetrics(index);
/*  64 */       int lsb = (int)metrics.getLSB();
/*  65 */       if (lsb > 0) lsb = 0; 
/*  66 */       int rsb = (int)metrics.getRSB();
/*  67 */       if (rsb > 0) rsb = 0;
/*     */       
/*  69 */       int glyphWidth = bounds.width - lsb - rsb;
/*  70 */       int glyphHeight = bounds.height;
/*  71 */       if (glyphWidth > 0 && glyphHeight > 0) {
/*  72 */         this.width = (short)(glyphWidth + padLeft + padRight);
/*  73 */         this.height = (short)(glyphHeight + padTop + padBottom);
/*  74 */         this.yOffset = (short)(unicodeFont.getAscent() + bounds.y - padTop);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  80 */       char[] chars = Character.toChars(codePoint);
/*  81 */       GlyphVector charVector = unicodeFont.getFont().layoutGlyphVector(GlyphPage.renderContext, chars, 0, chars.length, 0);
/*     */       
/*  83 */       GlyphMetrics charMetrics = charVector.getGlyphMetrics(0);
/*  84 */       this.xOffset = (charVector.getGlyphPixelBounds(0, GlyphPage.renderContext, 0.0F, 0.0F)).x - unicodeFont.getPaddingLeft();
/*  85 */       this
/*  86 */         .xAdvance = (int)(metrics.getAdvanceX() + unicodeFont.getPaddingAdvanceX() + unicodeFont.getPaddingLeft() + unicodeFont.getPaddingRight());
/*     */       
/*  88 */       this.shape = vector.getGlyphOutline(index, (-bounds.x + unicodeFont.getPaddingLeft()), (-bounds.y + unicodeFont.getPaddingTop()));
/*     */       
/*  90 */       this.isMissing = !unicodeFont.getFont().canDisplay((char)codePoint);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCodePoint() {
/*  96 */     return this.codePoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMissing() {
/* 101 */     return this.isMissing;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 106 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 111 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 116 */     return this.shape;
/*     */   }
/*     */   
/*     */   public void setShape(Shape shape) {
/* 120 */     this.shape = shape;
/*     */   }
/*     */   
/*     */   public void setTexture(Texture texture, float u, float v, float u2, float v2) {
/* 124 */     this.texture = texture;
/* 125 */     this.u = u;
/* 126 */     this.v = v;
/* 127 */     this.u2 = u2;
/* 128 */     this.v2 = v2;
/*     */   }
/*     */   
/*     */   public Texture getTexture() {
/* 132 */     return this.texture;
/*     */   }
/*     */   
/*     */   public float getU() {
/* 136 */     return this.u;
/*     */   }
/*     */   
/*     */   public float getV() {
/* 140 */     return this.v;
/*     */   }
/*     */   
/*     */   public float getU2() {
/* 144 */     return this.u2;
/*     */   }
/*     */   
/*     */   public float getV2() {
/* 148 */     return this.v2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getYOffset() {
/* 153 */     return this.yOffset;
/*     */   }
/*     */   
/*     */   public int getXOffset() {
/* 157 */     return this.xOffset;
/*     */   }
/*     */   
/*     */   public int getXAdvance() {
/* 161 */     return this.xAdvance;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\Glyph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */