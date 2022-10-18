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
/*     */ public class Polyline
/*     */   implements Shape2D
/*     */ {
/*     */   private float[] localVertices;
/*     */   private float[] worldVertices;
/*     */   private float x;
/*     */   private float y;
/*     */   private float originX;
/*     */   private float originY;
/*     */   private float rotation;
/*  25 */   private float scaleX = 1.0F; private float scaleY = 1.0F;
/*     */   private float length;
/*     */   private float scaledLength;
/*     */   private boolean calculateScaledLength = true;
/*     */   private boolean calculateLength = true;
/*     */   private boolean dirty = true;
/*     */   
/*     */   public Polyline() {
/*  33 */     this.localVertices = new float[0];
/*     */   }
/*     */   
/*     */   public Polyline(float[] vertices) {
/*  37 */     if (vertices.length < 4) throw new IllegalArgumentException("polylines must contain at least 2 points."); 
/*  38 */     this.localVertices = vertices;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getVertices() {
/*  43 */     return this.localVertices;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getTransformedVertices() {
/*  48 */     if (!this.dirty) return this.worldVertices; 
/*  49 */     this.dirty = false;
/*     */     
/*  51 */     float[] localVertices = this.localVertices;
/*  52 */     if (this.worldVertices == null || this.worldVertices.length < localVertices.length) this.worldVertices = new float[localVertices.length];
/*     */     
/*  54 */     float[] worldVertices = this.worldVertices;
/*  55 */     float positionX = this.x;
/*  56 */     float positionY = this.y;
/*  57 */     float originX = this.originX;
/*  58 */     float originY = this.originY;
/*  59 */     float scaleX = this.scaleX;
/*  60 */     float scaleY = this.scaleY;
/*  61 */     boolean scale = (scaleX != 1.0F || scaleY != 1.0F);
/*  62 */     float rotation = this.rotation;
/*  63 */     float cos = MathUtils.cosDeg(rotation);
/*  64 */     float sin = MathUtils.sinDeg(rotation);
/*     */     
/*  66 */     for (int i = 0, n = localVertices.length; i < n; i += 2) {
/*  67 */       float x = localVertices[i] - originX;
/*  68 */       float y = localVertices[i + 1] - originY;
/*     */ 
/*     */       
/*  71 */       if (scale) {
/*  72 */         x *= scaleX;
/*  73 */         y *= scaleY;
/*     */       } 
/*     */ 
/*     */       
/*  77 */       if (rotation != 0.0F) {
/*  78 */         float oldX = x;
/*  79 */         x = cos * x - sin * y;
/*  80 */         y = sin * oldX + cos * y;
/*     */       } 
/*     */       
/*  83 */       worldVertices[i] = positionX + x + originX;
/*  84 */       worldVertices[i + 1] = positionY + y + originY;
/*     */     } 
/*  86 */     return worldVertices;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLength() {
/*  91 */     if (!this.calculateLength) return this.length; 
/*  92 */     this.calculateLength = false;
/*     */     
/*  94 */     this.length = 0.0F;
/*  95 */     for (int i = 0, n = this.localVertices.length - 2; i < n; i += 2) {
/*  96 */       float x = this.localVertices[i + 2] - this.localVertices[i];
/*  97 */       float y = this.localVertices[i + 1] - this.localVertices[i + 3];
/*  98 */       this.length += (float)Math.sqrt((x * x + y * y));
/*     */     } 
/*     */     
/* 101 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScaledLength() {
/* 106 */     if (!this.calculateScaledLength) return this.scaledLength; 
/* 107 */     this.calculateScaledLength = false;
/*     */     
/* 109 */     this.scaledLength = 0.0F;
/* 110 */     for (int i = 0, n = this.localVertices.length - 2; i < n; i += 2) {
/* 111 */       float x = this.localVertices[i + 2] * this.scaleX - this.localVertices[i] * this.scaleX;
/* 112 */       float y = this.localVertices[i + 1] * this.scaleY - this.localVertices[i + 3] * this.scaleY;
/* 113 */       this.scaledLength += (float)Math.sqrt((x * x + y * y));
/*     */     } 
/*     */     
/* 116 */     return this.scaledLength;
/*     */   }
/*     */   
/*     */   public float getX() {
/* 120 */     return this.x;
/*     */   }
/*     */   
/*     */   public float getY() {
/* 124 */     return this.y;
/*     */   }
/*     */   
/*     */   public float getOriginX() {
/* 128 */     return this.originX;
/*     */   }
/*     */   
/*     */   public float getOriginY() {
/* 132 */     return this.originY;
/*     */   }
/*     */   
/*     */   public float getRotation() {
/* 136 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public float getScaleX() {
/* 140 */     return this.scaleX;
/*     */   }
/*     */   
/*     */   public float getScaleY() {
/* 144 */     return this.scaleY;
/*     */   }
/*     */   
/*     */   public void setOrigin(float originX, float originY) {
/* 148 */     this.originX = originX;
/* 149 */     this.originY = originY;
/* 150 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void setPosition(float x, float y) {
/* 154 */     this.x = x;
/* 155 */     this.y = y;
/* 156 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void setVertices(float[] vertices) {
/* 160 */     if (vertices.length < 4) throw new IllegalArgumentException("polylines must contain at least 2 points."); 
/* 161 */     this.localVertices = vertices;
/* 162 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void setRotation(float degrees) {
/* 166 */     this.rotation = degrees;
/* 167 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void rotate(float degrees) {
/* 171 */     this.rotation += degrees;
/* 172 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void setScale(float scaleX, float scaleY) {
/* 176 */     this.scaleX = scaleX;
/* 177 */     this.scaleY = scaleY;
/* 178 */     this.dirty = true;
/* 179 */     this.calculateScaledLength = true;
/*     */   }
/*     */   
/*     */   public void scale(float amount) {
/* 183 */     this.scaleX += amount;
/* 184 */     this.scaleY += amount;
/* 185 */     this.dirty = true;
/* 186 */     this.calculateScaledLength = true;
/*     */   }
/*     */   
/*     */   public void calculateLength() {
/* 190 */     this.calculateLength = true;
/*     */   }
/*     */   
/*     */   public void calculateScaledLength() {
/* 194 */     this.calculateScaledLength = true;
/*     */   }
/*     */   
/*     */   public void dirty() {
/* 198 */     this.dirty = true;
/*     */   }
/*     */   
/*     */   public void translate(float x, float y) {
/* 202 */     this.x += x;
/* 203 */     this.y += y;
/* 204 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Vector2 point) {
/* 209 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(float x, float y) {
/* 214 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Polyline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */