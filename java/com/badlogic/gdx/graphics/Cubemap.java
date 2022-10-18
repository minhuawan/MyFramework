/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.assets.loaders.CubemapLoader;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.glutils.FacedCubemapData;
/*     */ import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class Cubemap
/*     */   extends GLTexture
/*     */ {
/*     */   private static AssetManager assetManager;
/*  42 */   static final Map<Application, Array<Cubemap>> managedCubemaps = new HashMap<Application, Array<Cubemap>>();
/*     */   protected CubemapData data;
/*     */   
/*     */   public enum CubemapSide
/*     */   {
/*  47 */     PositiveX(0, 34069, 0.0F, -1.0F, 0.0F, 1.0F, 0.0F, 0.0F),
/*     */     
/*  49 */     NegativeX(1, 34070, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F),
/*     */     
/*  51 */     PositiveY(2, 34071, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F),
/*     */     
/*  53 */     NegativeY(3, 34072, 0.0F, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F),
/*     */     
/*  55 */     PositiveZ(4, 34073, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.0F),
/*     */     
/*  57 */     NegativeZ(5, 34074, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0F);
/*     */     
/*     */     public final int index;
/*     */     
/*     */     public final int glEnum;
/*     */     
/*     */     public final Vector3 up;
/*     */     
/*     */     public final Vector3 direction;
/*     */ 
/*     */     
/*     */     CubemapSide(int index, int glEnum, float upX, float upY, float upZ, float directionX, float directionY, float directionZ) {
/*  69 */       this.index = index;
/*  70 */       this.glEnum = glEnum;
/*  71 */       this.up = new Vector3(upX, upY, upZ);
/*  72 */       this.direction = new Vector3(directionX, directionY, directionZ);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getGLEnum() {
/*  77 */       return this.glEnum;
/*     */     }
/*     */ 
/*     */     
/*     */     public Vector3 getUp(Vector3 out) {
/*  82 */       return out.set(this.up);
/*     */     }
/*     */ 
/*     */     
/*     */     public Vector3 getDirection(Vector3 out) {
/*  87 */       return out.set(this.direction);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cubemap(CubemapData data) {
/*  95 */     super(34067);
/*  96 */     this.data = data;
/*  97 */     load(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Cubemap(FileHandle positiveX, FileHandle negativeX, FileHandle positiveY, FileHandle negativeY, FileHandle positiveZ, FileHandle negativeZ) {
/* 103 */     this(positiveX, negativeX, positiveY, negativeY, positiveZ, negativeZ, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Cubemap(FileHandle positiveX, FileHandle negativeX, FileHandle positiveY, FileHandle negativeY, FileHandle positiveZ, FileHandle negativeZ, boolean useMipMaps) {
/* 109 */     this(TextureData.Factory.loadFromFile(positiveX, useMipMaps), TextureData.Factory.loadFromFile(negativeX, useMipMaps), 
/* 110 */         TextureData.Factory.loadFromFile(positiveY, useMipMaps), TextureData.Factory.loadFromFile(negativeY, useMipMaps), 
/* 111 */         TextureData.Factory.loadFromFile(positiveZ, useMipMaps), TextureData.Factory.loadFromFile(negativeZ, useMipMaps));
/*     */   }
/*     */ 
/*     */   
/*     */   public Cubemap(Pixmap positiveX, Pixmap negativeX, Pixmap positiveY, Pixmap negativeY, Pixmap positiveZ, Pixmap negativeZ) {
/* 116 */     this(positiveX, negativeX, positiveY, negativeY, positiveZ, negativeZ, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Cubemap(Pixmap positiveX, Pixmap negativeX, Pixmap positiveY, Pixmap negativeY, Pixmap positiveZ, Pixmap negativeZ, boolean useMipMaps) {
/* 122 */     this((positiveX == null) ? null : (TextureData)new PixmapTextureData(positiveX, null, useMipMaps, false), (negativeX == null) ? null : (TextureData)new PixmapTextureData(negativeX, null, useMipMaps, false), (positiveY == null) ? null : (TextureData)new PixmapTextureData(positiveY, null, useMipMaps, false), (negativeY == null) ? null : (TextureData)new PixmapTextureData(negativeY, null, useMipMaps, false), (positiveZ == null) ? null : (TextureData)new PixmapTextureData(positiveZ, null, useMipMaps, false), (negativeZ == null) ? null : (TextureData)new PixmapTextureData(negativeZ, null, useMipMaps, false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cubemap(int width, int height, int depth, Pixmap.Format format) {
/* 131 */     this((TextureData)new PixmapTextureData(new Pixmap(depth, height, format), null, false, true), (TextureData)new PixmapTextureData(new Pixmap(depth, height, format), null, false, true), (TextureData)new PixmapTextureData(new Pixmap(width, depth, format), null, false, true), (TextureData)new PixmapTextureData(new Pixmap(width, depth, format), null, false, true), (TextureData)new PixmapTextureData(new Pixmap(width, height, format), null, false, true), (TextureData)new PixmapTextureData(new Pixmap(width, height, format), null, false, true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cubemap(TextureData positiveX, TextureData negativeX, TextureData positiveY, TextureData negativeY, TextureData positiveZ, TextureData negativeZ) {
/* 140 */     super(34067);
/* 141 */     this.minFilter = Texture.TextureFilter.Nearest;
/* 142 */     this.magFilter = Texture.TextureFilter.Nearest;
/* 143 */     this.uWrap = Texture.TextureWrap.ClampToEdge;
/* 144 */     this.vWrap = Texture.TextureWrap.ClampToEdge;
/* 145 */     this.data = (CubemapData)new FacedCubemapData(positiveX, negativeX, positiveY, negativeY, positiveZ, negativeZ);
/* 146 */     load(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(CubemapData data) {
/* 151 */     if (!data.isPrepared()) data.prepare(); 
/* 152 */     bind();
/* 153 */     unsafeSetFilter(this.minFilter, this.magFilter, true);
/* 154 */     unsafeSetWrap(this.uWrap, this.vWrap, true);
/* 155 */     data.consumeCubemapData();
/* 156 */     Gdx.gl.glBindTexture(this.glTarget, 0);
/*     */   }
/*     */   
/*     */   public CubemapData getCubemapData() {
/* 160 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 165 */     return this.data.isManaged();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void reload() {
/* 170 */     if (!isManaged()) throw new GdxRuntimeException("Tried to reload an unmanaged Cubemap"); 
/* 171 */     this.glHandle = Gdx.gl.glGenTexture();
/* 172 */     load(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 177 */     return this.data.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 182 */     return this.data.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/* 187 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 197 */     if (this.glHandle == 0)
/* 198 */       return;  delete();
/* 199 */     if (this.data.isManaged() && managedCubemaps.get(Gdx.app) != null) ((Array)managedCubemaps.get(Gdx.app)).removeValue(this, true); 
/*     */   }
/*     */   
/*     */   private static void addManagedCubemap(Application app, Cubemap cubemap) {
/* 203 */     Array<Cubemap> managedCubemapArray = managedCubemaps.get(app);
/* 204 */     if (managedCubemapArray == null) managedCubemapArray = new Array(); 
/* 205 */     managedCubemapArray.add(cubemap);
/* 206 */     managedCubemaps.put(app, managedCubemapArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clearAllCubemaps(Application app) {
/* 211 */     managedCubemaps.remove(app);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invalidateAllCubemaps(Application app) {
/* 216 */     Array<Cubemap> managedCubemapArray = managedCubemaps.get(app);
/* 217 */     if (managedCubemapArray == null)
/*     */       return; 
/* 219 */     if (assetManager == null) {
/* 220 */       for (int i = 0; i < managedCubemapArray.size; i++) {
/* 221 */         Cubemap cubemap = (Cubemap)managedCubemapArray.get(i);
/* 222 */         cubemap.reload();
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 228 */       assetManager.finishLoading();
/*     */ 
/*     */ 
/*     */       
/* 232 */       Array<Cubemap> cubemaps = new Array(managedCubemapArray);
/* 233 */       for (Cubemap cubemap : cubemaps) {
/* 234 */         String fileName = assetManager.getAssetFileName(cubemap);
/* 235 */         if (fileName == null) {
/* 236 */           cubemap.reload();
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 242 */         final int refCount = assetManager.getReferenceCount(fileName);
/* 243 */         assetManager.setReferenceCount(fileName, 0);
/* 244 */         cubemap.glHandle = 0;
/*     */ 
/*     */ 
/*     */         
/* 248 */         CubemapLoader.CubemapParameter params = new CubemapLoader.CubemapParameter();
/* 249 */         params.cubemapData = cubemap.getCubemapData();
/* 250 */         params.minFilter = cubemap.getMinFilter();
/* 251 */         params.magFilter = cubemap.getMagFilter();
/* 252 */         params.wrapU = cubemap.getUWrap();
/* 253 */         params.wrapV = cubemap.getVWrap();
/* 254 */         params.cubemap = cubemap;
/* 255 */         params.loadedCallback = new AssetLoaderParameters.LoadedCallback()
/*     */           {
/*     */             public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
/* 258 */               assetManager.setReferenceCount(fileName, refCount);
/*     */             }
/*     */           };
/*     */ 
/*     */         
/* 263 */         assetManager.unload(fileName);
/* 264 */         cubemap.glHandle = Gdx.gl.glGenTexture();
/* 265 */         assetManager.load(fileName, Cubemap.class, (AssetLoaderParameters)params);
/*     */       } 
/*     */       
/* 268 */       managedCubemapArray.clear();
/* 269 */       managedCubemapArray.addAll(cubemaps);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setAssetManager(AssetManager manager) {
/* 278 */     assetManager = manager;
/*     */   }
/*     */   
/*     */   public static String getManagedStatus() {
/* 282 */     StringBuilder builder = new StringBuilder();
/* 283 */     builder.append("Managed cubemap/app: { ");
/* 284 */     for (Application app : managedCubemaps.keySet()) {
/* 285 */       builder.append(((Array)managedCubemaps.get(app)).size);
/* 286 */       builder.append(" ");
/*     */     } 
/* 288 */     builder.append("}");
/* 289 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNumManagedCubemaps() {
/* 294 */     return ((Array)managedCubemaps.get(Gdx.app)).size;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\Cubemap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */