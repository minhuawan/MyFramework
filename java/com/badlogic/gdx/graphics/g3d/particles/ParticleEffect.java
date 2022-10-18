/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
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
/*     */ public class ParticleEffect
/*     */   implements Disposable, ResourceData.Configurable
/*     */ {
/*     */   private Array<ParticleController> controllers;
/*     */   private BoundingBox bounds;
/*     */   
/*     */   public ParticleEffect() {
/*  36 */     this.controllers = new Array(true, 3, ParticleController.class);
/*     */   }
/*     */   
/*     */   public ParticleEffect(ParticleEffect effect) {
/*  40 */     this.controllers = new Array(true, effect.controllers.size);
/*  41 */     for (int i = 0, n = effect.controllers.size; i < n; i++)
/*  42 */       this.controllers.add(((ParticleController)effect.controllers.get(i)).copy()); 
/*     */   }
/*     */   
/*     */   public ParticleEffect(ParticleController... emitters) {
/*  46 */     this.controllers = new Array((Object[])emitters);
/*     */   }
/*     */   
/*     */   public void init() {
/*  50 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  51 */       ((ParticleController)this.controllers.get(i)).init(); 
/*     */   }
/*     */   
/*     */   public void start() {
/*  55 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  56 */       ((ParticleController)this.controllers.get(i)).start(); 
/*     */   }
/*     */   
/*     */   public void end() {
/*  60 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  61 */       ((ParticleController)this.controllers.get(i)).end(); 
/*     */   }
/*     */   
/*     */   public void reset() {
/*  65 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  66 */       ((ParticleController)this.controllers.get(i)).reset(); 
/*     */   }
/*     */   
/*     */   public void update() {
/*  70 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  71 */       ((ParticleController)this.controllers.get(i)).update(); 
/*     */   }
/*     */   
/*     */   public void draw() {
/*  75 */     for (int i = 0, n = this.controllers.size; i < n; i++)
/*  76 */       ((ParticleController)this.controllers.get(i)).draw(); 
/*     */   }
/*     */   
/*     */   public boolean isComplete() {
/*  80 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/*  81 */       if (!((ParticleController)this.controllers.get(i)).isComplete()) {
/*  82 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransform(Matrix4 transform) {
/*  91 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/*  92 */       ((ParticleController)this.controllers.get(i)).setTransform(transform);
/*     */     }
/*     */   }
/*     */   
/*     */   public void rotate(Quaternion rotation) {
/*  97 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/*  98 */       ((ParticleController)this.controllers.get(i)).rotate(rotation);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(Vector3 axis, float angle) {
/* 105 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 106 */       ((ParticleController)this.controllers.get(i)).rotate(axis, angle);
/*     */     }
/*     */   }
/*     */   
/*     */   public void translate(Vector3 translation) {
/* 111 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 112 */       ((ParticleController)this.controllers.get(i)).translate(translation);
/*     */     }
/*     */   }
/*     */   
/*     */   public void scale(float scaleX, float scaleY, float scaleZ) {
/* 117 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 118 */       ((ParticleController)this.controllers.get(i)).scale(scaleX, scaleY, scaleZ);
/*     */     }
/*     */   }
/*     */   
/*     */   public void scale(Vector3 scale) {
/* 123 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 124 */       ((ParticleController)this.controllers.get(i)).scale(scale.x, scale.y, scale.z);
/*     */     }
/*     */   }
/*     */   
/*     */   public Array<ParticleController> getControllers() {
/* 129 */     return this.controllers;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleController findController(String name) {
/* 134 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 135 */       ParticleController emitter = (ParticleController)this.controllers.get(i);
/* 136 */       if (emitter.name.equals(name)) return emitter; 
/*     */     } 
/* 138 */     return null;
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 142 */     for (int i = 0, n = this.controllers.size; i < n; i++) {
/* 143 */       ((ParticleController)this.controllers.get(i)).dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/* 149 */     if (this.bounds == null) this.bounds = new BoundingBox();
/*     */     
/* 151 */     BoundingBox bounds = this.bounds;
/* 152 */     bounds.inf();
/* 153 */     for (ParticleController emitter : this.controllers)
/* 154 */       bounds.ext(emitter.getBoundingBox()); 
/* 155 */     return bounds;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBatch(Array<ParticleBatch<?>> batches) {
/* 161 */     for (ParticleController controller : this.controllers) {
/* 162 */       for (ParticleBatch<?> batch : batches) {
/* 163 */         if (controller.renderer.setBatch(batch))
/*     */           break; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public ParticleEffect copy() {
/* 169 */     return new ParticleEffect(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(AssetManager assetManager, ResourceData data) {
/* 174 */     for (ParticleController controller : this.controllers) {
/* 175 */       controller.save(assetManager, data);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager assetManager, ResourceData data) {
/* 181 */     int i = 0;
/* 182 */     for (ParticleController controller : this.controllers)
/* 183 */       controller.load(assetManager, data); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\ParticleEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */