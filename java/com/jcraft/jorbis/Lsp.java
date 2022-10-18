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
/*     */ class Lsp
/*     */ {
/*     */   static final float M_PI = 3.1415927F;
/*     */   
/*     */   static void lsp_to_curve(float[] curve, int[] map, int n, int ln, float[] lsp, int m, float amp, float ampoffset) {
/*  46 */     float wdel = 3.1415927F / ln; int i;
/*  47 */     for (i = 0; i < m; i++)
/*  48 */       lsp[i] = Lookup.coslook(lsp[i]); 
/*  49 */     int m2 = m / 2 * 2;
/*     */     
/*  51 */     i = 0;
/*  52 */     label35: while (i < n) {
/*  53 */       int k = map[i];
/*  54 */       float p = 0.70710677F;
/*  55 */       float q = 0.70710677F;
/*  56 */       float w = Lookup.coslook(wdel * k);
/*     */       
/*  58 */       for (int j = 0; j < m2; j += 2) {
/*  59 */         q *= lsp[j] - w;
/*  60 */         p *= lsp[j + 1] - w;
/*     */       } 
/*     */       
/*  63 */       if ((m & 0x1) != 0) {
/*     */ 
/*     */         
/*  66 */         q *= lsp[m - 1] - w;
/*  67 */         q *= q;
/*  68 */         p *= p * (1.0F - w * w);
/*     */       }
/*     */       else {
/*     */         
/*  72 */         q *= q * (1.0F + w);
/*  73 */         p *= p * (1.0F - w);
/*     */       } 
/*     */ 
/*     */       
/*  77 */       q = p + q;
/*  78 */       int hx = Float.floatToIntBits(q);
/*  79 */       int ix = Integer.MAX_VALUE & hx;
/*  80 */       int qexp = 0;
/*     */       
/*  82 */       if (ix < 2139095040 && ix != 0) {
/*     */ 
/*     */ 
/*     */         
/*  86 */         if (ix < 8388608) {
/*  87 */           q = (float)(q * 3.3554432E7D);
/*  88 */           hx = Float.floatToIntBits(q);
/*  89 */           ix = Integer.MAX_VALUE & hx;
/*  90 */           qexp = -25;
/*     */         } 
/*  92 */         qexp += (ix >>> 23) - 126;
/*  93 */         hx = hx & 0x807FFFFF | 0x3F000000;
/*  94 */         q = Float.intBitsToFloat(hx);
/*     */       } 
/*     */       
/*  97 */       q = Lookup.fromdBlook(amp * Lookup.invsqlook(q) * Lookup.invsq2explook(qexp + m) - ampoffset);
/*     */ 
/*     */       
/*     */       while (true) {
/* 101 */         curve[i++] = curve[i++] * q;
/*     */         
/* 103 */         if (i < n) { if (map[i] != k)
/*     */             continue label35; 
/*     */           continue; }
/*     */         
/*     */         continue label35;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\jcraft\jorbis\Lsp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */