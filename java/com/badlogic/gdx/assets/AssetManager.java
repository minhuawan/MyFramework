/*     */ package com.badlogic.gdx.assets;
/*     */ 
/*     */ import com.badlogic.gdx.assets.loaders.AssetLoader;
/*     */ import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
/*     */ import com.badlogic.gdx.assets.loaders.MusicLoader;
/*     */ import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
/*     */ import com.badlogic.gdx.assets.loaders.PixmapLoader;
/*     */ import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
/*     */ import com.badlogic.gdx.assets.loaders.SkinLoader;
/*     */ import com.badlogic.gdx.assets.loaders.SoundLoader;
/*     */ import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
/*     */ import com.badlogic.gdx.assets.loaders.TextureLoader;
/*     */ import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
/*     */ import com.badlogic.gdx.audio.Music;
/*     */ import com.badlogic.gdx.audio.Sound;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEffect;
/*     */ import com.badlogic.gdx.graphics.g2d.PolygonRegion;
/*     */ import com.badlogic.gdx.graphics.g2d.PolygonRegionLoader;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
/*     */ import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.scenes.scene2d.ui.Skin;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.BaseJsonReader;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.I18NBundle;
/*     */ import com.badlogic.gdx.utils.JsonReader;
/*     */ import com.badlogic.gdx.utils.Logger;
/*     */ import com.badlogic.gdx.utils.ObjectIntMap;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.ObjectSet;
/*     */ import com.badlogic.gdx.utils.TimeUtils;
/*     */ import com.badlogic.gdx.utils.UBJsonReader;
/*     */ import com.badlogic.gdx.utils.async.AsyncExecutor;
/*     */ import com.badlogic.gdx.utils.async.ThreadUtils;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*     */ import java.util.Stack;
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
/*     */ public class AssetManager
/*     */   implements Disposable
/*     */ {
/*  67 */   final ObjectMap<Class, ObjectMap<String, RefCountedContainer>> assets = new ObjectMap();
/*  68 */   final ObjectMap<String, Class> assetTypes = new ObjectMap();
/*  69 */   final ObjectMap<String, Array<String>> assetDependencies = new ObjectMap();
/*  70 */   final ObjectSet<String> injected = new ObjectSet();
/*     */   
/*  72 */   final ObjectMap<Class, ObjectMap<String, AssetLoader>> loaders = new ObjectMap();
/*  73 */   final Array<AssetDescriptor> loadQueue = new Array();
/*     */   
/*     */   final AsyncExecutor executor;
/*  76 */   final Stack<AssetLoadingTask> tasks = new Stack<AssetLoadingTask>();
/*  77 */   AssetErrorListener listener = null;
/*  78 */   int loaded = 0;
/*  79 */   int toLoad = 0;
/*  80 */   int peakTasks = 0;
/*     */   
/*     */   final FileHandleResolver resolver;
/*     */   
/*  84 */   Logger log = new Logger("AssetManager", 0);
/*     */ 
/*     */   
/*     */   public AssetManager() {
/*  88 */     this((FileHandleResolver)new InternalFileHandleResolver());
/*     */   }
/*     */ 
/*     */   
/*     */   public AssetManager(FileHandleResolver resolver) {
/*  93 */     this(resolver, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AssetManager(FileHandleResolver resolver, boolean defaultLoaders) {
/* 100 */     this.resolver = resolver;
/* 101 */     if (defaultLoaders) {
/* 102 */       setLoader(BitmapFont.class, (AssetLoader<BitmapFont, AssetLoaderParameters<BitmapFont>>)new BitmapFontLoader(resolver));
/* 103 */       setLoader(Music.class, (AssetLoader<Music, AssetLoaderParameters<Music>>)new MusicLoader(resolver));
/* 104 */       setLoader(Pixmap.class, (AssetLoader<Pixmap, AssetLoaderParameters<Pixmap>>)new PixmapLoader(resolver));
/* 105 */       setLoader(Sound.class, (AssetLoader<Sound, AssetLoaderParameters<Sound>>)new SoundLoader(resolver));
/* 106 */       setLoader(TextureAtlas.class, (AssetLoader<TextureAtlas, AssetLoaderParameters<TextureAtlas>>)new TextureAtlasLoader(resolver));
/* 107 */       setLoader(Texture.class, (AssetLoader<Texture, AssetLoaderParameters<Texture>>)new TextureLoader(resolver));
/* 108 */       setLoader(Skin.class, (AssetLoader<Skin, AssetLoaderParameters<Skin>>)new SkinLoader(resolver));
/* 109 */       setLoader(ParticleEffect.class, (AssetLoader<ParticleEffect, AssetLoaderParameters<ParticleEffect>>)new ParticleEffectLoader(resolver));
/* 110 */       setLoader(ParticleEffect.class, (AssetLoader<ParticleEffect, AssetLoaderParameters<ParticleEffect>>)new ParticleEffectLoader(resolver));
/*     */       
/* 112 */       setLoader(PolygonRegion.class, (AssetLoader<PolygonRegion, AssetLoaderParameters<PolygonRegion>>)new PolygonRegionLoader(resolver));
/* 113 */       setLoader(I18NBundle.class, (AssetLoader<I18NBundle, AssetLoaderParameters<I18NBundle>>)new I18NBundleLoader(resolver));
/* 114 */       setLoader(Model.class, ".g3dj", (AssetLoader<Model, AssetLoaderParameters<Model>>)new G3dModelLoader((BaseJsonReader)new JsonReader(), resolver));
/* 115 */       setLoader(Model.class, ".g3db", (AssetLoader<Model, AssetLoaderParameters<Model>>)new G3dModelLoader((BaseJsonReader)new UBJsonReader(), resolver));
/* 116 */       setLoader(Model.class, ".obj", (AssetLoader<Model, AssetLoaderParameters<Model>>)new ObjLoader(resolver));
/* 117 */       setLoader(ShaderProgram.class, (AssetLoader<ShaderProgram, AssetLoaderParameters<ShaderProgram>>)new ShaderProgramLoader(resolver));
/*     */     } 
/* 119 */     this.executor = new AsyncExecutor(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileHandleResolver getFileHandleResolver() {
/* 126 */     return this.resolver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> T get(String fileName) {
/* 132 */     Class<T> type = (Class<T>)this.assetTypes.get(fileName);
/* 133 */     if (type == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 134 */     ObjectMap<String, RefCountedContainer> assetsByType = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 135 */     if (assetsByType == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 136 */     RefCountedContainer assetContainer = (RefCountedContainer)assetsByType.get(fileName);
/* 137 */     if (assetContainer == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 138 */     T asset = assetContainer.getObject(type);
/* 139 */     if (asset == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 140 */     return asset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> T get(String fileName, Class<T> type) {
/* 147 */     ObjectMap<String, RefCountedContainer> assetsByType = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 148 */     if (assetsByType == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 149 */     RefCountedContainer assetContainer = (RefCountedContainer)assetsByType.get(fileName);
/* 150 */     if (assetContainer == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 151 */     T asset = assetContainer.getObject(type);
/* 152 */     if (asset == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 153 */     return asset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> Array<T> getAll(Class<T> type, Array<T> out) {
/* 159 */     ObjectMap<String, RefCountedContainer> assetsByType = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 160 */     if (assetsByType != null) {
/* 161 */       for (ObjectMap.Entries<ObjectMap.Entry<String, RefCountedContainer>> entries = assetsByType.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<String, RefCountedContainer> asset = entries.next();
/* 162 */         out.add(((RefCountedContainer)asset.value).getObject(type)); }
/*     */     
/*     */     }
/* 165 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> T get(AssetDescriptor<T> assetDescriptor) {
/* 171 */     return get(assetDescriptor.fileName, assetDescriptor.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void unload(String fileName) {
/* 179 */     if (this.tasks.size() > 0) {
/* 180 */       AssetLoadingTask currAsset = this.tasks.firstElement();
/* 181 */       if (currAsset.assetDesc.fileName.equals(fileName)) {
/* 182 */         currAsset.cancel = true;
/* 183 */         this.log.debug("Unload (from tasks): " + fileName);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 189 */     int foundIndex = -1;
/* 190 */     for (int i = 0; i < this.loadQueue.size; i++) {
/* 191 */       if (((AssetDescriptor)this.loadQueue.get(i)).fileName.equals(fileName)) {
/* 192 */         foundIndex = i;
/*     */         break;
/*     */       } 
/*     */     } 
/* 196 */     if (foundIndex != -1) {
/* 197 */       this.toLoad--;
/* 198 */       this.loadQueue.removeIndex(foundIndex);
/* 199 */       this.log.debug("Unload (from queue): " + fileName);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 204 */     Class type = (Class)this.assetTypes.get(fileName);
/* 205 */     if (type == null) throw new GdxRuntimeException("Asset not loaded: " + fileName);
/*     */     
/* 207 */     RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(fileName);
/*     */ 
/*     */     
/* 210 */     assetRef.decRefCount();
/* 211 */     if (assetRef.getRefCount() <= 0) {
/* 212 */       this.log.debug("Unload (dispose): " + fileName);
/*     */ 
/*     */       
/* 215 */       if (assetRef.getObject((Class)Object.class) instanceof Disposable) ((Disposable)assetRef.<Object>getObject(Object.class)).dispose();
/*     */ 
/*     */       
/* 218 */       this.assetTypes.remove(fileName);
/* 219 */       ((ObjectMap)this.assets.get(type)).remove(fileName);
/*     */     } else {
/* 221 */       this.log.debug("Unload (decrement): " + fileName);
/*     */     } 
/*     */ 
/*     */     
/* 225 */     Array<String> dependencies = (Array<String>)this.assetDependencies.get(fileName);
/* 226 */     if (dependencies != null) {
/* 227 */       for (String dependency : dependencies) {
/* 228 */         if (isLoaded(dependency)) unload(dependency);
/*     */       
/*     */       } 
/*     */     }
/* 232 */     if (assetRef.getRefCount() <= 0) {
/* 233 */       this.assetDependencies.remove(fileName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> boolean containsAsset(T asset) {
/* 240 */     ObjectMap<String, RefCountedContainer> typedAssets = (ObjectMap<String, RefCountedContainer>)this.assets.get(asset.getClass());
/* 241 */     if (typedAssets == null) return false; 
/* 242 */     for (ObjectMap.Keys<String> keys = typedAssets.keys().iterator(); keys.hasNext(); ) { String fileName = keys.next();
/* 243 */       T otherAsset = ((RefCountedContainer)typedAssets.get(fileName)).getObject((Class)Object.class);
/* 244 */       if (otherAsset == asset || asset.equals(otherAsset)) return true;  }
/*     */     
/* 246 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> String getAssetFileName(T asset) {
/* 252 */     for (ObjectMap.Keys<Class<?>> keys = this.assets.keys().iterator(); keys.hasNext(); ) { Class assetType = keys.next();
/* 253 */       ObjectMap<String, RefCountedContainer> typedAssets = (ObjectMap<String, RefCountedContainer>)this.assets.get(assetType);
/* 254 */       for (ObjectMap.Keys<String> keys1 = typedAssets.keys().iterator(); keys1.hasNext(); ) { String fileName = keys1.next();
/* 255 */         T otherAsset = ((RefCountedContainer)typedAssets.get(fileName)).getObject((Class)Object.class);
/* 256 */         if (otherAsset == asset || asset.equals(otherAsset)) return fileName;  }
/*     */        }
/*     */     
/* 259 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isLoaded(String fileName) {
/* 265 */     if (fileName == null) return false; 
/* 266 */     return this.assetTypes.containsKey(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isLoaded(String fileName, Class<?> type) {
/* 272 */     ObjectMap<String, RefCountedContainer> assetsByType = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 273 */     if (assetsByType == null) return false; 
/* 274 */     RefCountedContainer assetContainer = (RefCountedContainer)assetsByType.get(fileName);
/* 275 */     if (assetContainer == null) return false; 
/* 276 */     return (assetContainer.getObject(type) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> AssetLoader getLoader(Class<T> type) {
/* 283 */     return getLoader(type, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> AssetLoader getLoader(Class<T> type, String fileName) {
/* 292 */     ObjectMap<String, AssetLoader> loaders = (ObjectMap<String, AssetLoader>)this.loaders.get(type);
/* 293 */     if (loaders == null || loaders.size < 1) return null; 
/* 294 */     if (fileName == null) return (AssetLoader)loaders.get(""); 
/* 295 */     AssetLoader result = null;
/* 296 */     int l = -1;
/* 297 */     for (ObjectMap.Entries<ObjectMap.Entry<String, AssetLoader>> entries = loaders.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<String, AssetLoader> entry = entries.next();
/* 298 */       if (((String)entry.key).length() > l && fileName.endsWith((String)entry.key)) {
/* 299 */         result = (AssetLoader)entry.value;
/* 300 */         l = ((String)entry.key).length();
/*     */       }  }
/*     */     
/* 303 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> void load(String fileName, Class<T> type) {
/* 310 */     load(fileName, type, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T> void load(String fileName, Class<T> type, AssetLoaderParameters<T> parameter) {
/* 318 */     AssetLoader loader = getLoader(type, fileName);
/* 319 */     if (loader == null) throw new GdxRuntimeException("No loader for type: " + ClassReflection.getSimpleName(type));
/*     */ 
/*     */     
/* 322 */     if (this.loadQueue.size == 0) {
/* 323 */       this.loaded = 0;
/* 324 */       this.toLoad = 0;
/* 325 */       this.peakTasks = 0;
/*     */     } 
/*     */ 
/*     */     
/*     */     int i;
/*     */     
/* 331 */     for (i = 0; i < this.loadQueue.size; i++) {
/* 332 */       AssetDescriptor desc = (AssetDescriptor)this.loadQueue.get(i);
/* 333 */       if (desc.fileName.equals(fileName) && !desc.type.equals(type)) {
/* 334 */         throw new GdxRuntimeException("Asset with name '" + fileName + "' already in preload queue, but has different type (expected: " + 
/* 335 */             ClassReflection.getSimpleName(type) + ", found: " + 
/* 336 */             ClassReflection.getSimpleName(desc.type) + ")");
/*     */       }
/*     */     } 
/*     */     
/* 340 */     for (i = 0; i < this.tasks.size(); i++) {
/* 341 */       AssetDescriptor desc = ((AssetLoadingTask)this.tasks.get(i)).assetDesc;
/* 342 */       if (desc.fileName.equals(fileName) && !desc.type.equals(type)) {
/* 343 */         throw new GdxRuntimeException("Asset with name '" + fileName + "' already in task list, but has different type (expected: " + 
/* 344 */             ClassReflection.getSimpleName(type) + ", found: " + 
/* 345 */             ClassReflection.getSimpleName(desc.type) + ")");
/*     */       }
/*     */     } 
/*     */     
/* 349 */     Class otherType = (Class)this.assetTypes.get(fileName);
/* 350 */     if (otherType != null && !otherType.equals(type)) {
/* 351 */       throw new GdxRuntimeException("Asset with name '" + fileName + "' already loaded, but has different type (expected: " + 
/* 352 */           ClassReflection.getSimpleName(type) + ", found: " + ClassReflection.getSimpleName(otherType) + ")");
/*     */     }
/* 354 */     this.toLoad++;
/* 355 */     AssetDescriptor<T> assetDesc = new AssetDescriptor<T>(fileName, type, parameter);
/* 356 */     this.loadQueue.add(assetDesc);
/* 357 */     this.log.debug("Queued: " + assetDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void load(AssetDescriptor desc) {
/* 363 */     load(desc.fileName, desc.type, desc.params);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean update() {
/*     */     try {
/* 370 */       if (this.tasks.size() == 0) {
/*     */         
/* 372 */         while (this.loadQueue.size != 0 && this.tasks.size() == 0) {
/* 373 */           nextTask();
/*     */         }
/*     */         
/* 376 */         if (this.tasks.size() == 0) return true; 
/*     */       } 
/* 378 */       return (updateTask() && this.loadQueue.size == 0 && this.tasks.size() == 0);
/* 379 */     } catch (Throwable t) {
/* 380 */       handleTaskError(t);
/* 381 */       return (this.loadQueue.size == 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean update(int millis) {
/* 390 */     long endTime = TimeUtils.millis() + millis;
/*     */     while (true) {
/* 392 */       boolean done = update();
/* 393 */       if (done || TimeUtils.millis() > endTime) return done; 
/* 394 */       ThreadUtils.yield();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void finishLoading() {
/* 400 */     this.log.debug("Waiting for loading to complete...");
/* 401 */     while (!update())
/* 402 */       ThreadUtils.yield(); 
/* 403 */     this.log.debug("Loading complete.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void finishLoadingAsset(String fileName) {
/* 409 */     this.log.debug("Waiting for asset to be loaded: " + fileName);
/* 410 */     while (!isLoaded(fileName)) {
/* 411 */       update();
/* 412 */       ThreadUtils.yield();
/*     */     } 
/* 414 */     this.log.debug("Asset loaded: " + fileName);
/*     */   }
/*     */   
/*     */   synchronized void injectDependencies(String parentAssetFilename, Array<AssetDescriptor> dependendAssetDescs) {
/* 418 */     ObjectSet<String> injected = this.injected;
/* 419 */     for (AssetDescriptor desc : dependendAssetDescs) {
/* 420 */       if (injected.contains(desc.fileName))
/* 421 */         continue;  injected.add(desc.fileName);
/* 422 */       injectDependency(parentAssetFilename, desc);
/*     */     } 
/* 424 */     injected.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void injectDependency(String parentAssetFilename, AssetDescriptor dependendAssetDesc) {
/* 429 */     Array<String> dependencies = (Array<String>)this.assetDependencies.get(parentAssetFilename);
/* 430 */     if (dependencies == null) {
/* 431 */       dependencies = new Array();
/* 432 */       this.assetDependencies.put(parentAssetFilename, dependencies);
/*     */     } 
/* 434 */     dependencies.add(dependendAssetDesc.fileName);
/*     */ 
/*     */     
/* 437 */     if (isLoaded(dependendAssetDesc.fileName)) {
/* 438 */       this.log.debug("Dependency already loaded: " + dependendAssetDesc);
/* 439 */       Class type = (Class)this.assetTypes.get(dependendAssetDesc.fileName);
/* 440 */       RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(dependendAssetDesc.fileName);
/* 441 */       assetRef.incRefCount();
/* 442 */       incrementRefCountedDependencies(dependendAssetDesc.fileName);
/*     */     }
/*     */     else {
/*     */       
/* 446 */       this.log.info("Loading dependency: " + dependendAssetDesc);
/* 447 */       addTask(dependendAssetDesc);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void nextTask() {
/* 454 */     AssetDescriptor assetDesc = (AssetDescriptor)this.loadQueue.removeIndex(0);
/*     */ 
/*     */     
/* 457 */     if (isLoaded(assetDesc.fileName)) {
/* 458 */       this.log.debug("Already loaded: " + assetDesc);
/* 459 */       Class type = (Class)this.assetTypes.get(assetDesc.fileName);
/* 460 */       RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(assetDesc.fileName);
/* 461 */       assetRef.incRefCount();
/* 462 */       incrementRefCountedDependencies(assetDesc.fileName);
/* 463 */       if (assetDesc.params != null && assetDesc.params.loadedCallback != null) {
/* 464 */         assetDesc.params.loadedCallback.finishedLoading(this, assetDesc.fileName, assetDesc.type);
/*     */       }
/* 466 */       this.loaded++;
/*     */     } else {
/*     */       
/* 469 */       this.log.info("Loading: " + assetDesc);
/* 470 */       addTask(assetDesc);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addTask(AssetDescriptor assetDesc) {
/* 477 */     AssetLoader loader = getLoader(assetDesc.type, assetDesc.fileName);
/* 478 */     if (loader == null) throw new GdxRuntimeException("No loader for type: " + ClassReflection.getSimpleName(assetDesc.type)); 
/* 479 */     this.tasks.push(new AssetLoadingTask(this, assetDesc, loader, this.executor));
/* 480 */     this.peakTasks++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> void addAsset(String fileName, Class<T> type, T asset) {
/* 486 */     this.assetTypes.put(fileName, type);
/*     */ 
/*     */     
/* 489 */     ObjectMap<String, RefCountedContainer> typeToAssets = (ObjectMap<String, RefCountedContainer>)this.assets.get(type);
/* 490 */     if (typeToAssets == null) {
/* 491 */       typeToAssets = new ObjectMap();
/* 492 */       this.assets.put(type, typeToAssets);
/*     */     } 
/* 494 */     typeToAssets.put(fileName, new RefCountedContainer(asset));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean updateTask() {
/* 500 */     AssetLoadingTask task = this.tasks.peek();
/*     */     
/* 502 */     boolean complete = true;
/*     */     try {
/* 504 */       complete = (task.cancel || task.update());
/* 505 */     } catch (RuntimeException ex) {
/* 506 */       task.cancel = true;
/* 507 */       taskFailed(task.assetDesc, ex);
/*     */     } 
/*     */ 
/*     */     
/* 511 */     if (complete) {
/*     */       
/* 513 */       if (this.tasks.size() == 1) {
/* 514 */         this.loaded++;
/* 515 */         this.peakTasks = 0;
/*     */       } 
/* 517 */       this.tasks.pop();
/*     */       
/* 519 */       if (task.cancel) return true;
/*     */       
/* 521 */       addAsset(task.assetDesc.fileName, task.assetDesc.type, task.getAsset());
/*     */ 
/*     */       
/* 524 */       if (task.assetDesc.params != null && task.assetDesc.params.loadedCallback != null) {
/* 525 */         task.assetDesc.params.loadedCallback.finishedLoading(this, task.assetDesc.fileName, task.assetDesc.type);
/*     */       }
/*     */       
/* 528 */       long endTime = TimeUtils.nanoTime();
/* 529 */       this.log.debug("Loaded: " + ((float)(endTime - task.startTime) / 1000000.0F) + "ms " + task.assetDesc);
/*     */       
/* 531 */       return true;
/*     */     } 
/* 533 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void taskFailed(AssetDescriptor assetDesc, RuntimeException ex) {
/* 539 */     throw ex;
/*     */   }
/*     */   
/*     */   private void incrementRefCountedDependencies(String parent) {
/* 543 */     Array<String> dependencies = (Array<String>)this.assetDependencies.get(parent);
/* 544 */     if (dependencies == null)
/*     */       return; 
/* 546 */     for (String dependency : dependencies) {
/* 547 */       Class type = (Class)this.assetTypes.get(dependency);
/* 548 */       RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(dependency);
/* 549 */       assetRef.incRefCount();
/* 550 */       incrementRefCountedDependencies(dependency);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleTaskError(Throwable t) {
/* 557 */     this.log.error("Error loading asset.", t);
/*     */     
/* 559 */     if (this.tasks.isEmpty()) throw new GdxRuntimeException(t);
/*     */ 
/*     */     
/* 562 */     AssetLoadingTask task = this.tasks.pop();
/* 563 */     AssetDescriptor assetDesc = task.assetDesc;
/*     */ 
/*     */     
/* 566 */     if (task.dependenciesLoaded && task.dependencies != null) {
/* 567 */       for (AssetDescriptor desc : task.dependencies) {
/* 568 */         unload(desc.fileName);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 573 */     this.tasks.clear();
/*     */ 
/*     */     
/* 576 */     if (this.listener != null) {
/* 577 */       this.listener.error(assetDesc, t);
/*     */     } else {
/* 579 */       throw new GdxRuntimeException(t);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> type, AssetLoader<T, P> loader) {
/* 587 */     setLoader(type, null, loader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized <T, P extends AssetLoaderParameters<T>> void setLoader(Class<T> type, String suffix, AssetLoader<T, P> loader) {
/* 596 */     if (type == null) throw new IllegalArgumentException("type cannot be null."); 
/* 597 */     if (loader == null) throw new IllegalArgumentException("loader cannot be null."); 
/* 598 */     this.log.debug("Loader set: " + ClassReflection.getSimpleName(type) + " -> " + ClassReflection.getSimpleName(loader.getClass()));
/* 599 */     ObjectMap<String, AssetLoader> loaders = (ObjectMap<String, AssetLoader>)this.loaders.get(type);
/* 600 */     if (loaders == null) this.loaders.put(type, loaders = new ObjectMap()); 
/* 601 */     loaders.put((suffix == null) ? "" : suffix, loader);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getLoadedAssets() {
/* 606 */     return this.assetTypes.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getQueuedAssets() {
/* 611 */     return this.loadQueue.size + this.tasks.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized float getProgress() {
/* 616 */     if (this.toLoad == 0) return 1.0F; 
/* 617 */     float fractionalLoaded = this.loaded;
/* 618 */     if (this.peakTasks > 0) {
/* 619 */       fractionalLoaded += (this.peakTasks - this.tasks.size()) / this.peakTasks;
/*     */     }
/* 621 */     return Math.min(1.0F, fractionalLoaded / this.toLoad);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setErrorListener(AssetErrorListener listener) {
/* 627 */     this.listener = listener;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void dispose() {
/* 633 */     this.log.debug("Disposing.");
/* 634 */     clear();
/* 635 */     this.executor.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 640 */     this.loadQueue.clear();
/* 641 */     while (!update());
/*     */ 
/*     */     
/* 644 */     ObjectIntMap<String> dependencyCount = new ObjectIntMap();
/* 645 */     while (this.assetTypes.size > 0) {
/*     */       
/* 647 */       dependencyCount.clear();
/* 648 */       Array<String> assets = this.assetTypes.keys().toArray();
/* 649 */       for (String asset : assets) {
/* 650 */         dependencyCount.put(asset, 0);
/*     */       }
/*     */       
/* 653 */       for (String asset : assets) {
/* 654 */         Array<String> dependencies = (Array<String>)this.assetDependencies.get(asset);
/* 655 */         if (dependencies == null)
/* 656 */           continue;  for (String dependency : dependencies) {
/* 657 */           int count = dependencyCount.get(dependency, 0);
/* 658 */           count++;
/* 659 */           dependencyCount.put(dependency, count);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 664 */       for (String asset : assets) {
/* 665 */         if (dependencyCount.get(asset, 0) == 0) {
/* 666 */           unload(asset);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 671 */     this.assets.clear();
/* 672 */     this.assetTypes.clear();
/* 673 */     this.assetDependencies.clear();
/* 674 */     this.loaded = 0;
/* 675 */     this.toLoad = 0;
/* 676 */     this.peakTasks = 0;
/* 677 */     this.loadQueue.clear();
/* 678 */     this.tasks.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Logger getLogger() {
/* 683 */     return this.log;
/*     */   }
/*     */   
/*     */   public void setLogger(Logger logger) {
/* 687 */     this.log = logger;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getReferenceCount(String fileName) {
/* 693 */     Class type = (Class)this.assetTypes.get(fileName);
/* 694 */     if (type == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 695 */     return ((RefCountedContainer)((ObjectMap)this.assets.get(type)).get(fileName)).getRefCount();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setReferenceCount(String fileName, int refCount) {
/* 701 */     Class type = (Class)this.assetTypes.get(fileName);
/* 702 */     if (type == null) throw new GdxRuntimeException("Asset not loaded: " + fileName); 
/* 703 */     ((RefCountedContainer)((ObjectMap)this.assets.get(type)).get(fileName)).setRefCount(refCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getDiagnostics() {
/* 708 */     StringBuffer buffer = new StringBuffer();
/* 709 */     for (ObjectMap.Keys<String> keys = this.assetTypes.keys().iterator(); keys.hasNext(); ) { String fileName = keys.next();
/* 710 */       buffer.append(fileName);
/* 711 */       buffer.append(", ");
/*     */       
/* 713 */       Class type = (Class)this.assetTypes.get(fileName);
/* 714 */       RefCountedContainer assetRef = (RefCountedContainer)((ObjectMap)this.assets.get(type)).get(fileName);
/* 715 */       Array<String> dependencies = (Array<String>)this.assetDependencies.get(fileName);
/*     */       
/* 717 */       buffer.append(ClassReflection.getSimpleName(type));
/*     */       
/* 719 */       buffer.append(", refs: ");
/* 720 */       buffer.append(assetRef.getRefCount());
/*     */       
/* 722 */       if (dependencies != null) {
/* 723 */         buffer.append(", deps: [");
/* 724 */         for (String dep : dependencies) {
/* 725 */           buffer.append(dep);
/* 726 */           buffer.append(",");
/*     */         } 
/* 728 */         buffer.append("]");
/*     */       } 
/* 730 */       buffer.append("\n"); }
/*     */     
/* 732 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Array<String> getAssetNames() {
/* 737 */     return this.assetTypes.keys().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Array<String> getDependencies(String fileName) {
/* 742 */     return (Array<String>)this.assetDependencies.get(fileName);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Class getAssetType(String fileName) {
/* 747 */     return (Class)this.assetTypes.get(fileName);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\AssetManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */