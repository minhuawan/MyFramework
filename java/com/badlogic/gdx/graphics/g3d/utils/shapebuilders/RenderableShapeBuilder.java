/*     */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.RenderableProvider;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.FlushablePool;
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
/*     */ 
/*     */ public class RenderableShapeBuilder
/*     */   extends BaseShapeBuilder
/*     */ {
/*     */   private static short[] indices;
/*     */   private static float[] vertices;
/*     */   
/*     */   private static class RenderablePool
/*     */     extends FlushablePool<Renderable>
/*     */   {
/*     */     protected Renderable newObject() {
/*  39 */       return new Renderable();
/*     */     }
/*     */ 
/*     */     
/*     */     public Renderable obtain() {
/*  44 */       Renderable renderable = (Renderable)super.obtain();
/*  45 */       renderable.environment = null;
/*  46 */       renderable.material = null;
/*  47 */       renderable.meshPart.set("", null, 0, 0, 0);
/*  48 */       renderable.shader = null;
/*  49 */       return renderable;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  55 */   private static final RenderablePool renderablesPool = new RenderablePool();
/*  56 */   private static final Array<Renderable> renderables = new Array();
/*     */ 
/*     */   
/*     */   private static final int FLOAT_BYTES = 4;
/*     */ 
/*     */ 
/*     */   
/*     */   public static void buildNormals(MeshPartBuilder builder, RenderableProvider renderableProvider, float vectorSize) {
/*  64 */     buildNormals(builder, renderableProvider, vectorSize, tmpColor0.set(0.0F, 0.0F, 1.0F, 1.0F), tmpColor1.set(1.0F, 0.0F, 0.0F, 1.0F), tmpColor2
/*  65 */         .set(0.0F, 1.0F, 0.0F, 1.0F));
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
/*     */   public static void buildNormals(MeshPartBuilder builder, RenderableProvider renderableProvider, float vectorSize, Color normalColor, Color tangentColor, Color binormalColor) {
/*  78 */     renderableProvider.getRenderables(renderables, (Pool)renderablesPool);
/*     */     
/*  80 */     for (Renderable renderable : renderables) {
/*  81 */       buildNormals(builder, renderable, vectorSize, normalColor, tangentColor, binormalColor);
/*     */     }
/*     */     
/*  84 */     renderablesPool.flush();
/*  85 */     renderables.clear();
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
/*     */   public static void buildNormals(MeshPartBuilder builder, Renderable renderable, float vectorSize, Color normalColor, Color tangentColor, Color binormalColor) {
/*  97 */     Mesh mesh = renderable.meshPart.mesh;
/*     */ 
/*     */     
/* 100 */     int positionOffset = -1;
/* 101 */     if (mesh.getVertexAttribute(1) != null) {
/* 102 */       positionOffset = (mesh.getVertexAttribute(1)).offset / 4;
/*     */     }
/*     */     
/* 105 */     int normalOffset = -1;
/* 106 */     if (mesh.getVertexAttribute(8) != null) {
/* 107 */       normalOffset = (mesh.getVertexAttribute(8)).offset / 4;
/*     */     }
/*     */     
/* 110 */     int tangentOffset = -1;
/* 111 */     if (mesh.getVertexAttribute(128) != null) {
/* 112 */       tangentOffset = (mesh.getVertexAttribute(128)).offset / 4;
/*     */     }
/*     */     
/* 115 */     int binormalOffset = -1;
/* 116 */     if (mesh.getVertexAttribute(256) != null) {
/* 117 */       binormalOffset = (mesh.getVertexAttribute(256)).offset / 4;
/*     */     }
/* 119 */     int attributesSize = mesh.getVertexSize() / 4;
/* 120 */     int verticesOffset = 0;
/* 121 */     int verticesQuantity = 0;
/*     */     
/* 123 */     if (mesh.getNumIndices() > 0) {
/*     */       
/* 125 */       ensureIndicesCapacity(mesh.getNumIndices());
/* 126 */       mesh.getIndices(renderable.meshPart.offset, renderable.meshPart.size, indices, 0);
/*     */       
/* 128 */       short minVertice = minVerticeInIndices();
/* 129 */       short maxVertice = maxVerticeInIndices();
/*     */       
/* 131 */       verticesOffset = minVertice;
/* 132 */       verticesQuantity = maxVertice - minVertice;
/*     */     } else {
/* 134 */       verticesOffset = renderable.meshPart.offset;
/* 135 */       verticesQuantity = renderable.meshPart.size;
/*     */     } 
/*     */     
/* 138 */     ensureVerticesCapacity(verticesQuantity * attributesSize);
/* 139 */     mesh.getVertices(verticesOffset * attributesSize, verticesQuantity * attributesSize, vertices, 0);
/*     */     
/* 141 */     for (int i = verticesOffset; i < verticesQuantity; i++) {
/* 142 */       int id = i * attributesSize;
/*     */ 
/*     */       
/* 145 */       tmpV0.set(vertices[id + positionOffset], vertices[id + positionOffset + 1], vertices[id + positionOffset + 2]);
/*     */ 
/*     */       
/* 148 */       if (normalOffset != -1) {
/* 149 */         tmpV1.set(vertices[id + normalOffset], vertices[id + normalOffset + 1], vertices[id + normalOffset + 2]);
/* 150 */         tmpV2.set(tmpV0).add(tmpV1.scl(vectorSize));
/*     */       } 
/*     */       
/* 153 */       if (tangentOffset != -1) {
/* 154 */         tmpV3.set(vertices[id + tangentOffset], vertices[id + tangentOffset + 1], vertices[id + tangentOffset + 2]);
/* 155 */         tmpV4.set(tmpV0).add(tmpV3.scl(vectorSize));
/*     */       } 
/*     */       
/* 158 */       if (binormalOffset != -1) {
/* 159 */         tmpV5.set(vertices[id + binormalOffset], vertices[id + binormalOffset + 1], vertices[id + binormalOffset + 2]);
/* 160 */         tmpV6.set(tmpV0).add(tmpV5.scl(vectorSize));
/*     */       } 
/*     */ 
/*     */       
/* 164 */       tmpV0.mul(renderable.worldTransform);
/* 165 */       tmpV2.mul(renderable.worldTransform);
/* 166 */       tmpV4.mul(renderable.worldTransform);
/* 167 */       tmpV6.mul(renderable.worldTransform);
/*     */ 
/*     */       
/* 170 */       if (normalOffset != -1) {
/* 171 */         builder.setColor(normalColor);
/* 172 */         builder.line(tmpV0, tmpV2);
/*     */       } 
/*     */       
/* 175 */       if (tangentOffset != -1) {
/* 176 */         builder.setColor(tangentColor);
/* 177 */         builder.line(tmpV0, tmpV4);
/*     */       } 
/*     */       
/* 180 */       if (binormalOffset != -1) {
/* 181 */         builder.setColor(binormalColor);
/* 182 */         builder.line(tmpV0, tmpV6);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void ensureVerticesCapacity(int capacity) {
/* 188 */     if (vertices == null || vertices.length < capacity) vertices = new float[capacity]; 
/*     */   }
/*     */   
/*     */   private static void ensureIndicesCapacity(int capacity) {
/* 192 */     if (indices == null || indices.length < capacity) indices = new short[capacity]; 
/*     */   }
/*     */   
/*     */   private static short minVerticeInIndices() {
/* 196 */     short min = Short.MAX_VALUE;
/* 197 */     for (int i = 0; i < indices.length; i++) {
/* 198 */       if (indices[i] < min) min = indices[i]; 
/* 199 */     }  return min;
/*     */   }
/*     */   
/*     */   private static short maxVerticeInIndices() {
/* 203 */     short max = Short.MIN_VALUE;
/* 204 */     for (int i = 0; i < indices.length; i++) {
/* 205 */       if (indices[i] > max) max = indices[i]; 
/* 206 */     }  return max;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\RenderableShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */