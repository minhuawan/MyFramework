/*     */ package com.badlogic.gdx.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.math.FloatCounter;
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
/*     */ public class PerformanceCounter
/*     */ {
/*     */   private static final float nano2seconds = 1.0E-9F;
/*  30 */   private long startTime = 0L;
/*  31 */   private long lastTick = 0L;
/*     */ 
/*     */   
/*     */   public final FloatCounter time;
/*     */ 
/*     */   
/*     */   public final FloatCounter load;
/*     */   
/*     */   public final String name;
/*     */   
/*  41 */   public float current = 0.0F;
/*     */   
/*     */   public boolean valid = false;
/*     */   
/*     */   public PerformanceCounter(String name) {
/*  46 */     this(name, 5);
/*     */   }
/*     */   
/*     */   public PerformanceCounter(String name, int windowSize) {
/*  50 */     this.name = name;
/*  51 */     this.time = new FloatCounter(windowSize);
/*  52 */     this.load = new FloatCounter(1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  58 */     long t = TimeUtils.nanoTime();
/*  59 */     if (this.lastTick > 0L) tick((float)(t - this.lastTick) * 1.0E-9F); 
/*  60 */     this.lastTick = t;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick(float delta) {
/*  66 */     if (!this.valid) {
/*  67 */       Gdx.app.error("PerformanceCounter", "Invalid data, check if you called PerformanceCounter#stop()");
/*     */       
/*     */       return;
/*     */     } 
/*  71 */     this.time.put(this.current);
/*     */     
/*  73 */     float currentLoad = (delta == 0.0F) ? 0.0F : (this.current / delta);
/*  74 */     this.load.put((delta > 1.0F) ? currentLoad : (delta * currentLoad + (1.0F - delta) * this.load.latest));
/*     */     
/*  76 */     this.current = 0.0F;
/*  77 */     this.valid = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  82 */     this.startTime = TimeUtils.nanoTime();
/*  83 */     this.valid = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  89 */     if (this.startTime > 0L) {
/*  90 */       this.current += (float)(TimeUtils.nanoTime() - this.startTime) * 1.0E-9F;
/*  91 */       this.startTime = 0L;
/*  92 */       this.valid = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  98 */     this.time.reset();
/*  99 */     this.load.reset();
/* 100 */     this.startTime = 0L;
/* 101 */     this.lastTick = 0L;
/* 102 */     this.current = 0.0F;
/* 103 */     this.valid = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 109 */     StringBuilder sb = new StringBuilder();
/* 110 */     return toString(sb).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public StringBuilder toString(StringBuilder sb) {
/* 115 */     sb.append(this.name).append(": [time: ").append(this.time.value).append(", load: ").append(this.load.value).append("]");
/* 116 */     return sb;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\PerformanceCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */