/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Mesh;
/*    */ import com.badlogic.gdx.graphics.g3d.Model;
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UnweightedMeshSpawnShapeValue
/*    */   extends MeshSpawnShapeValue
/*    */ {
/*    */   private float[] vertices;
/*    */   private short[] indices;
/*    */   private int positionOffset;
/*    */   private int vertexSize;
/*    */   private int vertexCount;
/*    */   private int triangleCount;
/*    */   
/*    */   public UnweightedMeshSpawnShapeValue(UnweightedMeshSpawnShapeValue value) {
/* 33 */     super(value);
/* 34 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public UnweightedMeshSpawnShapeValue() {}
/*    */ 
/*    */   
/*    */   public void setMesh(Mesh mesh, Model model) {
/* 42 */     super.setMesh(mesh, model);
/* 43 */     this.vertexSize = mesh.getVertexSize() / 4;
/* 44 */     this.positionOffset = (mesh.getVertexAttribute(1)).offset / 4;
/* 45 */     int indicesCount = mesh.getNumIndices();
/* 46 */     if (indicesCount > 0) {
/* 47 */       this.indices = new short[indicesCount];
/* 48 */       mesh.getIndices(this.indices);
/* 49 */       this.triangleCount = this.indices.length / 3;
/*    */     } else {
/* 51 */       this.indices = null;
/* 52 */     }  this.vertexCount = mesh.getNumVertices();
/* 53 */     this.vertices = new float[this.vertexCount * this.vertexSize];
/* 54 */     mesh.getVertices(this.vertices);
/*    */   }
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 59 */     if (this.indices == null) {
/*    */       
/* 61 */       int triangleIndex = MathUtils.random(this.vertexCount - 3) * this.vertexSize;
/* 62 */       int p1Offset = triangleIndex + this.positionOffset, p2Offset = p1Offset + this.vertexSize, p3Offset = p2Offset + this.vertexSize;
/* 63 */       float x1 = this.vertices[p1Offset], y1 = this.vertices[p1Offset + 1], z1 = this.vertices[p1Offset + 2], x2 = this.vertices[p2Offset], y2 = this.vertices[p2Offset + 1], z2 = this.vertices[p2Offset + 2], x3 = this.vertices[p3Offset], y3 = this.vertices[p3Offset + 1], z3 = this.vertices[p3Offset + 2];
/* 64 */       MeshSpawnShapeValue.Triangle.pick(x1, y1, z1, x2, y2, z2, x3, y3, z3, vector);
/*    */     } else {
/*    */       
/* 67 */       int triangleIndex = MathUtils.random(this.triangleCount - 1) * 3;
/* 68 */       int p1Offset = this.indices[triangleIndex] * this.vertexSize + this.positionOffset, p2Offset = this.indices[triangleIndex + 1] * this.vertexSize + this.positionOffset;
/* 69 */       int p3Offset = this.indices[triangleIndex + 2] * this.vertexSize + this.positionOffset;
/* 70 */       float x1 = this.vertices[p1Offset], y1 = this.vertices[p1Offset + 1], z1 = this.vertices[p1Offset + 2], x2 = this.vertices[p2Offset], y2 = this.vertices[p2Offset + 1], z2 = this.vertices[p2Offset + 2], x3 = this.vertices[p3Offset], y3 = this.vertices[p3Offset + 1], z3 = this.vertices[p3Offset + 2];
/* 71 */       MeshSpawnShapeValue.Triangle.pick(x1, y1, z1, x2, y2, z2, x3, y3, z3, vector);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 77 */     return new UnweightedMeshSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\UnweightedMeshSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */