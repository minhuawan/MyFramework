/*     */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
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
/*     */ public class BoxShapeBuilder
/*     */   extends BaseShapeBuilder
/*     */ {
/*     */   public static void build(MeshPartBuilder builder, BoundingBox box) {
/*  35 */     builder.box(box.getCorner000(obtainV3()), box.getCorner010(obtainV3()), box.getCorner100(obtainV3()), box.getCorner110(obtainV3()), box
/*  36 */         .getCorner001(obtainV3()), box.getCorner011(obtainV3()), box.getCorner101(obtainV3()), box.getCorner111(obtainV3()));
/*  37 */     freeAll();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, MeshPartBuilder.VertexInfo corner000, MeshPartBuilder.VertexInfo corner010, MeshPartBuilder.VertexInfo corner100, MeshPartBuilder.VertexInfo corner110, MeshPartBuilder.VertexInfo corner001, MeshPartBuilder.VertexInfo corner011, MeshPartBuilder.VertexInfo corner101, MeshPartBuilder.VertexInfo corner111) {
/*  43 */     builder.ensureVertices(8);
/*  44 */     short i000 = builder.vertex(corner000);
/*  45 */     short i100 = builder.vertex(corner100);
/*  46 */     short i110 = builder.vertex(corner110);
/*  47 */     short i010 = builder.vertex(corner010);
/*  48 */     short i001 = builder.vertex(corner001);
/*  49 */     short i101 = builder.vertex(corner101);
/*  50 */     short i111 = builder.vertex(corner111);
/*  51 */     short i011 = builder.vertex(corner011);
/*     */     
/*  53 */     int primitiveType = builder.getPrimitiveType();
/*  54 */     if (primitiveType == 1) {
/*  55 */       builder.ensureIndices(24);
/*  56 */       builder.rect(i000, i100, i110, i010);
/*  57 */       builder.rect(i101, i001, i011, i111);
/*  58 */       builder.index(i000, i001, i010, i011, i110, i111, i100, i101);
/*  59 */     } else if (primitiveType == 0) {
/*  60 */       builder.ensureRectangleIndices(2);
/*  61 */       builder.rect(i000, i100, i110, i010);
/*  62 */       builder.rect(i101, i001, i011, i111);
/*     */     } else {
/*  64 */       builder.ensureRectangleIndices(6);
/*  65 */       builder.rect(i000, i100, i110, i010);
/*  66 */       builder.rect(i101, i001, i011, i111);
/*  67 */       builder.rect(i000, i010, i011, i001);
/*  68 */       builder.rect(i101, i111, i110, i100);
/*  69 */       builder.rect(i101, i100, i000, i001);
/*  70 */       builder.rect(i110, i111, i011, i010);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, Vector3 corner000, Vector3 corner010, Vector3 corner100, Vector3 corner110, Vector3 corner001, Vector3 corner011, Vector3 corner101, Vector3 corner111) {
/*  77 */     if ((builder.getAttributes().getMask() & 0x198L) == 0L) {
/*  78 */       build(builder, vertTmp1.set(corner000, null, null, null), vertTmp2.set(corner010, null, null, null), vertTmp3
/*  79 */           .set(corner100, null, null, null), vertTmp4.set(corner110, null, null, null), vertTmp5
/*  80 */           .set(corner001, null, null, null), vertTmp6.set(corner011, null, null, null), vertTmp7
/*  81 */           .set(corner101, null, null, null), vertTmp8.set(corner111, null, null, null));
/*     */     } else {
/*  83 */       builder.ensureVertices(24);
/*  84 */       builder.ensureRectangleIndices(6);
/*  85 */       Vector3 nor = tmpV1.set(corner000).lerp(corner110, 0.5F).sub(tmpV2.set(corner001).lerp(corner111, 0.5F)).nor();
/*  86 */       builder.rect(corner000, corner010, corner110, corner100, nor);
/*  87 */       builder.rect(corner011, corner001, corner101, corner111, nor.scl(-1.0F));
/*  88 */       nor = tmpV1.set(corner000).lerp(corner101, 0.5F).sub(tmpV2.set(corner010).lerp(corner111, 0.5F)).nor();
/*  89 */       builder.rect(corner001, corner000, corner100, corner101, nor);
/*  90 */       builder.rect(corner010, corner011, corner111, corner110, nor.scl(-1.0F));
/*  91 */       nor = tmpV1.set(corner000).lerp(corner011, 0.5F).sub(tmpV2.set(corner100).lerp(corner111, 0.5F)).nor();
/*  92 */       builder.rect(corner001, corner011, corner010, corner000, nor);
/*  93 */       builder.rect(corner100, corner110, corner111, corner101, nor.scl(-1.0F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, Matrix4 transform) {
/*  99 */     build(builder, obtainV3().set(-0.5F, -0.5F, -0.5F).mul(transform), obtainV3().set(-0.5F, 0.5F, -0.5F).mul(transform), 
/* 100 */         obtainV3().set(0.5F, -0.5F, -0.5F).mul(transform), obtainV3().set(0.5F, 0.5F, -0.5F).mul(transform), 
/* 101 */         obtainV3().set(-0.5F, -0.5F, 0.5F).mul(transform), obtainV3().set(-0.5F, 0.5F, 0.5F).mul(transform), 
/* 102 */         obtainV3().set(0.5F, -0.5F, 0.5F).mul(transform), obtainV3().set(0.5F, 0.5F, 0.5F).mul(transform));
/* 103 */     freeAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float width, float height, float depth) {
/* 108 */     build(builder, 0.0F, 0.0F, 0.0F, width, height, depth);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, float x, float y, float z, float width, float height, float depth) {
/* 113 */     float hw = width * 0.5F;
/* 114 */     float hh = height * 0.5F;
/* 115 */     float hd = depth * 0.5F;
/* 116 */     float x0 = x - hw, y0 = y - hh, z0 = z - hd, x1 = x + hw, y1 = y + hh, z1 = z + hd;
/* 117 */     build(builder, 
/* 118 */         obtainV3().set(x0, y0, z0), obtainV3().set(x0, y1, z0), obtainV3().set(x1, y0, z0), obtainV3().set(x1, y1, z0), 
/* 119 */         obtainV3().set(x0, y0, z1), obtainV3().set(x0, y1, z1), obtainV3().set(x1, y0, z1), obtainV3().set(x1, y1, z1));
/* 120 */     freeAll();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\BoxShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */