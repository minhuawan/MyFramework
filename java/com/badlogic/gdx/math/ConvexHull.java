/*     */ package com.badlogic.gdx.math;
/*     */ 
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
/*     */ public class ConvexHull
/*     */ {
/*  26 */   private final IntArray quicksortStack = new IntArray();
/*     */   private float[] sortedPoints;
/*  28 */   private final FloatArray hull = new FloatArray();
/*  29 */   private final IntArray indices = new IntArray();
/*  30 */   private final ShortArray originalIndices = new ShortArray(false, 0);
/*     */ 
/*     */   
/*     */   public FloatArray computePolygon(FloatArray points, boolean sorted) {
/*  34 */     return computePolygon(points.items, 0, points.size, sorted);
/*     */   }
/*     */ 
/*     */   
/*     */   public FloatArray computePolygon(float[] polygon, boolean sorted) {
/*  39 */     return computePolygon(polygon, 0, polygon.length, sorted);
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
/*     */   public FloatArray computePolygon(float[] points, int offset, int count, boolean sorted) {
/*  51 */     int end = offset + count;
/*     */     
/*  53 */     if (!sorted) {
/*  54 */       if (this.sortedPoints == null || this.sortedPoints.length < count) this.sortedPoints = new float[count]; 
/*  55 */       System.arraycopy(points, offset, this.sortedPoints, 0, count);
/*  56 */       points = this.sortedPoints;
/*  57 */       offset = 0;
/*  58 */       sort(points, count);
/*     */     } 
/*     */     
/*  61 */     FloatArray hull = this.hull;
/*  62 */     hull.clear();
/*     */     
/*     */     int i;
/*  65 */     for (i = offset; i < end; i += 2) {
/*  66 */       float x = points[i];
/*  67 */       float y = points[i + 1];
/*  68 */       while (hull.size >= 4 && ccw(x, y) <= 0.0F)
/*  69 */         hull.size -= 2; 
/*  70 */       hull.add(x);
/*  71 */       hull.add(y);
/*     */     } 
/*     */     
/*     */     int t;
/*  75 */     for (i = end - 4, t = hull.size + 2; i >= offset; i -= 2) {
/*  76 */       float x = points[i];
/*  77 */       float y = points[i + 1];
/*  78 */       while (hull.size >= t && ccw(x, y) <= 0.0F)
/*  79 */         hull.size -= 2; 
/*  80 */       hull.add(x);
/*  81 */       hull.add(y);
/*     */     } 
/*     */     
/*  84 */     return hull;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntArray computeIndices(FloatArray points, boolean sorted, boolean yDown) {
/*  89 */     return computeIndices(points.items, 0, points.size, sorted, yDown);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntArray computeIndices(float[] polygon, boolean sorted, boolean yDown) {
/*  94 */     return computeIndices(polygon, 0, polygon.length, sorted, yDown);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntArray computeIndices(float[] points, int offset, int count, boolean sorted, boolean yDown) {
/*  99 */     int end = offset + count;
/*     */     
/* 101 */     if (!sorted) {
/* 102 */       if (this.sortedPoints == null || this.sortedPoints.length < count) this.sortedPoints = new float[count]; 
/* 103 */       System.arraycopy(points, offset, this.sortedPoints, 0, count);
/* 104 */       points = this.sortedPoints;
/* 105 */       offset = 0;
/* 106 */       sortWithIndices(points, count, yDown);
/*     */     } 
/*     */     
/* 109 */     IntArray indices = this.indices;
/* 110 */     indices.clear();
/*     */     
/* 112 */     FloatArray hull = this.hull;
/* 113 */     hull.clear();
/*     */     
/*     */     int i, index;
/* 116 */     for (i = offset, index = i / 2; i < end; i += 2, index++) {
/* 117 */       float x = points[i];
/* 118 */       float y = points[i + 1];
/* 119 */       while (hull.size >= 4 && ccw(x, y) <= 0.0F) {
/* 120 */         hull.size -= 2;
/* 121 */         indices.size--;
/*     */       } 
/* 123 */       hull.add(x);
/* 124 */       hull.add(y);
/* 125 */       indices.add(index);
/*     */     } 
/*     */     
/*     */     int t;
/* 129 */     for (i = end - 4, index = i / 2, t = hull.size + 2; i >= offset; i -= 2, index--) {
/* 130 */       float x = points[i];
/* 131 */       float y = points[i + 1];
/* 132 */       while (hull.size >= t && ccw(x, y) <= 0.0F) {
/* 133 */         hull.size -= 2;
/* 134 */         indices.size--;
/*     */       } 
/* 136 */       hull.add(x);
/* 137 */       hull.add(y);
/* 138 */       indices.add(index);
/*     */     } 
/*     */ 
/*     */     
/* 142 */     if (!sorted) {
/* 143 */       short[] originalIndicesArray = this.originalIndices.items;
/* 144 */       int[] indicesArray = indices.items;
/* 145 */       for (int j = 0, n = indices.size; j < n; j++) {
/* 146 */         indicesArray[j] = originalIndicesArray[indicesArray[j]];
/*     */       }
/*     */     } 
/* 149 */     return indices;
/*     */   }
/*     */ 
/*     */   
/*     */   private float ccw(float p3x, float p3y) {
/* 154 */     FloatArray hull = this.hull;
/* 155 */     int size = hull.size;
/* 156 */     float p1x = hull.get(size - 4);
/* 157 */     float p1y = hull.get(size - 3);
/* 158 */     float p2x = hull.get(size - 2);
/* 159 */     float p2y = hull.peek();
/* 160 */     return (p2x - p1x) * (p3y - p1y) - (p2y - p1y) * (p3x - p1x);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sort(float[] values, int count) {
/* 166 */     int lower = 0;
/* 167 */     int upper = count - 1;
/* 168 */     IntArray stack = this.quicksortStack;
/* 169 */     stack.add(lower);
/* 170 */     stack.add(upper - 1);
/* 171 */     while (stack.size > 0) {
/* 172 */       upper = stack.pop();
/* 173 */       lower = stack.pop();
/* 174 */       if (upper <= lower)
/* 175 */         continue;  int i = quicksortPartition(values, lower, upper);
/* 176 */       if (i - lower > upper - i) {
/* 177 */         stack.add(lower);
/* 178 */         stack.add(i - 2);
/*     */       } 
/* 180 */       stack.add(i + 2);
/* 181 */       stack.add(upper);
/* 182 */       if (upper - i >= i - lower) {
/* 183 */         stack.add(lower);
/* 184 */         stack.add(i - 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int quicksortPartition(float[] values, int lower, int upper) {
/* 190 */     float x = values[lower];
/* 191 */     float y = values[lower + 1];
/* 192 */     int up = upper;
/* 193 */     int down = lower;
/*     */ 
/*     */     
/* 196 */     while (down < up) {
/* 197 */       while (down < up && values[down] <= x)
/* 198 */         down += 2; 
/* 199 */       while (values[up] > x || (values[up] == x && values[up + 1] < y))
/* 200 */         up -= 2; 
/* 201 */       if (down < up) {
/* 202 */         float temp = values[down];
/* 203 */         values[down] = values[up];
/* 204 */         values[up] = temp;
/*     */         
/* 206 */         temp = values[down + 1];
/* 207 */         values[down + 1] = values[up + 1];
/* 208 */         values[up + 1] = temp;
/*     */       } 
/*     */     } 
/* 211 */     values[lower] = values[up];
/* 212 */     values[up] = x;
/*     */     
/* 214 */     values[lower + 1] = values[up + 1];
/* 215 */     values[up + 1] = y;
/*     */     
/* 217 */     return up;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sortWithIndices(float[] values, int count, boolean yDown) {
/* 223 */     int pointCount = count / 2;
/* 224 */     this.originalIndices.clear();
/* 225 */     this.originalIndices.ensureCapacity(pointCount);
/* 226 */     short[] originalIndicesArray = this.originalIndices.items; short i;
/* 227 */     for (i = 0; i < pointCount; i = (short)(i + 1)) {
/* 228 */       originalIndicesArray[i] = i;
/*     */     }
/* 230 */     int lower = 0;
/* 231 */     int upper = count - 1;
/* 232 */     IntArray stack = this.quicksortStack;
/* 233 */     stack.add(lower);
/* 234 */     stack.add(upper - 1);
/* 235 */     while (stack.size > 0) {
/* 236 */       upper = stack.pop();
/* 237 */       lower = stack.pop();
/* 238 */       if (upper <= lower)
/* 239 */         continue;  int j = quicksortPartitionWithIndices(values, lower, upper, yDown, originalIndicesArray);
/* 240 */       if (j - lower > upper - j) {
/* 241 */         stack.add(lower);
/* 242 */         stack.add(j - 2);
/*     */       } 
/* 244 */       stack.add(j + 2);
/* 245 */       stack.add(upper);
/* 246 */       if (upper - j >= j - lower) {
/* 247 */         stack.add(lower);
/* 248 */         stack.add(j - 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int quicksortPartitionWithIndices(float[] values, int lower, int upper, boolean yDown, short[] originalIndices) {
/* 254 */     float x = values[lower];
/* 255 */     float y = values[lower + 1];
/* 256 */     int up = upper;
/* 257 */     int down = lower;
/*     */ 
/*     */     
/* 260 */     while (down < up) {
/* 261 */       while (down < up && values[down] <= x)
/* 262 */         down += 2; 
/* 263 */       if (yDown) {
/* 264 */         while (values[up] > x || (values[up] == x && values[up + 1] < y))
/* 265 */           up -= 2; 
/*     */       } else {
/* 267 */         while (values[up] > x || (values[up] == x && values[up + 1] > y))
/* 268 */           up -= 2; 
/*     */       } 
/* 270 */       if (down < up) {
/* 271 */         float temp = values[down];
/* 272 */         values[down] = values[up];
/* 273 */         values[up] = temp;
/*     */         
/* 275 */         temp = values[down + 1];
/* 276 */         values[down + 1] = values[up + 1];
/* 277 */         values[up + 1] = temp;
/*     */         
/* 279 */         short s = originalIndices[down / 2];
/* 280 */         originalIndices[down / 2] = originalIndices[up / 2];
/* 281 */         originalIndices[up / 2] = s;
/*     */       } 
/*     */     } 
/* 284 */     values[lower] = values[up];
/* 285 */     values[up] = x;
/*     */     
/* 287 */     values[lower + 1] = values[up + 1];
/* 288 */     values[up + 1] = y;
/*     */     
/* 290 */     short tempIndex = originalIndices[lower / 2];
/* 291 */     originalIndices[lower / 2] = originalIndices[up / 2];
/* 292 */     originalIndices[up / 2] = tempIndex;
/*     */     
/* 294 */     return up;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\ConvexHull.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */