/*     */ package com.badlogic.gdx.maps.objects;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.MapObject;
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
/*     */ public class TextureMapObject
/*     */   extends MapObject
/*     */ {
/*  25 */   private float x = 0.0F;
/*  26 */   private float y = 0.0F;
/*  27 */   private float originX = 0.0F;
/*  28 */   private float originY = 0.0F;
/*  29 */   private float scaleX = 1.0F;
/*  30 */   private float scaleY = 1.0F;
/*  31 */   private float rotation = 0.0F;
/*  32 */   private TextureRegion textureRegion = null;
/*     */ 
/*     */   
/*     */   public float getX() {
/*  36 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setX(float x) {
/*  41 */     this.x = x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/*  46 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setY(float y) {
/*  51 */     this.y = y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOriginX() {
/*  56 */     return this.originX;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOriginX(float x) {
/*  61 */     this.originX = x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOriginY() {
/*  66 */     return this.originY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOriginY(float y) {
/*  71 */     this.originY = y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScaleX() {
/*  76 */     return this.scaleX;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScaleX(float x) {
/*  81 */     this.scaleX = x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScaleY() {
/*  86 */     return this.scaleY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScaleY(float y) {
/*  91 */     this.scaleY = y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRotation() {
/*  96 */     return this.rotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotation(float rotation) {
/* 101 */     this.rotation = rotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureRegion getTextureRegion() {
/* 106 */     return this.textureRegion;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTextureRegion(TextureRegion region) {
/* 111 */     this.textureRegion = region;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureMapObject() {
/* 116 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureMapObject(TextureRegion textureRegion) {
/* 124 */     this.textureRegion = textureRegion;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\objects\TextureMapObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */