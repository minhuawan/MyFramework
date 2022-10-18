/*     */ package com.badlogic.gdx.graphics.g3d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.InputAdapter;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.IntIntMap;
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
/*     */ public class FirstPersonCameraController
/*     */   extends InputAdapter
/*     */ {
/*     */   private final Camera camera;
/*  30 */   private final IntIntMap keys = new IntIntMap();
/*  31 */   private int STRAFE_LEFT = 29;
/*  32 */   private int STRAFE_RIGHT = 32;
/*  33 */   private int FORWARD = 51;
/*  34 */   private int BACKWARD = 47;
/*  35 */   private int UP = 45;
/*  36 */   private int DOWN = 33;
/*  37 */   private float velocity = 5.0F;
/*  38 */   private float degreesPerPixel = 0.5F;
/*  39 */   private final Vector3 tmp = new Vector3();
/*     */   
/*     */   public FirstPersonCameraController(Camera camera) {
/*  42 */     this.camera = camera;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyDown(int keycode) {
/*  47 */     this.keys.put(keycode, keycode);
/*  48 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyUp(int keycode) {
/*  53 */     this.keys.remove(keycode, 0);
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVelocity(float velocity) {
/*  60 */     this.velocity = velocity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDegreesPerPixel(float degreesPerPixel) {
/*  66 */     this.degreesPerPixel = degreesPerPixel;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchDragged(int screenX, int screenY, int pointer) {
/*  71 */     float deltaX = -Gdx.input.getDeltaX() * this.degreesPerPixel;
/*  72 */     float deltaY = -Gdx.input.getDeltaY() * this.degreesPerPixel;
/*  73 */     this.camera.direction.rotate(this.camera.up, deltaX);
/*  74 */     this.tmp.set(this.camera.direction).crs(this.camera.up).nor();
/*  75 */     this.camera.direction.rotate(this.tmp, deltaY);
/*     */     
/*  77 */     return true;
/*     */   }
/*     */   
/*     */   public void update() {
/*  81 */     update(Gdx.graphics.getDeltaTime());
/*     */   }
/*     */   
/*     */   public void update(float deltaTime) {
/*  85 */     if (this.keys.containsKey(this.FORWARD)) {
/*  86 */       this.tmp.set(this.camera.direction).nor().scl(deltaTime * this.velocity);
/*  87 */       this.camera.position.add(this.tmp);
/*     */     } 
/*  89 */     if (this.keys.containsKey(this.BACKWARD)) {
/*  90 */       this.tmp.set(this.camera.direction).nor().scl(-deltaTime * this.velocity);
/*  91 */       this.camera.position.add(this.tmp);
/*     */     } 
/*  93 */     if (this.keys.containsKey(this.STRAFE_LEFT)) {
/*  94 */       this.tmp.set(this.camera.direction).crs(this.camera.up).nor().scl(-deltaTime * this.velocity);
/*  95 */       this.camera.position.add(this.tmp);
/*     */     } 
/*  97 */     if (this.keys.containsKey(this.STRAFE_RIGHT)) {
/*  98 */       this.tmp.set(this.camera.direction).crs(this.camera.up).nor().scl(deltaTime * this.velocity);
/*  99 */       this.camera.position.add(this.tmp);
/*     */     } 
/* 101 */     if (this.keys.containsKey(this.UP)) {
/* 102 */       this.tmp.set(this.camera.up).nor().scl(deltaTime * this.velocity);
/* 103 */       this.camera.position.add(this.tmp);
/*     */     } 
/* 105 */     if (this.keys.containsKey(this.DOWN)) {
/* 106 */       this.tmp.set(this.camera.up).nor().scl(-deltaTime * this.velocity);
/* 107 */       this.camera.position.add(this.tmp);
/*     */     } 
/* 109 */     this.camera.update(true);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\FirstPersonCameraController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */