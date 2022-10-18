/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class Bezier<T extends Vector<T>>
/*     */   implements Path<T>
/*     */ {
/*     */   public static <T extends Vector<T>> T linear(T out, float t, T p0, T p1, T tmp) {
/*  36 */     return out.set(p0).scl(1.0F - t).add(tmp.set(p1).scl(t));
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
/*     */   public static <T extends Vector<T>> T linear_derivative(T out, float t, T p0, T p1, T tmp) {
/*  48 */     return out.set(p1).sub(p0);
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
/*     */   public static <T extends Vector<T>> T quadratic(T out, float t, T p0, T p1, T p2, T tmp) {
/*  61 */     float dt = 1.0F - t;
/*  62 */     return out.set(p0).scl(dt * dt).add(tmp.set(p1).scl(2.0F * dt * t)).add(tmp.set(p2).scl(t * t));
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
/*     */   public static <T extends Vector<T>> T quadratic_derivative(T out, float t, T p0, T p1, T p2, T tmp) {
/*  76 */     float dt = 1.0F - t;
/*  77 */     return out.set(p1).sub(p0).scl(2.0F).scl(1.0F - t).add(tmp.set(p2).sub(p1).scl(t).scl(2.0F));
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
/*     */   public static <T extends Vector<T>> T cubic(T out, float t, T p0, T p1, T p2, T p3, T tmp) {
/*  92 */     float dt = 1.0F - t;
/*  93 */     float dt2 = dt * dt;
/*  94 */     float t2 = t * t;
/*  95 */     return out.set(p0).scl(dt2 * dt).add(tmp.set(p1).scl(3.0F * dt2 * t)).add(tmp.set(p2).scl(3.0F * dt * t2))
/*  96 */       .add(tmp.set(p3).scl(t2 * t));
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
/*     */   public static <T extends Vector<T>> T cubic_derivative(T out, float t, T p0, T p1, T p2, T p3, T tmp) {
/* 111 */     float dt = 1.0F - t;
/* 112 */     float dt2 = dt * dt;
/* 113 */     float t2 = t * t;
/* 114 */     return out.set(p1).sub(p0).scl(dt2 * 3.0F).add(tmp.set(p2).sub(p1).scl(dt * t * 6.0F)).add(tmp.set(p3).sub(p2).scl(t2 * 3.0F));
/*     */   }
/*     */   
/* 117 */   public Array<T> points = new Array();
/*     */   
/*     */   private T tmp;
/*     */   
/*     */   private T tmp2;
/*     */   
/*     */   private T tmp3;
/*     */   
/*     */   public Bezier(T... points) {
/* 126 */     set(points);
/*     */   }
/*     */   
/*     */   public Bezier(T[] points, int offset, int length) {
/* 130 */     set(points, offset, length);
/*     */   }
/*     */   
/*     */   public Bezier(Array<T> points, int offset, int length) {
/* 134 */     set(points, offset, length);
/*     */   }
/*     */   
/*     */   public Bezier set(T... points) {
/* 138 */     return set(points, 0, points.length);
/*     */   }
/*     */   
/*     */   public Bezier set(T[] points, int offset, int length) {
/* 142 */     if (length < 2 || length > 4)
/* 143 */       throw new GdxRuntimeException("Only first, second and third degree Bezier curves are supported."); 
/* 144 */     if (this.tmp == null) this.tmp = points[0].cpy(); 
/* 145 */     if (this.tmp2 == null) this.tmp2 = points[0].cpy(); 
/* 146 */     if (this.tmp3 == null) this.tmp3 = points[0].cpy(); 
/* 147 */     this.points.clear();
/* 148 */     this.points.addAll((Object[])points, offset, length);
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public Bezier set(Array<T> points, int offset, int length) {
/* 153 */     if (length < 2 || length > 4)
/* 154 */       throw new GdxRuntimeException("Only first, second and third degree Bezier curves are supported."); 
/* 155 */     if (this.tmp == null) this.tmp = ((Vector<T>)points.get(0)).cpy(); 
/* 156 */     this.points.clear();
/* 157 */     this.points.addAll(points, offset, length);
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public T valueAt(T out, float t) {
/* 163 */     int n = this.points.size;
/* 164 */     if (n == 2)
/* 165 */     { linear(out, t, (T)this.points.get(0), (T)this.points.get(1), this.tmp); }
/* 166 */     else if (n == 3)
/* 167 */     { quadratic(out, t, (T)this.points.get(0), (T)this.points.get(1), (T)this.points.get(2), this.tmp); }
/* 168 */     else if (n == 4) { cubic(out, t, (T)this.points.get(0), (T)this.points.get(1), (T)this.points.get(2), (T)this.points.get(3), this.tmp); }
/* 169 */      return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public T derivativeAt(T out, float t) {
/* 174 */     int n = this.points.size;
/* 175 */     if (n == 2)
/* 176 */     { linear_derivative(out, t, (T)this.points.get(0), (T)this.points.get(1), this.tmp); }
/* 177 */     else if (n == 3)
/* 178 */     { quadratic_derivative(out, t, (T)this.points.get(0), (T)this.points.get(1), (T)this.points.get(2), this.tmp); }
/* 179 */     else if (n == 4) { cubic_derivative(out, t, (T)this.points.get(0), (T)this.points.get(1), (T)this.points.get(2), (T)this.points.get(3), this.tmp); }
/* 180 */      return out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float approximate(T v) {
/* 186 */     Vector<Vector> vector = (Vector)this.points.get(0);
/* 187 */     Vector vector1 = (Vector)this.points.get(this.points.size - 1);
/* 188 */     T p3 = v;
/* 189 */     float l1Sqr = vector.dst2(vector1);
/* 190 */     float l2Sqr = p3.dst2(vector1);
/* 191 */     float l3Sqr = p3.dst2(vector);
/* 192 */     float l1 = (float)Math.sqrt(l1Sqr);
/* 193 */     float s = (l2Sqr + l1Sqr - l3Sqr) / 2.0F * l1;
/* 194 */     return MathUtils.clamp((l1 - s) / l1, 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float locate(T v) {
/* 200 */     return approximate(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public float approxLength(int samples) {
/* 205 */     float tempLength = 0.0F;
/* 206 */     for (int i = 0; i < samples; i++) {
/* 207 */       this.tmp2.set(this.tmp3);
/* 208 */       valueAt(this.tmp3, i / (samples - 1.0F));
/* 209 */       if (i > 0) tempLength += this.tmp2.dst(this.tmp3); 
/*     */     } 
/* 211 */     return tempLength;
/*     */   }
/*     */   
/*     */   public Bezier() {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Bezier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */