/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.assets.loaders.TextureLoader;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Texture
/*     */   extends GLTexture
/*     */ {
/*     */   private static AssetManager assetManager;
/*     */   TextureData data;
/*  49 */   static final Map<Application, Array<Texture>> managedTextures = new HashMap<Application, Array<Texture>>();
/*     */   
/*     */   public enum TextureFilter {
/*  52 */     Nearest(9728), Linear(9729), MipMap(9987), MipMapNearestNearest(9984),
/*  53 */     MipMapLinearNearest(9985), MipMapNearestLinear(9986),
/*  54 */     MipMapLinearLinear(9987);
/*     */     
/*     */     final int glEnum;
/*     */     
/*     */     TextureFilter(int glEnum) {
/*  59 */       this.glEnum = glEnum;
/*     */     }
/*     */     
/*     */     public boolean isMipMap() {
/*  63 */       return (this.glEnum != 9728 && this.glEnum != 9729);
/*     */     }
/*     */     
/*     */     public int getGLEnum() {
/*  67 */       return this.glEnum;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum TextureWrap {
/*  72 */     MirroredRepeat(33648), ClampToEdge(33071), Repeat(10497);
/*     */     
/*     */     final int glEnum;
/*     */     
/*     */     TextureWrap(int glEnum) {
/*  77 */       this.glEnum = glEnum;
/*     */     }
/*     */     
/*     */     public int getGLEnum() {
/*  81 */       return this.glEnum;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Texture(String internalPath) {
/*  88 */     this(Gdx.files.internal(internalPath));
/*     */   }
/*     */   
/*     */   public Texture(FileHandle file) {
/*  92 */     this(file, (Pixmap.Format)null, false);
/*     */   }
/*     */   
/*     */   public Texture(FileHandle file, boolean useMipMaps) {
/*  96 */     this(file, (Pixmap.Format)null, useMipMaps);
/*     */   }
/*     */   
/*     */   public Texture(FileHandle file, Pixmap.Format format, boolean useMipMaps) {
/* 100 */     this(TextureData.Factory.loadFromFile(file, format, useMipMaps));
/*     */   }
/*     */   
/*     */   public Texture(Pixmap pixmap) {
/* 104 */     this((TextureData)new PixmapTextureData(pixmap, null, false, false));
/*     */   }
/*     */   
/*     */   public Texture(Pixmap pixmap, boolean useMipMaps) {
/* 108 */     this((TextureData)new PixmapTextureData(pixmap, null, useMipMaps, false));
/*     */   }
/*     */   
/*     */   public Texture(Pixmap pixmap, Pixmap.Format format, boolean useMipMaps) {
/* 112 */     this((TextureData)new PixmapTextureData(pixmap, format, useMipMaps, false));
/*     */   }
/*     */   
/*     */   public Texture(int width, int height, Pixmap.Format format) {
/* 116 */     this((TextureData)new PixmapTextureData(new Pixmap(width, height, format), null, false, true));
/*     */   }
/*     */   
/*     */   public Texture(TextureData data) {
/* 120 */     this(3553, Gdx.gl.glGenTexture(), data);
/*     */   }
/*     */   
/*     */   protected Texture(int glTarget, int glHandle, TextureData data) {
/* 124 */     super(glTarget, glHandle);
/* 125 */     load(data);
/* 126 */     if (data.isManaged()) addManagedTexture(Gdx.app, this); 
/*     */   }
/*     */   
/*     */   public void load(TextureData data) {
/* 130 */     if (this.data != null && data.isManaged() != this.data.isManaged())
/* 131 */       throw new GdxRuntimeException("New data must have the same managed status as the old data"); 
/* 132 */     this.data = data;
/*     */     
/* 134 */     if (!data.isPrepared()) data.prepare();
/*     */     
/* 136 */     bind();
/* 137 */     uploadImageData(3553, data);
/*     */     
/* 139 */     setFilter(this.minFilter, this.magFilter);
/* 140 */     setWrap(this.uWrap, this.vWrap);
/* 141 */     Gdx.gl.glBindTexture(this.glTarget, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reload() {
/* 148 */     if (!isManaged()) throw new GdxRuntimeException("Tried to reload unmanaged Texture"); 
/* 149 */     this.glHandle = Gdx.gl.glGenTexture();
/* 150 */     load(this.data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(Pixmap pixmap, int x, int y) {
/* 160 */     if (this.data.isManaged()) throw new GdxRuntimeException("can't draw to a managed texture");
/*     */     
/* 162 */     bind();
/* 163 */     Gdx.gl.glTexSubImage2D(this.glTarget, 0, x, y, pixmap.getWidth(), pixmap.getHeight(), pixmap.getGLFormat(), pixmap.getGLType(), pixmap
/* 164 */         .getPixels());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 169 */     return this.data.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 174 */     return this.data.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/* 179 */     return 0;
/*     */   }
/*     */   
/*     */   public TextureData getTextureData() {
/* 183 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 188 */     return this.data.isManaged();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 197 */     if (this.glHandle == 0)
/* 198 */       return;  delete();
/* 199 */     if (this.data.isManaged() && managedTextures.get(Gdx.app) != null) ((Array)managedTextures.get(Gdx.app)).removeValue(this, true); 
/*     */   }
/*     */   
/*     */   private static void addManagedTexture(Application app, Texture texture) {
/* 203 */     Array<Texture> managedTextureArray = managedTextures.get(app);
/* 204 */     if (managedTextureArray == null) managedTextureArray = new Array(); 
/* 205 */     managedTextureArray.add(texture);
/* 206 */     managedTextures.put(app, managedTextureArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clearAllTextures(Application app) {
/* 211 */     managedTextures.remove(app);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invalidateAllTextures(Application app) {
/* 216 */     Array<Texture> managedTextureArray = managedTextures.get(app);
/* 217 */     if (managedTextureArray == null)
/*     */       return; 
/* 219 */     if (assetManager == null) {
/* 220 */       for (int i = 0; i < managedTextureArray.size; i++) {
/* 221 */         Texture texture = (Texture)managedTextureArray.get(i);
/* 222 */         texture.reload();
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 228 */       assetManager.finishLoading();
/*     */ 
/*     */ 
/*     */       
/* 232 */       Array<Texture> textures = new Array(managedTextureArray);
/* 233 */       for (Texture texture : textures) {
/* 234 */         String fileName = assetManager.getAssetFileName(texture);
/* 235 */         if (fileName == null) {
/* 236 */           texture.reload();
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 242 */         final int refCount = assetManager.getReferenceCount(fileName);
/* 243 */         assetManager.setReferenceCount(fileName, 0);
/* 244 */         texture.glHandle = 0;
/*     */ 
/*     */ 
/*     */         
/* 248 */         TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
/* 249 */         params.textureData = texture.getTextureData();
/* 250 */         params.minFilter = texture.getMinFilter();
/* 251 */         params.magFilter = texture.getMagFilter();
/* 252 */         params.wrapU = texture.getUWrap();
/* 253 */         params.wrapV = texture.getVWrap();
/* 254 */         params.genMipMaps = texture.data.useMipMaps();
/* 255 */         params.texture = texture;
/* 256 */         params.loadedCallback = new AssetLoaderParameters.LoadedCallback()
/*     */           {
/*     */             public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
/* 259 */               assetManager.setReferenceCount(fileName, refCount);
/*     */             }
/*     */           };
/*     */ 
/*     */         
/* 264 */         assetManager.unload(fileName);
/* 265 */         texture.glHandle = Gdx.gl.glGenTexture();
/* 266 */         assetManager.load(fileName, Texture.class, (AssetLoaderParameters)params);
/*     */       } 
/*     */       
/* 269 */       managedTextureArray.clear();
/* 270 */       managedTextureArray.addAll(textures);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setAssetManager(AssetManager manager) {
/* 279 */     assetManager = manager;
/*     */   }
/*     */   
/*     */   public static String getManagedStatus() {
/* 283 */     StringBuilder builder = new StringBuilder();
/* 284 */     builder.append("Managed textures/app: { ");
/* 285 */     for (Application app : managedTextures.keySet()) {
/* 286 */       builder.append(((Array)managedTextures.get(app)).size);
/* 287 */       builder.append(" ");
/*     */     } 
/* 289 */     builder.append("}");
/* 290 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNumManagedTextures() {
/* 295 */     return ((Array)managedTextures.get(Gdx.app)).size;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\Texture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */