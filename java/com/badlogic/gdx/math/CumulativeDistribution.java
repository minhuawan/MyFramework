/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CumulativeDistribution<T>
/*     */ {
/*     */   public class CumulativeValue
/*     */   {
/*     */     public T value;
/*     */     public float frequency;
/*     */     public float interval;
/*     */     
/*     */     public CumulativeValue(T value, float frequency, float interval) {
/*  20 */       this.value = value;
/*  21 */       this.frequency = frequency;
/*  22 */       this.interval = interval;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  28 */   private Array<CumulativeValue> values = new Array(false, 10, CumulativeValue.class);
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(T value, float intervalSize) {
/*  33 */     this.values.add(new CumulativeValue(value, 0.0F, intervalSize));
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(T value) {
/*  38 */     this.values.add(new CumulativeValue(value, 0.0F, 0.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate() {
/*  43 */     float sum = 0.0F;
/*  44 */     for (int i = 0; i < this.values.size; i++) {
/*  45 */       sum += (((CumulativeValue[])this.values.items)[i]).interval;
/*  46 */       (((CumulativeValue[])this.values.items)[i]).frequency = sum;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateNormalized() {
/*  52 */     float sum = 0.0F;
/*  53 */     for (int i = 0; i < this.values.size; i++) {
/*  54 */       sum += (((CumulativeValue[])this.values.items)[i]).interval;
/*     */     }
/*  56 */     float intervalSum = 0.0F;
/*  57 */     for (int j = 0; j < this.values.size; j++) {
/*  58 */       intervalSum += (((CumulativeValue[])this.values.items)[j]).interval / sum;
/*  59 */       (((CumulativeValue[])this.values.items)[j]).frequency = intervalSum;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateUniform() {
/*  65 */     float freq = 1.0F / this.values.size;
/*  66 */     for (int i = 0; i < this.values.size; i++) {
/*     */       
/*  68 */       (((CumulativeValue[])this.values.items)[i]).interval = freq;
/*  69 */       (((CumulativeValue[])this.values.items)[i]).frequency = (i + 1) * freq;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T value(float probability) {
/*  79 */     CumulativeValue value = null;
/*  80 */     int imax = this.values.size - 1, imin = 0;
/*  81 */     while (imin <= imax) {
/*  82 */       int imid = imin + (imax - imin) / 2;
/*  83 */       value = ((CumulativeValue[])this.values.items)[imid];
/*  84 */       if (probability < value.frequency) {
/*  85 */         imax = imid - 1; continue;
/*  86 */       }  if (probability > value.frequency) {
/*  87 */         imin = imid + 1;
/*     */       }
/*     */     } 
/*     */     
/*  91 */     return (((CumulativeValue[])this.values.items)[imin]).value;
/*     */   }
/*     */ 
/*     */   
/*     */   public T value() {
/*  96 */     return value(MathUtils.random());
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 101 */     return this.values.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getInterval(int index) {
/* 106 */     return (((CumulativeValue[])this.values.items)[index]).interval;
/*     */   }
/*     */ 
/*     */   
/*     */   public T getValue(int index) {
/* 111 */     return (((CumulativeValue[])this.values.items)[index]).value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(T obj, float intervalSize) {
/* 117 */     for (CumulativeValue value : this.values) {
/* 118 */       if (value.value == obj) {
/* 119 */         value.interval = intervalSize;
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setInterval(int index, float intervalSize) {
/* 126 */     (((CumulativeValue[])this.values.items)[index]).interval = intervalSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 131 */     this.values.clear();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\CumulativeDistribution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */