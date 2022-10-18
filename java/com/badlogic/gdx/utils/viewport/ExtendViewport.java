/*     */ package com.badlogic.gdx.utils.viewport;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.math.Vector2;
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
/*     */ public class ExtendViewport
/*     */   extends Viewport
/*     */ {
/*     */   private float minWorldWidth;
/*     */   private float minWorldHeight;
/*     */   private float maxWorldWidth;
/*     */   private float maxWorldHeight;
/*     */   
/*     */   public ExtendViewport(float minWorldWidth, float minWorldHeight) {
/*  34 */     this(minWorldWidth, minWorldHeight, 0.0F, 0.0F, (Camera)new OrthographicCamera());
/*     */   }
/*     */ 
/*     */   
/*     */   public ExtendViewport(float minWorldWidth, float minWorldHeight, Camera camera) {
/*  39 */     this(minWorldWidth, minWorldHeight, 0.0F, 0.0F, camera);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendViewport(float minWorldWidth, float minWorldHeight, float maxWorldWidth, float maxWorldHeight) {
/*  45 */     this(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight, (Camera)new OrthographicCamera());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendViewport(float minWorldWidth, float minWorldHeight, float maxWorldWidth, float maxWorldHeight, Camera camera) {
/*  52 */     this.minWorldWidth = minWorldWidth;
/*  53 */     this.minWorldHeight = minWorldHeight;
/*  54 */     this.maxWorldWidth = maxWorldWidth;
/*  55 */     this.maxWorldHeight = maxWorldHeight;
/*  56 */     setCamera(camera);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(int screenWidth, int screenHeight, boolean centerCamera) {
/*  62 */     float worldWidth = this.minWorldWidth;
/*  63 */     float worldHeight = this.minWorldHeight;
/*  64 */     Vector2 scaled = Scaling.fit.apply(worldWidth, worldHeight, screenWidth, screenHeight);
/*     */ 
/*     */     
/*  67 */     int viewportWidth = Math.round(scaled.x);
/*  68 */     int viewportHeight = Math.round(scaled.y);
/*  69 */     if (viewportWidth < screenWidth) {
/*  70 */       float toViewportSpace = viewportHeight / worldHeight;
/*  71 */       float toWorldSpace = worldHeight / viewportHeight;
/*  72 */       float lengthen = (screenWidth - viewportWidth) * toWorldSpace;
/*  73 */       if (this.maxWorldWidth > 0.0F) lengthen = Math.min(lengthen, this.maxWorldWidth - this.minWorldWidth); 
/*  74 */       worldWidth += lengthen;
/*  75 */       viewportWidth += Math.round(lengthen * toViewportSpace);
/*  76 */     } else if (viewportHeight < screenHeight) {
/*  77 */       float toViewportSpace = viewportWidth / worldWidth;
/*  78 */       float toWorldSpace = worldWidth / viewportWidth;
/*  79 */       float lengthen = (screenHeight - viewportHeight) * toWorldSpace;
/*  80 */       if (this.maxWorldHeight > 0.0F) lengthen = Math.min(lengthen, this.maxWorldHeight - this.minWorldHeight); 
/*  81 */       worldHeight += lengthen;
/*  82 */       viewportHeight += Math.round(lengthen * toViewportSpace);
/*     */     } 
/*     */     
/*  85 */     setWorldSize(worldWidth, worldHeight);
/*     */ 
/*     */     
/*  88 */     setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);
/*     */     
/*  90 */     apply(centerCamera);
/*     */   }
/*     */   
/*     */   public float getMinWorldWidth() {
/*  94 */     return this.minWorldWidth;
/*     */   }
/*     */   
/*     */   public void setMinWorldWidth(float minWorldWidth) {
/*  98 */     this.minWorldWidth = minWorldWidth;
/*     */   }
/*     */   
/*     */   public float getMinWorldHeight() {
/* 102 */     return this.minWorldHeight;
/*     */   }
/*     */   
/*     */   public void setMinWorldHeight(float minWorldHeight) {
/* 106 */     this.minWorldHeight = minWorldHeight;
/*     */   }
/*     */   
/*     */   public float getMaxWorldWidth() {
/* 110 */     return this.maxWorldWidth;
/*     */   }
/*     */   
/*     */   public void setMaxWorldWidth(float maxWorldWidth) {
/* 114 */     this.maxWorldWidth = maxWorldWidth;
/*     */   }
/*     */   
/*     */   public float getMaxWorldHeight() {
/* 118 */     return this.maxWorldHeight;
/*     */   }
/*     */   
/*     */   public void setMaxWorldHeight(float maxWorldHeight) {
/* 122 */     this.maxWorldHeight = maxWorldHeight;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\viewport\ExtendViewport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */