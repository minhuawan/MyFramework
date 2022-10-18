/*     */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*     */ 
/*     */ import com.badlogic.gdx.math.MathUtils;
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
/*     */ public class ScaledNumericValue
/*     */   extends RangedNumericValue
/*     */ {
/*  26 */   private float[] scaling = new float[] { 1.0F };
/*  27 */   public float[] timeline = new float[] { 0.0F };
/*     */   
/*     */   private float highMin;
/*     */   
/*     */   public float newHighValue() {
/*  32 */     return this.highMin + (this.highMax - this.highMin) * MathUtils.random();
/*     */   }
/*     */   private float highMax; private boolean relative = false;
/*     */   public void setHigh(float value) {
/*  36 */     this.highMin = value;
/*  37 */     this.highMax = value;
/*     */   }
/*     */   
/*     */   public void setHigh(float min, float max) {
/*  41 */     this.highMin = min;
/*  42 */     this.highMax = max;
/*     */   }
/*     */   
/*     */   public float getHighMin() {
/*  46 */     return this.highMin;
/*     */   }
/*     */   
/*     */   public void setHighMin(float highMin) {
/*  50 */     this.highMin = highMin;
/*     */   }
/*     */   
/*     */   public float getHighMax() {
/*  54 */     return this.highMax;
/*     */   }
/*     */   
/*     */   public void setHighMax(float highMax) {
/*  58 */     this.highMax = highMax;
/*     */   }
/*     */   
/*     */   public float[] getScaling() {
/*  62 */     return this.scaling;
/*     */   }
/*     */   
/*     */   public void setScaling(float[] values) {
/*  66 */     this.scaling = values;
/*     */   }
/*     */   
/*     */   public float[] getTimeline() {
/*  70 */     return this.timeline;
/*     */   }
/*     */   
/*     */   public void setTimeline(float[] timeline) {
/*  74 */     this.timeline = timeline;
/*     */   }
/*     */   
/*     */   public boolean isRelative() {
/*  78 */     return this.relative;
/*     */   }
/*     */   
/*     */   public void setRelative(boolean relative) {
/*  82 */     this.relative = relative;
/*     */   }
/*     */   
/*     */   public float getScale(float percent) {
/*  86 */     int endIndex = -1;
/*  87 */     int n = this.timeline.length;
/*     */ 
/*     */     
/*  90 */     for (int i = 1; i < n; i++) {
/*  91 */       float t = this.timeline[i];
/*  92 */       if (t > percent) {
/*  93 */         endIndex = i;
/*     */         break;
/*     */       } 
/*     */     } 
/*  97 */     if (endIndex == -1) return this.scaling[n - 1]; 
/*  98 */     int startIndex = endIndex - 1;
/*  99 */     float startValue = this.scaling[startIndex];
/* 100 */     float startTime = this.timeline[startIndex];
/* 101 */     return startValue + (this.scaling[endIndex] - startValue) * (percent - startTime) / (this.timeline[endIndex] - startTime);
/*     */   }
/*     */   
/*     */   public void load(ScaledNumericValue value) {
/* 105 */     load(value);
/* 106 */     this.highMax = value.highMax;
/* 107 */     this.highMin = value.highMin;
/* 108 */     this.scaling = new float[value.scaling.length];
/* 109 */     System.arraycopy(value.scaling, 0, this.scaling, 0, this.scaling.length);
/* 110 */     this.timeline = new float[value.timeline.length];
/* 111 */     System.arraycopy(value.timeline, 0, this.timeline, 0, this.timeline.length);
/* 112 */     this.relative = value.relative;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 117 */     super.write(json);
/* 118 */     json.writeValue("highMin", Float.valueOf(this.highMin));
/* 119 */     json.writeValue("highMax", Float.valueOf(this.highMax));
/* 120 */     json.writeValue("relative", Boolean.valueOf(this.relative));
/* 121 */     json.writeValue("scaling", this.scaling);
/* 122 */     json.writeValue("timeline", this.timeline);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/* 127 */     super.read(json, jsonData);
/* 128 */     this.highMin = ((Float)json.readValue("highMin", float.class, jsonData)).floatValue();
/* 129 */     this.highMax = ((Float)json.readValue("highMax", float.class, jsonData)).floatValue();
/* 130 */     this.relative = ((Boolean)json.readValue("relative", boolean.class, jsonData)).booleanValue();
/* 131 */     this.scaling = (float[])json.readValue("scaling", float[].class, jsonData);
/* 132 */     this.timeline = (float[])json.readValue("timeline", float[].class, jsonData);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\ScaledNumericValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */