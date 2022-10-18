/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.RenderableProvider;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Pool;
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
/*     */ public final class ParticleSystem
/*     */   implements RenderableProvider
/*     */ {
/*     */   private static ParticleSystem instance;
/*     */   private Array<ParticleBatch<?>> batches;
/*     */   private Array<ParticleEffect> effects;
/*     */   
/*     */   public static ParticleSystem get() {
/*  33 */     if (instance == null) instance = new ParticleSystem(); 
/*  34 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParticleSystem() {
/*  41 */     this.batches = new Array();
/*  42 */     this.effects = new Array();
/*     */   }
/*     */   
/*     */   public void add(ParticleBatch<?> batch) {
/*  46 */     this.batches.add(batch);
/*     */   }
/*     */   
/*     */   public void add(ParticleEffect effect) {
/*  50 */     this.effects.add(effect);
/*     */   }
/*     */   
/*     */   public void remove(ParticleEffect effect) {
/*  54 */     this.effects.removeValue(effect, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll() {
/*  59 */     this.effects.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  64 */     for (ParticleEffect effect : this.effects) {
/*  65 */       effect.update();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateAndDraw() {
/*  70 */     for (ParticleEffect effect : this.effects) {
/*  71 */       effect.update();
/*  72 */       effect.draw();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  78 */     for (ParticleBatch<?> batch : this.batches) {
/*  79 */       batch.begin();
/*     */     }
/*     */   }
/*     */   
/*     */   public void draw() {
/*  84 */     for (ParticleEffect effect : this.effects) {
/*  85 */       effect.draw();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/*  91 */     for (ParticleBatch<?> batch : this.batches) {
/*  92 */       batch.end();
/*     */     }
/*     */   }
/*     */   
/*     */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/*  97 */     for (ParticleBatch<?> batch : this.batches)
/*  98 */       batch.getRenderables(renderables, pool); 
/*     */   }
/*     */   
/*     */   public Array<ParticleBatch<?>> getBatches() {
/* 102 */     return this.batches;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\ParticleSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */