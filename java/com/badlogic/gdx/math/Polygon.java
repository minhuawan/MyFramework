/*     */ package com.badlogic.gdx.math;
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
/*     */ public class Polygon
/*     */   implements Shape2D
/*     */ {
/*     */   private float[] localVertices;
/*     */   private float[] worldVertices;
/*     */   private float x;
/*     */   private float y;
/*     */   private float originX;
/*     */   private float originY;
/*     */   private float rotation;
/*  26 */   private float scaleX = 1.0F; private float scaleY = 1.0F;
/*     */   
/*     */   private boolean dirty = true;
/*     */   private Rectangle bounds;
/*     */   
/*     */   public Polygon() {
/*  32 */     this.localVertices = new float[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Polygon(float[] vertices) {
/*  42 */     if (vertices.length < 6) throw new IllegalArgumentException("polygons must contain at least 3 points."); 
/*  43 */     this.localVertices = vertices;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getVertices() {
/*  48 */     return this.localVertices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float[] getTransformedVertices() {
/*  56 */     if (!this.dirty) return this.worldVertices; 
/*  57 */     this.dirty = false;
/*     */     
/*  59 */     float[] localVertices = this.localVertices;
/*  60 */     if (this.worldVertices == null || this.worldVertices.length != localVertices.length) this.worldVertices = new float[localVertices.length];
/*     */     
/*  62 */     float[] worldVertices = this.worldVertices;
/*  63 */     float positionX = this.x;
/*  64 */     float positionY = this.y;
/*  65 */     float originX = this.originX;
/*  66 */     float originY = this.originY;
/*  67 */     float scaleX = this.scaleX;
/*  68 */     float scaleY = this.scaleY;
/*  69 */     boolean scale = (scaleX != 1.0F || scaleY != 1.0F);
/*  70 */     float rotation = this.rotation;
/*  71 */     float cos = MathUtils.cosDeg(rotation);
/*  72 */     float sin = MathUtils.sinDeg(rotation);
/*     */     
/*  74 */     for (int i = 0, n = localVertices.length; i < n; i += 2) {
/*  75 */       float x = localVertices[i] - originX;
/*  76 */       float y = localVertices[i + 1] - originY;
/*     */ 
/*     */       
/*  79 */       if (scale) {
/*  80 */         x *= scaleX;
/*  81 */         y *= scaleY;
/*     */       } 
/*     */ 
/*     */       
/*  85 */       if (rotation != 0.0F) {
/*  86 */         float oldX = x;
/*  87 */         x = cos * x - sin * y;
/*  88 */         y = sin * oldX + cos * y;
/*     */       } 
/*     */       
/*  91 */       worldVertices[i] = positionX + x + originX;
/*  92 */       worldVertices[i + 1] = positionY + y + originY;
/*     */     } 
/*  94 */     return worldVertices;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrigin(float originX, float originY) {
/*  99 */     this.originX = originX;
/* 100 */     this.originY = originY;
/* 101 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y) {
/* 106 */     this.x = x;
/* 107 */     this.y = y;
/* 108 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVertices(float[] vertices) {
/* 117 */     if (vertices.length < 6) throw new IllegalArgumentException("polygons must contain at least 3 points."); 
/* 118 */     this.localVertices = vertices;
/* 119 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void translate(float x, float y) {
/* 124 */     this.x += x;
/* 125 */     this.y += y;
/* 126 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotation(float degrees) {
/* 131 */     this.rotation = degrees;
/* 132 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotate(float degrees) {
/* 137 */     this.rotation += degrees;
/* 138 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScale(float scaleX, float scaleY) {
/* 143 */     this.scaleX = scaleX;
/* 144 */     this.scaleY = scaleY;
/* 145 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(float amount) {
/* 150 */     this.scaleX += amount;
/* 151 */     this.scaleY += amount;
/* 152 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dirty() {
/* 157 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public float area() {
/* 162 */     float[] vertices = getTransformedVertices();
/* 163 */     return GeometryUtils.polygonArea(vertices, 0, vertices.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBoundingRectangle() {
/* 172 */     float[] vertices = getTransformedVertices();
/*     */     
/* 174 */     float minX = vertices[0];
/* 175 */     float minY = vertices[1];
/* 176 */     float maxX = vertices[0];
/* 177 */     float maxY = vertices[1];
/*     */     
/* 179 */     int numFloats = vertices.length;
/* 180 */     for (int i = 2; i < numFloats; i += 2) {
/* 181 */       minX = (minX > vertices[i]) ? vertices[i] : minX;
/* 182 */       minY = (minY > vertices[i + 1]) ? vertices[i + 1] : minY;
/* 183 */       maxX = (maxX < vertices[i]) ? vertices[i] : maxX;
/* 184 */       maxY = (maxY < vertices[i + 1]) ? vertices[i + 1] : maxY;
/*     */     } 
/*     */     
/* 187 */     if (this.bounds == null) this.bounds = new Rectangle(); 
/* 188 */     this.bounds.x = minX;
/* 189 */     this.bounds.y = minY;
/* 190 */     this.bounds.width = maxX - minX;
/* 191 */     this.bounds.height = maxY - minY;
/*     */     
/* 193 */     return this.bounds;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(float x, float y) {
/* 199 */     float[] vertices = getTransformedVertices();
/* 200 */     int numFloats = vertices.length;
/* 201 */     int intersects = 0;
/*     */     
/* 203 */     for (int i = 0; i < numFloats; i += 2) {
/* 204 */       float x1 = vertices[i];
/* 205 */       float y1 = vertices[i + 1];
/* 206 */       float x2 = vertices[(i + 2) % numFloats];
/* 207 */       float y2 = vertices[(i + 3) % numFloats];
/* 208 */       if (((y1 <= y && y < y2) || (y2 <= y && y < y1)) && x < (x2 - x1) / (y2 - y1) * (y - y1) + x1) intersects++; 
/*     */     } 
/* 210 */     return ((intersects & 0x1) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Vector2 point) {
/* 215 */     return contains(point.x, point.y);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX() {
/* 220 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/* 225 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOriginX() {
/* 230 */     return this.originX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOriginY() {
/* 235 */     return this.originY;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRotation() {
/* 240 */     return this.rotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScaleX() {
/* 245 */     return this.scaleX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScaleY() {
/* 250 */     return this.scaleY;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Polygon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */