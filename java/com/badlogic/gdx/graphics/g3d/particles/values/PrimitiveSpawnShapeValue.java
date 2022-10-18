/*     */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*     */ 
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
/*     */ public abstract class PrimitiveSpawnShapeValue
/*     */   extends SpawnShapeValue
/*     */ {
/*     */   public ScaledNumericValue spawnWidthValue;
/*     */   public ScaledNumericValue spawnHeightValue;
/*     */   public ScaledNumericValue spawnDepthValue;
/*     */   protected float spawnWidth;
/*     */   protected float spawnWidthDiff;
/*     */   protected float spawnHeight;
/*     */   protected float spawnHeightDiff;
/*     */   protected float spawnDepth;
/*     */   protected float spawnDepthDiff;
/*  26 */   protected static final Vector3 TMP_V1 = new Vector3();
/*     */   
/*     */   public enum SpawnSide {
/*  29 */     both, top, bottom;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean edges = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public PrimitiveSpawnShapeValue() {
/*  39 */     this.spawnWidthValue = new ScaledNumericValue();
/*  40 */     this.spawnHeightValue = new ScaledNumericValue();
/*  41 */     this.spawnDepthValue = new ScaledNumericValue();
/*     */   }
/*     */   
/*     */   public PrimitiveSpawnShapeValue(PrimitiveSpawnShapeValue value) {
/*  45 */     super(value);
/*  46 */     this.spawnWidthValue = new ScaledNumericValue();
/*  47 */     this.spawnHeightValue = new ScaledNumericValue();
/*  48 */     this.spawnDepthValue = new ScaledNumericValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActive(boolean active) {
/*  53 */     super.setActive(active);
/*  54 */     this.spawnWidthValue.setActive(true);
/*  55 */     this.spawnHeightValue.setActive(true);
/*  56 */     this.spawnDepthValue.setActive(true);
/*     */   }
/*     */   
/*     */   public boolean isEdges() {
/*  60 */     return this.edges;
/*     */   }
/*     */   
/*     */   public void setEdges(boolean edges) {
/*  64 */     this.edges = edges;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getSpawnWidth() {
/*  68 */     return this.spawnWidthValue;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getSpawnHeight() {
/*  72 */     return this.spawnHeightValue;
/*     */   }
/*     */   
/*     */   public ScaledNumericValue getSpawnDepth() {
/*  76 */     return this.spawnDepthValue;
/*     */   }
/*     */   
/*     */   public void setDimensions(float width, float height, float depth) {
/*  80 */     this.spawnWidthValue.setHigh(width);
/*  81 */     this.spawnHeightValue.setHigh(height);
/*  82 */     this.spawnDepthValue.setHigh(depth);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  87 */     this.spawnWidth = this.spawnWidthValue.newLowValue();
/*  88 */     this.spawnWidthDiff = this.spawnWidthValue.newHighValue();
/*  89 */     if (!this.spawnWidthValue.isRelative()) this.spawnWidthDiff -= this.spawnWidth;
/*     */     
/*  91 */     this.spawnHeight = this.spawnHeightValue.newLowValue();
/*  92 */     this.spawnHeightDiff = this.spawnHeightValue.newHighValue();
/*  93 */     if (!this.spawnHeightValue.isRelative()) this.spawnHeightDiff -= this.spawnHeight;
/*     */     
/*  95 */     this.spawnDepth = this.spawnDepthValue.newLowValue();
/*  96 */     this.spawnDepthDiff = this.spawnDepthValue.newHighValue();
/*  97 */     if (!this.spawnDepthValue.isRelative()) this.spawnDepthDiff -= this.spawnDepth;
/*     */   
/*     */   }
/*     */   
/*     */   public void load(ParticleValue value) {
/* 102 */     super.load(value);
/* 103 */     PrimitiveSpawnShapeValue shape = (PrimitiveSpawnShapeValue)value;
/* 104 */     this.edges = shape.edges;
/* 105 */     this.spawnWidthValue.load(shape.spawnWidthValue);
/* 106 */     this.spawnHeightValue.load(shape.spawnHeightValue);
/* 107 */     this.spawnDepthValue.load(shape.spawnDepthValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 112 */     super.write(json);
/* 113 */     json.writeValue("spawnWidthValue", this.spawnWidthValue);
/* 114 */     json.writeValue("spawnHeightValue", this.spawnHeightValue);
/* 115 */     json.writeValue("spawnDepthValue", this.spawnDepthValue);
/* 116 */     json.writeValue("edges", Boolean.valueOf(this.edges));
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 121 */     super.read(json, jsonData);
/* 122 */     this.spawnWidthValue = (ScaledNumericValue)json.readValue("spawnWidthValue", ScaledNumericValue.class, jsonData);
/* 123 */     this.spawnHeightValue = (ScaledNumericValue)json.readValue("spawnHeightValue", ScaledNumericValue.class, jsonData);
/* 124 */     this.spawnDepthValue = (ScaledNumericValue)json.readValue("spawnDepthValue", ScaledNumericValue.class, jsonData);
/* 125 */     this.edges = ((Boolean)json.readValue("edges", boolean.class, jsonData)).booleanValue();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\PrimitiveSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */