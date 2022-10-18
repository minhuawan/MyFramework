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
/*    */ 
/*    */ public class CylinderShapeBuilder
/*    */   extends BaseShapeBuilder
/*    */ {
/*    */   public static void build(MeshPartBuilder builder, float width, float height, float depth, int divisions) {
/* 28 */     build(builder, width, height, depth, divisions, 0.0F, 360.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void build(MeshPartBuilder builder, float width, float height, float depth, int divisions, float angleFrom, float angleTo) {
/* 33 */     build(builder, width, height, depth, divisions, angleFrom, angleTo, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void build(MeshPartBuilder builder, float width, float height, float depth, int divisions, float angleFrom, float angleTo, boolean close) {
/* 40 */     float hw = width * 0.5F;
/* 41 */     float hh = height * 0.5F;
/* 42 */     float hd = depth * 0.5F;
/* 43 */     float ao = 0.017453292F * angleFrom;
/* 44 */     float step = 0.017453292F * (angleTo - angleFrom) / divisions;
/* 45 */     float us = 1.0F / divisions;
/* 46 */     float u = 0.0F;
/* 47 */     float angle = 0.0F;
/* 48 */     MeshPartBuilder.VertexInfo curr1 = vertTmp3.set(null, null, null, null);
/* 49 */     curr1.hasUV = curr1.hasPosition = curr1.hasNormal = true;
/* 50 */     MeshPartBuilder.VertexInfo curr2 = vertTmp4.set(null, null, null, null);
/* 51 */     curr2.hasUV = curr2.hasPosition = curr2.hasNormal = true;
/* 52 */     short i3 = 0, i4 = 0;
/*    */     
/* 54 */     builder.ensureVertices(2 * (divisions + 1));
/* 55 */     builder.ensureRectangleIndices(divisions);
/* 56 */     for (int i = 0; i <= divisions; i++) {
/* 57 */       angle = ao + step * i;
/* 58 */       u = 1.0F - us * i;
/* 59 */       curr1.position.set(MathUtils.cos(angle) * hw, 0.0F, MathUtils.sin(angle) * hd);
/* 60 */       curr1.normal.set(curr1.position).nor();
/* 61 */       curr1.position.y = -hh;
/* 62 */       curr1.uv.set(u, 1.0F);
/* 63 */       curr2.position.set(curr1.position);
/* 64 */       curr2.normal.set(curr1.normal);
/* 65 */       curr2.position.y = hh;
/* 66 */       curr2.uv.set(u, 0.0F);
/* 67 */       short i2 = builder.vertex(curr1);
/* 68 */       short i1 = builder.vertex(curr2);
/* 69 */       if (i != 0) builder.rect(i3, i1, i2, i4); 
/* 70 */       i4 = i2;
/* 71 */       i3 = i1;
/*    */     } 
/* 73 */     if (close) {
/* 74 */       EllipseShapeBuilder.build(builder, width, depth, 0.0F, 0.0F, divisions, 0.0F, hh, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, angleFrom, angleTo);
/*    */       
/* 76 */       EllipseShapeBuilder.build(builder, width, depth, 0.0F, 0.0F, divisions, 0.0F, -hh, 0.0F, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 180.0F - angleTo, 180.0F - angleFrom);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\CylinderShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */