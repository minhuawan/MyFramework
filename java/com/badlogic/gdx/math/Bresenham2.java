/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Pool;
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
/*     */ public class Bresenham2
/*     */ {
/*  29 */   private final Array<GridPoint2> points = new Array();
/*  30 */   private final Pool<GridPoint2> pool = new Pool<GridPoint2>()
/*     */     {
/*     */       protected GridPoint2 newObject() {
/*  33 */         return new GridPoint2();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<GridPoint2> line(GridPoint2 start, GridPoint2 end) {
/*  42 */     return line(start.x, start.y, end.x, end.y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<GridPoint2> line(int startX, int startY, int endX, int endY) {
/*  52 */     this.pool.freeAll(this.points);
/*  53 */     this.points.clear();
/*  54 */     return line(startX, startY, endX, endY, this.pool, this.points);
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
/*     */   public Array<GridPoint2> line(int startX, int startY, int endX, int endY, Pool<GridPoint2> pool, Array<GridPoint2> output) {
/*  67 */     int w = endX - startX;
/*  68 */     int h = endY - startY;
/*  69 */     int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
/*  70 */     if (w < 0) {
/*  71 */       dx1 = -1;
/*  72 */       dx2 = -1;
/*  73 */     } else if (w > 0) {
/*  74 */       dx1 = 1;
/*  75 */       dx2 = 1;
/*     */     } 
/*  77 */     if (h < 0)
/*  78 */     { dy1 = -1; }
/*  79 */     else if (h > 0) { dy1 = 1; }
/*  80 */      int longest = Math.abs(w);
/*  81 */     int shortest = Math.abs(h);
/*  82 */     if (longest <= shortest) {
/*  83 */       longest = Math.abs(h);
/*  84 */       shortest = Math.abs(w);
/*  85 */       if (h < 0)
/*  86 */       { dy2 = -1; }
/*  87 */       else if (h > 0) { dy2 = 1; }
/*  88 */        dx2 = 0;
/*     */     } 
/*  90 */     int numerator = longest >> 1;
/*  91 */     for (int i = 0; i <= longest; i++) {
/*  92 */       GridPoint2 point = (GridPoint2)pool.obtain();
/*  93 */       point.set(startX, startY);
/*  94 */       output.add(point);
/*  95 */       numerator += shortest;
/*  96 */       if (numerator > longest) {
/*  97 */         numerator -= longest;
/*  98 */         startX += dx1;
/*  99 */         startY += dy1;
/*     */       } else {
/* 101 */         startX += dx2;
/* 102 */         startY += dy2;
/*     */       } 
/*     */     } 
/* 105 */     return output;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\Bresenham2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */