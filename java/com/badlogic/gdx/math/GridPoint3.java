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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GridPoint3
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5922187982746752830L;
/*     */   public int x;
/*     */   public int y;
/*     */   public int z;
/*     */   
/*     */   public GridPoint3() {}
/*     */   
/*     */   public GridPoint3(int x, int y, int z) {
/*  41 */     this.x = x;
/*  42 */     this.y = y;
/*  43 */     this.z = z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint3(GridPoint3 point) {
/*  50 */     this.x = point.x;
/*  51 */     this.y = point.y;
/*  52 */     this.z = point.z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint3 set(GridPoint3 point) {
/*  61 */     this.x = point.x;
/*  62 */     this.y = point.y;
/*  63 */     this.z = point.z;
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint3 set(int x, int y, int z) {
/*  75 */     this.x = x;
/*  76 */     this.y = y;
/*  77 */     this.z = z;
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst2(GridPoint3 other) {
/*  86 */     int xd = other.x - this.x;
/*  87 */     int yd = other.y - this.y;
/*  88 */     int zd = other.z - this.z;
/*     */     
/*  90 */     return (xd * xd + yd * yd + zd * zd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst2(int x, int y, int z) {
/* 100 */     int xd = x - this.x;
/* 101 */     int yd = y - this.y;
/* 102 */     int zd = z - this.z;
/*     */     
/* 104 */     return (xd * xd + yd * yd + zd * zd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst(GridPoint3 other) {
/* 112 */     int xd = other.x - this.x;
/* 113 */     int yd = other.y - this.y;
/* 114 */     int zd = other.z - this.z;
/*     */     
/* 116 */     return (float)Math.sqrt((xd * xd + yd * yd + zd * zd));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst(int x, int y, int z) {
/* 126 */     int xd = x - this.x;
/* 127 */     int yd = y - this.y;
/* 128 */     int zd = z - this.z;
/*     */     
/* 130 */     return (float)Math.sqrt((xd * xd + yd * yd + zd * zd));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint3 add(GridPoint3 other) {
/* 140 */     this.x += other.x;
/* 141 */     this.y += other.y;
/* 142 */     this.z += other.z;
/* 143 */     return this;
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
/*     */   public GridPoint3 add(int x, int y, int z) {
/* 155 */     this.x += x;
/* 156 */     this.y += y;
/* 157 */     this.z += z;
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint3 sub(GridPoint3 other) {
/* 168 */     this.x -= other.x;
/* 169 */     this.y -= other.y;
/* 170 */     this.z -= other.z;
/* 171 */     return this;
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
/*     */   public GridPoint3 sub(int x, int y, int z) {
/* 183 */     this.x -= x;
/* 184 */     this.y -= y;
/* 185 */     this.z -= z;
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint3 cpy() {
/* 193 */     return new GridPoint3(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 198 */     if (this == o) return true; 
/* 199 */     if (o == null || o.getClass() != getClass()) return false; 
/* 200 */     GridPoint3 g = (GridPoint3)o;
/* 201 */     return (this.x == g.x && this.y == g.y && this.z == g.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 206 */     int prime = 17;
/* 207 */     int result = 1;
/* 208 */     result = 17 * result + this.x;
/* 209 */     result = 17 * result + this.y;
/* 210 */     result = 17 * result + this.z;
/* 211 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 216 */     return "(" + this.x + ", " + this.y + ", " + this.z + ")";
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\GridPoint3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */