/*     */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*     */ 
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
/*     */ public class GradientColorValue
/*     */   extends ParticleValue
/*     */ {
/*  25 */   private static float[] temp = new float[3];
/*     */   
/*  27 */   private float[] colors = new float[] { 1.0F, 1.0F, 1.0F };
/*  28 */   public float[] timeline = new float[] { 0.0F };
/*     */   
/*     */   public float[] getTimeline() {
/*  31 */     return this.timeline;
/*     */   }
/*     */   
/*     */   public void setTimeline(float[] timeline) {
/*  35 */     this.timeline = timeline;
/*     */   }
/*     */   
/*     */   public float[] getColors() {
/*  39 */     return this.colors;
/*     */   }
/*     */   
/*     */   public void setColors(float[] colors) {
/*  43 */     this.colors = colors;
/*     */   }
/*     */   
/*     */   public float[] getColor(float percent) {
/*  47 */     getColor(percent, temp, 0);
/*  48 */     return temp;
/*     */   }
/*     */   
/*     */   public void getColor(float percent, float[] out, int index) {
/*  52 */     int startIndex = 0, endIndex = -1;
/*  53 */     float[] timeline = this.timeline;
/*  54 */     int n = timeline.length;
/*  55 */     for (int i = 1; i < n; i++) {
/*  56 */       float t = timeline[i];
/*  57 */       if (t > percent) {
/*  58 */         endIndex = i;
/*     */         break;
/*     */       } 
/*  61 */       startIndex = i;
/*     */     } 
/*  63 */     float startTime = timeline[startIndex];
/*  64 */     startIndex *= 3;
/*  65 */     float r1 = this.colors[startIndex];
/*  66 */     float g1 = this.colors[startIndex + 1];
/*  67 */     float b1 = this.colors[startIndex + 2];
/*  68 */     if (endIndex == -1) {
/*  69 */       out[index] = r1;
/*  70 */       out[index + 1] = g1;
/*  71 */       out[index + 2] = b1;
/*     */       return;
/*     */     } 
/*  74 */     float factor = (percent - startTime) / (timeline[endIndex] - startTime);
/*  75 */     endIndex *= 3;
/*  76 */     out[index] = r1 + (this.colors[endIndex] - r1) * factor;
/*  77 */     out[index + 1] = g1 + (this.colors[endIndex + 1] - g1) * factor;
/*  78 */     out[index + 2] = b1 + (this.colors[endIndex + 2] - b1) * factor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/*  83 */     super.write(json);
/*  84 */     json.writeValue("colors", this.colors);
/*  85 */     json.writeValue("timeline", this.timeline);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonData) {
/*  90 */     super.read(json, jsonData);
/*  91 */     this.colors = (float[])json.readValue("colors", float[].class, jsonData);
/*  92 */     this.timeline = (float[])json.readValue("timeline", float[].class, jsonData);
/*     */   }
/*     */   
/*     */   public void load(GradientColorValue value) {
/*  96 */     load(value);
/*  97 */     this.colors = new float[value.colors.length];
/*  98 */     System.arraycopy(value.colors, 0, this.colors, 0, this.colors.length);
/*  99 */     this.timeline = new float[value.timeline.length];
/* 100 */     System.arraycopy(value.timeline, 0, this.timeline, 0, this.timeline.length);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\GradientColorValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */