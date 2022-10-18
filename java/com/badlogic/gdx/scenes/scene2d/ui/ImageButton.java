/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
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
/*     */ public class ImageButton
/*     */   extends Button
/*     */ {
/*     */   private final Image image;
/*     */   private ImageButtonStyle style;
/*     */   
/*     */   public ImageButton(Skin skin) {
/*  33 */     this(skin.<ImageButtonStyle>get(ImageButtonStyle.class));
/*  34 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public ImageButton(Skin skin, String styleName) {
/*  38 */     this(skin.<ImageButtonStyle>get(styleName, ImageButtonStyle.class));
/*  39 */     setSkin(skin);
/*     */   }
/*     */   
/*     */   public ImageButton(ImageButtonStyle style) {
/*  43 */     super(style);
/*  44 */     this.image = new Image();
/*  45 */     this.image.setScaling(Scaling.fit);
/*  46 */     add(this.image);
/*  47 */     setStyle(style);
/*  48 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public ImageButton(Drawable imageUp) {
/*  52 */     this(new ImageButtonStyle(null, null, null, imageUp, null, null));
/*     */   }
/*     */   
/*     */   public ImageButton(Drawable imageUp, Drawable imageDown) {
/*  56 */     this(new ImageButtonStyle(null, null, null, imageUp, imageDown, null));
/*     */   }
/*     */   
/*     */   public ImageButton(Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
/*  60 */     this(new ImageButtonStyle(null, null, null, imageUp, imageDown, imageChecked));
/*     */   }
/*     */   
/*     */   public void setStyle(Button.ButtonStyle style) {
/*  64 */     if (!(style instanceof ImageButtonStyle)) throw new IllegalArgumentException("style must be an ImageButtonStyle."); 
/*  65 */     super.setStyle(style);
/*  66 */     this.style = (ImageButtonStyle)style;
/*  67 */     if (this.image != null) updateImage(); 
/*     */   }
/*     */   
/*     */   public ImageButtonStyle getStyle() {
/*  71 */     return this.style;
/*     */   }
/*     */   
/*     */   private void updateImage() {
/*  75 */     Drawable drawable = null;
/*  76 */     if (isDisabled() && this.style.imageDisabled != null) {
/*  77 */       drawable = this.style.imageDisabled;
/*  78 */     } else if (isPressed() && this.style.imageDown != null) {
/*  79 */       drawable = this.style.imageDown;
/*  80 */     } else if (this.isChecked && this.style.imageChecked != null) {
/*  81 */       drawable = (this.style.imageCheckedOver != null && isOver()) ? this.style.imageCheckedOver : this.style.imageChecked;
/*  82 */     } else if (isOver() && this.style.imageOver != null) {
/*  83 */       drawable = this.style.imageOver;
/*  84 */     } else if (this.style.imageUp != null) {
/*  85 */       drawable = this.style.imageUp;
/*  86 */     }  this.image.setDrawable(drawable);
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/*  90 */     updateImage();
/*  91 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */   
/*     */   public Image getImage() {
/*  95 */     return this.image;
/*     */   }
/*     */   
/*     */   public Cell getImageCell() {
/*  99 */     return getCell(this.image);
/*     */   }
/*     */   
/*     */   public static class ImageButtonStyle extends Button.ButtonStyle {
/*     */     public Drawable imageUp;
/*     */     public Drawable imageDown;
/*     */     public Drawable imageOver;
/*     */     public Drawable imageChecked;
/*     */     public Drawable imageCheckedOver;
/*     */     public Drawable imageDisabled;
/*     */     
/*     */     public ImageButtonStyle() {}
/*     */     
/*     */     public ImageButtonStyle(Drawable up, Drawable down, Drawable checked, Drawable imageUp, Drawable imageDown, Drawable imageChecked) {
/* 113 */       super(up, down, checked);
/* 114 */       this.imageUp = imageUp;
/* 115 */       this.imageDown = imageDown;
/* 116 */       this.imageChecked = imageChecked;
/*     */     }
/*     */     
/*     */     public ImageButtonStyle(ImageButtonStyle style) {
/* 120 */       super(style);
/* 121 */       this.imageUp = style.imageUp;
/* 122 */       this.imageDown = style.imageDown;
/* 123 */       this.imageOver = style.imageOver;
/* 124 */       this.imageChecked = style.imageChecked;
/* 125 */       this.imageCheckedOver = style.imageCheckedOver;
/* 126 */       this.imageDisabled = style.imageDisabled;
/*     */     }
/*     */     
/*     */     public ImageButtonStyle(Button.ButtonStyle style) {
/* 130 */       super(style);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\ImageButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */