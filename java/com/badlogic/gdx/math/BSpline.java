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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BSpline<T extends Vector<T>>
/*     */   implements Path<T>
/*     */ {
/*     */   private static final float d6 = 0.16666667F;
/*     */   public T[] controlPoints;
/*     */   public Array<T> knots;
/*     */   public int degree;
/*     */   public boolean continuous;
/*     */   public int spanCount;
/*     */   private T tmp;
/*     */   private T tmp2;
/*     */   private T tmp3;
/*     */   
/*     */   public static <T extends Vector<T>> T cubic(T out, float t, T[] points, boolean continuous, T tmp) {
/*  34 */     int n = continuous ? points.length : (points.length - 3);
/*  35 */     float u = t * n;
/*  36 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/*  37 */     u -= i;
/*  38 */     return cubic(out, i, u, points, continuous, tmp);
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
/*     */   public static <T extends Vector<T>> T cubic_derivative(T out, float t, T[] points, boolean continuous, T tmp) {
/*  50 */     int n = continuous ? points.length : (points.length - 3);
/*  51 */     float u = t * n;
/*  52 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/*  53 */     u -= i;
/*  54 */     return cubic(out, i, u, points, continuous, tmp);
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
/*     */   public static <T extends Vector<T>> T cubic(T out, int i, float u, T[] points, boolean continuous, T tmp) {
/*  67 */     int n = points.length;
/*  68 */     float dt = 1.0F - u;
/*  69 */     float t2 = u * u;
/*  70 */     float t3 = t2 * u;
/*  71 */     out.set(points[i]).scl((3.0F * t3 - 6.0F * t2 + 4.0F) * 0.16666667F);
/*  72 */     if (continuous || i > 0) out.add(tmp.set(points[(n + i - 1) % n]).scl(dt * dt * dt * 0.16666667F)); 
/*  73 */     if (continuous || i < n - 1) out.add(tmp.set(points[(i + 1) % n]).scl((-3.0F * t3 + 3.0F * t2 + 3.0F * u + 1.0F) * 0.16666667F)); 
/*  74 */     if (continuous || i < n - 2) out.add(tmp.set(points[(i + 2) % n]).scl(t3 * 0.16666667F)); 
/*  75 */     return out;
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
/*     */   public static <T extends Vector<T>> T cubic_derivative(T out, int i, float u, T[] points, boolean continuous, T tmp) {
/*  88 */     int n = points.length;
/*  89 */     float dt = 1.0F - u;
/*  90 */     float t2 = u * u;
/*  91 */     float t3 = t2 * u;
/*  92 */     out.set(points[i]).scl(1.5F * t2 - 2.0F * u);
/*  93 */     if (continuous || i > 0) out.add(tmp.set(points[(n + i - 1) % n]).scl(-0.5F * dt * dt)); 
/*  94 */     if (continuous || i < n - 1) out.add(tmp.set(points[(i + 1) % n]).scl(-1.5F * t2 + u + 0.5F)); 
/*  95 */     if (continuous || i < n - 2) out.add(tmp.set(points[(i + 2) % n]).scl(0.5F * t2)); 
/*  96 */     return out;
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
/*     */   public static <T extends Vector<T>> T calculate(T out, float t, T[] points, int degree, boolean continuous, T tmp) {
/* 109 */     int n = continuous ? points.length : (points.length - degree);
/* 110 */     float u = t * n;
/* 111 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/* 112 */     u -= i;
/* 113 */     return calculate(out, i, u, points, degree, continuous, tmp);
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
/*     */   public static <T extends Vector<T>> T derivative(T out, float t, T[] points, int degree, boolean continuous, T tmp) {
/* 126 */     int n = continuous ? points.length : (points.length - degree);
/* 127 */     float u = t * n;
/* 128 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/* 129 */     u -= i;
/* 130 */     return derivative(out, i, u, points, degree, continuous, tmp);
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
/*     */   public static <T extends Vector<T>> T calculate(T out, int i, float u, T[] points, int degree, boolean continuous, T tmp) {
/* 144 */     switch (degree) {
/*     */       case 3:
/* 146 */         return cubic(out, i, u, points, continuous, tmp);
/*     */     } 
/* 148 */     return out;
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
/*     */   public static <T extends Vector<T>> T derivative(T out, int i, float u, T[] points, int degree, boolean continuous, T tmp) {
/* 162 */     switch (degree) {
/*     */       case 3:
/* 164 */         return cubic_derivative(out, i, u, points, continuous, tmp);
/*     */     } 
/* 166 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BSpline() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BSpline(T[] controlPoints, int degree, boolean continuous) {
/* 182 */     set(controlPoints, degree, continuous);
/*     */   }
/*     */   
/*     */   public BSpline set(T[] controlPoints, int degree, boolean continuous) {
/* 186 */     if (this.tmp == null) this.tmp = controlPoints[0].cpy(); 
/* 187 */     if (this.tmp2 == null) this.tmp2 = controlPoints[0].cpy(); 
/* 188 */     if (this.tmp3 == null) this.tmp3 = controlPoints[0].cpy(); 
/* 189 */     this.controlPoints = controlPoints;
/* 190 */     this.degree = degree;
/* 191 */     this.continuous = continuous;
/* 192 */     this.spanCount = continuous ? controlPoints.length : (controlPoints.length - degree);
/* 193 */     if (this.knots == null) {
/* 194 */       this.knots = new Array(this.spanCount);
/*     */     } else {
/* 196 */       this.knots.clear();
/* 197 */       this.knots.ensureCapacity(this.spanCount);
/*     */     } 
/* 199 */     for (int i = 0; i < this.spanCount; i++) {
/* 200 */       this.knots.add(calculate(controlPoints[0].cpy(), continuous ? i : (int)(i + 0.5F * degree), 0.0F, controlPoints, degree, continuous, this.tmp));
/*     */     }
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public T valueAt(T out, float t) {
/* 207 */     int n = this.spanCount;
/* 208 */     float u = t * n;
/* 209 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/* 210 */     u -= i;
/* 211 */     return valueAt(out, i, u);
/*     */   }
/*     */ 
/*     */   
/*     */   public T valueAt(T out, int span, float u) {
/* 216 */     return calculate(out, this.continuous ? span : (span + (int)(this.degree * 0.5F)), u, this.controlPoints, this.degree, this.continuous, this.tmp);
/*     */   }
/*     */ 
/*     */   
/*     */   public T derivativeAt(T out, float t) {
/* 221 */     int n = this.spanCount;
/* 222 */     float u = t * n;
/* 223 */     int i = (t >= 1.0F) ? (n - 1) : (int)u;
/* 224 */     u -= i;
/* 225 */     return derivativeAt(out, i, u);
/*     */   }
/*     */ 
/*     */   
/*     */   public T derivativeAt(T out, int span, float u) {
/* 230 */     return derivative(out, this.continuous ? span : (span + (int)(this.degree * 0.5F)), u, this.controlPoints, this.degree, this.continuous, this.tmp);
/*     */   }
/*     */ 
/*     */   
/*     */   public int nearest(T in) {
/* 235 */     return nearest(in, 0, this.spanCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public int nearest(T in, int start, int count) {
/* 240 */     while (start < 0)
/* 241 */       start += this.spanCount; 
/* 242 */     int result = start % this.spanCount;
/* 243 */     float dst = in.dst2((Vector)this.knots.get(result));
/* 244 */     for (int i = 1; i < count; i++) {
/* 245 */       int idx = (start + i) % this.spanCount;
/* 246 */       float d = in.dst2((Vector)this.knots.get(idx));
/* 247 */       if (d < dst) {
/* 248 */         dst = d;
/* 249 */         result = idx;
/*     */       } 
/*     */     } 
/* 252 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public float approximate(T v) {
/* 257 */     return approximate(v, nearest(v));
/*     */   }
/*     */   
/*     */   public float approximate(T in, int start, int count) {
/* 261 */     return approximate(in, nearest(in, start, count)); } public float approximate(T in, int near) {
/*     */     Vector<Vector> vector4;
/*     */     Vector vector5;
/*     */     T P3;
/* 265 */     int n = near;
/* 266 */     Vector vector1 = (Vector)this.knots.get(n);
/* 267 */     Vector<Vector> vector2 = (Vector)this.knots.get((n > 0) ? (n - 1) : (this.spanCount - 1));
/* 268 */     Vector vector3 = (Vector)this.knots.get((n + 1) % this.spanCount);
/* 269 */     float dstPrev2 = in.dst2(vector2);
/* 270 */     float dstNext2 = in.dst2(vector3);
/*     */     
/* 272 */     if (dstNext2 < dstPrev2) {
/* 273 */       vector4 = vector1;
/* 274 */       vector5 = vector3;
/* 275 */       P3 = in;
/*     */     } else {
/* 277 */       vector4 = vector2;
/* 278 */       vector5 = vector1;
/* 279 */       P3 = in;
/* 280 */       n = (n > 0) ? (n - 1) : (this.spanCount - 1);
/*     */     } 
/* 282 */     float L1Sqr = vector4.dst2(vector5);
/* 283 */     float L2Sqr = P3.dst2(vector5);
/* 284 */     float L3Sqr = P3.dst2(vector4);
/* 285 */     float L1 = (float)Math.sqrt(L1Sqr);
/* 286 */     float s = (L2Sqr + L1Sqr - L3Sqr) / 2.0F * L1;
/* 287 */     float u = MathUtils.clamp((L1 - s) / L1, 0.0F, 1.0F);
/* 288 */     return (n + u) / this.spanCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float locate(T v) {
/* 294 */     return approximate(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public float approxLength(int samples) {
/* 299 */     float tempLength = 0.0F;
/* 300 */     for (int i = 0; i < samples; i++) {
/* 301 */       this.tmp2.set(this.tmp3);
/* 302 */       valueAt(this.tmp3, i / (samples - 1.0F));
/* 303 */       if (i > 0) tempLength += this.tmp2.dst(this.tmp3); 
/*     */     } 
/* 305 */     return tempLength;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\BSpline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */