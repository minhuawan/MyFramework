/*    */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.math.Matrix4;
/*    */ import com.badlogic.gdx.utils.ShortArray;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SphereShapeBuilder
/*    */   extends BaseShapeBuilder
/*    */ {
/* 28 */   private static final ShortArray tmpIndices = new ShortArray();
/*    */   
/*    */   public static void build(MeshPartBuilder builder, float width, float height, float depth, int divisionsU, int divisionsV) {
/* 31 */     build(builder, width, height, depth, divisionsU, divisionsV, 0.0F, 360.0F, 0.0F, 180.0F);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static void build(MeshPartBuilder builder, Matrix4 transform, float width, float height, float depth, int divisionsU, int divisionsV) {
/* 38 */     build(builder, transform, width, height, depth, divisionsU, divisionsV, 0.0F, 360.0F, 0.0F, 180.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void build(MeshPartBuilder builder, float width, float height, float depth, int divisionsU, int divisionsV, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
/* 43 */     build(builder, matTmp1.idt(), width, height, depth, divisionsU, divisionsV, angleUFrom, angleUTo, angleVFrom, angleVTo);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static void build(MeshPartBuilder builder, Matrix4 transform, float width, float height, float depth, int divisionsU, int divisionsV, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
/* 51 */     float hw = width * 0.5F;
/* 52 */     float hh = height * 0.5F;
/* 53 */     float hd = depth * 0.5F;
/* 54 */     float auo = 0.017453292F * angleUFrom;
/* 55 */     float stepU = 0.017453292F * (angleUTo - angleUFrom) / divisionsU;
/* 56 */     float avo = 0.017453292F * angleVFrom;
/* 57 */     float stepV = 0.017453292F * (angleVTo - angleVFrom) / divisionsV;
/* 58 */     float us = 1.0F / divisionsU;
/* 59 */     float vs = 1.0F / divisionsV;
/* 60 */     float u = 0.0F;
/* 61 */     float v = 0.0F;
/* 62 */     float angleU = 0.0F;
/* 63 */     float angleV = 0.0F;
/* 64 */     MeshPartBuilder.VertexInfo curr1 = vertTmp3.set(null, null, null, null);
/* 65 */     curr1.hasUV = curr1.hasPosition = curr1.hasNormal = true;
/*    */     
/* 67 */     int s = divisionsU + 3;
/* 68 */     tmpIndices.clear();
/* 69 */     tmpIndices.ensureCapacity(divisionsU * 2);
/* 70 */     tmpIndices.size = s;
/* 71 */     int tempOffset = 0;
/*    */     
/* 73 */     builder.ensureVertices((divisionsV + 1) * (divisionsU + 1));
/* 74 */     builder.ensureRectangleIndices(divisionsU);
/* 75 */     for (int iv = 0; iv <= divisionsV; iv++) {
/* 76 */       angleV = avo + stepV * iv;
/* 77 */       v = vs * iv;
/* 78 */       float t = MathUtils.sin(angleV);
/* 79 */       float h = MathUtils.cos(angleV) * hh;
/* 80 */       for (int iu = 0; iu <= divisionsU; iu++) {
/* 81 */         angleU = auo + stepU * iu;
/* 82 */         u = 1.0F - us * iu;
/*    */         
/* 84 */         curr1.position.set(MathUtils.cos(angleU) * hw * t, h, MathUtils.sin(angleU) * hd * t).mul(transform);
/* 85 */         curr1.normal.set(curr1.position).nor();
/* 86 */         curr1.uv.set(u, v);
/* 87 */         tmpIndices.set(tempOffset, builder.vertex(curr1));
/* 88 */         int o = tempOffset + s;
/* 89 */         if (iv > 0 && iu > 0)
/* 90 */           builder.rect(tmpIndices.get(tempOffset), tmpIndices.get((o - 1) % s), tmpIndices.get((o - divisionsU + 2) % s), tmpIndices
/* 91 */               .get((o - divisionsU + 1) % s)); 
/* 92 */         tempOffset = (tempOffset + 1) % tmpIndices.size;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\SphereShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */