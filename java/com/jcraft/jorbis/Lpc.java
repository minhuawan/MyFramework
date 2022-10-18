/*     */ package com.jcraft.jorbis;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Lpc
/*     */ {
/*  31 */   Drft fft = new Drft();
/*     */ 
/*     */ 
/*     */   
/*     */   int ln;
/*     */ 
/*     */   
/*     */   int m;
/*     */ 
/*     */ 
/*     */   
/*     */   static float lpc_from_data(float[] data, float[] lpc, int n, int m) {
/*  43 */     float[] aut = new float[m + 1];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     int j = m + 1;
/*  50 */     while (j-- != 0) {
/*  51 */       float d = 0.0F;
/*  52 */       for (int k = j; k < n; k++)
/*  53 */         d += data[k] * data[k - j]; 
/*  54 */       aut[j] = d;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  59 */     float error = aut[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     for (int i = 0; i < m; i++) {
/*  68 */       float r = -aut[i + 1];
/*     */       
/*  70 */       if (error == 0.0F) {
/*  71 */         for (int k = 0; k < m; k++)
/*  72 */           lpc[k] = 0.0F; 
/*  73 */         return 0.0F;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  81 */       for (j = 0; j < i; j++)
/*  82 */         r -= lpc[j] * aut[i - j]; 
/*  83 */       r /= error;
/*     */ 
/*     */ 
/*     */       
/*  87 */       lpc[i] = r;
/*  88 */       for (j = 0; j < i / 2; j++) {
/*  89 */         float tmp = lpc[j];
/*  90 */         lpc[j] = lpc[j] + r * lpc[i - 1 - j];
/*  91 */         lpc[i - 1 - j] = lpc[i - 1 - j] + r * tmp;
/*     */       } 
/*  93 */       if (i % 2 != 0) {
/*  94 */         lpc[j] = lpc[j] + lpc[j] * r;
/*     */       }
/*  96 */       error = (float)(error * (1.0D - (r * r)));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     return error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float lpc_from_curve(float[] curve, float[] lpc) {
/* 109 */     int n = this.ln;
/* 110 */     float[] work = new float[n + n];
/* 111 */     float fscale = (float)(0.5D / n);
/*     */ 
/*     */     
/*     */     int i;
/*     */     
/* 116 */     for (i = 0; i < n; i++) {
/* 117 */       work[i * 2] = curve[i] * fscale;
/* 118 */       work[i * 2 + 1] = 0.0F;
/*     */     } 
/* 120 */     work[n * 2 - 1] = curve[n - 1] * fscale;
/*     */     
/* 122 */     n *= 2;
/* 123 */     this.fft.backward(work);
/*     */ 
/*     */     
/*     */     int j;
/*     */     
/* 128 */     for (i = 0, j = n / 2; i < n / 2; ) {
/* 129 */       float temp = work[i];
/* 130 */       work[i++] = work[j];
/* 131 */       work[j++] = temp;
/*     */     } 
/*     */     
/* 134 */     return lpc_from_data(work, lpc, n, this.m);
/*     */   }
/*     */   
/*     */   void init(int mapped, int m) {
/* 138 */     this.ln = mapped;
/* 139 */     this.m = m;
/*     */ 
/*     */     
/* 142 */     this.fft.init(mapped * 2);
/*     */   }
/*     */   
/*     */   void clear() {
/* 146 */     this.fft.clear();
/*     */   }
/*     */   
/*     */   static float FAST_HYPOT(float a, float b) {
/* 150 */     return (float)Math.sqrt((a * a + b * b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void lpc_to_curve(float[] curve, float[] lpc, float amp) {
/*     */     int i;
/* 162 */     for (i = 0; i < this.ln * 2; i++) {
/* 163 */       curve[i] = 0.0F;
/*     */     }
/* 165 */     if (amp == 0.0F) {
/*     */       return;
/*     */     }
/* 168 */     for (i = 0; i < this.m; i++) {
/* 169 */       curve[i * 2 + 1] = lpc[i] / 4.0F * amp;
/* 170 */       curve[i * 2 + 2] = -lpc[i] / 4.0F * amp;
/*     */     } 
/*     */     
/* 173 */     this.fft.backward(curve);
/*     */ 
/*     */     
/* 176 */     int l2 = this.ln * 2;
/* 177 */     float unit = (float)(1.0D / amp);
/* 178 */     curve[0] = (float)(1.0D / (curve[0] * 2.0F + unit));
/* 179 */     for (int j = 1; j < this.ln; j++) {
/* 180 */       float real = curve[j] + curve[l2 - j];
/* 181 */       float imag = curve[j] - curve[l2 - j];
/*     */       
/* 183 */       float a = real + unit;
/* 184 */       curve[j] = (float)(1.0D / FAST_HYPOT(a, imag));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\jcraft\jorbis\Lpc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */