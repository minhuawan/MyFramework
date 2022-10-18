/*     */ package com.badlogic.gdx.math.collision;
/*     */ 
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import java.io.Serializable;
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
/*     */ public class Ray
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -620692054835390878L;
/*  29 */   public final Vector3 origin = new Vector3();
/*  30 */   public final Vector3 direction = new Vector3();
/*     */ 
/*     */ 
/*     */   
/*     */   public Ray() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Ray(Vector3 origin, Vector3 direction) {
/*  39 */     this.origin.set(origin);
/*  40 */     this.direction.set(direction).nor();
/*     */   }
/*     */ 
/*     */   
/*     */   public Ray cpy() {
/*  45 */     return new Ray(this.origin, this.direction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector3 getEndPoint(Vector3 out, float distance) {
/*  53 */     return out.set(this.direction).scl(distance).add(this.origin);
/*     */   }
/*     */   
/*  56 */   static Vector3 tmp = new Vector3();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ray mul(Matrix4 matrix) {
/*  63 */     tmp.set(this.origin).add(this.direction);
/*  64 */     tmp.mul(matrix);
/*  65 */     this.origin.mul(matrix);
/*  66 */     this.direction.set(tmp.sub(this.origin));
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  72 */     return "ray [" + this.origin + ":" + this.direction + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ray set(Vector3 origin, Vector3 direction) {
/*  81 */     this.origin.set(origin);
/*  82 */     this.direction.set(direction);
/*  83 */     return this;
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
/*     */   public Ray set(float x, float y, float z, float dx, float dy, float dz) {
/*  96 */     this.origin.set(x, y, z);
/*  97 */     this.direction.set(dx, dy, dz);
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ray set(Ray ray) {
/* 106 */     this.origin.set(ray.origin);
/* 107 */     this.direction.set(ray.direction);
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 113 */     if (o == this) return true; 
/* 114 */     if (o == null || o.getClass() != getClass()) return false; 
/* 115 */     Ray r = (Ray)o;
/* 116 */     return (this.direction.equals(r.direction) && this.origin.equals(r.origin));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 121 */     int prime = 73;
/* 122 */     int result = 1;
/* 123 */     result = 73 * result + this.direction.hashCode();
/* 124 */     result = 73 * result + this.origin.hashCode();
/* 125 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\collision\Ray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */