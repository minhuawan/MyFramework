/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.NinePatch;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
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
/*     */ public class Image
/*     */   extends Widget
/*     */ {
/*     */   private Scaling scaling;
/*  37 */   private int align = 1;
/*     */   
/*     */   private float imageX;
/*     */   private float imageY;
/*     */   
/*     */   public Image() {
/*  43 */     this((Drawable)null);
/*     */   }
/*     */   private float imageWidth; private float imageHeight;
/*     */   private Drawable drawable;
/*     */   
/*     */   public Image(NinePatch patch) {
/*  49 */     this((Drawable)new NinePatchDrawable(patch), Scaling.stretch, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Image(TextureRegion region) {
/*  55 */     this((Drawable)new TextureRegionDrawable(region), Scaling.stretch, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Image(Texture texture) {
/*  60 */     this((Drawable)new TextureRegionDrawable(new TextureRegion(texture)));
/*     */   }
/*     */ 
/*     */   
/*     */   public Image(Skin skin, String drawableName) {
/*  65 */     this(skin.getDrawable(drawableName), Scaling.stretch, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Image(Drawable drawable) {
/*  71 */     this(drawable, Scaling.stretch, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Image(Drawable drawable, Scaling scaling) {
/*  77 */     this(drawable, scaling, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Image(Drawable drawable, Scaling scaling, int align) {
/*  82 */     setDrawable(drawable);
/*  83 */     this.scaling = scaling;
/*  84 */     this.align = align;
/*  85 */     setSize(getPrefWidth(), getPrefHeight());
/*     */   }
/*     */   
/*     */   public void layout() {
/*  89 */     if (this.drawable == null)
/*     */       return; 
/*  91 */     float regionWidth = this.drawable.getMinWidth();
/*  92 */     float regionHeight = this.drawable.getMinHeight();
/*  93 */     float width = getWidth();
/*  94 */     float height = getHeight();
/*     */     
/*  96 */     Vector2 size = this.scaling.apply(regionWidth, regionHeight, width, height);
/*  97 */     this.imageWidth = size.x;
/*  98 */     this.imageHeight = size.y;
/*     */     
/* 100 */     if ((this.align & 0x8) != 0) {
/* 101 */       this.imageX = 0.0F;
/* 102 */     } else if ((this.align & 0x10) != 0) {
/* 103 */       this.imageX = (int)(width - this.imageWidth);
/*     */     } else {
/* 105 */       this.imageX = (int)(width / 2.0F - this.imageWidth / 2.0F);
/*     */     } 
/* 107 */     if ((this.align & 0x2) != 0) {
/* 108 */       this.imageY = (int)(height - this.imageHeight);
/* 109 */     } else if ((this.align & 0x4) != 0) {
/* 110 */       this.imageY = 0.0F;
/*     */     } else {
/* 112 */       this.imageY = (int)(height / 2.0F - this.imageHeight / 2.0F);
/*     */     } 
/*     */   }
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 116 */     validate();
/*     */     
/* 118 */     Color color = getColor();
/* 119 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/*     */     
/* 121 */     float x = getX();
/* 122 */     float y = getY();
/* 123 */     float scaleX = getScaleX();
/* 124 */     float scaleY = getScaleY();
/*     */     
/* 126 */     if (this.drawable instanceof TransformDrawable) {
/* 127 */       float rotation = getRotation();
/* 128 */       if (scaleX != 1.0F || scaleY != 1.0F || rotation != 0.0F) {
/* 129 */         ((TransformDrawable)this.drawable).draw(batch, x + this.imageX, y + this.imageY, getOriginX() - this.imageX, getOriginY() - this.imageY, this.imageWidth, this.imageHeight, scaleX, scaleY, rotation);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 134 */     if (this.drawable != null) this.drawable.draw(batch, x + this.imageX, y + this.imageY, this.imageWidth * scaleX, this.imageHeight * scaleY); 
/*     */   }
/*     */   
/*     */   public void setDrawable(Skin skin, String drawableName) {
/* 138 */     setDrawable(skin.getDrawable(drawableName));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDrawable(Drawable drawable) {
/* 143 */     if (this.drawable == drawable)
/* 144 */       return;  if (drawable != null) {
/* 145 */       if (getPrefWidth() != drawable.getMinWidth() || getPrefHeight() != drawable.getMinHeight()) invalidateHierarchy(); 
/*     */     } else {
/* 147 */       invalidateHierarchy();
/* 148 */     }  this.drawable = drawable;
/*     */   }
/*     */ 
/*     */   
/*     */   public Drawable getDrawable() {
/* 153 */     return this.drawable;
/*     */   }
/*     */   
/*     */   public void setScaling(Scaling scaling) {
/* 157 */     if (scaling == null) throw new IllegalArgumentException("scaling cannot be null."); 
/* 158 */     this.scaling = scaling;
/* 159 */     invalidate();
/*     */   }
/*     */   
/*     */   public void setAlign(int align) {
/* 163 */     this.align = align;
/* 164 */     invalidate();
/*     */   }
/*     */   
/*     */   public float getMinWidth() {
/* 168 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getMinHeight() {
/* 172 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 176 */     if (this.drawable != null) return this.drawable.getMinWidth(); 
/* 177 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 181 */     if (this.drawable != null) return this.drawable.getMinHeight(); 
/* 182 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float getImageX() {
/* 186 */     return this.imageX;
/*     */   }
/*     */   
/*     */   public float getImageY() {
/* 190 */     return this.imageY;
/*     */   }
/*     */   
/*     */   public float getImageWidth() {
/* 194 */     return this.imageWidth;
/*     */   }
/*     */   
/*     */   public float getImageHeight() {
/* 198 */     return this.imageHeight;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Image.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */