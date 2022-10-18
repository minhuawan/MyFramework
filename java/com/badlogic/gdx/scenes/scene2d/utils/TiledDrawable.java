/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
/*     */ public class TiledDrawable
/*     */   extends TextureRegionDrawable
/*     */ {
/*  27 */   private final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TiledDrawable(TextureRegion region) {
/*  34 */     super(region);
/*     */   }
/*     */   
/*     */   public TiledDrawable(TextureRegionDrawable drawable) {
/*  38 */     super(drawable);
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float x, float y, float width, float height) {
/*  42 */     float batchColor = batch.getPackedColor();
/*  43 */     batch.setColor(batch.getColor().mul(this.color));
/*     */     
/*  45 */     TextureRegion region = getRegion();
/*  46 */     float regionWidth = region.getRegionWidth(), regionHeight = region.getRegionHeight();
/*  47 */     int fullX = (int)(width / regionWidth), fullY = (int)(height / regionHeight);
/*  48 */     float remainingX = width - regionWidth * fullX, remainingY = height - regionHeight * fullY;
/*  49 */     float startX = x, startY = y;
/*  50 */     float endX = x + width - remainingX, endY = y + height - remainingY;
/*  51 */     for (int i = 0; i < fullX; i++) {
/*  52 */       y = startY;
/*  53 */       for (int ii = 0; ii < fullY; ii++) {
/*  54 */         batch.draw(region, x, y, regionWidth, regionHeight);
/*  55 */         y += regionHeight;
/*     */       } 
/*  57 */       x += regionWidth;
/*     */     } 
/*  59 */     Texture texture = region.getTexture();
/*  60 */     float u = region.getU();
/*  61 */     float v2 = region.getV2();
/*  62 */     if (remainingX > 0.0F) {
/*     */       
/*  64 */       float u2 = u + remainingX / texture.getWidth();
/*  65 */       float v = region.getV();
/*  66 */       y = startY;
/*  67 */       for (int ii = 0; ii < fullY; ii++) {
/*  68 */         batch.draw(texture, x, y, remainingX, regionHeight, u, v2, u2, v);
/*  69 */         y += regionHeight;
/*     */       } 
/*     */       
/*  72 */       if (remainingY > 0.0F) {
/*  73 */         v = v2 - remainingY / texture.getHeight();
/*  74 */         batch.draw(texture, x, y, remainingX, remainingY, u, v2, u2, v);
/*     */       } 
/*     */     } 
/*  77 */     if (remainingY > 0.0F) {
/*     */       
/*  79 */       float u2 = region.getU2();
/*  80 */       float v = v2 - remainingY / texture.getHeight();
/*  81 */       x = startX;
/*  82 */       for (int j = 0; j < fullX; j++) {
/*  83 */         batch.draw(texture, x, y, regionWidth, remainingY, u, v2, u2, v);
/*  84 */         x += regionWidth;
/*     */       } 
/*     */     } 
/*     */     
/*  88 */     batch.setColor(batchColor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/*  93 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Color getColor() {
/*  97 */     return this.color;
/*     */   }
/*     */   
/*     */   public TiledDrawable tint(Color tint) {
/* 101 */     TiledDrawable drawable = new TiledDrawable(this);
/* 102 */     drawable.color.set(tint);
/* 103 */     drawable.setLeftWidth(getLeftWidth());
/* 104 */     drawable.setRightWidth(getRightWidth());
/* 105 */     drawable.setTopHeight(getTopHeight());
/* 106 */     drawable.setBottomHeight(getBottomHeight());
/* 107 */     return drawable;
/*     */   }
/*     */   
/*     */   public TiledDrawable() {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\TiledDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */