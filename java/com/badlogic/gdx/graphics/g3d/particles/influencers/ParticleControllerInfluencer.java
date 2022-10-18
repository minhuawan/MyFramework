/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.Pool;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class ParticleControllerInfluencer
/*     */   extends Influencer
/*     */ {
/*     */   public Array<ParticleController> templates;
/*     */   ParallelArray.ObjectChannel<ParticleController> particleControllerChannel;
/*     */   
/*     */   public static class Single
/*     */     extends ParticleControllerInfluencer
/*     */   {
/*     */     public Single(ParticleController... templates) {
/*  41 */       super(templates);
/*     */     }
/*     */ 
/*     */     
/*     */     public Single() {}
/*     */ 
/*     */     
/*     */     public Single(Single particleControllerSingle) {
/*  49 */       super(particleControllerSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/*  54 */       ParticleController first = (ParticleController)this.templates.first();
/*  55 */       for (int i = 0, c = this.controller.particles.capacity; i < c; i++) {
/*  56 */         ParticleController copy = first.copy();
/*  57 */         copy.init();
/*  58 */         ((ParticleController[])this.particleControllerChannel.data)[i] = copy;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  64 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/*  65 */         ((ParticleController[])this.particleControllerChannel.data)[i].start();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void killParticles(int startIndex, int count) {
/*  71 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/*  72 */         ((ParticleController[])this.particleControllerChannel.data)[i].end();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Single copy() {
/*  78 */       return new Single(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Random
/*     */     extends ParticleControllerInfluencer
/*     */   {
/*     */     ParticleControllerPool pool;
/*     */     
/*     */     private class ParticleControllerPool
/*     */       extends Pool<ParticleController> {
/*     */       public ParticleController newObject() {
/*  90 */         ParticleController controller = ((ParticleController)ParticleControllerInfluencer.Random.this.templates.random()).copy();
/*  91 */         controller.init();
/*  92 */         return controller;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void clear() {
/*  98 */         for (int i = 0, free = ParticleControllerInfluencer.Random.this.pool.getFree(); i < free; i++) {
/*  99 */           ((ParticleController)ParticleControllerInfluencer.Random.this.pool.obtain()).dispose();
/*     */         }
/* 101 */         super.clear();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Random() {
/* 109 */       this.pool = new ParticleControllerPool();
/*     */     }
/*     */     
/*     */     public Random(ParticleController... templates) {
/* 113 */       super(templates);
/* 114 */       this.pool = new ParticleControllerPool();
/*     */     }
/*     */     
/*     */     public Random(Random particleControllerRandom) {
/* 118 */       super(particleControllerRandom);
/* 119 */       this.pool = new ParticleControllerPool();
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/* 124 */       this.pool.clear();
/*     */       
/* 126 */       for (int i = 0; i < this.controller.emitter.maxParticleCount; i++) {
/* 127 */         this.pool.free(this.pool.newObject());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 133 */       this.pool.clear();
/* 134 */       super.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/* 139 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/* 140 */         ParticleController controller = (ParticleController)this.pool.obtain();
/* 141 */         controller.start();
/* 142 */         ((ParticleController[])this.particleControllerChannel.data)[i] = controller;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void killParticles(int startIndex, int count) {
/* 148 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/* 149 */         ParticleController controller = ((ParticleController[])this.particleControllerChannel.data)[i];
/* 150 */         controller.end();
/* 151 */         this.pool.free(controller);
/* 152 */         ((ParticleController[])this.particleControllerChannel.data)[i] = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Random copy() {
/* 158 */       return new Random(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParticleControllerInfluencer() {
/* 166 */     this.templates = new Array(true, 1, ParticleController.class);
/*     */   }
/*     */   
/*     */   public ParticleControllerInfluencer(ParticleController... templates) {
/* 170 */     this.templates = new Array((Object[])templates);
/*     */   }
/*     */   
/*     */   public ParticleControllerInfluencer(ParticleControllerInfluencer influencer) {
/* 174 */     this((ParticleController[])influencer.templates.items);
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 179 */     this.particleControllerChannel = (ParallelArray.ObjectChannel<ParticleController>)this.controller.particles.addChannel(ParticleChannels.ParticleController);
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 184 */     for (int i = 0; i < this.controller.particles.size; i++) {
/* 185 */       ((ParticleController[])this.particleControllerChannel.data)[i].end();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 191 */     if (this.controller != null) {
/* 192 */       for (int i = 0; i < this.controller.particles.size; i++) {
/* 193 */         ParticleController controller = ((ParticleController[])this.particleControllerChannel.data)[i];
/* 194 */         if (controller != null) {
/* 195 */           controller.dispose();
/* 196 */           ((ParticleController[])this.particleControllerChannel.data)[i] = null;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(AssetManager manager, ResourceData resources) {
/* 204 */     ResourceData.SaveData data = resources.createSaveData();
/* 205 */     Array<ParticleEffect> effects = manager.getAll(ParticleEffect.class, new Array());
/*     */     
/* 207 */     Array<ParticleController> controllers = new Array(this.templates);
/* 208 */     Array<IntArray> effectsIndices = new Array();
/*     */     
/* 210 */     for (int i = 0; i < effects.size && controllers.size > 0; i++) {
/* 211 */       ParticleEffect effect = (ParticleEffect)effects.get(i);
/* 212 */       Array<ParticleController> effectControllers = effect.getControllers();
/* 213 */       Iterator<ParticleController> iterator = controllers.iterator();
/* 214 */       IntArray indices = null;
/* 215 */       while (iterator.hasNext()) {
/* 216 */         ParticleController controller = iterator.next();
/* 217 */         int index = -1;
/* 218 */         if ((index = effectControllers.indexOf(controller, true)) > -1) {
/* 219 */           if (indices == null) {
/* 220 */             indices = new IntArray();
/*     */           }
/* 222 */           iterator.remove();
/* 223 */           indices.add(index);
/*     */         } 
/*     */       } 
/*     */       
/* 227 */       if (indices != null) {
/* 228 */         data.saveAsset(manager.getAssetFileName(effect), ParticleEffect.class);
/* 229 */         effectsIndices.add(indices);
/*     */       } 
/*     */     } 
/* 232 */     data.save("indices", effectsIndices);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager manager, ResourceData resources) {
/* 237 */     ResourceData.SaveData data = resources.getSaveData();
/* 238 */     Array<IntArray> effectsIndices = (Array<IntArray>)data.load("indices");
/*     */     
/* 240 */     Iterator<IntArray> iterator = effectsIndices.iterator(); AssetDescriptor descriptor;
/* 241 */     while ((descriptor = data.loadAsset()) != null) {
/* 242 */       ParticleEffect effect = (ParticleEffect)manager.get(descriptor);
/* 243 */       if (effect == null) throw new RuntimeException("Template is null"); 
/* 244 */       Array<ParticleController> effectControllers = effect.getControllers();
/* 245 */       IntArray effectIndices = iterator.next();
/*     */       
/* 247 */       for (int i = 0, n = effectIndices.size; i < n; i++)
/* 248 */         this.templates.add(effectControllers.get(effectIndices.get(i))); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\ParticleControllerInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */