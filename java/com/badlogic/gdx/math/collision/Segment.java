/*    */ package com.badlogic.gdx.math.collision;
/*    */ 
/*    */ import com.badlogic.gdx.math.Vector3;
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
/*    */ public class Segment
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 2739667069736519602L;
/* 31 */   public final Vector3 a = new Vector3();
/*    */ 
/*    */   
/* 34 */   public final Vector3 b = new Vector3();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Segment(Vector3 a, Vector3 b) {
/* 41 */     this.a.set(a);
/* 42 */     this.b.set(b);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Segment(float aX, float aY, float aZ, float bX, float bY, float bZ) {
/* 53 */     this.a.set(aX, aY, aZ);
/* 54 */     this.b.set(bX, bY, bZ);
/*    */   }
/*    */   
/*    */   public float len() {
/* 58 */     return this.a.dst(this.b);
/*    */   }
/*    */   
/*    */   public float len2() {
/* 62 */     return this.a.dst2(this.b);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 67 */     if (o == this) return true; 
/* 68 */     if (o == null || o.getClass() != getClass()) return false; 
/* 69 */     Segment s = (Segment)o;
/* 70 */     return (this.a.equals(s.a) && this.b.equals(s.b));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 75 */     int prime = 71;
/* 76 */     int result = 1;
/* 77 */     result = 71 * result + this.a.hashCode();
/* 78 */     result = 71 * result + this.b.hashCode();
/* 79 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\collision\Segment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */