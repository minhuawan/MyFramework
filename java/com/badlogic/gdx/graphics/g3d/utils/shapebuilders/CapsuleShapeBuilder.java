/*    */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*    */ import com.badlogic.gdx.utils.GdxRuntimeException;
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
/*    */ public class CapsuleShapeBuilder
/*    */   extends BaseShapeBuilder
/*    */ {
/*    */   public static void build(MeshPartBuilder builder, float radius, float height, int divisions) {
/* 26 */     if (height < 2.0F * radius) throw new GdxRuntimeException("Height must be at least twice the radius"); 
/* 27 */     float d = 2.0F * radius;
/* 28 */     CylinderShapeBuilder.build(builder, d, height - d, d, divisions, 0.0F, 360.0F, false);
/* 29 */     SphereShapeBuilder.build(builder, matTmp1.setToTranslation(0.0F, 0.5F * (height - d), 0.0F), d, d, d, divisions, divisions, 0.0F, 360.0F, 0.0F, 90.0F);
/*    */     
/* 31 */     SphereShapeBuilder.build(builder, matTmp1.setToTranslation(0.0F, -0.5F * (height - d), 0.0F), d, d, d, divisions, divisions, 0.0F, 360.0F, 90.0F, 180.0F);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\CapsuleShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */