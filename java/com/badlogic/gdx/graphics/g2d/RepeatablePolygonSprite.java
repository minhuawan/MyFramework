/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.math.EarClippingTriangulator;
/*     */ import com.badlogic.gdx.math.Intersector;
/*     */ import com.badlogic.gdx.math.Polygon;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ShortArray;
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
/*     */ public class RepeatablePolygonSprite
/*     */ {
/*     */   private TextureRegion region;
/*     */   private float density;
/*     */   private boolean dirty = true;
/*  39 */   private Array<float[]> parts = new Array();
/*     */   
/*  41 */   private Array<float[]> vertices = new Array();
/*  42 */   private Array<short[]> indices = new Array();
/*     */   private int cols;
/*     */   private int rows;
/*     */   private float gridWidth;
/*     */   private float gridHeight;
/*  47 */   public float x = 0.0F;
/*  48 */   public float y = 0.0F;
/*  49 */   private Color color = Color.WHITE;
/*  50 */   private Vector2 offset = new Vector2();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPolygon(TextureRegion region, float[] vertices) {
/*  59 */     setPolygon(region, vertices, -1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPolygon(TextureRegion region, float[] vertices, float density) {
/*  70 */     this.region = region;
/*     */     
/*  72 */     vertices = offset(vertices);
/*     */     
/*  74 */     Polygon polygon = new Polygon(vertices);
/*  75 */     Polygon tmpPoly = new Polygon();
/*  76 */     Polygon intersectionPoly = new Polygon();
/*  77 */     EarClippingTriangulator triangulator = new EarClippingTriangulator();
/*     */ 
/*     */ 
/*     */     
/*  81 */     Rectangle boundRect = polygon.getBoundingRectangle();
/*     */     
/*  83 */     if (density == -1.0F) density = boundRect.getWidth() / region.getRegionWidth();
/*     */     
/*  85 */     float regionAspectRatio = region.getRegionHeight() / region.getRegionWidth();
/*  86 */     this.cols = (int)Math.ceil(density);
/*  87 */     this.gridWidth = boundRect.getWidth() / density;
/*  88 */     this.gridHeight = regionAspectRatio * this.gridWidth;
/*  89 */     this.rows = (int)Math.ceil((boundRect.getHeight() / this.gridHeight));
/*     */     
/*  91 */     for (int col = 0; col < this.cols; col++) {
/*  92 */       for (int row = 0; row < this.rows; row++) {
/*  93 */         float[] verts = new float[8];
/*  94 */         int idx = 0;
/*  95 */         verts[idx++] = col * this.gridWidth;
/*  96 */         verts[idx++] = row * this.gridHeight;
/*  97 */         verts[idx++] = col * this.gridWidth;
/*  98 */         verts[idx++] = (row + 1) * this.gridHeight;
/*  99 */         verts[idx++] = (col + 1) * this.gridWidth;
/* 100 */         verts[idx++] = (row + 1) * this.gridHeight;
/* 101 */         verts[idx++] = (col + 1) * this.gridWidth;
/* 102 */         verts[idx] = row * this.gridHeight;
/* 103 */         tmpPoly.setVertices(verts);
/*     */         
/* 105 */         Intersector.intersectPolygons(polygon, tmpPoly, intersectionPoly);
/* 106 */         verts = intersectionPoly.getVertices();
/* 107 */         if (verts.length > 0) {
/* 108 */           this.parts.add(snapToGrid(verts));
/* 109 */           ShortArray arr = triangulator.computeTriangles(verts);
/* 110 */           this.indices.add(arr.toArray());
/*     */         }
/*     */         else {
/*     */           
/* 114 */           this.parts.add(null);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     buildVertices();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float[] snapToGrid(float[] vertices) {
/* 130 */     for (int i = 0; i < vertices.length; i += 2) {
/* 131 */       float numX = vertices[i] / this.gridWidth % 1.0F;
/* 132 */       float numY = vertices[i + 1] / this.gridHeight % 1.0F;
/* 133 */       if (numX > 0.99F || numX < 0.01F) {
/* 134 */         vertices[i] = this.gridWidth * Math.round(vertices[i] / this.gridWidth);
/*     */       }
/* 136 */       if (numY > 0.99F || numY < 0.01F) {
/* 137 */         vertices[i + 1] = this.gridHeight * Math.round(vertices[i + 1] / this.gridHeight);
/*     */       }
/*     */     } 
/*     */     
/* 141 */     return vertices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float[] offset(float[] vertices) {
/* 151 */     this.offset.set(vertices[0], vertices[1]); int i;
/* 152 */     for (i = 0; i < vertices.length - 1; i += 2) {
/* 153 */       if (this.offset.x > vertices[i]) {
/* 154 */         this.offset.x = vertices[i];
/*     */       }
/* 156 */       if (this.offset.y > vertices[i + 1]) {
/* 157 */         this.offset.y = vertices[i + 1];
/*     */       }
/*     */     } 
/* 160 */     for (i = 0; i < vertices.length; i += 2) {
/* 161 */       vertices[i] = vertices[i] - this.offset.x;
/* 162 */       vertices[i + 1] = vertices[i + 1] - this.offset.y;
/*     */     } 
/*     */     
/* 165 */     return vertices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildVertices() {
/* 172 */     this.vertices.clear();
/* 173 */     for (int i = 0; i < this.parts.size; i++) {
/* 174 */       float[] verts = (float[])this.parts.get(i);
/* 175 */       if (verts != null) {
/*     */         
/* 177 */         float[] fullVerts = new float[5 * verts.length / 2];
/* 178 */         int idx = 0;
/*     */         
/* 180 */         int col = i / this.rows;
/* 181 */         int row = i % this.rows;
/*     */         
/* 183 */         for (int j = 0; j < verts.length; j += 2) {
/* 184 */           fullVerts[idx++] = verts[j] + this.offset.x + this.x;
/* 185 */           fullVerts[idx++] = verts[j + 1] + this.offset.y + this.y;
/*     */           
/* 187 */           fullVerts[idx++] = this.color.toFloatBits();
/*     */           
/* 189 */           float u = verts[j] % this.gridWidth / this.gridWidth;
/* 190 */           float v = verts[j + 1] % this.gridHeight / this.gridHeight;
/* 191 */           if (verts[j] == col * this.gridWidth) u = 0.0F; 
/* 192 */           if (verts[j] == (col + 1) * this.gridWidth) u = 1.0F; 
/* 193 */           if (verts[j + 1] == row * this.gridHeight) v = 0.0F; 
/* 194 */           if (verts[j + 1] == (row + 1) * this.gridHeight) v = 1.0F; 
/* 195 */           u = this.region.getU() + (this.region.getU2() - this.region.getU()) * u;
/* 196 */           v = this.region.getV() + (this.region.getV2() - this.region.getV()) * v;
/* 197 */           fullVerts[idx++] = u;
/* 198 */           fullVerts[idx++] = v;
/*     */         } 
/* 200 */         this.vertices.add(fullVerts);
/*     */       } 
/* 202 */     }  this.dirty = false;
/*     */   }
/*     */   
/*     */   public void draw(PolygonSpriteBatch batch) {
/* 206 */     if (this.dirty) {
/* 207 */       buildVertices();
/*     */     }
/* 209 */     for (int i = 0; i < this.vertices.size; i++) {
/* 210 */       batch.draw(this.region.getTexture(), (float[])this.vertices.get(i), 0, ((float[])this.vertices.get(i)).length, (short[])this.indices.get(i), 0, ((short[])this.indices.get(i)).length);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 218 */     this.color = color;
/* 219 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void setPosition(float x, float y) {
/* 223 */     this.x = x;
/* 224 */     this.y = y;
/* 225 */     this.dirty = true;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\RepeatablePolygonSprite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */