/*     */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*     */ public class EllipseShapeBuilder
/*     */   extends BaseShapeBuilder
/*     */ {
/*     */   public static void build(MeshPartBuilder builder, float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {
/*  32 */     build(builder, radius, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, 0.0F, 360.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float radius, int divisions, Vector3 center, Vector3 normal) {
/*  37 */     build(builder, radius, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float radius, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal) {
/*  43 */     build(builder, radius, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z, tangent.x, tangent.y, tangent.z, binormal.x, binormal.y, binormal.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ) {
/*  50 */     build(builder, radius, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tangentX, tangentY, tangentZ, binormalX, binormalY, binormalZ, 0.0F, 360.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {
/*  57 */     build(builder, radius * 2.0F, radius * 2.0F, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float radius, int divisions, Vector3 center, Vector3 normal, float angleFrom, float angleTo) {
/*  62 */     build(builder, radius, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float radius, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal, float angleFrom, float angleTo) {
/*  68 */     build(builder, radius, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z, tangent.x, tangent.y, tangent.z, binormal.x, binormal.y, binormal.z, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {
/*  76 */     build(builder, radius * 2.0F, radius * 2.0F, 0.0F, 0.0F, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tangentX, tangentY, tangentZ, binormalX, binormalY, binormalZ, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {
/*  83 */     build(builder, width, height, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, 0.0F, 360.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, int divisions, Vector3 center, Vector3 normal) {
/*  89 */     build(builder, width, height, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal) {
/*  95 */     build(builder, width, height, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z, tangent.x, tangent.y, tangent.z, binormal.x, binormal.y, binormal.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ) {
/* 103 */     build(builder, width, height, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tangentX, tangentY, tangentZ, binormalX, binormalY, binormalZ, 0.0F, 360.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {
/* 110 */     build(builder, width, height, 0.0F, 0.0F, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, int divisions, Vector3 center, Vector3 normal, float angleFrom, float angleTo) {
/* 116 */     build(builder, width, height, 0.0F, 0.0F, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal, float angleFrom, float angleTo) {
/* 122 */     build(builder, width, height, 0.0F, 0.0F, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z, tangent.x, tangent.y, tangent.z, binormal.x, binormal.y, binormal.z, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {
/* 130 */     build(builder, width, height, 0.0F, 0.0F, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tangentX, tangentY, tangentZ, binormalX, binormalY, binormalZ, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {
/* 138 */     tmpV1.set(normalX, normalY, normalZ).crs(0.0F, 0.0F, 1.0F);
/* 139 */     tmpV2.set(normalX, normalY, normalZ).crs(0.0F, 1.0F, 0.0F);
/* 140 */     if (tmpV2.len2() > tmpV1.len2()) tmpV1.set(tmpV2); 
/* 141 */     tmpV2.set(tmpV1.nor()).crs(normalX, normalY, normalZ).nor();
/* 142 */     build(builder, width, height, innerWidth, innerHeight, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, tmpV1.x, tmpV1.y, tmpV1.z, tmpV2.x, tmpV2.y, tmpV2.z, angleFrom, angleTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {
/* 149 */     build(builder, width, height, innerWidth, innerHeight, divisions, centerX, centerY, centerZ, normalX, normalY, normalZ, 0.0F, 360.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, float innerWidth, float innerHeight, int divisions, Vector3 center, Vector3 normal) {
/* 156 */     build(builder, width, height, innerWidth, innerHeight, divisions, center.x, center.y, center.z, normal.x, normal.y, normal.z, 0.0F, 360.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {
/* 164 */     if (innerWidth <= 0.0F || innerHeight <= 0.0F) {
/* 165 */       builder.ensureVertices(divisions + 2);
/* 166 */       builder.ensureTriangleIndices(divisions);
/* 167 */     } else if (innerWidth == width && innerHeight == height) {
/* 168 */       builder.ensureVertices(divisions + 1);
/* 169 */       builder.ensureIndices(divisions + 1);
/* 170 */       if (builder.getPrimitiveType() != 1) {
/* 171 */         throw new GdxRuntimeException("Incorrect primitive type : expect GL_LINES because innerWidth == width && innerHeight == height");
/*     */       }
/*     */     } else {
/* 174 */       builder.ensureVertices((divisions + 1) * 2);
/* 175 */       builder.ensureRectangleIndices(divisions + 1);
/*     */     } 
/*     */     
/* 178 */     float ao = 0.017453292F * angleFrom;
/* 179 */     float step = 0.017453292F * (angleTo - angleFrom) / divisions;
/* 180 */     Vector3 sxEx = tmpV1.set(tangentX, tangentY, tangentZ).scl(width * 0.5F);
/* 181 */     Vector3 syEx = tmpV2.set(binormalX, binormalY, binormalZ).scl(height * 0.5F);
/* 182 */     Vector3 sxIn = tmpV3.set(tangentX, tangentY, tangentZ).scl(innerWidth * 0.5F);
/* 183 */     Vector3 syIn = tmpV4.set(binormalX, binormalY, binormalZ).scl(innerHeight * 0.5F);
/* 184 */     MeshPartBuilder.VertexInfo currIn = vertTmp3.set(null, null, null, null);
/* 185 */     currIn.hasUV = currIn.hasPosition = currIn.hasNormal = true;
/* 186 */     currIn.uv.set(0.5F, 0.5F);
/* 187 */     currIn.position.set(centerX, centerY, centerZ);
/* 188 */     currIn.normal.set(normalX, normalY, normalZ);
/* 189 */     MeshPartBuilder.VertexInfo currEx = vertTmp4.set(null, null, null, null);
/* 190 */     currEx.hasUV = currEx.hasPosition = currEx.hasNormal = true;
/* 191 */     currEx.uv.set(0.5F, 0.5F);
/* 192 */     currEx.position.set(centerX, centerY, centerZ);
/* 193 */     currEx.normal.set(normalX, normalY, normalZ);
/* 194 */     short center = builder.vertex(currEx);
/* 195 */     float angle = 0.0F;
/* 196 */     float us = 0.5F * innerWidth / width;
/* 197 */     float vs = 0.5F * innerHeight / height;
/* 198 */     short i2 = 0, i3 = 0, i4 = 0;
/* 199 */     for (int i = 0; i <= divisions; i++) {
/* 200 */       angle = ao + step * i;
/* 201 */       float x = MathUtils.cos(angle);
/* 202 */       float y = MathUtils.sin(angle);
/* 203 */       currEx.position.set(centerX, centerY, centerZ).add(sxEx.x * x + syEx.x * y, sxEx.y * x + syEx.y * y, sxEx.z * x + syEx.z * y);
/*     */       
/* 205 */       currEx.uv.set(0.5F + 0.5F * x, 0.5F + 0.5F * y);
/* 206 */       short i1 = builder.vertex(currEx);
/*     */       
/* 208 */       if (innerWidth <= 0.0F || innerHeight <= 0.0F) {
/* 209 */         if (i != 0) builder.triangle(i1, i2, center); 
/* 210 */         i2 = i1;
/* 211 */       } else if (innerWidth == width && innerHeight == height) {
/* 212 */         if (i != 0) builder.line(i1, i2); 
/* 213 */         i2 = i1;
/*     */       } else {
/* 215 */         currIn.position.set(centerX, centerY, centerZ).add(sxIn.x * x + syIn.x * y, sxIn.y * x + syIn.y * y, sxIn.z * x + syIn.z * y);
/*     */         
/* 217 */         currIn.uv.set(0.5F + us * x, 0.5F + vs * y);
/* 218 */         i2 = i1;
/* 219 */         i1 = builder.vertex(currIn);
/*     */         
/* 221 */         if (i != 0) builder.rect(i1, i2, i4, i3); 
/* 222 */         i4 = i2;
/* 223 */         i3 = i1;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\EllipseShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */