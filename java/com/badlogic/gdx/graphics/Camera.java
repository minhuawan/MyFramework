/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.math.Frustum;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.Ray;
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
/*     */ public abstract class Camera
/*     */ {
/*  33 */   public final Vector3 position = new Vector3();
/*     */   
/*  35 */   public final Vector3 direction = new Vector3(0.0F, 0.0F, -1.0F);
/*     */   
/*  37 */   public final Vector3 up = new Vector3(0.0F, 1.0F, 0.0F);
/*     */ 
/*     */   
/*  40 */   public final Matrix4 projection = new Matrix4();
/*     */   
/*  42 */   public final Matrix4 view = new Matrix4();
/*     */   
/*  44 */   public final Matrix4 combined = new Matrix4();
/*     */   
/*  46 */   public final Matrix4 invProjectionView = new Matrix4();
/*     */ 
/*     */   
/*  49 */   public float near = 1.0F;
/*     */   
/*  51 */   public float far = 100.0F;
/*     */ 
/*     */   
/*  54 */   public float viewportWidth = 0.0F;
/*     */   
/*  56 */   public float viewportHeight = 0.0F;
/*     */ 
/*     */   
/*  59 */   public final Frustum frustum = new Frustum();
/*     */   
/*  61 */   private final Vector3 tmpVec = new Vector3();
/*  62 */   private final Ray ray = new Ray(new Vector3(), new Vector3());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void update();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void update(boolean paramBoolean);
/*     */ 
/*     */ 
/*     */   
/*     */   public void lookAt(float x, float y, float z) {
/*  77 */     this.tmpVec.set(x, y, z).sub(this.position).nor();
/*  78 */     if (!this.tmpVec.isZero()) {
/*  79 */       float dot = this.tmpVec.dot(this.up);
/*  80 */       if (Math.abs(dot - 1.0F) < 1.0E-9F) {
/*     */         
/*  82 */         this.up.set(this.direction).scl(-1.0F);
/*  83 */       } else if (Math.abs(dot + 1.0F) < 1.0E-9F) {
/*     */         
/*  85 */         this.up.set(this.direction);
/*     */       } 
/*  87 */       this.direction.set(this.tmpVec);
/*  88 */       normalizeUp();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void lookAt(Vector3 target) {
/*  95 */     lookAt(target.x, target.y, target.z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void normalizeUp() {
/* 101 */     this.tmpVec.set(this.direction).crs(this.up).nor();
/* 102 */     this.up.set(this.tmpVec).crs(this.direction).nor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(float angle, float axisX, float axisY, float axisZ) {
/* 113 */     this.direction.rotate(angle, axisX, axisY, axisZ);
/* 114 */     this.up.rotate(angle, axisX, axisY, axisZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(Vector3 axis, float angle) {
/* 123 */     this.direction.rotate(axis, angle);
/* 124 */     this.up.rotate(axis, angle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(Matrix4 transform) {
/* 132 */     this.direction.rot(transform);
/* 133 */     this.up.rot(transform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(Quaternion quat) {
/* 141 */     quat.transform(this.direction);
/* 142 */     quat.transform(this.up);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotateAround(Vector3 point, Vector3 axis, float angle) {
/* 152 */     this.tmpVec.set(point);
/* 153 */     this.tmpVec.sub(this.position);
/* 154 */     translate(this.tmpVec);
/* 155 */     rotate(axis, angle);
/* 156 */     this.tmpVec.rotate(axis, angle);
/* 157 */     translate(-this.tmpVec.x, -this.tmpVec.y, -this.tmpVec.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void transform(Matrix4 transform) {
/* 164 */     this.position.mul(transform);
/* 165 */     rotate(transform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void translate(float x, float y, float z) {
/* 173 */     this.position.add(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void translate(Vector3 vec) {
/* 179 */     this.position.add(vec);
/*     */   }
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
/*     */   public Vector3 unproject(Vector3 screenCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
/* 195 */     float x = screenCoords.x, y = screenCoords.y;
/* 196 */     x -= viewportX;
/* 197 */     y = Gdx.graphics.getHeight() - y - 1.0F;
/* 198 */     y -= viewportY;
/* 199 */     screenCoords.x = 2.0F * x / viewportWidth - 1.0F;
/* 200 */     screenCoords.y = 2.0F * y / viewportHeight - 1.0F;
/* 201 */     screenCoords.z = 2.0F * screenCoords.z - 1.0F;
/* 202 */     screenCoords.prj(this.invProjectionView);
/* 203 */     return screenCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 unproject(Vector3 screenCoords) {
/* 214 */     unproject(screenCoords, 0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/* 215 */     return screenCoords;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 project(Vector3 worldCoords) {
/* 224 */     project(worldCoords, 0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/* 225 */     return worldCoords;
/*     */   }
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
/*     */   public Vector3 project(Vector3 worldCoords, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
/* 240 */     worldCoords.prj(this.combined);
/* 241 */     worldCoords.x = viewportWidth * (worldCoords.x + 1.0F) / 2.0F + viewportX;
/* 242 */     worldCoords.y = viewportHeight * (worldCoords.y + 1.0F) / 2.0F + viewportY;
/* 243 */     worldCoords.z = (worldCoords.z + 1.0F) / 2.0F;
/* 244 */     return worldCoords;
/*     */   }
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
/*     */   public Ray getPickRay(float screenX, float screenY, float viewportX, float viewportY, float viewportWidth, float viewportHeight) {
/* 257 */     unproject(this.ray.origin.set(screenX, screenY, 0.0F), viewportX, viewportY, viewportWidth, viewportHeight);
/* 258 */     unproject(this.ray.direction.set(screenX, screenY, 1.0F), viewportX, viewportY, viewportWidth, viewportHeight);
/* 259 */     this.ray.direction.sub(this.ray.origin).nor();
/* 260 */     return this.ray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ray getPickRay(float screenX, float screenY) {
/* 268 */     return getPickRay(screenX, screenY, 0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\Camera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */