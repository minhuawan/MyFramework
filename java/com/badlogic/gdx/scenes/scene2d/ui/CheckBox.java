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
/*     */ public class CheckBox
/*     */   extends TextButton
/*     */ {
/*     */   private Image image;
/*     */   private Cell imageCell;
/*     */   private CheckBoxStyle style;
/*     */   
/*     */   public CheckBox(String text, Skin skin) {
/*  34 */     this(text, skin.<CheckBoxStyle>get(CheckBoxStyle.class));
/*     */   }
/*     */   
/*     */   public CheckBox(String text, Skin skin, String styleName) {
/*  38 */     this(text, skin.<CheckBoxStyle>get(styleName, CheckBoxStyle.class));
/*     */   }
/*     */   
/*     */   public CheckBox(String text, CheckBoxStyle style) {
/*  42 */     super(text, style);
/*  43 */     clearChildren();
/*  44 */     Label label = getLabel();
/*  45 */     this.imageCell = add(this.image = new Image(style.checkboxOff, Scaling.none));
/*  46 */     add(label);
/*  47 */     label.setAlignment(8);
/*  48 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public void setStyle(Button.ButtonStyle style) {
/*  52 */     if (!(style instanceof CheckBoxStyle)) throw new IllegalArgumentException("style must be a CheckBoxStyle."); 
/*  53 */     super.setStyle(style);
/*  54 */     this.style = (CheckBoxStyle)style;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CheckBoxStyle getStyle() {
/*  60 */     return this.style;
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*  64 */     Drawable checkbox = null;
/*  65 */     if (isDisabled())
/*  66 */       if (this.isChecked && this.style.checkboxOnDisabled != null) {
/*  67 */         checkbox = this.style.checkboxOnDisabled;
/*     */       } else {
/*  69 */         checkbox = this.style.checkboxOffDisabled;
/*     */       }  
/*  71 */     if (checkbox == null)
/*  72 */       if (this.isChecked && this.style.checkboxOn != null) {
/*  73 */         checkbox = this.style.checkboxOn;
/*  74 */       } else if (isOver() && this.style.checkboxOver != null && !isDisabled()) {
/*  75 */         checkbox = this.style.checkboxOver;
/*     */       } else {
/*  77 */         checkbox = this.style.checkboxOff;
/*     */       }  
/*  79 */     this.image.setDrawable(checkbox);
/*  80 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */   
/*     */   public Image getImage() {
/*  84 */     return this.image;
/*     */   }
/*     */   
/*     */   public Cell getImageCell() {
/*  88 */     return this.imageCell;
/*     */   }
/*     */   
/*     */   public static class CheckBoxStyle
/*     */     extends TextButton.TextButtonStyle {
/*     */     public Drawable checkboxOn;
/*     */     public Drawable checkboxOff;
/*     */     public Drawable checkboxOver;
/*     */     public Drawable checkboxOnDisabled;
/*     */     public Drawable checkboxOffDisabled;
/*     */     
/*     */     public CheckBoxStyle() {}
/*     */     
/*     */     public CheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn, BitmapFont font, Color fontColor) {
/* 102 */       this.checkboxOff = checkboxOff;
/* 103 */       this.checkboxOn = checkboxOn;
/* 104 */       this.font = font;
/* 105 */       this.fontColor = fontColor;
/*     */     }
/*     */     
/*     */     public CheckBoxStyle(CheckBoxStyle style) {
/* 109 */       super(style);
/* 110 */       this.checkboxOff = style.checkboxOff;
/* 111 */       this.checkboxOn = style.checkboxOn;
/* 112 */       this.checkboxOver = style.checkboxOver;
/* 113 */       this.checkboxOffDisabled = style.checkboxOffDisabled;
/* 114 */       this.checkboxOnDisabled = style.checkboxOnDisabled;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\CheckBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */