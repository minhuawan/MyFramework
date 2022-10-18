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
/*     */ public class EarClippingTriangulator
/*     */ {
/*     */   private static final int CONCAVE = -1;
/*     */   private static final int TANGENTIAL = 0;
/*     */   private static final int CONVEX = 1;
/*  43 */   private final ShortArray indicesArray = new ShortArray();
/*     */   private short[] indices;
/*     */   private float[] vertices;
/*     */   private int vertexCount;
/*  47 */   private final IntArray vertexTypes = new IntArray();
/*  48 */   private final ShortArray triangles = new ShortArray();
/*     */ 
/*     */   
/*     */   public ShortArray computeTriangles(FloatArray vertices) {
/*  52 */     return computeTriangles(vertices.items, 0, vertices.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public ShortArray computeTriangles(float[] vertices) {
/*  57 */     return computeTriangles(vertices, 0, vertices.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortArray computeTriangles(float[] vertices, int offset, int count) {
/*  65 */     this.vertices = vertices;
/*  66 */     int vertexCount = this.vertexCount = count / 2;
/*  67 */     int vertexOffset = offset / 2;
/*     */     
/*  69 */     ShortArray indicesArray = this.indicesArray;
/*  70 */     indicesArray.clear();
/*  71 */     indicesArray.ensureCapacity(vertexCount);
/*  72 */     indicesArray.size = vertexCount;
/*  73 */     short[] indices = this.indices = indicesArray.items;
/*  74 */     if (areVerticesClockwise(vertices, offset, count)) {
/*  75 */       short s; for (s = 0; s < vertexCount; s = (short)(s + 1))
/*  76 */         indices[s] = (short)(vertexOffset + s); 
/*     */     } else {
/*  78 */       for (int j = 0, k = vertexCount - 1; j < vertexCount; j++) {
/*  79 */         indices[j] = (short)(vertexOffset + k - j);
/*     */       }
/*     */     } 
/*  82 */     IntArray vertexTypes = this.vertexTypes;
/*  83 */     vertexTypes.clear();
/*  84 */     vertexTypes.ensureCapacity(vertexCount);
/*  85 */     for (int i = 0, n = vertexCount; i < n; i++) {
/*  86 */       vertexTypes.add(classifyVertex(i));
/*     */     }
/*     */     
/*  89 */     ShortArray triangles = this.triangles;
/*  90 */     triangles.clear();
/*  91 */     triangles.ensureCapacity(Math.max(0, vertexCount - 2) * 3);
/*  92 */     triangulate();
/*  93 */     return triangles;
/*     */   }
/*     */   
/*     */   private void triangulate() {
/*  97 */     int[] vertexTypes = this.vertexTypes.items;
/*     */     
/*  99 */     while (this.vertexCount > 3) {
/* 100 */       int earTipIndex = findEarTip();
/* 101 */       cutEarTip(earTipIndex);
/*     */ 
/*     */       
/* 104 */       int previousIndex = previousIndex(earTipIndex);
/* 105 */       int nextIndex = (earTipIndex == this.vertexCount) ? 0 : earTipIndex;
/* 106 */       vertexTypes[previousIndex] = classifyVertex(previousIndex);
/* 107 */       vertexTypes[nextIndex] = classifyVertex(nextIndex);
/*     */     } 
/*     */     
/* 110 */     if (this.vertexCount == 3) {
/* 111 */       ShortArray triangles = this.triangles;
/* 112 */       short[] indices = this.indices;
/* 113 */       triangles.add(indices[0]);
/* 114 */       triangles.add(indices[1]);
/* 115 */       triangles.add(indices[2]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int classifyVertex(int index) {
/* 121 */     short[] indices = this.indices;
/* 122 */     int previous = indices[previousIndex(index)] * 2;
/* 123 */     int current = indices[index] * 2;
/* 124 */     int next = indices[nextIndex(index)] * 2;
/* 125 */     float[] vertices = this.vertices;
/* 126 */     return computeSpannedAreaSign(vertices[previous], vertices[previous + 1], vertices[current], vertices[current + 1], vertices[next], vertices[next + 1]);
/*     */   }
/*     */ 
/*     */   
/*     */   private int findEarTip() {
/* 131 */     int vertexCount = this.vertexCount;
/* 132 */     for (int i = 0; i < vertexCount; i++) {
/* 133 */       if (isEarTip(i)) return i;
/*     */     
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     int[] vertexTypes = this.vertexTypes.items;
/* 143 */     for (int j = 0; j < vertexCount; j++) {
/* 144 */       if (vertexTypes[j] != -1) return j; 
/* 145 */     }  return 0;
/*     */   }
/*     */   
/*     */   private boolean isEarTip(int earTipIndex) {
/* 149 */     int[] vertexTypes = this.vertexTypes.items;
/* 150 */     if (vertexTypes[earTipIndex] == -1) return false;
/*     */     
/* 152 */     int previousIndex = previousIndex(earTipIndex);
/* 153 */     int nextIndex = nextIndex(earTipIndex);
/* 154 */     short[] indices = this.indices;
/* 155 */     int p1 = indices[previousIndex] * 2;
/* 156 */     int p2 = indices[earTipIndex] * 2;
/* 157 */     int p3 = indices[nextIndex] * 2;
/* 158 */     float[] vertices = this.vertices;
/* 159 */     float p1x = vertices[p1], p1y = vertices[p1 + 1];
/* 160 */     float p2x = vertices[p2], p2y = vertices[p2 + 1];
/* 161 */     float p3x = vertices[p3], p3y = vertices[p3 + 1];
/*     */     
/*     */     int i;
/*     */     
/* 165 */     for (i = nextIndex(nextIndex); i != previousIndex; i = nextIndex(i)) {
/*     */ 
/*     */       
/* 168 */       if (vertexTypes[i] != 1) {
/* 169 */         int v = indices[i] * 2;
/* 170 */         float vx = vertices[v];
/* 171 */         float vy = vertices[v + 1];
/*     */ 
/*     */ 
/*     */         
/* 175 */         if (computeSpannedAreaSign(p3x, p3y, p1x, p1y, vx, vy) >= 0 && 
/* 176 */           computeSpannedAreaSign(p1x, p1y, p2x, p2y, vx, vy) >= 0 && 
/* 177 */           computeSpannedAreaSign(p2x, p2y, p3x, p3y, vx, vy) >= 0) return false;
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     return true;
/*     */   }
/*     */   
/*     */   private void cutEarTip(int earTipIndex) {
/* 186 */     short[] indices = this.indices;
/* 187 */     ShortArray triangles = this.triangles;
/*     */     
/* 189 */     triangles.add(indices[previousIndex(earTipIndex)]);
/* 190 */     triangles.add(indices[earTipIndex]);
/* 191 */     triangles.add(indices[nextIndex(earTipIndex)]);
/*     */     
/* 193 */     this.indicesArray.removeIndex(earTipIndex);
/* 194 */     this.vertexTypes.removeIndex(earTipIndex);
/* 195 */     this.vertexCount--;
/*     */   }
/*     */   
/*     */   private int previousIndex(int index) {
/* 199 */     return ((index == 0) ? this.vertexCount : index) - 1;
/*     */   }
/*     */   
/*     */   private int nextIndex(int index) {
/* 203 */     return (index + 1) % this.vertexCount;
/*     */   }
/*     */   
/*     */   private static boolean areVerticesClockwise(float[] vertices, int offset, int count) {
/* 207 */     if (count <= 2) return false; 
/* 208 */     float area = 0.0F;
/* 209 */     for (int i = offset, n = offset + count - 3; i < n; i += 2) {
/* 210 */       float f1 = vertices[i];
/* 211 */       float f2 = vertices[i + 1];
/* 212 */       float f3 = vertices[i + 2];
/* 213 */       float f4 = vertices[i + 3];
/* 214 */       area += f1 * f4 - f3 * f2;
/*     */     } 
/* 216 */     float p1x = vertices[offset + count - 2];
/* 217 */     float p1y = vertices[offset + count - 1];
/* 218 */     float p2x = vertices[offset];
/* 219 */     float p2y = vertices[offset + 1];
/* 220 */     return (area + p1x * p2y - p2x * p1y < 0.0F);
/*     */   }
/*     */   
/*     */   private static int computeSpannedAreaSign(float p1x, float p1y, float p2x, float p2y, float p3x, float p3y) {
/* 224 */     float area = p1x * (p3y - p2y);
/* 225 */     area += p2x * (p1y - p3y);
/* 226 */     area += p3x * (p2y - p1y);
/* 227 */     return (int)Math.signum(area);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\math\EarClippingTriangulator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */