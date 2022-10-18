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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WindowedMean
/*     */ {
/*     */   float[] values;
/*  26 */   int added_values = 0;
/*     */   int last_value;
/*  28 */   float mean = 0.0F;
/*     */ 
/*     */   
/*     */   boolean dirty = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public WindowedMean(int window_size) {
/*  36 */     this.values = new float[window_size];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEnoughData() {
/*  41 */     return (this.added_values >= this.values.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  46 */     this.added_values = 0;
/*  47 */     this.last_value = 0;
/*  48 */     for (int i = 0; i < this.values.length; i++)
/*  49 */       this.values[i] = 0.0F; 
/*  50 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addValue(float value) {
/*  57 */     if (this.added_values < this.values.length)
/*  58 */       this.added_values++; 
/*  59 */     this.values[this.last_value++] = value;
/*  60 */     if (this.last_value > this.values.length - 1) this.last_value = 0; 
/*  61 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getMean() {
/*  68 */     if (hasEnoughData()) {
/*  69 */       if (this.dirty == true) {
/*  70 */         float mean = 0.0F;
/*  71 */         for (int i = 0; i < this.values.length; i++) {
/*  72 */           mean += this.values[i];
/*     */         }
/*  74 */         this.mean = mean / this.values.length;
/*  75 */         this.dirty = false;
/*     */       } 
/*  77 */       return this.mean;
/*     */     } 
/*  79 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOldest() {
/*  84 */     return (this.last_value == this.values.length - 1) ? this.values[0] : this.values[this.last_value + 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLatest() {
/*  89 */     return this.values[(this.last_value - 1 == -1) ? (this.values.length - 1) : (this.last_value - 1)];
/*     */   }
/*     */ 
/*     */   
/*     */   public float standardDeviation() {
/*  94 */     if (!hasEnoughData()) return 0.0F;
/*     */     
/*  96 */     float mean = getMean();
/*  97 */     float sum = 0.0F;
/*  98 */     for (int i = 0; i < this.values.length; i++) {
/*  99 */       sum += (this.values[i] - mean) * (this.values[i] - mean);
/*     */     }
/*     */     
/* 102 */     return (float)Math.sqrt((sum / this.values.length));
/*     */   }
/*     */   
/*     */   public int getWindowSize() {
/* 106 */     return this.values.length;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\WindowedMean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */