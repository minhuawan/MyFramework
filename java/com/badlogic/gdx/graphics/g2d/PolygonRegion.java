/*    */ package com.badlogic.gdx.graphics.g2d;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PolygonRegion
/*    */ {
/*    */   final float[] textureCoords;
/*    */   final float[] vertices;
/*    */   final short[] triangles;
/*    */   final TextureRegion region;
/*    */   
/*    */   public PolygonRegion(TextureRegion region, float[] vertices, short[] triangles) {
/* 34 */     this.region = region;
/* 35 */     this.vertices = vertices;
/* 36 */     this.triangles = triangles;
/*    */     
/* 38 */     float[] textureCoords = this.textureCoords = new float[vertices.length];
/* 39 */     float u = region.u, v = region.v;
/* 40 */     float uvWidth = region.u2 - u;
/* 41 */     float uvHeight = region.v2 - v;
/* 42 */     int width = region.regionWidth;
/* 43 */     int height = region.regionHeight;
/* 44 */     for (int i = 0, n = vertices.length; i < n; i++) {
/* 45 */       textureCoords[i] = u + uvWidth * vertices[i] / width;
/* 46 */       i++;
/* 47 */       textureCoords[i] = v + uvHeight * (1.0F - vertices[i] / height);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public float[] getVertices() {
/* 53 */     return this.vertices;
/*    */   }
/*    */   
/*    */   public short[] getTriangles() {
/* 57 */     return this.triangles;
/*    */   }
/*    */   
/*    */   public float[] getTextureCoords() {
/* 61 */     return this.textureCoords;
/*    */   }
/*    */   
/*    */   public TextureRegion getRegion() {
/* 65 */     return this.region;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\PolygonRegion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */