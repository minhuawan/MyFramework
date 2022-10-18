/*     */ package com.badlogic.gdx.graphics.g3d.model;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
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
/*     */ public class MeshPart
/*     */ {
/*     */   public String id;
/*     */   public int primitiveType;
/*     */   public int offset;
/*     */   public int size;
/*     */   public Mesh mesh;
/*  59 */   public final Vector3 center = new Vector3();
/*     */ 
/*     */ 
/*     */   
/*  63 */   public final Vector3 halfExtents = new Vector3();
/*     */ 
/*     */   
/*  66 */   public float radius = -1.0F;
/*     */   
/*  68 */   private static final BoundingBox bounds = new BoundingBox();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPart() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPart(String id, Mesh mesh, int offset, int size, int type) {
/*  81 */     set(id, mesh, offset, size, type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPart(MeshPart copyFrom) {
/*  87 */     set(copyFrom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPart set(MeshPart other) {
/*  94 */     this.id = other.id;
/*  95 */     this.mesh = other.mesh;
/*  96 */     this.offset = other.offset;
/*  97 */     this.size = other.size;
/*  98 */     this.primitiveType = other.primitiveType;
/*  99 */     this.center.set(other.center);
/* 100 */     this.halfExtents.set(other.halfExtents);
/* 101 */     this.radius = other.radius;
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshPart set(String id, Mesh mesh, int offset, int size, int type) {
/* 108 */     this.id = id;
/* 109 */     this.mesh = mesh;
/* 110 */     this.offset = offset;
/* 111 */     this.size = size;
/* 112 */     this.primitiveType = type;
/* 113 */     this.center.set(0.0F, 0.0F, 0.0F);
/* 114 */     this.halfExtents.set(0.0F, 0.0F, 0.0F);
/* 115 */     this.radius = -1.0F;
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/* 124 */     this.mesh.calculateBoundingBox(bounds, this.offset, this.size);
/* 125 */     bounds.getCenter(this.center);
/* 126 */     bounds.getDimensions(this.halfExtents).scl(0.5F);
/* 127 */     this.radius = this.halfExtents.len();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(MeshPart other) {
/* 135 */     return (other == this || (other != null && other.mesh == this.mesh && other.primitiveType == this.primitiveType && other.offset == this.offset && other.size == this.size));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object arg0) {
/* 141 */     if (arg0 == null) return false; 
/* 142 */     if (arg0 == this) return true; 
/* 143 */     if (!(arg0 instanceof MeshPart)) return false; 
/* 144 */     return equals((MeshPart)arg0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(ShaderProgram shader, boolean autoBind) {
/* 152 */     this.mesh.render(shader, this.primitiveType, this.offset, this.size, autoBind);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(ShaderProgram shader) {
/* 159 */     this.mesh.render(shader, this.primitiveType, this.offset, this.size);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\model\MeshPart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */