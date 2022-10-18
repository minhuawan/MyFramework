/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderData;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public abstract class ParticleSorter
/*     */ {
/*     */   protected Camera camera;
/*  28 */   static final Vector3 TMP_V1 = new Vector3();
/*     */   
/*     */   public abstract <T extends ParticleControllerRenderData> int[] sort(Array<T> paramArray);
/*     */   
/*  32 */   public static class None extends ParticleSorter { int currentCapacity = 0;
/*     */     
/*     */     int[] indices;
/*     */     
/*     */     public void ensureCapacity(int capacity) {
/*  37 */       if (this.currentCapacity < capacity) {
/*  38 */         this.indices = new int[capacity];
/*  39 */         for (int i = 0; i < capacity; i++)
/*  40 */           this.indices[i] = i; 
/*  41 */         this.currentCapacity = capacity;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends ParticleControllerRenderData> int[] sort(Array<T> renderData) {
/*  47 */       return this.indices;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class Distance extends ParticleSorter {
/*     */     private float[] distances;
/*     */     private int[] particleIndices;
/*     */     private int[] particleOffsets;
/*  55 */     private int currentSize = 0;
/*     */ 
/*     */     
/*     */     public void ensureCapacity(int capacity) {
/*  59 */       if (this.currentSize < capacity) {
/*  60 */         this.distances = new float[capacity];
/*  61 */         this.particleIndices = new int[capacity];
/*  62 */         this.particleOffsets = new int[capacity];
/*  63 */         this.currentSize = capacity;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public <T extends ParticleControllerRenderData> int[] sort(Array<T> renderData) {
/*  69 */       float[] val = this.camera.view.val;
/*  70 */       float cx = val[2], cy = val[6], cz = val[10];
/*  71 */       int count = 0, i = 0;
/*  72 */       for (ParticleControllerRenderData data : renderData) {
/*  73 */         int k; int c; for (k = 0, c = i + data.controller.particles.size; i < c; i++, k += data.positionChannel.strideSize) {
/*  74 */           this.distances[i] = cx * data.positionChannel.data[k + 0] + cy * data.positionChannel.data[k + 1] + cz * data.positionChannel.data[k + 2];
/*     */ 
/*     */           
/*  77 */           this.particleIndices[i] = i;
/*     */         } 
/*  79 */         count += data.controller.particles.size;
/*     */       } 
/*     */       
/*  82 */       qsort(0, count - 1);
/*     */       
/*  84 */       for (i = 0; i < count; i++) {
/*  85 */         this.particleOffsets[this.particleIndices[i]] = i;
/*     */       }
/*  87 */       return this.particleOffsets;
/*     */     }
/*     */ 
/*     */     
/*     */     public void qsort(int si, int ei) {
/*  92 */       if (si < ei) {
/*     */ 
/*     */ 
/*     */         
/*  96 */         if (ei - si <= 8) {
/*  97 */           for (int k = si; k <= ei; k++) {
/*  98 */             for (int m = k; m > si && this.distances[m - 1] > this.distances[m]; m--) {
/*  99 */               float tmp = this.distances[m];
/* 100 */               this.distances[m] = this.distances[m - 1];
/* 101 */               this.distances[m - 1] = tmp;
/*     */ 
/*     */               
/* 104 */               int tmpIndex = this.particleIndices[m];
/* 105 */               this.particleIndices[m] = this.particleIndices[m - 1];
/* 106 */               this.particleIndices[m - 1] = tmpIndex;
/*     */             } 
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 112 */         float pivot = this.distances[si];
/* 113 */         int i = si + 1;
/* 114 */         int particlesPivotIndex = this.particleIndices[si];
/*     */ 
/*     */         
/* 117 */         for (int j = si + 1; j <= ei; j++) {
/* 118 */           if (pivot > this.distances[j]) {
/* 119 */             if (j > i) {
/*     */               
/* 121 */               float tmp = this.distances[j];
/* 122 */               this.distances[j] = this.distances[i];
/* 123 */               this.distances[i] = tmp;
/*     */ 
/*     */               
/* 126 */               int tmpIndex = this.particleIndices[j];
/* 127 */               this.particleIndices[j] = this.particleIndices[i];
/* 128 */               this.particleIndices[i] = tmpIndex;
/*     */             } 
/* 130 */             i++;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 135 */         this.distances[si] = this.distances[i - 1];
/* 136 */         this.distances[i - 1] = pivot;
/* 137 */         this.particleIndices[si] = this.particleIndices[i - 1];
/* 138 */         this.particleIndices[i - 1] = particlesPivotIndex;
/*     */ 
/*     */         
/* 141 */         qsort(si, i - 2);
/* 142 */         qsort(i, ei);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCamera(Camera camera) {
/* 154 */     this.camera = camera;
/*     */   }
/*     */   
/*     */   public void ensureCapacity(int capacity) {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\ParticleSorter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */