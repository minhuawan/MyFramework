/*     */ package com.badlogic.gdx.graphics;
/*     */ 
/*     */ import com.badlogic.gdx.Application;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.nio.Buffer;
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
/*     */ public class TextureArray
/*     */   extends GLTexture
/*     */ {
/*  36 */   static final Map<Application, Array<TextureArray>> managedTextureArrays = new HashMap<Application, Array<TextureArray>>();
/*     */   
/*     */   private TextureArrayData data;
/*     */   
/*     */   public TextureArray(String... internalPaths) {
/*  41 */     this(getInternalHandles(internalPaths));
/*     */   }
/*     */   
/*     */   public TextureArray(FileHandle... files) {
/*  45 */     this(false, files);
/*     */   }
/*     */   
/*     */   public TextureArray(boolean useMipMaps, FileHandle... files) {
/*  49 */     this(useMipMaps, Pixmap.Format.RGBA8888, files);
/*     */   }
/*     */   
/*     */   public TextureArray(boolean useMipMaps, Pixmap.Format format, FileHandle... files) {
/*  53 */     this(TextureArrayData.Factory.loadFromFiles(format, useMipMaps, files));
/*     */   }
/*     */   
/*     */   public TextureArray(TextureArrayData data) {
/*  57 */     super(35866, Gdx.gl.glGenTexture());
/*     */     
/*  59 */     if (Gdx.gl30 == null) {
/*  60 */       throw new GdxRuntimeException("TextureArray requires a device running with GLES 3.0 compatibilty");
/*     */     }
/*     */     
/*  63 */     load(data);
/*     */     
/*  65 */     if (data.isManaged()) addManagedTexture(Gdx.app, this); 
/*     */   }
/*     */   
/*     */   private static FileHandle[] getInternalHandles(String... internalPaths) {
/*  69 */     FileHandle[] handles = new FileHandle[internalPaths.length];
/*  70 */     for (int i = 0; i < internalPaths.length; i++) {
/*  71 */       handles[i] = Gdx.files.internal(internalPaths[i]);
/*     */     }
/*  73 */     return handles;
/*     */   }
/*     */   
/*     */   private void load(TextureArrayData data) {
/*  77 */     if (this.data != null && data.isManaged() != this.data.isManaged())
/*  78 */       throw new GdxRuntimeException("New data must have the same managed status as the old data"); 
/*  79 */     this.data = data;
/*     */     
/*  81 */     bind();
/*  82 */     Gdx.gl30.glTexImage3D(35866, 0, data.getInternalFormat(), data.getWidth(), data.getHeight(), data.getDepth(), 0, data.getInternalFormat(), data.getGLType(), (Buffer)null);
/*     */     
/*  84 */     if (!data.isPrepared()) data.prepare();
/*     */     
/*  86 */     data.consumeTextureArrayData();
/*     */     
/*  88 */     setFilter(this.minFilter, this.magFilter);
/*  89 */     setWrap(this.uWrap, this.vWrap);
/*  90 */     Gdx.gl.glBindTexture(this.glTarget, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/*  95 */     return this.data.getWidth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 100 */     return this.data.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/* 105 */     return this.data.getDepth();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 110 */     return this.data.isManaged();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void reload() {
/* 115 */     if (!isManaged()) throw new GdxRuntimeException("Tried to reload an unmanaged TextureArray"); 
/* 116 */     this.glHandle = Gdx.gl.glGenTexture();
/* 117 */     load(this.data);
/*     */   }
/*     */   
/*     */   private static void addManagedTexture(Application app, TextureArray texture) {
/* 121 */     Array<TextureArray> managedTextureArray = managedTextureArrays.get(app);
/* 122 */     if (managedTextureArray == null) managedTextureArray = new Array(); 
/* 123 */     managedTextureArray.add(texture);
/* 124 */     managedTextureArrays.put(app, managedTextureArray);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearAllTextureArrays(Application app) {
/* 130 */     managedTextureArrays.remove(app);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invalidateAllTextureArrays(Application app) {
/* 135 */     Array<TextureArray> managedTextureArray = managedTextureArrays.get(app);
/* 136 */     if (managedTextureArray == null)
/*     */       return; 
/* 138 */     for (int i = 0; i < managedTextureArray.size; i++) {
/* 139 */       TextureArray textureArray = (TextureArray)managedTextureArray.get(i);
/* 140 */       textureArray.reload();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getManagedStatus() {
/* 145 */     StringBuilder builder = new StringBuilder();
/* 146 */     builder.append("Managed TextureArrays/app: { ");
/* 147 */     for (Application app : managedTextureArrays.keySet()) {
/* 148 */       builder.append(((Array)managedTextureArrays.get(app)).size);
/* 149 */       builder.append(" ");
/*     */     } 
/* 151 */     builder.append("}");
/* 152 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getNumManagedTextureArrays() {
/* 157 */     return ((Array)managedTextureArrays.get(Gdx.app)).size;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\TextureArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */