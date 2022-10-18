/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.VertexAttributes;
/*    */ import com.badlogic.gdx.math.CumulativeDistribution;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WeightMeshSpawnShapeValue
/*    */   extends MeshSpawnShapeValue
/*    */ {
/*    */   private CumulativeDistribution<MeshSpawnShapeValue.Triangle> distribution;
/*    */   
/*    */   public WeightMeshSpawnShapeValue(WeightMeshSpawnShapeValue value) {
/* 32 */     super(value);
/* 33 */     this.distribution = new CumulativeDistribution();
/* 34 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public WeightMeshSpawnShapeValue() {
/* 39 */     this.distribution = new CumulativeDistribution();
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 44 */     calculateWeights();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void calculateWeights() {
/* 50 */     this.distribution.clear();
/* 51 */     VertexAttributes attributes = this.mesh.getVertexAttributes();
/* 52 */     int indicesCount = this.mesh.getNumIndices();
/* 53 */     int vertexCount = this.mesh.getNumVertices();
/* 54 */     int vertexSize = (short)(attributes.vertexSize / 4), positionOffset = (short)((attributes.findByUsage(1)).offset / 4);
/* 55 */     float[] vertices = new float[vertexCount * vertexSize];
/* 56 */     this.mesh.getVertices(vertices);
/* 57 */     if (indicesCount > 0) {
/* 58 */       short[] indices = new short[indicesCount];
/* 59 */       this.mesh.getIndices(indices);
/*    */ 
/*    */       
/* 62 */       for (int i = 0; i < indicesCount; i += 3) {
/* 63 */         int p1Offset = indices[i] * vertexSize + positionOffset, p2Offset = indices[i + 1] * vertexSize + positionOffset, p3Offset = indices[i + 2] * vertexSize + positionOffset;
/*    */         
/* 65 */         float x1 = vertices[p1Offset], y1 = vertices[p1Offset + 1], z1 = vertices[p1Offset + 2], x2 = vertices[p2Offset], y2 = vertices[p2Offset + 1], z2 = vertices[p2Offset + 2], x3 = vertices[p3Offset], y3 = vertices[p3Offset + 1], z3 = vertices[p3Offset + 2];
/* 66 */         float area = Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0F);
/* 67 */         this.distribution.add(new MeshSpawnShapeValue.Triangle(x1, y1, z1, x2, y2, z2, x3, y3, z3), area);
/*    */       } 
/*    */     } else {
/*    */       int i;
/* 71 */       for (i = 0; i < vertexCount; i += vertexSize) {
/* 72 */         int p1Offset = i + positionOffset, p2Offset = p1Offset + vertexSize, p3Offset = p2Offset + vertexSize;
/* 73 */         float x1 = vertices[p1Offset], y1 = vertices[p1Offset + 1], z1 = vertices[p1Offset + 2], x2 = vertices[p2Offset], y2 = vertices[p2Offset + 1], z2 = vertices[p2Offset + 2], x3 = vertices[p3Offset], y3 = vertices[p3Offset + 1], z3 = vertices[p3Offset + 2];
/* 74 */         float area = Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0F);
/* 75 */         this.distribution.add(new MeshSpawnShapeValue.Triangle(x1, y1, z1, x2, y2, z2, x3, y3, z3), area);
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 80 */     this.distribution.generateNormalized();
/*    */   }
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 85 */     MeshSpawnShapeValue.Triangle t = (MeshSpawnShapeValue.Triangle)this.distribution.value();
/* 86 */     float a = MathUtils.random(), b = MathUtils.random();
/* 87 */     vector.set(t.x1 + a * (t.x2 - t.x1) + b * (t.x3 - t.x1), t.y1 + a * (t.y2 - t.y1) + b * (t.y3 - t.y1), t.z1 + a * (t.z2 - t.z1) + b * (t.z3 - t.z1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 93 */     return new WeightMeshSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\WeightMeshSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */