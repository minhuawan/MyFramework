/*     */ package com.badlogic.gdx.math;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ public class Plane
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1240652082930747866L;
/*     */   
/*     */   public enum PlaneSide
/*     */   {
/*  32 */     OnPlane, Back, Front;
/*     */   }
/*     */   
/*  35 */   public final Vector3 normal = new Vector3();
/*  36 */   public float d = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Plane() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Plane(Vector3 normal, float d) {
/*  50 */     this.normal.set(normal).nor();
/*  51 */     this.d = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Plane(Vector3 normal, Vector3 point) {
/*  59 */     this.normal.set(normal).nor();
/*  60 */     this.d = -this.normal.dot(point);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Plane(Vector3 point1, Vector3 point2, Vector3 point3) {
/*  70 */     set(point1, point2, point3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Vector3 point1, Vector3 point2, Vector3 point3) {
/*  80 */     this.normal.set(point1).sub(point2).crs(point2.x - point3.x, point2.y - point3.y, point2.z - point3.z).nor();
/*  81 */     this.d = -point1.dot(this.normal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(float nx, float ny, float nz, float d) {
/*  91 */     this.normal.set(nx, ny, nz);
/*  92 */     this.d = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float distance(Vector3 point) {
/* 100 */     return this.normal.dot(point) + this.d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlaneSide testPoint(Vector3 point) {
/* 109 */     float dist = this.normal.dot(point) + this.d;
/*     */     
/* 111 */     if (dist == 0.0F)
/* 112 */       return PlaneSide.OnPlane; 
/* 113 */     if (dist < 0.0F) {
/* 114 */       return PlaneSide.Back;
/*     */     }
/* 116 */     return PlaneSide.Front;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlaneSide testPoint(float x, float y, float z) {
/* 127 */     float dist = this.normal.dot(x, y, z) + this.d;
/*     */     
/* 129 */     if (dist == 0.0F)
/* 130 */       return PlaneSide.OnPlane; 
/* 131 */     if (dist < 0.0F) {
/* 132 */       return PlaneSide.Back;
/*     */     }
/* 134 */     return PlaneSide.Front;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFrontFacing(Vector3 direction) {
/* 143 */     float dot = this.normal.dot(direction);
/* 144 */     return (dot <= 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector3 getNormal() {
/* 149 */     return this.normal;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getD() {
/* 154 */     return this.d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Vector3 point, Vector3 normal) {
/* 162 */     this.normal.set(normal);
/* 163 */     this.d = -point.dot(normal);
/*     */   }
/*     */   
/*     */   public void set(float pointX, float pointY, float pointZ, float norX, float norY, float norZ) {
/* 167 */     this.normal.set(norX, norY, norZ);
/* 168 */     this.d = -(pointX * norX + pointY * norY + pointZ * norZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Plane plane) {
/* 175 */     this.normal.set(plane.normal);
/* 176 */     this.d = plane.d;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 180 */     return this.normal.toString() + ", " + this.d;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Plane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */