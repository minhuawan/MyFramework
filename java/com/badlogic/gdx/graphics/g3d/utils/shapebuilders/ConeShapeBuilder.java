/*    */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*    */ import com.badlogic.gdx.math.MathUtils;
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
/*    */ public class ConeShapeBuilder
/*    */   extends BaseShapeBuilder
/*    */ {
/*    */   public static void build(MeshPartBuilder builder, float width, float height, float depth, int divisions) {
/* 27 */     build(builder, width, height, depth, divisions, 0.0F, 360.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void build(MeshPartBuilder builder, float width, float height, float depth, int divisions, float angleFrom, float angleTo) {
/* 32 */     build(builder, width, height, depth, divisions, angleFrom, angleTo, true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void build(MeshPartBuilder builder, float width, float height, float depth, int divisions, float angleFrom, float angleTo, boolean close) {
/* 38 */     builder.ensureVertices(divisions + 2);
/* 39 */     builder.ensureTriangleIndices(divisions);
/*    */     
/* 41 */     float hw = width * 0.5F;
/* 42 */     float hh = height * 0.5F;
/* 43 */     float hd = depth * 0.5F;
/* 44 */     float ao = 0.017453292F * angleFrom;
/* 45 */     float step = 0.017453292F * (angleTo - angleFrom) / divisions;
/* 46 */     float us = 1.0F / divisions;
/* 47 */     float u = 0.0F;
/* 48 */     float angle = 0.0F;
/* 49 */     MeshPartBuilder.VertexInfo curr1 = vertTmp3.set(null, null, null, null);
/* 50 */     curr1.hasUV = curr1.hasPosition = curr1.hasNormal = true;
/* 51 */     MeshPartBuilder.VertexInfo curr2 = vertTmp4.set(null, null, null, null).setPos(0.0F, hh, 0.0F).setNor(0.0F, 1.0F, 0.0F).setUV(0.5F, 0.0F);
/* 52 */     short base = builder.vertex(curr2);
/* 53 */     short i2 = 0;
/* 54 */     for (int i = 0; i <= divisions; i++) {
/* 55 */       angle = ao + step * i;
/* 56 */       u = 1.0F - us * i;
/* 57 */       curr1.position.set(MathUtils.cos(angle) * hw, 0.0F, MathUtils.sin(angle) * hd);
/* 58 */       curr1.normal.set(curr1.position).nor();
/* 59 */       curr1.position.y = -hh;
/* 60 */       curr1.uv.set(u, 1.0F);
/* 61 */       short i1 = builder.vertex(curr1);
/* 62 */       if (i != 0) builder.triangle(base, i1, i2); 
/* 63 */       i2 = i1;
/*    */     } 
/* 65 */     if (close)
/* 66 */       EllipseShapeBuilder.build(builder, width, depth, 0.0F, 0.0F, divisions, 0.0F, -hh, 0.0F, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 180.0F - angleTo, 180.0F - angleFrom); 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\ConeShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */