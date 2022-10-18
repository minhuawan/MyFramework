/*     */ package com.badlogic.gdx.graphics.g3d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.VertexAttributes;
/*     */ import com.badlogic.gdx.graphics.g3d.Material;
/*     */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*     */ import com.badlogic.gdx.graphics.g3d.RenderableProvider;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class ShapeCache
/*     */   implements Disposable, RenderableProvider
/*     */ {
/*     */   private final MeshBuilder builder;
/*     */   private final Mesh mesh;
/*     */   private boolean building;
/*  66 */   private final String id = "id";
/*  67 */   private final Renderable renderable = new Renderable();
/*     */ 
/*     */   
/*     */   public ShapeCache() {
/*  71 */     this(5000, 5000, new VertexAttributes(new VertexAttribute[] { new VertexAttribute(1, 3, "a_position"), new VertexAttribute(4, 4, "a_color") }), 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShapeCache(int maxVertices, int maxIndices, VertexAttributes attributes, int primitiveType) {
/*  82 */     this.mesh = new Mesh(false, maxVertices, maxIndices, attributes);
/*     */ 
/*     */     
/*  85 */     this.builder = new MeshBuilder();
/*     */ 
/*     */     
/*  88 */     this.renderable.meshPart.mesh = this.mesh;
/*  89 */     this.renderable.meshPart.primitiveType = primitiveType;
/*  90 */     this.renderable.material = new Material();
/*     */   }
/*     */ 
/*     */   
/*     */   public MeshPartBuilder begin() {
/*  95 */     return begin(1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPartBuilder begin(int primitiveType) {
/* 101 */     if (this.building) throw new GdxRuntimeException("Call end() after calling begin()"); 
/* 102 */     this.building = true;
/*     */     
/* 104 */     this.builder.begin(this.mesh.getVertexAttributes());
/* 105 */     this.builder.part("id", primitiveType, this.renderable.meshPart);
/* 106 */     return this.builder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 111 */     if (!this.building) throw new GdxRuntimeException("Call begin() prior to calling end()"); 
/* 112 */     this.building = false;
/*     */     
/* 114 */     this.builder.end(this.mesh);
/*     */   }
/*     */ 
/*     */   
/*     */   public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
/* 119 */     renderables.add(this.renderable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Material getMaterial() {
/* 125 */     return this.renderable.material;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix4 getWorldTransform() {
/* 131 */     return this.renderable.worldTransform;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 136 */     this.mesh.dispose();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\ShapeCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */