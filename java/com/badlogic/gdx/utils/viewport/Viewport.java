/*     */ package com.badlogic.gdx.utils.viewport;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.glutils.HdpiUtils;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.Ray;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
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
/*     */ public abstract class Viewport
/*     */ {
/*     */   private Camera camera;
/*     */   private float worldWidth;
/*     */   private float worldHeight;
/*     */   private int screenX;
/*     */   private int screenY;
/*     */   private int screenWidth;
/*     */   private int screenHeight;
/*  39 */   private final Vector3 tmp = new Vector3();
/*     */ 
/*     */   
/*     */   public void apply() {
/*  43 */     apply(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(boolean centerCamera) {
/*  49 */     HdpiUtils.glViewport(this.screenX, this.screenY, this.screenWidth, this.screenHeight);
/*  50 */     this.camera.viewportWidth = this.worldWidth;
/*  51 */     this.camera.viewportHeight = this.worldHeight;
/*  52 */     if (centerCamera) this.camera.position.set(this.worldWidth / 2.0F, this.worldHeight / 2.0F, 0.0F); 
/*  53 */     this.camera.update();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void update(int screenWidth, int screenHeight) {
/*  58 */     update(screenWidth, screenHeight, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(int screenWidth, int screenHeight, boolean centerCamera) {
/*  66 */     apply(centerCamera);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 unproject(Vector2 screenCoords) {
/*  73 */     this.tmp.set(screenCoords.x, screenCoords.y, 1.0F);
/*  74 */     this.camera.unproject(this.tmp, this.screenX, this.screenY, this.screenWidth, this.screenHeight);
/*  75 */     screenCoords.set(this.tmp.x, this.tmp.y);
/*  76 */     return screenCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 project(Vector2 worldCoords) {
/*  83 */     this.tmp.set(worldCoords.x, worldCoords.y, 1.0F);
/*  84 */     this.camera.project(this.tmp, this.screenX, this.screenY, this.screenWidth, this.screenHeight);
/*  85 */     worldCoords.set(this.tmp.x, this.tmp.y);
/*  86 */     return worldCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 unproject(Vector3 screenCoords) {
/*  93 */     this.camera.unproject(screenCoords, this.screenX, this.screenY, this.screenWidth, this.screenHeight);
/*  94 */     return screenCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 project(Vector3 worldCoords) {
/* 101 */     this.camera.project(worldCoords, this.screenX, this.screenY, this.screenWidth, this.screenHeight);
/* 102 */     return worldCoords;
/*     */   }
/*     */ 
/*     */   
/*     */   public Ray getPickRay(float screenX, float screenY) {
/* 107 */     return this.camera.getPickRay(screenX, screenY, this.screenX, this.screenY, this.screenWidth, this.screenHeight);
/*     */   }
/*     */ 
/*     */   
/*     */   public void calculateScissors(Matrix4 batchTransform, Rectangle area, Rectangle scissor) {
/* 112 */     ScissorStack.calculateScissors(this.camera, this.screenX, this.screenY, this.screenWidth, this.screenHeight, batchTransform, area, scissor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2 toScreenCoordinates(Vector2 worldCoords, Matrix4 transformMatrix) {
/* 118 */     this.tmp.set(worldCoords.x, worldCoords.y, 0.0F);
/* 119 */     this.tmp.mul(transformMatrix);
/* 120 */     this.camera.project(this.tmp);
/* 121 */     this.tmp.y = Gdx.graphics.getHeight() - this.tmp.y;
/* 122 */     worldCoords.x = this.tmp.x;
/* 123 */     worldCoords.y = this.tmp.y;
/* 124 */     return worldCoords;
/*     */   }
/*     */   
/*     */   public Camera getCamera() {
/* 128 */     return this.camera;
/*     */   }
/*     */   
/*     */   public void setCamera(Camera camera) {
/* 132 */     this.camera = camera;
/*     */   }
/*     */   
/*     */   public float getWorldWidth() {
/* 136 */     return this.worldWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldWidth(float worldWidth) {
/* 141 */     this.worldWidth = worldWidth;
/*     */   }
/*     */   
/*     */   public float getWorldHeight() {
/* 145 */     return this.worldHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldHeight(float worldHeight) {
/* 150 */     this.worldHeight = worldHeight;
/*     */   }
/*     */   
/*     */   public void setWorldSize(float worldWidth, float worldHeight) {
/* 154 */     this.worldWidth = worldWidth;
/* 155 */     this.worldHeight = worldHeight;
/*     */   }
/*     */   
/*     */   public int getScreenX() {
/* 159 */     return this.screenX;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScreenX(int screenX) {
/* 164 */     this.screenX = screenX;
/*     */   }
/*     */   
/*     */   public int getScreenY() {
/* 168 */     return this.screenY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScreenY(int screenY) {
/* 173 */     this.screenY = screenY;
/*     */   }
/*     */   
/*     */   public int getScreenWidth() {
/* 177 */     return this.screenWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScreenWidth(int screenWidth) {
/* 182 */     this.screenWidth = screenWidth;
/*     */   }
/*     */   
/*     */   public int getScreenHeight() {
/* 186 */     return this.screenHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScreenHeight(int screenHeight) {
/* 191 */     this.screenHeight = screenHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScreenPosition(int screenX, int screenY) {
/* 196 */     this.screenX = screenX;
/* 197 */     this.screenY = screenY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScreenSize(int screenWidth, int screenHeight) {
/* 202 */     this.screenWidth = screenWidth;
/* 203 */     this.screenHeight = screenHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScreenBounds(int screenX, int screenY, int screenWidth, int screenHeight) {
/* 208 */     this.screenX = screenX;
/* 209 */     this.screenY = screenY;
/* 210 */     this.screenWidth = screenWidth;
/* 211 */     this.screenHeight = screenHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLeftGutterWidth() {
/* 216 */     return this.screenX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRightGutterX() {
/* 221 */     return this.screenX + this.screenWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRightGutterWidth() {
/* 226 */     return Gdx.graphics.getWidth() - this.screenX + this.screenWidth;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBottomGutterHeight() {
/* 231 */     return this.screenY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTopGutterY() {
/* 236 */     return this.screenY + this.screenHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTopGutterHeight() {
/* 241 */     return Gdx.graphics.getHeight() - this.screenY + this.screenHeight;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\viewport\Viewport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */