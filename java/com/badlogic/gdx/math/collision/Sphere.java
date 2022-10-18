/*    */ package com.badlogic.gdx.math.collision;
/*    */ 
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ import com.badlogic.gdx.utils.NumberUtils;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Sphere
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -6487336868908521596L;
/*    */   public float radius;
/*    */   public final Vector3 center;
/*    */   private static final float PI_4_3 = 4.1887903F;
/*    */   
/*    */   public Sphere(Vector3 center, float radius) {
/* 41 */     this.center = new Vector3(center);
/* 42 */     this.radius = radius;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean overlaps(Sphere sphere) {
/* 48 */     return (this.center.dst2(sphere.center) < (this.radius + sphere.radius) * (this.radius + sphere.radius));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 53 */     int prime = 71;
/* 54 */     int result = 1;
/* 55 */     result = 71 * result + this.center.hashCode();
/* 56 */     result = 71 * result + NumberUtils.floatToRawIntBits(this.radius);
/* 57 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 62 */     if (this == o) return true; 
/* 63 */     if (o == null || o.getClass() != getClass()) return false; 
/* 64 */     Sphere s = (Sphere)o;
/* 65 */     return (this.radius == s.radius && this.center.equals(s.center));
/*    */   }
/*    */   
/*    */   public float volume() {
/* 69 */     return 4.1887903F * this.radius * this.radius * this.radius;
/*    */   }
/*    */   
/*    */   public float surfaceArea() {
/* 73 */     return 12.566371F * this.radius * this.radius;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\collision\Sphere.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */