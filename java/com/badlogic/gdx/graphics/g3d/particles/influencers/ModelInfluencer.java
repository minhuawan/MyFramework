/*     */ package com.badlogic.gdx.graphics.g3d.particles.influencers;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.ModelInstance;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ResourceData;
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
/*     */ public abstract class ModelInfluencer
/*     */   extends Influencer
/*     */ {
/*     */   public Array<Model> models;
/*     */   ParallelArray.ObjectChannel<ModelInstance> modelChannel;
/*     */   
/*     */   public static class Single
/*     */     extends ModelInfluencer
/*     */   {
/*     */     public Single() {}
/*     */     
/*     */     public Single(Single influencer) {
/*  42 */       super(influencer);
/*     */     }
/*     */     
/*     */     public Single(Model... models) {
/*  46 */       super(models);
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/*  51 */       Model first = (Model)this.models.first();
/*  52 */       for (int i = 0, c = this.controller.emitter.maxParticleCount; i < c; i++) {
/*  53 */         ((ModelInstance[])this.modelChannel.data)[i] = new ModelInstance(first);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Single copy() {
/*  59 */       return new Single(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Random
/*     */     extends ModelInfluencer
/*     */   {
/*     */     ModelInstancePool pool;
/*     */     
/*     */     private class ModelInstancePool
/*     */       extends Pool<ModelInstance> {
/*     */       public ModelInstance newObject() {
/*  71 */         return new ModelInstance((Model)ModelInfluencer.Random.this.models.random());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Random() {
/*  79 */       this.pool = new ModelInstancePool();
/*     */     }
/*     */     
/*     */     public Random(Random influencer) {
/*  83 */       super(influencer);
/*  84 */       this.pool = new ModelInstancePool();
/*     */     }
/*     */     
/*     */     public Random(Model... models) {
/*  88 */       super(models);
/*  89 */       this.pool = new ModelInstancePool();
/*     */     }
/*     */ 
/*     */     
/*     */     public void init() {
/*  94 */       this.pool.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public void activateParticles(int startIndex, int count) {
/*  99 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/* 100 */         ((ModelInstance[])this.modelChannel.data)[i] = (ModelInstance)this.pool.obtain();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void killParticles(int startIndex, int count) {
/* 106 */       for (int i = startIndex, c = startIndex + count; i < c; i++) {
/* 107 */         this.pool.free(((ModelInstance[])this.modelChannel.data)[i]);
/* 108 */         ((ModelInstance[])this.modelChannel.data)[i] = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Random copy() {
/* 114 */       return new Random(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelInfluencer() {
/* 122 */     this.models = new Array(true, 1, Model.class);
/*     */   }
/*     */   
/*     */   public ModelInfluencer(Model... models) {
/* 126 */     this.models = new Array((Object[])models);
/*     */   }
/*     */   
/*     */   public ModelInfluencer(ModelInfluencer influencer) {
/* 130 */     this((Model[])influencer.models.toArray(Model.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public void allocateChannels() {
/* 135 */     this.modelChannel = (ParallelArray.ObjectChannel<ModelInstance>)this.controller.particles.addChannel(ParticleChannels.ModelInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(AssetManager manager, ResourceData resources) {
/* 140 */     ResourceData.SaveData data = resources.createSaveData();
/* 141 */     for (Model model : this.models) {
/* 142 */       data.saveAsset(manager.getAssetFileName(model), Model.class);
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(AssetManager manager, ResourceData resources) {
/* 147 */     ResourceData.SaveData data = resources.getSaveData();
/*     */     AssetDescriptor descriptor;
/* 149 */     while ((descriptor = data.loadAsset()) != null) {
/* 150 */       Model model = (Model)manager.get(descriptor);
/* 151 */       if (model == null) throw new RuntimeException("Model is null"); 
/* 152 */       this.models.add(model);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\influencers\ModelInfluencer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */