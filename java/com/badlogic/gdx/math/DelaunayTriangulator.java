/*     */ package com.badlogic.gdx.math;
/*     */ 
/*     */ import com.badlogic.gdx.utils.BooleanArray;
/*     */ import com.badlogic.gdx.utils.FloatArray;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.ShortArray;
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
/*     */ public class DelaunayTriangulator
/*     */ {
/*     */   private static final float EPSILON = 1.0E-6F;
/*     */   private static final int INSIDE = 0;
/*     */   private static final int COMPLETE = 1;
/*     */   private static final int INCOMPLETE = 2;
/*  32 */   private final IntArray quicksortStack = new IntArray();
/*     */   private float[] sortedPoints;
/*  34 */   private final ShortArray triangles = new ShortArray(false, 16);
/*  35 */   private final ShortArray originalIndices = new ShortArray(false, 0);
/*  36 */   private final IntArray edges = new IntArray();
/*  37 */   private final BooleanArray complete = new BooleanArray(false, 16);
/*  38 */   private final float[] superTriangle = new float[6];
/*  39 */   private final Vector2 centroid = new Vector2();
/*     */ 
/*     */   
/*     */   public ShortArray computeTriangles(FloatArray points, boolean sorted) {
/*  43 */     return computeTriangles(points.items, 0, points.size, sorted);
/*     */   }
/*     */ 
/*     */   
/*     */   public ShortArray computeTriangles(float[] polygon, boolean sorted) {
/*  48 */     return computeTriangles(polygon, 0, polygon.length, sorted);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortArray computeTriangles(float[] points, int offset, int count, boolean sorted) {
/*  59 */     ShortArray triangles = this.triangles;
/*  60 */     triangles.clear();
/*  61 */     if (count < 6) return triangles; 
/*  62 */     triangles.ensureCapacity(count);
/*     */     
/*  64 */     if (!sorted) {
/*  65 */       if (this.sortedPoints == null || this.sortedPoints.length < count) this.sortedPoints = new float[count]; 
/*  66 */       System.arraycopy(points, offset, this.sortedPoints, 0, count);
/*  67 */       points = this.sortedPoints;
/*  68 */       offset = 0;
/*  69 */       sort(points, count);
/*     */     } 
/*     */     
/*  72 */     int end = offset + count;
/*     */ 
/*     */     
/*  75 */     float xmin = points[0], ymin = points[1];
/*  76 */     float xmax = xmin, ymax = ymin;
/*  77 */     for (int i = offset + 2; i < end; i++) {
/*  78 */       float value = points[i];
/*  79 */       if (value < xmin) xmin = value; 
/*  80 */       if (value > xmax) xmax = value; 
/*  81 */       i++;
/*  82 */       value = points[i];
/*  83 */       if (value < ymin) ymin = value; 
/*  84 */       if (value > ymax) ymax = value; 
/*     */     } 
/*  86 */     float dx = xmax - xmin, dy = ymax - ymin;
/*  87 */     float dmax = ((dx > dy) ? dx : dy) * 20.0F;
/*  88 */     float xmid = (xmax + xmin) / 2.0F, ymid = (ymax + ymin) / 2.0F;
/*     */ 
/*     */     
/*  91 */     float[] superTriangle = this.superTriangle;
/*  92 */     superTriangle[0] = xmid - dmax;
/*  93 */     superTriangle[1] = ymid - dmax;
/*  94 */     superTriangle[2] = xmid;
/*  95 */     superTriangle[3] = ymid + dmax;
/*  96 */     superTriangle[4] = xmid + dmax;
/*  97 */     superTriangle[5] = ymid - dmax;
/*     */     
/*  99 */     IntArray edges = this.edges;
/* 100 */     edges.ensureCapacity(count / 2);
/*     */     
/* 102 */     BooleanArray complete = this.complete;
/* 103 */     complete.clear();
/* 104 */     complete.ensureCapacity(count);
/*     */ 
/*     */     
/* 107 */     triangles.add(end);
/* 108 */     triangles.add(end + 2);
/* 109 */     triangles.add(end + 4);
/* 110 */     complete.add(false);
/*     */ 
/*     */     
/* 113 */     for (int pointIndex = offset; pointIndex < end; pointIndex += 2) {
/* 114 */       float x = points[pointIndex], y = points[pointIndex + 1];
/*     */ 
/*     */       
/* 117 */       short[] arrayOfShort = triangles.items;
/* 118 */       boolean[] completeArray = complete.items;
/* 119 */       for (int triangleIndex = triangles.size - 1; triangleIndex >= 0; triangleIndex -= 3) {
/* 120 */         int completeIndex = triangleIndex / 3;
/* 121 */         if (!completeArray[completeIndex]) {
/* 122 */           float x1, y1, x2, y2, x3, y3; int p1 = arrayOfShort[triangleIndex - 2];
/* 123 */           int p2 = arrayOfShort[triangleIndex - 1];
/* 124 */           int p3 = arrayOfShort[triangleIndex];
/*     */           
/* 126 */           if (p1 >= end) {
/* 127 */             int m = p1 - end;
/* 128 */             x1 = superTriangle[m];
/* 129 */             y1 = superTriangle[m + 1];
/*     */           } else {
/* 131 */             x1 = points[p1];
/* 132 */             y1 = points[p1 + 1];
/*     */           } 
/* 134 */           if (p2 >= end) {
/* 135 */             int m = p2 - end;
/* 136 */             x2 = superTriangle[m];
/* 137 */             y2 = superTriangle[m + 1];
/*     */           } else {
/* 139 */             x2 = points[p2];
/* 140 */             y2 = points[p2 + 1];
/*     */           } 
/* 142 */           if (p3 >= end) {
/* 143 */             int m = p3 - end;
/* 144 */             x3 = superTriangle[m];
/* 145 */             y3 = superTriangle[m + 1];
/*     */           } else {
/* 147 */             x3 = points[p3];
/* 148 */             y3 = points[p3 + 1];
/*     */           } 
/* 150 */           switch (circumCircle(x, y, x1, y1, x2, y2, x3, y3)) {
/*     */             case 1:
/* 152 */               completeArray[completeIndex] = true;
/*     */               break;
/*     */             case 0:
/* 155 */               edges.add(p1);
/* 156 */               edges.add(p2);
/* 157 */               edges.add(p2);
/* 158 */               edges.add(p3);
/* 159 */               edges.add(p3);
/* 160 */               edges.add(p1);
/*     */               
/* 162 */               triangles.removeIndex(triangleIndex);
/* 163 */               triangles.removeIndex(triangleIndex - 1);
/* 164 */               triangles.removeIndex(triangleIndex - 2);
/* 165 */               complete.removeIndex(completeIndex);
/*     */               break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 170 */       int[] edgesArray = edges.items;
/* 171 */       for (int k = 0, n = edges.size; k < n; k += 2) {
/*     */         
/* 173 */         int p1 = edgesArray[k];
/* 174 */         if (p1 != -1) {
/* 175 */           int p2 = edgesArray[k + 1];
/* 176 */           boolean skip = false;
/* 177 */           for (int ii = k + 2; ii < n; ii += 2) {
/* 178 */             if (p1 == edgesArray[ii + 1] && p2 == edgesArray[ii]) {
/* 179 */               skip = true;
/* 180 */               edgesArray[ii] = -1;
/*     */             } 
/*     */           } 
/* 183 */           if (!skip)
/*     */           
/*     */           { 
/* 186 */             triangles.add(p1);
/* 187 */             triangles.add(edgesArray[k + 1]);
/* 188 */             triangles.add(pointIndex);
/* 189 */             complete.add(false); } 
/*     */         } 
/* 191 */       }  edges.clear();
/*     */     } 
/*     */ 
/*     */     
/* 195 */     short[] trianglesArray = triangles.items; int j;
/* 196 */     for (j = triangles.size - 1; j >= 0; j -= 3) {
/* 197 */       if (trianglesArray[j] >= end || trianglesArray[j - 1] >= end || trianglesArray[j - 2] >= end) {
/* 198 */         triangles.removeIndex(j);
/* 199 */         triangles.removeIndex(j - 1);
/* 200 */         triangles.removeIndex(j - 2);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 205 */     if (!sorted) {
/* 206 */       short[] originalIndicesArray = this.originalIndices.items;
/* 207 */       for (int k = 0, n = triangles.size; k < n; k++) {
/* 208 */         trianglesArray[k] = (short)(originalIndicesArray[trianglesArray[k] / 2] * 2);
/*     */       }
/*     */     } 
/*     */     
/* 212 */     if (offset == 0) {
/* 213 */       int n; for (j = 0, n = triangles.size; j < n; j++)
/* 214 */         trianglesArray[j] = (short)(trianglesArray[j] / 2); 
/*     */     } else {
/* 216 */       int n; for (j = 0, n = triangles.size; j < n; j++) {
/* 217 */         trianglesArray[j] = (short)((trianglesArray[j] - offset) / 2);
/*     */       }
/*     */     } 
/* 220 */     return triangles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int circumCircle(float xp, float yp, float x1, float y1, float x2, float y2, float x3, float y3) {
/* 228 */     float xc, yc, y1y2 = Math.abs(y1 - y2);
/* 229 */     float y2y3 = Math.abs(y2 - y3);
/* 230 */     if (y1y2 < 1.0E-6F) {
/* 231 */       if (y2y3 < 1.0E-6F) return 2; 
/* 232 */       float m2 = -(x3 - x2) / (y3 - y2);
/* 233 */       float mx2 = (x2 + x3) / 2.0F;
/* 234 */       float my2 = (y2 + y3) / 2.0F;
/* 235 */       xc = (x2 + x1) / 2.0F;
/* 236 */       yc = m2 * (xc - mx2) + my2;
/*     */     } else {
/* 238 */       float m1 = -(x2 - x1) / (y2 - y1);
/* 239 */       float mx1 = (x1 + x2) / 2.0F;
/* 240 */       float my1 = (y1 + y2) / 2.0F;
/* 241 */       if (y2y3 < 1.0E-6F) {
/* 242 */         xc = (x3 + x2) / 2.0F;
/* 243 */         yc = m1 * (xc - mx1) + my1;
/*     */       } else {
/* 245 */         float m2 = -(x3 - x2) / (y3 - y2);
/* 246 */         float mx2 = (x2 + x3) / 2.0F;
/* 247 */         float my2 = (y2 + y3) / 2.0F;
/* 248 */         xc = (m1 * mx1 - m2 * mx2 + my2 - my1) / (m1 - m2);
/* 249 */         yc = m1 * (xc - mx1) + my1;
/*     */       } 
/*     */     } 
/*     */     
/* 253 */     float dx = x2 - xc;
/* 254 */     float dy = y2 - yc;
/* 255 */     float rsqr = dx * dx + dy * dy;
/*     */     
/* 257 */     dx = xp - xc;
/* 258 */     dx *= dx;
/* 259 */     dy = yp - yc;
/* 260 */     if (dx + dy * dy - rsqr <= 1.0E-6F) return 0; 
/* 261 */     return (xp > xc && dx > rsqr) ? 1 : 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sort(float[] values, int count) {
/* 267 */     int pointCount = count / 2;
/* 268 */     this.originalIndices.clear();
/* 269 */     this.originalIndices.ensureCapacity(pointCount);
/* 270 */     short[] originalIndicesArray = this.originalIndices.items; short i;
/* 271 */     for (i = 0; i < pointCount; i = (short)(i + 1)) {
/* 272 */       originalIndicesArray[i] = i;
/*     */     }
/* 274 */     int lower = 0;
/* 275 */     int upper = count - 1;
/* 276 */     IntArray stack = this.quicksortStack;
/* 277 */     stack.add(lower);
/* 278 */     stack.add(upper - 1);
/* 279 */     while (stack.size > 0) {
/* 280 */       upper = stack.pop();
/* 281 */       lower = stack.pop();
/* 282 */       if (upper <= lower)
/* 283 */         continue;  int j = quicksortPartition(values, lower, upper, originalIndicesArray);
/* 284 */       if (j - lower > upper - j) {
/* 285 */         stack.add(lower);
/* 286 */         stack.add(j - 2);
/*     */       } 
/* 288 */       stack.add(j + 2);
/* 289 */       stack.add(upper);
/* 290 */       if (upper - j >= j - lower) {
/* 291 */         stack.add(lower);
/* 292 */         stack.add(j - 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int quicksortPartition(float[] values, int lower, int upper, short[] originalIndices) {
/* 298 */     float value = values[lower];
/* 299 */     int up = upper;
/* 300 */     int down = lower + 2;
/*     */ 
/*     */     
/* 303 */     while (down < up) {
/* 304 */       while (down < up && values[down] <= value)
/* 305 */         down += 2; 
/* 306 */       while (values[up] > value)
/* 307 */         up -= 2; 
/* 308 */       if (down < up) {
/* 309 */         float f = values[down];
/* 310 */         values[down] = values[up];
/* 311 */         values[up] = f;
/*     */         
/* 313 */         f = values[down + 1];
/* 314 */         values[down + 1] = values[up + 1];
/* 315 */         values[up + 1] = f;
/*     */         
/* 317 */         short s = originalIndices[down / 2];
/* 318 */         originalIndices[down / 2] = originalIndices[up / 2];
/* 319 */         originalIndices[up / 2] = s;
/*     */       } 
/*     */     } 
/* 322 */     values[lower] = values[up];
/* 323 */     values[up] = value;
/*     */     
/* 325 */     float tempValue = values[lower + 1];
/* 326 */     values[lower + 1] = values[up + 1];
/* 327 */     values[up + 1] = tempValue;
/*     */     
/* 329 */     short tempIndex = originalIndices[lower / 2];
/* 330 */     originalIndices[lower / 2] = originalIndices[up / 2];
/* 331 */     originalIndices[up / 2] = tempIndex;
/* 332 */     return up;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void trim(ShortArray triangles, float[] points, float[] hull, int offset, int count) {
/* 338 */     short[] trianglesArray = triangles.items;
/* 339 */     for (int i = triangles.size - 1; i >= 0; i -= 3) {
/* 340 */       int p1 = trianglesArray[i - 2] * 2;
/* 341 */       int p2 = trianglesArray[i - 1] * 2;
/* 342 */       int p3 = trianglesArray[i] * 2;
/* 343 */       GeometryUtils.triangleCentroid(points[p1], points[p1 + 1], points[p2], points[p2 + 1], points[p3], points[p3 + 1], this.centroid);
/*     */       
/* 345 */       if (!Intersector.isPointInPolygon(hull, offset, count, this.centroid.x, this.centroid.y)) {
/* 346 */         triangles.removeIndex(i);
/* 347 */         triangles.removeIndex(i - 1);
/* 348 */         triangles.removeIndex(i - 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\DelaunayTriangulator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */