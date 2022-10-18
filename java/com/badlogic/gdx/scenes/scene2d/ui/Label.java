/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
/*     */ import com.badlogic.gdx.graphics.g2d.GlyphLayout;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.StringBuilder;
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
/*     */ public class Label
/*     */   extends Widget
/*     */ {
/*  34 */   private static final Color tempColor = new Color();
/*  35 */   private static final GlyphLayout prefSizeLayout = new GlyphLayout();
/*     */   
/*     */   private LabelStyle style;
/*  38 */   private final GlyphLayout layout = new GlyphLayout();
/*  39 */   private final Vector2 prefSize = new Vector2();
/*  40 */   private final StringBuilder text = new StringBuilder();
/*     */   private BitmapFontCache cache;
/*  42 */   private int labelAlign = 8;
/*  43 */   private int lineAlign = 8;
/*     */   private boolean wrap;
/*     */   private float lastPrefHeight;
/*     */   private boolean prefSizeInvalid = true;
/*  47 */   private float fontScaleX = 1.0F; private float fontScaleY = 1.0F;
/*     */   private boolean fontScaleChanged = false;
/*     */   private String ellipsis;
/*     */   
/*     */   public Label(CharSequence text, Skin skin) {
/*  52 */     this(text, skin.<LabelStyle>get(LabelStyle.class));
/*     */   }
/*     */   
/*     */   public Label(CharSequence text, Skin skin, String styleName) {
/*  56 */     this(text, skin.<LabelStyle>get(styleName, LabelStyle.class));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Label(CharSequence text, Skin skin, String fontName, Color color) {
/*  62 */     this(text, new LabelStyle(skin.getFont(fontName), color));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Label(CharSequence text, Skin skin, String fontName, String colorName) {
/*  68 */     this(text, new LabelStyle(skin.getFont(fontName), skin.getColor(colorName)));
/*     */   }
/*     */   
/*     */   public Label(CharSequence text, LabelStyle style) {
/*  72 */     if (text != null) this.text.append(text); 
/*  73 */     setStyle(style);
/*  74 */     if (text != null && text.length() > 0) setSize(getPrefWidth(), getPrefHeight()); 
/*     */   }
/*     */   
/*     */   public void setStyle(LabelStyle style) {
/*  78 */     if (style == null) throw new IllegalArgumentException("style cannot be null."); 
/*  79 */     if (style.font == null) throw new IllegalArgumentException("Missing LabelStyle font."); 
/*  80 */     this.style = style;
/*  81 */     this.cache = style.font.newFontCache();
/*  82 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LabelStyle getStyle() {
/*  88 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setText(CharSequence newText) {
/*  93 */     if (newText == null) newText = ""; 
/*  94 */     if (newText instanceof StringBuilder) {
/*  95 */       if (this.text.equals(newText))
/*  96 */         return;  this.text.setLength(0);
/*  97 */       this.text.append((StringBuilder)newText);
/*     */     } else {
/*  99 */       if (textEquals(newText))
/* 100 */         return;  this.text.setLength(0);
/* 101 */       this.text.append(newText);
/*     */     } 
/* 103 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public boolean textEquals(CharSequence other) {
/* 107 */     int length = this.text.length;
/* 108 */     char[] chars = this.text.chars;
/* 109 */     if (length != other.length()) return false; 
/* 110 */     for (int i = 0; i < length; i++) {
/* 111 */       if (chars[i] != other.charAt(i)) return false; 
/* 112 */     }  return true;
/*     */   }
/*     */   
/*     */   public StringBuilder getText() {
/* 116 */     return this.text;
/*     */   }
/*     */   
/*     */   public void invalidate() {
/* 120 */     super.invalidate();
/* 121 */     this.prefSizeInvalid = true;
/*     */   }
/*     */   
/*     */   private void scaleAndComputePrefSize() {
/* 125 */     BitmapFont font = this.cache.getFont();
/* 126 */     float oldScaleX = font.getScaleX();
/* 127 */     float oldScaleY = font.getScaleY();
/* 128 */     if (this.fontScaleChanged) font.getData().setScale(this.fontScaleX, this.fontScaleY);
/*     */     
/* 130 */     computePrefSize();
/*     */     
/* 132 */     if (this.fontScaleChanged) font.getData().setScale(oldScaleX, oldScaleY); 
/*     */   }
/*     */   
/*     */   private void computePrefSize() {
/* 136 */     this.prefSizeInvalid = false;
/* 137 */     GlyphLayout prefSizeLayout = Label.prefSizeLayout;
/* 138 */     if (this.wrap && this.ellipsis == null) {
/* 139 */       float width = getWidth();
/* 140 */       if (this.style.background != null) width -= this.style.background.getLeftWidth() + this.style.background.getRightWidth(); 
/* 141 */       prefSizeLayout.setText(this.cache.getFont(), (CharSequence)this.text, Color.WHITE, width, 8, true);
/*     */     } else {
/* 143 */       prefSizeLayout.setText(this.cache.getFont(), (CharSequence)this.text);
/* 144 */     }  this.prefSize.set(prefSizeLayout.width, prefSizeLayout.height);
/*     */   }
/*     */   public void layout() {
/*     */     float textWidth, textHeight;
/* 148 */     BitmapFont font = this.cache.getFont();
/* 149 */     float oldScaleX = font.getScaleX();
/* 150 */     float oldScaleY = font.getScaleY();
/* 151 */     if (this.fontScaleChanged) font.getData().setScale(this.fontScaleX, this.fontScaleY);
/*     */     
/* 153 */     boolean wrap = (this.wrap && this.ellipsis == null);
/* 154 */     if (wrap) {
/* 155 */       float prefHeight = getPrefHeight();
/* 156 */       if (prefHeight != this.lastPrefHeight) {
/* 157 */         this.lastPrefHeight = prefHeight;
/* 158 */         invalidateHierarchy();
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     float width = getWidth(), height = getHeight();
/* 163 */     Drawable background = this.style.background;
/* 164 */     float x = 0.0F, y = 0.0F;
/* 165 */     if (background != null) {
/* 166 */       x = background.getLeftWidth();
/* 167 */       y = background.getBottomHeight();
/* 168 */       width -= background.getLeftWidth() + background.getRightWidth();
/* 169 */       height -= background.getBottomHeight() + background.getTopHeight();
/*     */     } 
/*     */     
/* 172 */     GlyphLayout layout = this.layout;
/*     */     
/* 174 */     if (wrap || this.text.indexOf("\n") != -1) {
/*     */       
/* 176 */       layout.setText(font, (CharSequence)this.text, 0, this.text.length, Color.WHITE, width, this.lineAlign, wrap, this.ellipsis);
/* 177 */       textWidth = layout.width;
/* 178 */       textHeight = layout.height;
/*     */       
/* 180 */       if ((this.labelAlign & 0x8) == 0)
/* 181 */         if ((this.labelAlign & 0x10) != 0) {
/* 182 */           x += width - textWidth;
/*     */         } else {
/* 184 */           x += (width - textWidth) / 2.0F;
/*     */         }  
/*     */     } else {
/* 187 */       textWidth = width;
/* 188 */       textHeight = (font.getData()).capHeight;
/*     */     } 
/*     */     
/* 191 */     if ((this.labelAlign & 0x2) != 0) {
/* 192 */       y += this.cache.getFont().isFlipped() ? 0.0F : (height - textHeight);
/* 193 */       y += this.style.font.getDescent();
/* 194 */     } else if ((this.labelAlign & 0x4) != 0) {
/* 195 */       y += this.cache.getFont().isFlipped() ? (height - textHeight) : 0.0F;
/* 196 */       y -= this.style.font.getDescent();
/*     */     } else {
/* 198 */       y += (height - textHeight) / 2.0F;
/*     */     } 
/* 200 */     if (!this.cache.getFont().isFlipped()) y += textHeight;
/*     */     
/* 202 */     layout.setText(font, (CharSequence)this.text, 0, this.text.length, Color.WHITE, textWidth, this.lineAlign, wrap, this.ellipsis);
/* 203 */     this.cache.setText(layout, x, y);
/*     */     
/* 205 */     if (this.fontScaleChanged) font.getData().setScale(oldScaleX, oldScaleY); 
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 209 */     validate();
/* 210 */     Color color = tempColor.set(getColor());
/* 211 */     color.a *= parentAlpha;
/* 212 */     if (this.style.background != null) {
/* 213 */       batch.setColor(color.r, color.g, color.b, color.a);
/* 214 */       this.style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
/*     */     } 
/* 216 */     if (this.style.fontColor != null) color.mul(this.style.fontColor); 
/* 217 */     this.cache.tint(color);
/* 218 */     this.cache.setPosition(getX(), getY());
/* 219 */     this.cache.draw(batch);
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 223 */     if (this.wrap) return 0.0F; 
/* 224 */     if (this.prefSizeInvalid) scaleAndComputePrefSize(); 
/* 225 */     float width = this.prefSize.x;
/* 226 */     Drawable background = this.style.background;
/* 227 */     if (background != null) width += background.getLeftWidth() + background.getRightWidth(); 
/* 228 */     return width;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 232 */     if (this.prefSizeInvalid) scaleAndComputePrefSize(); 
/* 233 */     float height = this.prefSize.y - this.style.font.getDescent() * this.fontScaleY * 2.0F;
/* 234 */     Drawable background = this.style.background;
/* 235 */     if (background != null) height += background.getTopHeight() + background.getBottomHeight(); 
/* 236 */     return height;
/*     */   }
/*     */   
/*     */   public GlyphLayout getGlyphLayout() {
/* 240 */     return this.layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWrap(boolean wrap) {
/* 251 */     this.wrap = wrap;
/* 252 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public int getLabelAlign() {
/* 256 */     return this.labelAlign;
/*     */   }
/*     */   
/*     */   public int getLineAlign() {
/* 260 */     return this.lineAlign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignment(int alignment) {
/* 267 */     setAlignment(alignment, alignment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignment(int labelAlign, int lineAlign) {
/* 274 */     this.labelAlign = labelAlign;
/*     */     
/* 276 */     if ((lineAlign & 0x8) != 0) {
/* 277 */       this.lineAlign = 8;
/* 278 */     } else if ((lineAlign & 0x10) != 0) {
/* 279 */       this.lineAlign = 16;
/*     */     } else {
/* 281 */       this.lineAlign = 1;
/*     */     } 
/* 283 */     invalidate();
/*     */   }
/*     */   
/*     */   public void setFontScale(float fontScale) {
/* 287 */     setFontScale(fontScale, fontScale);
/*     */   }
/*     */   
/*     */   public void setFontScale(float fontScaleX, float fontScaleY) {
/* 291 */     this.fontScaleChanged = true;
/* 292 */     this.fontScaleX = fontScaleX;
/* 293 */     this.fontScaleY = fontScaleY;
/* 294 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public float getFontScaleX() {
/* 298 */     return this.fontScaleX;
/*     */   }
/*     */   
/*     */   public void setFontScaleX(float fontScaleX) {
/* 302 */     setFontScale(fontScaleX, this.fontScaleY);
/*     */   }
/*     */   
/*     */   public float getFontScaleY() {
/* 306 */     return this.fontScaleY;
/*     */   }
/*     */   
/*     */   public void setFontScaleY(float fontScaleY) {
/* 310 */     setFontScale(this.fontScaleX, fontScaleY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEllipsis(String ellipsis) {
/* 316 */     this.ellipsis = ellipsis;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEllipsis(boolean ellipsis) {
/* 322 */     if (ellipsis) {
/* 323 */       this.ellipsis = "...";
/*     */     } else {
/* 325 */       this.ellipsis = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BitmapFontCache getBitmapFontCache() {
/* 330 */     return this.cache;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 334 */     return super.toString() + ": " + this.text;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class LabelStyle
/*     */   {
/*     */     public BitmapFont font;
/*     */     
/*     */     public Color fontColor;
/*     */     
/*     */     public Drawable background;
/*     */ 
/*     */     
/*     */     public LabelStyle() {}
/*     */     
/*     */     public LabelStyle(BitmapFont font, Color fontColor) {
/* 350 */       this.font = font;
/* 351 */       this.fontColor = fontColor;
/*     */     }
/*     */     
/*     */     public LabelStyle(LabelStyle style) {
/* 355 */       this.font = style.font;
/* 356 */       if (style.fontColor != null) this.fontColor = new Color(style.fontColor); 
/* 357 */       this.background = style.background;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Label.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */