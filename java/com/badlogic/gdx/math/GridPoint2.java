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
/*     */ public class GridPoint2
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4019969926331717380L;
/*     */   public int x;
/*     */   public int y;
/*     */   
/*     */   public GridPoint2() {}
/*     */   
/*     */   public GridPoint2(int x, int y) {
/*  39 */     this.x = x;
/*  40 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint2(GridPoint2 point) {
/*  47 */     this.x = point.x;
/*  48 */     this.y = point.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint2 set(GridPoint2 point) {
/*  57 */     this.x = point.x;
/*  58 */     this.y = point.y;
/*  59 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint2 set(int x, int y) {
/*  69 */     this.x = x;
/*  70 */     this.y = y;
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst2(GridPoint2 other) {
/*  79 */     int xd = other.x - this.x;
/*  80 */     int yd = other.y - this.y;
/*     */     
/*  82 */     return (xd * xd + yd * yd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst2(int x, int y) {
/*  91 */     int xd = x - this.x;
/*  92 */     int yd = y - this.y;
/*     */     
/*  94 */     return (xd * xd + yd * yd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst(GridPoint2 other) {
/* 102 */     int xd = other.x - this.x;
/* 103 */     int yd = other.y - this.y;
/*     */     
/* 105 */     return (float)Math.sqrt((xd * xd + yd * yd));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float dst(int x, int y) {
/* 114 */     int xd = x - this.x;
/* 115 */     int yd = y - this.y;
/*     */     
/* 117 */     return (float)Math.sqrt((xd * xd + yd * yd));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint2 add(GridPoint2 other) {
/* 127 */     this.x += other.x;
/* 128 */     this.y += other.y;
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint2 add(int x, int y) {
/* 140 */     this.x += x;
/* 141 */     this.y += y;
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint2 sub(GridPoint2 other) {
/* 152 */     this.x -= other.x;
/* 153 */     this.y -= other.y;
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint2 sub(int x, int y) {
/* 165 */     this.x -= x;
/* 166 */     this.y -= y;
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GridPoint2 cpy() {
/* 174 */     return new GridPoint2(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 179 */     if (this == o) return true; 
/* 180 */     if (o == null || o.getClass() != getClass()) return false; 
/* 181 */     GridPoint2 g = (GridPoint2)o;
/* 182 */     return (this.x == g.x && this.y == g.y);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 187 */     int prime = 53;
/* 188 */     int result = 1;
/* 189 */     result = 53 * result + this.x;
/* 190 */     result = 53 * result + this.y;
/* 191 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 196 */     return "(" + this.x + ", " + this.y + ")";
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\GridPoint2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */