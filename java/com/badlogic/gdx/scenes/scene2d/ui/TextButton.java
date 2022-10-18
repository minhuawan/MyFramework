/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
/*     */ public class TextButton
/*     */   extends Button
/*     */ {
/*     */   private final Label label;
/*     */   private TextButtonStyle style;
/*     */   
/*     */   public TextButton(String text, Skin skin) {
/*  33 */     this(text, skin.<TextButtonStyle>get(TextButtonStyle.class));
/*  34 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public TextButton(String text, Skin skin, String styleName) {
/*  38 */     this(text, skin.<TextButtonStyle>get(styleName, TextButtonStyle.class));
/*  39 */     setSkin(skin);
/*     */   }
/*     */ 
/*     */   
/*     */   public TextButton(String text, TextButtonStyle style) {
/*  44 */     setStyle(style);
/*  45 */     this.style = style;
/*  46 */     this.label = new Label(text, new Label.LabelStyle(style.font, style.fontColor));
/*  47 */     this.label.setAlignment(1);
/*  48 */     add(this.label).expand().fill();
/*  49 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public void setStyle(Button.ButtonStyle style) {
/*  53 */     if (style == null) throw new NullPointerException("style cannot be null"); 
/*  54 */     if (!(style instanceof TextButtonStyle)) throw new IllegalArgumentException("style must be a TextButtonStyle."); 
/*  55 */     super.setStyle(style);
/*  56 */     this.style = (TextButtonStyle)style;
/*  57 */     if (this.label != null) {
/*  58 */       TextButtonStyle textButtonStyle = (TextButtonStyle)style;
/*  59 */       Label.LabelStyle labelStyle = this.label.getStyle();
/*  60 */       labelStyle.font = textButtonStyle.font;
/*  61 */       labelStyle.fontColor = textButtonStyle.fontColor;
/*  62 */       this.label.setStyle(labelStyle);
/*     */     } 
/*     */   }
/*     */   
/*     */   public TextButtonStyle getStyle() {
/*  67 */     return this.style;
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*     */     Color fontColor;
/*  72 */     if (isDisabled() && this.style.disabledFontColor != null) {
/*  73 */       fontColor = this.style.disabledFontColor;
/*  74 */     } else if (isPressed() && this.style.downFontColor != null) {
/*  75 */       fontColor = this.style.downFontColor;
/*  76 */     } else if (this.isChecked && this.style.checkedFontColor != null) {
/*  77 */       fontColor = (isOver() && this.style.checkedOverFontColor != null) ? this.style.checkedOverFontColor : this.style.checkedFontColor;
/*  78 */     } else if (isOver() && this.style.overFontColor != null) {
/*  79 */       fontColor = this.style.overFontColor;
/*     */     } else {
/*  81 */       fontColor = this.style.fontColor;
/*  82 */     }  if (fontColor != null) (this.label.getStyle()).fontColor = fontColor; 
/*  83 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */   
/*     */   public Label getLabel() {
/*  87 */     return this.label;
/*     */   }
/*     */   
/*     */   public Cell<Label> getLabelCell() {
/*  91 */     return getCell(this.label);
/*     */   }
/*     */   
/*     */   public void setText(String text) {
/*  95 */     this.label.setText(text);
/*     */   }
/*     */   
/*     */   public CharSequence getText() {
/*  99 */     return (CharSequence)this.label.getText();
/*     */   }
/*     */   
/*     */   public static class TextButtonStyle extends Button.ButtonStyle { public BitmapFont font;
/*     */     public Color fontColor;
/*     */     public Color downFontColor;
/*     */     public Color overFontColor;
/*     */     public Color checkedFontColor;
/*     */     public Color checkedOverFontColor;
/*     */     public Color disabledFontColor;
/*     */     
/*     */     public TextButtonStyle() {}
/*     */     
/*     */     public TextButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
/* 113 */       super(up, down, checked);
/* 114 */       this.font = font;
/*     */     }
/*     */     
/*     */     public TextButtonStyle(TextButtonStyle style) {
/* 118 */       super(style);
/* 119 */       this.font = style.font;
/* 120 */       if (style.fontColor != null) this.fontColor = new Color(style.fontColor); 
/* 121 */       if (style.downFontColor != null) this.downFontColor = new Color(style.downFontColor); 
/* 122 */       if (style.overFontColor != null) this.overFontColor = new Color(style.overFontColor); 
/* 123 */       if (style.checkedFontColor != null) this.checkedFontColor = new Color(style.checkedFontColor); 
/* 124 */       if (style.checkedOverFontColor != null) this.checkedFontColor = new Color(style.checkedOverFontColor); 
/* 125 */       if (style.disabledFontColor != null) this.disabledFontColor = new Color(style.disabledFontColor); 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\TextButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */