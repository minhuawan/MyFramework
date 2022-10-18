/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.utils.Scaling;
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
/*     */ public class ImageTextButton
/*     */   extends Button
/*     */ {
/*     */   private final Image image;
/*     */   private final Label label;
/*     */   private ImageTextButtonStyle style;
/*     */   
/*     */   public ImageTextButton(String text, Skin skin) {
/*  39 */     this(text, skin.<ImageTextButtonStyle>get(ImageTextButtonStyle.class));
/*  40 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public ImageTextButton(String text, Skin skin, String styleName) {
/*  44 */     this(text, skin.<ImageTextButtonStyle>get(styleName, ImageTextButtonStyle.class));
/*  45 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public ImageTextButton(String text, ImageTextButtonStyle style) {
/*  49 */     super(style);
/*  50 */     this.style = style;
/*     */     
/*  52 */     defaults().space(3.0F);
/*     */     
/*  54 */     this.image = new Image();
/*  55 */     this.image.setScaling(Scaling.fit);
/*     */     
/*  57 */     this.label = new Label(text, new Label.LabelStyle(style.font, style.fontColor));
/*  58 */     this.label.setAlignment(1);
/*     */     
/*  60 */     add(this.image);
/*  61 */     add(this.label);
/*     */     
/*  63 */     setStyle(style);
/*     */     
/*  65 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public void setStyle(Button.ButtonStyle style) {
/*  69 */     if (!(style instanceof ImageTextButtonStyle)) throw new IllegalArgumentException("style must be a ImageTextButtonStyle."); 
/*  70 */     super.setStyle(style);
/*  71 */     this.style = (ImageTextButtonStyle)style;
/*  72 */     if (this.image != null) updateImage(); 
/*  73 */     if (this.label != null) {
/*  74 */       ImageTextButtonStyle textButtonStyle = (ImageTextButtonStyle)style;
/*  75 */       Label.LabelStyle labelStyle = this.label.getStyle();
/*  76 */       labelStyle.font = textButtonStyle.font;
/*  77 */       labelStyle.fontColor = textButtonStyle.fontColor;
/*  78 */       this.label.setStyle(labelStyle);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ImageTextButtonStyle getStyle() {
/*  83 */     return this.style;
/*     */   }
/*     */   
/*     */   private void updateImage() {
/*  87 */     Drawable drawable = null;
/*  88 */     if (isDisabled() && this.style.imageDisabled != null) {
/*  89 */       drawable = this.style.imageDisabled;
/*  90 */     } else if (isPressed() && this.style.imageDown != null) {
/*  91 */       drawable = this.style.imageDown;
/*  92 */     } else if (this.isChecked && this.style.imageChecked != null) {
/*  93 */       drawable = (this.style.imageCheckedOver != null && isOver()) ? this.style.imageCheckedOver : this.style.imageChecked;
/*  94 */     } else if (isOver() && this.style.imageOver != null) {
/*  95 */       drawable = this.style.imageOver;
/*  96 */     } else if (this.style.imageUp != null) {
/*  97 */       drawable = this.style.imageUp;
/*  98 */     }  this.image.setDrawable(drawable);
/*     */   }
/*     */   public void draw(Batch batch, float parentAlpha) {
/*     */     Color fontColor;
/* 102 */     updateImage();
/*     */     
/* 104 */     if (isDisabled() && this.style.disabledFontColor != null) {
/* 105 */       fontColor = this.style.disabledFontColor;
/* 106 */     } else if (isPressed() && this.style.downFontColor != null) {
/* 107 */       fontColor = this.style.downFontColor;
/* 108 */     } else if (this.isChecked && this.style.checkedFontColor != null) {
/* 109 */       fontColor = (isOver() && this.style.checkedOverFontColor != null) ? this.style.checkedOverFontColor : this.style.checkedFontColor;
/* 110 */     } else if (isOver() && this.style.overFontColor != null) {
/* 111 */       fontColor = this.style.overFontColor;
/*     */     } else {
/* 113 */       fontColor = this.style.fontColor;
/* 114 */     }  if (fontColor != null) (this.label.getStyle()).fontColor = fontColor; 
/* 115 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */   
/*     */   public Image getImage() {
/* 119 */     return this.image;
/*     */   }
/*     */   
/*     */   public Cell getImageCell() {
/* 123 */     return getCell(this.image);
/*     */   }
/*     */   
/*     */   public Label getLabel() {
/* 127 */     return this.label;
/*     */   }
/*     */   
/*     */   public Cell getLabelCell() {
/* 131 */     return getCell(this.label);
/*     */   }
/*     */   
/*     */   public void setText(CharSequence text) {
/* 135 */     this.label.setText(text);
/*     */   }
/*     */   
/*     */   public CharSequence getText() {
/* 139 */     return (CharSequence)this.label.getText();
/*     */   }
/*     */   
/*     */   public static class ImageTextButtonStyle extends TextButton.TextButtonStyle { public Drawable imageUp;
/*     */     public Drawable imageDown;
/*     */     public Drawable imageOver;
/*     */     public Drawable imageChecked;
/*     */     public Drawable imageCheckedOver;
/*     */     public Drawable imageDisabled;
/*     */     
/*     */     public ImageTextButtonStyle() {}
/*     */     
/*     */     public ImageTextButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
/* 152 */       super(up, down, checked, font);
/*     */     }
/*     */     
/*     */     public ImageTextButtonStyle(ImageTextButtonStyle style) {
/* 156 */       super(style);
/* 157 */       if (style.imageUp != null) this.imageUp = style.imageUp; 
/* 158 */       if (style.imageDown != null) this.imageDown = style.imageDown; 
/* 159 */       if (style.imageOver != null) this.imageOver = style.imageOver; 
/* 160 */       if (style.imageChecked != null) this.imageChecked = style.imageChecked; 
/* 161 */       if (style.imageCheckedOver != null) this.imageCheckedOver = style.imageCheckedOver; 
/* 162 */       if (style.imageDisabled != null) this.imageDisabled = style.imageDisabled; 
/*     */     }
/*     */     
/*     */     public ImageTextButtonStyle(TextButton.TextButtonStyle style) {
/* 166 */       super(style);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\ImageTextButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */