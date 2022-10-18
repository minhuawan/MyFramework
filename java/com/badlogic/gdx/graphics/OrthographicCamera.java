/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.math.Vector3;
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
/*     */ public class OrthographicCamera
/*     */   extends Camera
/*     */ {
/*  29 */   public float zoom = 1.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Vector3 tmp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrthographicCamera() {
/*  47 */     this.tmp = new Vector3(); this.near = 0.0F; } public OrthographicCamera(float viewportWidth, float viewportHeight) { this.tmp = new Vector3();
/*     */     this.viewportWidth = viewportWidth;
/*     */     this.viewportHeight = viewportHeight;
/*     */     this.near = 0.0F;
/*  51 */     update(); } public void update() { update(true); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(boolean updateFrustum) {
/*  56 */     this.projection.setToOrtho(this.zoom * -this.viewportWidth / 2.0F, this.zoom * this.viewportWidth / 2.0F, this.zoom * -(this.viewportHeight / 2.0F), this.zoom * this.viewportHeight / 2.0F, this.near, this.far);
/*     */     
/*  58 */     this.view.setToLookAt(this.position, this.tmp.set(this.position).add(this.direction), this.up);
/*  59 */     this.combined.set(this.projection);
/*  60 */     Matrix4.mul(this.combined.val, this.view.val);
/*     */     
/*  62 */     if (updateFrustum) {
/*  63 */       this.invProjectionView.set(this.combined);
/*  64 */       Matrix4.inv(this.invProjectionView.val);
/*  65 */       this.frustum.update(this.invProjectionView);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToOrtho(boolean yDown) {
/*  73 */     setToOrtho(yDown, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToOrtho(boolean yDown, float viewportWidth, float viewportHeight) {
/*  82 */     if (yDown) {
/*  83 */       this.up.set(0.0F, -1.0F, 0.0F);
/*  84 */       this.direction.set(0.0F, 0.0F, 1.0F);
/*     */     } else {
/*  86 */       this.up.set(0.0F, 1.0F, 0.0F);
/*  87 */       this.direction.set(0.0F, 0.0F, -1.0F);
/*     */     } 
/*  89 */     this.position.set(this.zoom * viewportWidth / 2.0F, this.zoom * viewportHeight / 2.0F, 0.0F);
/*  90 */     this.viewportWidth = viewportWidth;
/*  91 */     this.viewportHeight = viewportHeight;
/*  92 */     update();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(float angle) {
/*  98 */     rotate(this.direction, angle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void translate(float x, float y) {
/* 105 */     translate(x, y, 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void translate(Vector2 vec) {
/* 111 */     translate(vec.x, vec.y, 0.0F);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\OrthographicCamera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */