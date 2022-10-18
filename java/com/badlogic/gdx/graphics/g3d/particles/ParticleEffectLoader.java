/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticleEffectLoader
/*     */   extends AsynchronousAssetLoader<ParticleEffect, ParticleEffectLoader.ParticleEffectLoadParameter>
/*     */ {
/*  48 */   protected Array<ObjectMap.Entry<String, ResourceData<ParticleEffect>>> items = new Array();
/*     */   
/*     */   public ParticleEffectLoader(FileHandleResolver resolver) {
/*  51 */     super(resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, ParticleEffectLoadParameter parameter) {}
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ParticleEffectLoadParameter parameter) {
/*  60 */     Json json = new Json();
/*  61 */     ResourceData<ParticleEffect> data = (ResourceData<ParticleEffect>)json.fromJson(ResourceData.class, file);
/*  62 */     Array<ResourceData.AssetData> assets = null;
/*  63 */     synchronized (this.items) {
/*  64 */       ObjectMap.Entry<String, ResourceData<ParticleEffect>> entry = new ObjectMap.Entry();
/*  65 */       entry.key = fileName;
/*  66 */       entry.value = data;
/*  67 */       this.items.add(entry);
/*  68 */       assets = data.getAssets();
/*     */     } 
/*     */     
/*  71 */     Array<AssetDescriptor> descriptors = new Array();
/*  72 */     for (ResourceData.AssetData<?> assetData : assets) {
/*     */ 
/*     */       
/*  75 */       if (!resolve(assetData.filename).exists()) {
/*  76 */         assetData.filename = file.parent().child(Gdx.files.internal(assetData.filename).name()).path();
/*     */       }
/*     */       
/*  79 */       if (assetData.type == ParticleEffect.class) {
/*  80 */         descriptors.add(new AssetDescriptor(assetData.filename, assetData.type, parameter)); continue;
/*     */       } 
/*  82 */       descriptors.add(new AssetDescriptor(assetData.filename, assetData.type));
/*     */     } 
/*     */     
/*  85 */     return descriptors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save(ParticleEffect effect, ParticleEffectSaveParameter parameter) throws IOException {
/*  91 */     ResourceData<ParticleEffect> data = new ResourceData<ParticleEffect>(effect);
/*     */ 
/*     */     
/*  94 */     effect.save(parameter.manager, data);
/*     */ 
/*     */     
/*  97 */     if (parameter.batches != null) {
/*  98 */       for (ParticleBatch<?> batch : parameter.batches) {
/*  99 */         boolean save = false;
/* 100 */         for (ParticleController controller : effect.getControllers()) {
/* 101 */           if (controller.renderer.isCompatible(batch)) {
/* 102 */             save = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 107 */         if (save) batch.save(parameter.manager, data);
/*     */       
/*     */       } 
/*     */     }
/*     */     
/* 112 */     Json json = new Json();
/* 113 */     json.toJson(data, parameter.file);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleEffect loadSync(AssetManager manager, String fileName, FileHandle file, ParticleEffectLoadParameter parameter) {
/* 118 */     ResourceData<ParticleEffect> effectData = null;
/* 119 */     synchronized (this.items) {
/* 120 */       for (int i = 0; i < this.items.size; i++) {
/* 121 */         ObjectMap.Entry<String, ResourceData<ParticleEffect>> entry = (ObjectMap.Entry<String, ResourceData<ParticleEffect>>)this.items.get(i);
/* 122 */         if (((String)entry.key).equals(fileName)) {
/* 123 */           effectData = (ResourceData<ParticleEffect>)entry.value;
/* 124 */           this.items.removeIndex(i);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 130 */     ((ParticleEffect)effectData.resource).load(manager, effectData);
/* 131 */     if (parameter != null) {
/* 132 */       if (parameter.batches != null) {
/* 133 */         for (ParticleBatch<?> batch : parameter.batches) {
/* 134 */           batch.load(manager, effectData);
/*     */         }
/*     */       }
/* 137 */       ((ParticleEffect)effectData.resource).setBatch(parameter.batches);
/*     */     } 
/* 139 */     return (ParticleEffect)effectData.resource;
/*     */   }
/*     */   
/*     */   private <T> T find(Array<?> array, Class<T> type) {
/* 143 */     for (Object object : array) {
/* 144 */       if (ClassReflection.isAssignableFrom(type, object.getClass())) return (T)object; 
/*     */     } 
/* 146 */     return null;
/*     */   }
/*     */   
/*     */   public static class ParticleEffectLoadParameter extends AssetLoaderParameters<ParticleEffect> {
/*     */     Array<ParticleBatch<?>> batches;
/*     */     
/*     */     public ParticleEffectLoadParameter(Array<ParticleBatch<?>> batches) {
/* 153 */       this.batches = batches;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ParticleEffectSaveParameter
/*     */     extends AssetLoaderParameters<ParticleEffect>
/*     */   {
/*     */     Array<ParticleBatch<?>> batches;
/*     */     FileHandle file;
/*     */     AssetManager manager;
/*     */     
/*     */     public ParticleEffectSaveParameter(FileHandle file, AssetManager manager, Array<ParticleBatch<?>> batches) {
/* 166 */       this.batches = batches;
/* 167 */       this.file = file;
/* 168 */       this.manager = manager;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\ParticleEffectLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */