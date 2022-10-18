/*     */ package com.badlogic.gdx.assets;
/*     */ 
/*     */ import com.badlogic.gdx.assets.loaders.AssetLoader;
/*     */ import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
/*     */ import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.TimeUtils;
/*     */ import com.badlogic.gdx.utils.async.AsyncExecutor;
/*     */ import com.badlogic.gdx.utils.async.AsyncResult;
/*     */ import com.badlogic.gdx.utils.async.AsyncTask;
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
/*     */ class AssetLoadingTask
/*     */   implements AsyncTask<Void>
/*     */ {
/*     */   AssetManager manager;
/*     */   final AssetDescriptor assetDesc;
/*     */   final AssetLoader loader;
/*     */   final AsyncExecutor executor;
/*     */   final long startTime;
/*     */   volatile boolean asyncDone = false;
/*     */   volatile boolean dependenciesLoaded = false;
/*     */   volatile Array<AssetDescriptor> dependencies;
/*  44 */   volatile AsyncResult<Void> depsFuture = null;
/*  45 */   volatile AsyncResult<Void> loadFuture = null;
/*  46 */   volatile Object asset = null;
/*     */   
/*  48 */   int ticks = 0;
/*     */   volatile boolean cancel = false;
/*     */   
/*     */   public AssetLoadingTask(AssetManager manager, AssetDescriptor assetDesc, AssetLoader loader, AsyncExecutor threadPool) {
/*  52 */     this.manager = manager;
/*  53 */     this.assetDesc = assetDesc;
/*  54 */     this.loader = loader;
/*  55 */     this.executor = threadPool;
/*  56 */     this.startTime = (manager.log.getLevel() == 3) ? TimeUtils.nanoTime() : 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Void call() throws Exception {
/*  62 */     AsynchronousAssetLoader asyncLoader = (AsynchronousAssetLoader)this.loader;
/*  63 */     if (!this.dependenciesLoaded) {
/*  64 */       this.dependencies = asyncLoader.getDependencies(this.assetDesc.fileName, resolve(this.loader, this.assetDesc), this.assetDesc.params);
/*  65 */       if (this.dependencies != null) {
/*  66 */         removeDuplicates(this.dependencies);
/*  67 */         this.manager.injectDependencies(this.assetDesc.fileName, this.dependencies);
/*     */       } else {
/*     */         
/*  70 */         asyncLoader.loadAsync(this.manager, this.assetDesc.fileName, resolve(this.loader, this.assetDesc), this.assetDesc.params);
/*  71 */         this.asyncDone = true;
/*     */       } 
/*     */     } else {
/*  74 */       asyncLoader.loadAsync(this.manager, this.assetDesc.fileName, resolve(this.loader, this.assetDesc), this.assetDesc.params);
/*     */     } 
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean update() {
/*  86 */     this.ticks++;
/*  87 */     if (this.loader instanceof SynchronousAssetLoader) {
/*  88 */       handleSyncLoader();
/*     */     } else {
/*  90 */       handleAsyncLoader();
/*     */     } 
/*  92 */     return (this.asset != null);
/*     */   }
/*     */   
/*     */   private void handleSyncLoader() {
/*  96 */     SynchronousAssetLoader syncLoader = (SynchronousAssetLoader)this.loader;
/*  97 */     if (!this.dependenciesLoaded) {
/*  98 */       this.dependenciesLoaded = true;
/*  99 */       this.dependencies = syncLoader.getDependencies(this.assetDesc.fileName, resolve(this.loader, this.assetDesc), this.assetDesc.params);
/* 100 */       if (this.dependencies == null) {
/* 101 */         this.asset = syncLoader.load(this.manager, this.assetDesc.fileName, resolve(this.loader, this.assetDesc), this.assetDesc.params);
/*     */         return;
/*     */       } 
/* 104 */       removeDuplicates(this.dependencies);
/* 105 */       this.manager.injectDependencies(this.assetDesc.fileName, this.dependencies);
/*     */     } else {
/* 107 */       this.asset = syncLoader.load(this.manager, this.assetDesc.fileName, resolve(this.loader, this.assetDesc), this.assetDesc.params);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleAsyncLoader() {
/* 112 */     AsynchronousAssetLoader asyncLoader = (AsynchronousAssetLoader)this.loader;
/* 113 */     if (!this.dependenciesLoaded) {
/* 114 */       if (this.depsFuture == null) {
/* 115 */         this.depsFuture = this.executor.submit(this);
/*     */       }
/* 117 */       else if (this.depsFuture.isDone()) {
/*     */         try {
/* 119 */           this.depsFuture.get();
/* 120 */         } catch (Exception e) {
/* 121 */           throw new GdxRuntimeException("Couldn't load dependencies of asset: " + this.assetDesc.fileName, e);
/*     */         } 
/* 123 */         this.dependenciesLoaded = true;
/* 124 */         if (this.asyncDone) {
/* 125 */           this.asset = asyncLoader.loadSync(this.manager, this.assetDesc.fileName, resolve(this.loader, this.assetDesc), this.assetDesc.params);
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 130 */     else if (this.loadFuture == null && !this.asyncDone) {
/* 131 */       this.loadFuture = this.executor.submit(this);
/*     */     }
/* 133 */     else if (this.asyncDone) {
/* 134 */       this.asset = asyncLoader.loadSync(this.manager, this.assetDesc.fileName, resolve(this.loader, this.assetDesc), this.assetDesc.params);
/* 135 */     } else if (this.loadFuture.isDone()) {
/*     */       try {
/* 137 */         this.loadFuture.get();
/* 138 */       } catch (Exception e) {
/* 139 */         throw new GdxRuntimeException("Couldn't load asset: " + this.assetDesc.fileName, e);
/*     */       } 
/* 141 */       this.asset = asyncLoader.loadSync(this.manager, this.assetDesc.fileName, resolve(this.loader, this.assetDesc), this.assetDesc.params);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private FileHandle resolve(AssetLoader loader, AssetDescriptor assetDesc) {
/* 148 */     if (assetDesc.file == null) assetDesc.file = loader.resolve(assetDesc.fileName); 
/* 149 */     return assetDesc.file;
/*     */   }
/*     */   
/*     */   public Object getAsset() {
/* 153 */     return this.asset;
/*     */   }
/*     */   
/*     */   private void removeDuplicates(Array<AssetDescriptor> array) {
/* 157 */     boolean ordered = array.ordered;
/* 158 */     array.ordered = true;
/* 159 */     for (int i = 0; i < array.size; i++) {
/* 160 */       String fn = ((AssetDescriptor)array.get(i)).fileName;
/* 161 */       Class type = ((AssetDescriptor)array.get(i)).type;
/* 162 */       for (int j = array.size - 1; j > i; j--) {
/* 163 */         if (type == ((AssetDescriptor)array.get(j)).type && fn.equals(((AssetDescriptor)array.get(j)).fileName))
/* 164 */           array.removeIndex(j); 
/*     */       } 
/*     */     } 
/* 167 */     array.ordered = ordered;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\assets\AssetLoadingTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */