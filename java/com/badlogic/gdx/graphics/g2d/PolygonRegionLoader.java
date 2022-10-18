/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
/*     */ import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.math.EarClippingTriangulator;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedReader;
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
/*     */ public class PolygonRegionLoader
/*     */   extends SynchronousAssetLoader<PolygonRegion, PolygonRegionLoader.PolygonRegionParameters>
/*     */ {
/*     */   public static class PolygonRegionParameters
/*     */     extends AssetLoaderParameters<PolygonRegion>
/*     */   {
/*  44 */     public String texturePrefix = "i ";
/*     */ 
/*     */ 
/*     */     
/*  48 */     public int readerBuffer = 1024;
/*     */ 
/*     */     
/*  51 */     public String[] textureExtensions = new String[] { "png", "PNG", "jpeg", "JPEG", "jpg", "JPG", "cim", "CIM", "etc1", "ETC1", "ktx", "KTX", "zktx", "ZKTX" };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  56 */   private PolygonRegionParameters defaultParameters = new PolygonRegionParameters();
/*     */   
/*  58 */   private EarClippingTriangulator triangulator = new EarClippingTriangulator();
/*     */   
/*     */   public PolygonRegionLoader() {
/*  61 */     this((FileHandleResolver)new InternalFileHandleResolver());
/*     */   }
/*     */   
/*     */   public PolygonRegionLoader(FileHandleResolver resolver) {
/*  65 */     super(resolver);
/*     */   }
/*     */ 
/*     */   
/*     */   public PolygonRegion load(AssetManager manager, String fileName, FileHandle file, PolygonRegionParameters parameter) {
/*  70 */     Texture texture = (Texture)manager.get((String)manager.getDependencies(fileName).first());
/*  71 */     return load(new TextureRegion(texture), file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, PolygonRegionParameters params) {
/*  80 */     if (params == null) params = this.defaultParameters; 
/*  81 */     String image = null;
/*     */     try {
/*  83 */       BufferedReader reader = file.reader(params.readerBuffer);
/*  84 */       for (String line = reader.readLine(); line != null; line = reader.readLine()) {
/*  85 */         if (line.startsWith(params.texturePrefix)) {
/*  86 */           image = line.substring(params.texturePrefix.length()); break;
/*     */         } 
/*     */       } 
/*  89 */       reader.close();
/*  90 */     } catch (IOException e) {
/*  91 */       throw new GdxRuntimeException("Error reading " + fileName, e);
/*     */     } 
/*     */     
/*  94 */     if (image == null && params.textureExtensions != null) for (String extension : params.textureExtensions) {
/*  95 */         FileHandle sibling = file.sibling(file.nameWithoutExtension().concat("." + extension));
/*  96 */         if (sibling.exists()) image = sibling.name();
/*     */       
/*     */       }  
/*  99 */     if (image != null) {
/* 100 */       Array<AssetDescriptor> deps = new Array(1);
/* 101 */       deps.add(new AssetDescriptor(file.sibling(image), Texture.class));
/* 102 */       return deps;
/*     */     } 
/*     */     
/* 105 */     return null;
/*     */   }
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
/*     */   public PolygonRegion load(TextureRegion textureRegion, FileHandle file) {
/* 118 */     BufferedReader reader = file.reader(256);
/*     */     
/*     */     try { while (true) {
/* 121 */         String line = reader.readLine();
/* 122 */         if (line == null)
/* 123 */           break;  if (line.startsWith("s")) {
/*     */           
/* 125 */           String[] polygonStrings = line.substring(1).trim().split(",");
/* 126 */           float[] vertices = new float[polygonStrings.length];
/* 127 */           for (int i = 0, n = vertices.length; i < n; i++) {
/* 128 */             vertices[i] = Float.parseFloat(polygonStrings[i]);
/*     */           }
/* 130 */           return new PolygonRegion(textureRegion, vertices, this.triangulator.computeTriangles(vertices).toArray());
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 136 */       StreamUtils.closeQuietly(reader); } catch (IOException ex) { throw new GdxRuntimeException("Error reading polygon shape file: " + file, ex); } finally { StreamUtils.closeQuietly(reader); }
/*     */   
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\PolygonRegionLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */