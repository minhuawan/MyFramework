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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Ellipse
/*     */   implements Serializable, Shape2D
/*     */ {
/*     */   public float x;
/*     */   public float y;
/*     */   public float width;
/*     */   public float height;
/*     */   private static final long serialVersionUID = 7381533206532032099L;
/*     */   
/*     */   public Ellipse() {}
/*     */   
/*     */   public Ellipse(Ellipse ellipse) {
/*  42 */     this.x = ellipse.x;
/*  43 */     this.y = ellipse.y;
/*  44 */     this.width = ellipse.width;
/*  45 */     this.height = ellipse.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ellipse(float x, float y, float width, float height) {
/*  55 */     this.x = x;
/*  56 */     this.y = y;
/*  57 */     this.width = width;
/*  58 */     this.height = height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ellipse(Vector2 position, float width, float height) {
/*  67 */     this.x = position.x;
/*  68 */     this.y = position.y;
/*  69 */     this.width = width;
/*  70 */     this.height = height;
/*     */   }
/*     */   
/*     */   public Ellipse(Vector2 position, Vector2 size) {
/*  74 */     this.x = position.x;
/*  75 */     this.y = position.y;
/*  76 */     this.width = size.x;
/*  77 */     this.height = size.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ellipse(Circle circle) {
/*  84 */     this.x = circle.x;
/*  85 */     this.y = circle.y;
/*  86 */     this.width = circle.radius;
/*  87 */     this.height = circle.radius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(float x, float y) {
/*  97 */     x -= this.x;
/*  98 */     y -= this.y;
/*     */     
/* 100 */     return (x * x / this.width * 0.5F * this.width * 0.5F + y * y / this.height * 0.5F * this.height * 0.5F <= 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Vector2 point) {
/* 109 */     return contains(point.x, point.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(float x, float y, float width, float height) {
/* 119 */     this.x = x;
/* 120 */     this.y = y;
/* 121 */     this.width = width;
/* 122 */     this.height = height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Ellipse ellipse) {
/* 129 */     this.x = ellipse.x;
/* 130 */     this.y = ellipse.y;
/* 131 */     this.width = ellipse.width;
/* 132 */     this.height = ellipse.height;
/*     */   }
/*     */   
/*     */   public void set(Circle circle) {
/* 136 */     this.x = circle.x;
/* 137 */     this.y = circle.y;
/* 138 */     this.width = circle.radius;
/* 139 */     this.height = circle.radius;
/*     */   }
/*     */   
/*     */   public void set(Vector2 position, Vector2 size) {
/* 143 */     this.x = position.x;
/* 144 */     this.y = position.y;
/* 145 */     this.width = size.x;
/* 146 */     this.height = size.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ellipse setPosition(Vector2 position) {
/* 153 */     this.x = position.x;
/* 154 */     this.y = position.y;
/*     */     
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ellipse setPosition(float x, float y) {
/* 164 */     this.x = x;
/* 165 */     this.y = y;
/*     */     
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ellipse setSize(float width, float height) {
/* 175 */     this.width = width;
/* 176 */     this.height = height;
/*     */     
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public float area() {
/* 183 */     return 3.1415927F * this.width * this.height / 4.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float circumference() {
/* 191 */     float a = this.width / 2.0F;
/* 192 */     float b = this.height / 2.0F;
/* 193 */     if (a * 3.0F > b || b * 3.0F > a)
/*     */     {
/* 195 */       return (float)(3.1415927410125732D * ((3.0F * (a + b)) - Math.sqrt(((3.0F * a + b) * (a + 3.0F * b)))));
/*     */     }
/*     */     
/* 198 */     return (float)(6.2831854820251465D * Math.sqrt(((a * a + b * b) / 2.0F)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 204 */     if (o == this) return true; 
/* 205 */     if (o == null || o.getClass() != getClass()) return false; 
/* 206 */     Ellipse e = (Ellipse)o;
/* 207 */     return (this.x == e.x && this.y == e.y && this.width == e.width && this.height == e.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 212 */     int prime = 53;
/* 213 */     int result = 1;
/* 214 */     result = 53 * result + NumberUtils.floatToRawIntBits(this.height);
/* 215 */     result = 53 * result + NumberUtils.floatToRawIntBits(this.width);
/* 216 */     result = 53 * result + NumberUtils.floatToRawIntBits(this.x);
/* 217 */     result = 53 * result + NumberUtils.floatToRawIntBits(this.y);
/* 218 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Ellipse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */