/*     */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
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
/*     */ public final class EllipseSpawnShapeValue
/*     */   extends PrimitiveSpawnShapeValue
/*     */ {
/*  27 */   PrimitiveSpawnShapeValue.SpawnSide side = PrimitiveSpawnShapeValue.SpawnSide.both;
/*     */   
/*     */   public EllipseSpawnShapeValue(EllipseSpawnShapeValue value) {
/*  30 */     super(value);
/*  31 */     load(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EllipseSpawnShapeValue() {}
/*     */ 
/*     */   
/*     */   public void spawnAux(Vector3 vector, float percent) {
/*  40 */     float radiusX, radiusY, radiusZ, width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/*  41 */     float height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/*  42 */     float depth = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*     */ 
/*     */ 
/*     */     
/*  46 */     float minT = 0.0F, maxT = 6.2831855F;
/*  47 */     if (this.side == PrimitiveSpawnShapeValue.SpawnSide.top) {
/*  48 */       maxT = 3.1415927F;
/*  49 */     } else if (this.side == PrimitiveSpawnShapeValue.SpawnSide.bottom) {
/*  50 */       maxT = -3.1415927F;
/*     */     } 
/*  52 */     float t = MathUtils.random(minT, maxT);
/*     */ 
/*     */     
/*  55 */     if (this.edges) {
/*  56 */       if (width == 0.0F) {
/*  57 */         vector.set(0.0F, height / 2.0F * MathUtils.sin(t), depth / 2.0F * MathUtils.cos(t));
/*     */         return;
/*     */       } 
/*  60 */       if (height == 0.0F) {
/*  61 */         vector.set(width / 2.0F * MathUtils.cos(t), 0.0F, depth / 2.0F * MathUtils.sin(t));
/*     */         return;
/*     */       } 
/*  64 */       if (depth == 0.0F) {
/*  65 */         vector.set(width / 2.0F * MathUtils.cos(t), height / 2.0F * MathUtils.sin(t), 0.0F);
/*     */         
/*     */         return;
/*     */       } 
/*  69 */       radiusX = width / 2.0F;
/*  70 */       radiusY = height / 2.0F;
/*  71 */       radiusZ = depth / 2.0F;
/*     */     } else {
/*  73 */       radiusX = MathUtils.random(width / 2.0F);
/*  74 */       radiusY = MathUtils.random(height / 2.0F);
/*  75 */       radiusZ = MathUtils.random(depth / 2.0F);
/*     */     } 
/*     */     
/*  78 */     float z = MathUtils.random(-1.0F, 1.0F);
/*  79 */     float r = (float)Math.sqrt((1.0F - z * z));
/*  80 */     vector.set(radiusX * r * MathUtils.cos(t), radiusY * r * MathUtils.sin(t), radiusZ * z);
/*     */   }
/*     */   
/*     */   public PrimitiveSpawnShapeValue.SpawnSide getSide() {
/*  84 */     return this.side;
/*     */   }
/*     */   
/*     */   public void setSide(PrimitiveSpawnShapeValue.SpawnSide side) {
/*  88 */     this.side = side;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(ParticleValue value) {
/*  93 */     super.load(value);
/*  94 */     EllipseSpawnShapeValue shape = (EllipseSpawnShapeValue)value;
/*  95 */     this.side = shape.side;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnShapeValue copy() {
/* 100 */     return new EllipseSpawnShapeValue(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 105 */     super.write(json);
/* 106 */     json.writeValue("side", this.side);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 111 */     super.read(json, jsonData);
/* 112 */     this.side = (PrimitiveSpawnShapeValue.SpawnSide)json.readValue("side", PrimitiveSpawnShapeValue.SpawnSide.class, jsonData);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\EllipseSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */