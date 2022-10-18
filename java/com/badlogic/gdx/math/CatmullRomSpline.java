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
/*     */ public class CatmullRomSpline<T extends Vector<T>>
/*     */   implements Path<T>
/*     */ {
/*     */   public T[] controlPoints;
/*     */   public boolean continuous;
/*     */   public int spanCount;
/*     */   private T tmp;
/*     */   private T tmp2;
/*     */   private T tmp3;
/*     */   
/*     */   public static <T extends Vector<T>> T calculate(T out, float t, T[] points, boolean continuous, T tmp) {
/*  30 */     int n = continuous ? points.length : (points.length - 3);
/*  31 */     float u = t * n;
/*  32 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/*  33 */     u -= i;
/*  34 */     return calculate(out, i, u, points, continuous, tmp);
/*     */   }
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
/*     */   public static <T extends Vector<T>> T calculate(T out, int i, float u, T[] points, boolean continuous, T tmp) {
/*  47 */     int n = points.length;
/*  48 */     float u2 = u * u;
/*  49 */     float u3 = u2 * u;
/*  50 */     out.set(points[i]).scl(1.5F * u3 - 2.5F * u2 + 1.0F);
/*  51 */     if (continuous || i > 0) out.add(tmp.set(points[(n + i - 1) % n]).scl(-0.5F * u3 + u2 - 0.5F * u)); 
/*  52 */     if (continuous || i < n - 1) out.add(tmp.set(points[(i + 1) % n]).scl(-1.5F * u3 + 2.0F * u2 + 0.5F * u)); 
/*  53 */     if (continuous || i < n - 2) out.add(tmp.set(points[(i + 2) % n]).scl(0.5F * u3 - 0.5F * u2)); 
/*  54 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Vector<T>> T derivative(T out, float t, T[] points, boolean continuous, T tmp) {
/*  66 */     int n = continuous ? points.length : (points.length - 3);
/*  67 */     float u = t * n;
/*  68 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/*  69 */     u -= i;
/*  70 */     return derivative(out, i, u, points, continuous, tmp);
/*     */   }
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
/*     */   public static <T extends Vector<T>> T derivative(T out, int i, float u, T[] points, boolean continuous, T tmp) {
/*  86 */     int n = points.length;
/*  87 */     float u2 = u * u;
/*     */     
/*  89 */     out.set(points[i]).scl(-u * 5.0F + u2 * 4.5F);
/*  90 */     if (continuous || i > 0) out.add(tmp.set(points[(n + i - 1) % n]).scl(-0.5F + u * 2.0F - u2 * 1.5F)); 
/*  91 */     if (continuous || i < n - 1) out.add(tmp.set(points[(i + 1) % n]).scl(0.5F + u * 4.0F - u2 * 4.5F)); 
/*  92 */     if (continuous || i < n - 2) out.add(tmp.set(points[(i + 2) % n]).scl(-u + u2 * 1.5F)); 
/*  93 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CatmullRomSpline() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CatmullRomSpline(T[] controlPoints, boolean continuous) {
/* 107 */     set(controlPoints, continuous);
/*     */   }
/*     */   
/*     */   public CatmullRomSpline set(T[] controlPoints, boolean continuous) {
/* 111 */     if (this.tmp == null) this.tmp = controlPoints[0].cpy(); 
/* 112 */     if (this.tmp2 == null) this.tmp2 = controlPoints[0].cpy(); 
/* 113 */     if (this.tmp3 == null) this.tmp3 = controlPoints[0].cpy(); 
/* 114 */     this.controlPoints = controlPoints;
/* 115 */     this.continuous = continuous;
/* 116 */     this.spanCount = continuous ? controlPoints.length : (controlPoints.length - 3);
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public T valueAt(T out, float t) {
/* 122 */     int n = this.spanCount;
/* 123 */     float u = t * n;
/* 124 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/* 125 */     u -= i;
/* 126 */     return valueAt(out, i, u);
/*     */   }
/*     */ 
/*     */   
/*     */   public T valueAt(T out, int span, float u) {
/* 131 */     return calculate(out, this.continuous ? span : (span + 1), u, this.controlPoints, this.continuous, this.tmp);
/*     */   }
/*     */ 
/*     */   
/*     */   public T derivativeAt(T out, float t) {
/* 136 */     int n = this.spanCount;
/* 137 */     float u = t * n;
/* 138 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/* 139 */     u -= i;
/* 140 */     return derivativeAt(out, i, u);
/*     */   }
/*     */ 
/*     */   
/*     */   public T derivativeAt(T out, int span, float u) {
/* 145 */     return derivative(out, this.continuous ? span : (span + 1), u, this.controlPoints, this.continuous, this.tmp);
/*     */   }
/*     */ 
/*     */   
/*     */   public int nearest(T in) {
/* 150 */     return nearest(in, 0, this.spanCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public int nearest(T in, int start, int count) {
/* 155 */     while (start < 0)
/* 156 */       start += this.spanCount; 
/* 157 */     int result = start % this.spanCount;
/* 158 */     float dst = in.dst2(this.controlPoints[result]);
/* 159 */     for (int i = 1; i < count; i++) {
/* 160 */       int idx = (start + i) % this.spanCount;
/* 161 */       float d = in.dst2(this.controlPoints[idx]);
/* 162 */       if (d < dst) {
/* 163 */         dst = d;
/* 164 */         result = idx;
/*     */       } 
/*     */     } 
/* 167 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public float approximate(T v) {
/* 172 */     return approximate(v, nearest(v));
/*     */   }
/*     */   
/*     */   public float approximate(T in, int start, int count) {
/* 176 */     return approximate(in, nearest(in, start, count));
/*     */   }
/*     */   public float approximate(T in, int near) {
/*     */     T P1, P2, P3;
/* 180 */     int n = near;
/* 181 */     T nearest = this.controlPoints[n];
/* 182 */     T previous = this.controlPoints[(n > 0) ? (n - 1) : (this.spanCount - 1)];
/* 183 */     T next = this.controlPoints[(n + 1) % this.spanCount];
/* 184 */     float dstPrev2 = in.dst2(previous);
/* 185 */     float dstNext2 = in.dst2(next);
/*     */     
/* 187 */     if (dstNext2 < dstPrev2) {
/* 188 */       P1 = nearest;
/* 189 */       P2 = next;
/* 190 */       P3 = in;
/*     */     } else {
/* 192 */       P1 = previous;
/* 193 */       P2 = nearest;
/* 194 */       P3 = in;
/* 195 */       n = (n > 0) ? (n - 1) : (this.spanCount - 1);
/*     */     } 
/* 197 */     float L1Sqr = P1.dst2(P2);
/* 198 */     float L2Sqr = P3.dst2(P2);
/* 199 */     float L3Sqr = P3.dst2(P1);
/* 200 */     float L1 = (float)Math.sqrt(L1Sqr);
/* 201 */     float s = (L2Sqr + L1Sqr - L3Sqr) / 2.0F * L1;
/* 202 */     float u = MathUtils.clamp((L1 - s) / L1, 0.0F, 1.0F);
/* 203 */     return (n + u) / this.spanCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public float locate(T v) {
/* 208 */     return approximate(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public float approxLength(int samples) {
/* 213 */     float tempLength = 0.0F;
/* 214 */     for (int i = 0; i < samples; i++) {
/* 215 */       this.tmp2.set(this.tmp3);
/* 216 */       valueAt(this.tmp3, i / (samples - 1.0F));
/* 217 */       if (i > 0) tempLength += this.tmp2.dst(this.tmp3); 
/*     */     } 
/* 219 */     return tempLength;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\CatmullRomSpline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */