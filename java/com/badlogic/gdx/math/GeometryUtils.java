/*     */ package com.badlogic.gdx.math;
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
/*     */ public final class GeometryUtils
/*     */ {
/*  21 */   private static final Vector2 tmp1 = new Vector2(), tmp2 = new Vector2(), tmp3 = new Vector2();
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
/*     */   public static Vector2 toBarycoord(Vector2 p, Vector2 a, Vector2 b, Vector2 c, Vector2 barycentricOut) {
/*  38 */     Vector2 v0 = tmp1.set(b).sub(a);
/*  39 */     Vector2 v1 = tmp2.set(c).sub(a);
/*  40 */     Vector2 v2 = tmp3.set(p).sub(a);
/*  41 */     float d00 = v0.dot(v0);
/*  42 */     float d01 = v0.dot(v1);
/*  43 */     float d11 = v1.dot(v1);
/*  44 */     float d20 = v2.dot(v0);
/*  45 */     float d21 = v2.dot(v1);
/*  46 */     float denom = d00 * d11 - d01 * d01;
/*  47 */     barycentricOut.x = (d11 * d20 - d01 * d21) / denom;
/*  48 */     barycentricOut.y = (d00 * d21 - d01 * d20) / denom;
/*  49 */     return barycentricOut;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean barycoordInsideTriangle(Vector2 barycentric) {
/*  54 */     return (barycentric.x >= 0.0F && barycentric.y >= 0.0F && barycentric.x + barycentric.y <= 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector2 fromBarycoord(Vector2 barycentric, Vector2 a, Vector2 b, Vector2 c, Vector2 interpolatedOut) {
/*  60 */     float u = 1.0F - barycentric.x - barycentric.y;
/*  61 */     interpolatedOut.x = u * a.x + barycentric.x * b.x + barycentric.y * c.x;
/*  62 */     interpolatedOut.y = u * a.y + barycentric.x * b.y + barycentric.y * c.y;
/*  63 */     return interpolatedOut;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static float fromBarycoord(Vector2 barycentric, float a, float b, float c) {
/*  69 */     float u = 1.0F - barycentric.x - barycentric.y;
/*  70 */     return u * a + barycentric.x * b + barycentric.y * c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float lowestPositiveRoot(float a, float b, float c) {
/*  80 */     float det = b * b - 4.0F * a * c;
/*  81 */     if (det < 0.0F) return Float.NaN;
/*     */     
/*  83 */     float sqrtD = (float)Math.sqrt(det);
/*  84 */     float invA = 1.0F / 2.0F * a;
/*  85 */     float r1 = (-b - sqrtD) * invA;
/*  86 */     float r2 = (-b + sqrtD) * invA;
/*     */     
/*  88 */     if (r1 > r2) {
/*  89 */       float tmp = r2;
/*  90 */       r2 = r1;
/*  91 */       r1 = tmp;
/*     */     } 
/*     */     
/*  94 */     if (r1 > 0.0F) return r1; 
/*  95 */     if (r2 > 0.0F) return r2; 
/*  96 */     return Float.NaN;
/*     */   }
/*     */   
/*     */   public static boolean colinear(float x1, float y1, float x2, float y2, float x3, float y3) {
/* 100 */     float dx21 = x2 - x1, dy21 = y2 - y1;
/* 101 */     float dx32 = x3 - x2, dy32 = y3 - y2;
/* 102 */     float det = dx32 * dy21 - dx21 * dy32;
/* 103 */     return (Math.abs(det) < 1.0E-6F);
/*     */   }
/*     */   
/*     */   public static Vector2 triangleCentroid(float x1, float y1, float x2, float y2, float x3, float y3, Vector2 centroid) {
/* 107 */     centroid.x = (x1 + x2 + x3) / 3.0F;
/* 108 */     centroid.y = (y1 + y2 + y3) / 3.0F;
/* 109 */     return centroid;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vector2 triangleCircumcenter(float x1, float y1, float x2, float y2, float x3, float y3, Vector2 circumcenter) {
/* 114 */     float dx21 = x2 - x1, dy21 = y2 - y1;
/* 115 */     float dx32 = x3 - x2, dy32 = y3 - y2;
/* 116 */     float dx13 = x1 - x3, dy13 = y1 - y3;
/* 117 */     float det = dx32 * dy21 - dx21 * dy32;
/* 118 */     if (Math.abs(det) < 1.0E-6F)
/* 119 */       throw new IllegalArgumentException("Triangle points must not be colinear."); 
/* 120 */     det *= 2.0F;
/* 121 */     float sqr1 = x1 * x1 + y1 * y1, sqr2 = x2 * x2 + y2 * y2, sqr3 = x3 * x3 + y3 * y3;
/* 122 */     circumcenter.set((sqr1 * dy32 + sqr2 * dy13 + sqr3 * dy21) / det, -(sqr1 * dx32 + sqr2 * dx13 + sqr3 * dx21) / det);
/* 123 */     return circumcenter;
/*     */   }
/*     */   
/*     */   public static float triangleCircumradius(float x1, float y1, float x2, float y2, float x3, float y3) {
/*     */     float x, y;
/* 128 */     if (Math.abs(y2 - y1) < 1.0E-6F) {
/* 129 */       float m2 = -(x3 - x2) / (y3 - y2);
/* 130 */       float mx2 = (x2 + x3) / 2.0F;
/* 131 */       float my2 = (y2 + y3) / 2.0F;
/* 132 */       x = (x2 + x1) / 2.0F;
/* 133 */       y = m2 * (x - mx2) + my2;
/* 134 */     } else if (Math.abs(y3 - y2) < 1.0E-6F) {
/* 135 */       float m1 = -(x2 - x1) / (y2 - y1);
/* 136 */       float mx1 = (x1 + x2) / 2.0F;
/* 137 */       float my1 = (y1 + y2) / 2.0F;
/* 138 */       x = (x3 + x2) / 2.0F;
/* 139 */       y = m1 * (x - mx1) + my1;
/*     */     } else {
/* 141 */       float m1 = -(x2 - x1) / (y2 - y1);
/* 142 */       float m2 = -(x3 - x2) / (y3 - y2);
/* 143 */       float mx1 = (x1 + x2) / 2.0F;
/* 144 */       float mx2 = (x2 + x3) / 2.0F;
/* 145 */       float my1 = (y1 + y2) / 2.0F;
/* 146 */       float my2 = (y2 + y3) / 2.0F;
/* 147 */       x = (m1 * mx1 - m2 * mx2 + my2 - my1) / (m1 - m2);
/* 148 */       y = m1 * (x - mx1) + my1;
/*     */     } 
/* 150 */     float dx = x1 - x, dy = y1 - y;
/* 151 */     return (float)Math.sqrt((dx * dx + dy * dy));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float triangleQuality(float x1, float y1, float x2, float y2, float x3, float y3) {
/* 159 */     float length1 = (float)Math.sqrt((x1 * x1 + y1 * y1));
/* 160 */     float length2 = (float)Math.sqrt((x2 * x2 + y2 * y2));
/* 161 */     float length3 = (float)Math.sqrt((x3 * x3 + y3 * y3));
/* 162 */     return Math.min(length1, Math.min(length2, length3)) / triangleCircumradius(x1, y1, x2, y2, x3, y3);
/*     */   }
/*     */   
/*     */   public static float triangleArea(float x1, float y1, float x2, float y2, float x3, float y3) {
/* 166 */     return Math.abs((x1 - x3) * (y2 - y1) - (x1 - x2) * (y3 - y1)) * 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vector2 quadrilateralCentroid(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, Vector2 centroid) {
/* 171 */     float avgX1 = (x1 + x2 + x3) / 3.0F;
/* 172 */     float avgY1 = (y1 + y2 + y3) / 3.0F;
/* 173 */     float avgX2 = (x1 + x4 + x3) / 3.0F;
/* 174 */     float avgY2 = (y1 + y4 + y3) / 3.0F;
/* 175 */     centroid.x = avgX1 - (avgX1 - avgX2) / 2.0F;
/* 176 */     centroid.y = avgY1 - (avgY1 - avgY2) / 2.0F;
/* 177 */     return centroid;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vector2 polygonCentroid(float[] polygon, int offset, int count, Vector2 centroid) {
/* 182 */     if (count < 6) throw new IllegalArgumentException("A polygon must have 3 or more coordinate pairs."); 
/* 183 */     float x = 0.0F, y = 0.0F;
/*     */     
/* 185 */     float signedArea = 0.0F;
/* 186 */     int i = offset;
/* 187 */     for (int n = offset + count - 2; i < n; i += 2) {
/* 188 */       float f1 = polygon[i];
/* 189 */       float f2 = polygon[i + 1];
/* 190 */       float f3 = polygon[i + 2];
/* 191 */       float f4 = polygon[i + 3];
/* 192 */       float f5 = f1 * f4 - f3 * f2;
/* 193 */       signedArea += f5;
/* 194 */       x += (f1 + f3) * f5;
/* 195 */       y += (f2 + f4) * f5;
/*     */     } 
/*     */     
/* 198 */     float x0 = polygon[i];
/* 199 */     float y0 = polygon[i + 1];
/* 200 */     float x1 = polygon[offset];
/* 201 */     float y1 = polygon[offset + 1];
/* 202 */     float a = x0 * y1 - x1 * y0;
/* 203 */     signedArea += a;
/* 204 */     x += (x0 + x1) * a;
/* 205 */     y += (y0 + y1) * a;
/*     */     
/* 207 */     if (signedArea == 0.0F) {
/* 208 */       centroid.x = 0.0F;
/* 209 */       centroid.y = 0.0F;
/*     */     } else {
/* 211 */       signedArea *= 0.5F;
/* 212 */       centroid.x = x / 6.0F * signedArea;
/* 213 */       centroid.y = y / 6.0F * signedArea;
/*     */     } 
/* 215 */     return centroid;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float polygonArea(float[] polygon, int offset, int count) {
/* 220 */     float area = 0.0F;
/* 221 */     for (int i = offset, n = offset + count; i < n; i += 2) {
/* 222 */       int x1 = i;
/* 223 */       int y1 = i + 1;
/* 224 */       int x2 = (i + 2) % n;
/* 225 */       if (x2 < offset) x2 += offset; 
/* 226 */       int y2 = (i + 3) % n;
/* 227 */       if (y2 < offset) y2 += offset; 
/* 228 */       area += polygon[x1] * polygon[y2];
/* 229 */       area -= polygon[x2] * polygon[y1];
/*     */     } 
/* 231 */     area *= 0.5F;
/* 232 */     return area;
/*     */   }
/*     */   
/*     */   public static void ensureCCW(float[] polygon) {
/* 236 */     if (!areVerticesClockwise(polygon, 0, polygon.length))
/* 237 */       return;  int lastX = polygon.length - 2;
/* 238 */     for (int i = 0, n = polygon.length / 2; i < n; i += 2) {
/* 239 */       int other = lastX - i;
/* 240 */       float x = polygon[i];
/* 241 */       float y = polygon[i + 1];
/* 242 */       polygon[i] = polygon[other];
/* 243 */       polygon[i + 1] = polygon[other + 1];
/* 244 */       polygon[other] = x;
/* 245 */       polygon[other + 1] = y;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean areVerticesClockwise(float[] polygon, int offset, int count) {
/* 250 */     if (count <= 2) return false; 
/* 251 */     float area = 0.0F;
/* 252 */     for (int i = offset, n = offset + count - 3; i < n; i += 2) {
/* 253 */       float f1 = polygon[i];
/* 254 */       float f2 = polygon[i + 1];
/* 255 */       float f3 = polygon[i + 2];
/* 256 */       float f4 = polygon[i + 3];
/* 257 */       area += f1 * f4 - f3 * f2;
/*     */     } 
/* 259 */     float p1x = polygon[count - 2];
/* 260 */     float p1y = polygon[count - 1];
/* 261 */     float p2x = polygon[0];
/* 262 */     float p2y = polygon[1];
/* 263 */     return (area + p1x * p2y - p2x * p1y < 0.0F);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\GeometryUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */