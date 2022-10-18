/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import com.badlogic.gdx.utils.NumberUtils;
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
/*     */ 
/*     */ public class Circle
/*     */   implements Serializable, Shape2D
/*     */ {
/*     */   public float x;
/*     */   public float y;
/*     */   public float radius;
/*     */   
/*     */   public Circle() {}
/*     */   
/*     */   public Circle(float x, float y, float radius) {
/*  37 */     this.x = x;
/*  38 */     this.y = y;
/*  39 */     this.radius = radius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Circle(Vector2 position, float radius) {
/*  47 */     this.x = position.x;
/*  48 */     this.y = position.y;
/*  49 */     this.radius = radius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Circle(Circle circle) {
/*  56 */     this.x = circle.x;
/*  57 */     this.y = circle.y;
/*  58 */     this.radius = circle.radius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Circle(Vector2 center, Vector2 edge) {
/*  66 */     this.x = center.x;
/*  67 */     this.y = center.y;
/*  68 */     this.radius = Vector2.len(center.x - edge.x, center.y - edge.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(float x, float y, float radius) {
/*  77 */     this.x = x;
/*  78 */     this.y = y;
/*  79 */     this.radius = radius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Vector2 position, float radius) {
/*  87 */     this.x = position.x;
/*  88 */     this.y = position.y;
/*  89 */     this.radius = radius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Circle circle) {
/*  96 */     this.x = circle.x;
/*  97 */     this.y = circle.y;
/*  98 */     this.radius = circle.radius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Vector2 center, Vector2 edge) {
/* 106 */     this.x = center.x;
/* 107 */     this.y = center.y;
/* 108 */     this.radius = Vector2.len(center.x - edge.x, center.y - edge.y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(Vector2 position) {
/* 114 */     this.x = position.x;
/* 115 */     this.y = position.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(float x, float y) {
/* 122 */     this.x = x;
/* 123 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setX(float x) {
/* 129 */     this.x = x;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setY(float y) {
/* 135 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRadius(float radius) {
/* 141 */     this.radius = radius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(float x, float y) {
/* 151 */     x = this.x - x;
/* 152 */     y = this.y - y;
/* 153 */     return (x * x + y * y <= this.radius * this.radius);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Vector2 point) {
/* 162 */     float dx = this.x - point.x;
/* 163 */     float dy = this.y - point.y;
/* 164 */     return (dx * dx + dy * dy <= this.radius * this.radius);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Circle c) {
/* 170 */     float radiusDiff = this.radius - c.radius;
/* 171 */     if (radiusDiff < 0.0F) return false; 
/* 172 */     float dx = this.x - c.x;
/* 173 */     float dy = this.y - c.y;
/* 174 */     float dst = dx * dx + dy * dy;
/* 175 */     float radiusSum = this.radius + c.radius;
/* 176 */     return (radiusDiff * radiusDiff >= dst && dst < radiusSum * radiusSum);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean overlaps(Circle c) {
/* 182 */     float dx = this.x - c.x;
/* 183 */     float dy = this.y - c.y;
/* 184 */     float distance = dx * dx + dy * dy;
/* 185 */     float radiusSum = this.radius + c.radius;
/* 186 */     return (distance < radiusSum * radiusSum);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 192 */     return this.x + "," + this.y + "," + this.radius;
/*     */   }
/*     */ 
/*     */   
/*     */   public float circumference() {
/* 197 */     return this.radius * 6.2831855F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float area() {
/* 202 */     return this.radius * this.radius * 3.1415927F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 207 */     if (o == this) return true; 
/* 208 */     if (o == null || o.getClass() != getClass()) return false; 
/* 209 */     Circle c = (Circle)o;
/* 210 */     return (this.x == c.x && this.y == c.y && this.radius == c.radius);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 215 */     int prime = 41;
/* 216 */     int result = 1;
/* 217 */     result = 41 * result + NumberUtils.floatToRawIntBits(this.radius);
/* 218 */     result = 41 * result + NumberUtils.floatToRawIntBits(this.x);
/* 219 */     result = 41 * result + NumberUtils.floatToRawIntBits(this.y);
/* 220 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Circle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */