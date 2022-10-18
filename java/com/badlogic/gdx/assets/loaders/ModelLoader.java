/*     */ package com.badlogic.gdx.assets.loaders;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelTexture;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
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
/*     */ public abstract class ModelLoader<P extends ModelLoader.ModelParameters>
/*     */   extends AsynchronousAssetLoader<Model, P>
/*     */ {
/*     */   protected Array<ObjectMap.Entry<String, ModelData>> items;
/*     */   protected ModelParameters defaultParameters;
/*     */   
/*     */   public ModelLoader(FileHandleResolver resolver) {
/*  37 */     super(resolver);
/*     */ 
/*     */     
/*  40 */     this.items = new Array();
/*  41 */     this.defaultParameters = new ModelParameters();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelData loadModelData(FileHandle fileHandle) {
/*  48 */     return loadModelData(fileHandle, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Model loadModel(FileHandle fileHandle, TextureProvider textureProvider, P parameters) {
/*  53 */     ModelData data = loadModelData(fileHandle, parameters);
/*  54 */     return (data == null) ? null : new Model(data, textureProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   public Model loadModel(FileHandle fileHandle, P parameters) {
/*  59 */     return loadModel(fileHandle, (TextureProvider)new TextureProvider.FileTextureProvider(), parameters);
/*     */   }
/*     */ 
/*     */   
/*     */   public Model loadModel(FileHandle fileHandle, TextureProvider textureProvider) {
/*  64 */     return loadModel(fileHandle, textureProvider, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Model loadModel(FileHandle fileHandle) {
/*  69 */     return loadModel(fileHandle, (TextureProvider)new TextureProvider.FileTextureProvider(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, P parameters) {
/*  74 */     Array<AssetDescriptor> deps = new Array();
/*  75 */     ModelData data = loadModelData(file, parameters);
/*  76 */     if (data == null) return deps;
/*     */     
/*  78 */     ObjectMap.Entry<String, ModelData> item = new ObjectMap.Entry();
/*  79 */     item.key = fileName;
/*  80 */     item.value = data;
/*  81 */     synchronized (this.items) {
/*  82 */       this.items.add(item);
/*     */     } 
/*     */     
/*  85 */     TextureLoader.TextureParameter textureParameter = (parameters != null) ? ((ModelParameters)parameters).textureParameter : this.defaultParameters.textureParameter;
/*     */ 
/*     */ 
/*     */     
/*  89 */     for (ModelMaterial modelMaterial : data.materials) {
/*  90 */       if (modelMaterial.textures != null)
/*  91 */         for (ModelTexture modelTexture : modelMaterial.textures) {
/*  92 */           deps.add(new AssetDescriptor(modelTexture.fileName, Texture.class, textureParameter));
/*     */         } 
/*     */     } 
/*  95 */     return deps;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadAsync(AssetManager manager, String fileName, FileHandle file, P parameters) {}
/*     */ 
/*     */   
/*     */   public Model loadSync(AssetManager manager, String fileName, FileHandle file, P parameters) {
/* 104 */     ModelData data = null;
/* 105 */     synchronized (this.items) {
/* 106 */       for (int i = 0; i < this.items.size; i++) {
/* 107 */         if (((String)((ObjectMap.Entry)this.items.get(i)).key).equals(fileName)) {
/* 108 */           data = (ModelData)((ObjectMap.Entry)this.items.get(i)).value;
/* 109 */           this.items.removeIndex(i);
/*     */         } 
/*     */       } 
/*     */     } 
/* 113 */     if (data == null) return null; 
/* 114 */     Model result = new Model(data, (TextureProvider)new TextureProvider.AssetTextureProvider(manager));
/*     */ 
/*     */     
/* 117 */     Iterator<Disposable> disposables = result.getManagedDisposables().iterator();
/* 118 */     while (disposables.hasNext()) {
/* 119 */       Disposable disposable = disposables.next();
/* 120 */       if (disposable instanceof Texture) {
/* 121 */         disposables.remove();
/*     */       }
/*     */     } 
/* 124 */     data = null;
/* 125 */     return result;
/*     */   }
/*     */   
/*     */   public abstract ModelData loadModelData(FileHandle paramFileHandle, P paramP);
/*     */   
/*     */   public static class ModelParameters extends AssetLoaderParameters<Model> {
/*     */     public ModelParameters() {
/* 132 */       this.textureParameter = new TextureLoader.TextureParameter();
/* 133 */       this.textureParameter.minFilter = this.textureParameter.magFilter = Texture.TextureFilter.Linear;
/* 134 */       this.textureParameter.wrapU = this.textureParameter.wrapV = Texture.TextureWrap.Repeat;
/*     */     }
/*     */     
/*     */     public TextureLoader.TextureParameter textureParameter;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\loaders\ModelLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */