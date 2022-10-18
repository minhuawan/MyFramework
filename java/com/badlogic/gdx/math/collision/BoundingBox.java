/*     */ package com.badlogic.gdx.math.collision;
/*     */ 
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
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
/*     */ public class BoundingBox
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1286036817192127343L;
/*  32 */   private static final Vector3 tmpVector = new Vector3();
/*     */   
/*  34 */   public final Vector3 min = new Vector3();
/*  35 */   public final Vector3 max = new Vector3();
/*     */   
/*  37 */   private final Vector3 cnt = new Vector3();
/*  38 */   private final Vector3 dim = new Vector3();
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 getCenter(Vector3 out) {
/*  43 */     return out.set(this.cnt);
/*     */   }
/*     */   
/*     */   public float getCenterX() {
/*  47 */     return this.cnt.x;
/*     */   }
/*     */   
/*     */   public float getCenterY() {
/*  51 */     return this.cnt.y;
/*     */   }
/*     */   
/*     */   public float getCenterZ() {
/*  55 */     return this.cnt.z;
/*     */   }
/*     */   
/*     */   public Vector3 getCorner000(Vector3 out) {
/*  59 */     return out.set(this.min.x, this.min.y, this.min.z);
/*     */   }
/*     */   
/*     */   public Vector3 getCorner001(Vector3 out) {
/*  63 */     return out.set(this.min.x, this.min.y, this.max.z);
/*     */   }
/*     */   
/*     */   public Vector3 getCorner010(Vector3 out) {
/*  67 */     return out.set(this.min.x, this.max.y, this.min.z);
/*     */   }
/*     */   
/*     */   public Vector3 getCorner011(Vector3 out) {
/*  71 */     return out.set(this.min.x, this.max.y, this.max.z);
/*     */   }
/*     */   
/*     */   public Vector3 getCorner100(Vector3 out) {
/*  75 */     return out.set(this.max.x, this.min.y, this.min.z);
/*     */   }
/*     */   
/*     */   public Vector3 getCorner101(Vector3 out) {
/*  79 */     return out.set(this.max.x, this.min.y, this.max.z);
/*     */   }
/*     */   
/*     */   public Vector3 getCorner110(Vector3 out) {
/*  83 */     return out.set(this.max.x, this.max.y, this.min.z);
/*     */   }
/*     */   
/*     */   public Vector3 getCorner111(Vector3 out) {
/*  87 */     return out.set(this.max.x, this.max.y, this.max.z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 getDimensions(Vector3 out) {
/*  93 */     return out.set(this.dim);
/*     */   }
/*     */   
/*     */   public float getWidth() {
/*  97 */     return this.dim.x;
/*     */   }
/*     */   
/*     */   public float getHeight() {
/* 101 */     return this.dim.y;
/*     */   }
/*     */   
/*     */   public float getDepth() {
/* 105 */     return this.dim.z;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 getMin(Vector3 out) {
/* 111 */     return out.set(this.min);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 getMax(Vector3 out) {
/* 117 */     return out.set(this.max);
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundingBox() {
/* 122 */     clr();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox(BoundingBox bounds) {
/* 129 */     set(bounds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox(Vector3 minimum, Vector3 maximum) {
/* 137 */     set(minimum, maximum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox set(BoundingBox bounds) {
/* 145 */     return set(bounds.min, bounds.max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox set(Vector3 minimum, Vector3 maximum) {
/* 154 */     this.min.set((minimum.x < maximum.x) ? minimum.x : maximum.x, (minimum.y < maximum.y) ? minimum.y : maximum.y, (minimum.z < maximum.z) ? minimum.z : maximum.z);
/*     */     
/* 156 */     this.max.set((minimum.x > maximum.x) ? minimum.x : maximum.x, (minimum.y > maximum.y) ? minimum.y : maximum.y, (minimum.z > maximum.z) ? minimum.z : maximum.z);
/*     */     
/* 158 */     this.cnt.set(this.min).add(this.max).scl(0.5F);
/* 159 */     this.dim.set(this.max).sub(this.min);
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox set(Vector3[] points) {
/* 168 */     inf();
/* 169 */     for (Vector3 l_point : points)
/* 170 */       ext(l_point); 
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox set(List<Vector3> points) {
/* 179 */     inf();
/* 180 */     for (Vector3 l_point : points)
/* 181 */       ext(l_point); 
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox inf() {
/* 189 */     this.min.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
/* 190 */     this.max.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
/* 191 */     this.cnt.set(0.0F, 0.0F, 0.0F);
/* 192 */     this.dim.set(0.0F, 0.0F, 0.0F);
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox ext(Vector3 point) {
/* 200 */     return set(this.min.set(min(this.min.x, point.x), min(this.min.y, point.y), min(this.min.z, point.z)), this.max
/* 201 */         .set(Math.max(this.max.x, point.x), Math.max(this.max.y, point.y), Math.max(this.max.z, point.z)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox clr() {
/* 207 */     return set(this.min.set(0.0F, 0.0F, 0.0F), this.max.set(0.0F, 0.0F, 0.0F));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 213 */     return (this.min.x <= this.max.x && this.min.y <= this.max.y && this.min.z <= this.max.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox ext(BoundingBox a_bounds) {
/* 221 */     return set(this.min.set(min(this.min.x, a_bounds.min.x), min(this.min.y, a_bounds.min.y), min(this.min.z, a_bounds.min.z)), this.max
/* 222 */         .set(max(this.max.x, a_bounds.max.x), max(this.max.y, a_bounds.max.y), max(this.max.z, a_bounds.max.z)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox ext(Vector3 center, float radius) {
/* 231 */     return set(this.min.set(min(this.min.x, center.x - radius), min(this.min.y, center.y - radius), min(this.min.z, center.z - radius)), this.max
/* 232 */         .set(max(this.max.x, center.x + radius), max(this.max.y, center.y + radius), max(this.max.z, center.z + radius)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox ext(BoundingBox bounds, Matrix4 transform) {
/* 241 */     ext(tmpVector.set(bounds.min.x, bounds.min.y, bounds.min.z).mul(transform));
/* 242 */     ext(tmpVector.set(bounds.min.x, bounds.min.y, bounds.max.z).mul(transform));
/* 243 */     ext(tmpVector.set(bounds.min.x, bounds.max.y, bounds.min.z).mul(transform));
/* 244 */     ext(tmpVector.set(bounds.min.x, bounds.max.y, bounds.max.z).mul(transform));
/* 245 */     ext(tmpVector.set(bounds.max.x, bounds.min.y, bounds.min.z).mul(transform));
/* 246 */     ext(tmpVector.set(bounds.max.x, bounds.min.y, bounds.max.z).mul(transform));
/* 247 */     ext(tmpVector.set(bounds.max.x, bounds.max.y, bounds.min.z).mul(transform));
/* 248 */     ext(tmpVector.set(bounds.max.x, bounds.max.y, bounds.max.z).mul(transform));
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox mul(Matrix4 transform) {
/* 258 */     float x0 = this.min.x, y0 = this.min.y, z0 = this.min.z, x1 = this.max.x, y1 = this.max.y, z1 = this.max.z;
/* 259 */     inf();
/* 260 */     ext(tmpVector.set(x0, y0, z0).mul(transform));
/* 261 */     ext(tmpVector.set(x0, y0, z1).mul(transform));
/* 262 */     ext(tmpVector.set(x0, y1, z0).mul(transform));
/* 263 */     ext(tmpVector.set(x0, y1, z1).mul(transform));
/* 264 */     ext(tmpVector.set(x1, y0, z0).mul(transform));
/* 265 */     ext(tmpVector.set(x1, y0, z1).mul(transform));
/* 266 */     ext(tmpVector.set(x1, y1, z0).mul(transform));
/* 267 */     ext(tmpVector.set(x1, y1, z1).mul(transform));
/* 268 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(BoundingBox b) {
/* 275 */     return (!isValid() || (this.min.x <= b.min.x && this.min.y <= b.min.y && this.min.z <= b.min.z && this.max.x >= b.max.x && this.max.y >= b.max.y && this.max.z >= b.max.z));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(BoundingBox b) {
/* 283 */     if (!isValid()) return false;
/*     */ 
/*     */ 
/*     */     
/* 287 */     float lx = Math.abs(this.cnt.x - b.cnt.x);
/* 288 */     float sumx = this.dim.x / 2.0F + b.dim.x / 2.0F;
/*     */     
/* 290 */     float ly = Math.abs(this.cnt.y - b.cnt.y);
/* 291 */     float sumy = this.dim.y / 2.0F + b.dim.y / 2.0F;
/*     */     
/* 293 */     float lz = Math.abs(this.cnt.z - b.cnt.z);
/* 294 */     float sumz = this.dim.z / 2.0F + b.dim.z / 2.0F;
/*     */     
/* 296 */     return (lx <= sumx && ly <= sumy && lz <= sumz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Vector3 v) {
/* 304 */     return (this.min.x <= v.x && this.max.x >= v.x && this.min.y <= v.y && this.max.y >= v.y && this.min.z <= v.z && this.max.z >= v.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 309 */     return "[" + this.min + "|" + this.max + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox ext(float x, float y, float z) {
/* 319 */     return set(this.min.set(min(this.min.x, x), min(this.min.y, y), min(this.min.z, z)), this.max.set(max(this.max.x, x), max(this.max.y, y), max(this.max.z, z)));
/*     */   }
/*     */   
/*     */   static final float min(float a, float b) {
/* 323 */     return (a > b) ? b : a;
/*     */   }
/*     */   
/*     */   static final float max(float a, float b) {
/* 327 */     return (a > b) ? a : b;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\collision\BoundingBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */