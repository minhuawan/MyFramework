/*    */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*    */ import com.badlogic.gdx.math.Matrix4;
/*    */ import com.badlogic.gdx.math.Vector3;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArrowShapeBuilder
/*    */   extends BaseShapeBuilder
/*    */ {
/*    */   public static void build(MeshPartBuilder builder, float x1, float y1, float z1, float x2, float y2, float z2, float capLength, float stemThickness, int divisions) {
/* 37 */     Vector3 begin = obtainV3().set(x1, y1, z1), end = obtainV3().set(x2, y2, z2);
/* 38 */     float length = begin.dst(end);
/* 39 */     float coneHeight = length * capLength;
/* 40 */     float coneDiameter = 2.0F * (float)(coneHeight * Math.sqrt(0.3333333432674408D));
/* 41 */     float stemLength = length - coneHeight;
/* 42 */     float stemDiameter = coneDiameter * stemThickness;
/*    */     
/* 44 */     Vector3 up = obtainV3().set(end).sub(begin).nor();
/* 45 */     Vector3 forward = obtainV3().set(up).crs(Vector3.Z);
/* 46 */     if (forward.isZero()) forward.set(Vector3.X); 
/* 47 */     forward.crs(up).nor();
/* 48 */     Vector3 left = obtainV3().set(up).crs(forward).nor();
/* 49 */     Vector3 direction = obtainV3().set(end).sub(begin).nor();
/*    */ 
/*    */     
/* 52 */     Matrix4 userTransform = builder.getVertexTransform(obtainM4());
/* 53 */     Matrix4 transform = obtainM4();
/* 54 */     float[] val = transform.val;
/* 55 */     val[0] = left.x;
/* 56 */     val[4] = up.x;
/* 57 */     val[8] = forward.x;
/* 58 */     val[1] = left.y;
/* 59 */     val[5] = up.y;
/* 60 */     val[9] = forward.y;
/* 61 */     val[2] = left.z;
/* 62 */     val[6] = up.z;
/* 63 */     val[10] = forward.z;
/* 64 */     Matrix4 temp = obtainM4();
/*    */ 
/*    */     
/* 67 */     transform.setTranslation(obtainV3().set(direction).scl(stemLength / 2.0F).add(x1, y1, z1));
/* 68 */     builder.setVertexTransform(temp.set(transform).mul(userTransform));
/* 69 */     CylinderShapeBuilder.build(builder, stemDiameter, stemLength, stemDiameter, divisions);
/*    */ 
/*    */     
/* 72 */     transform.setTranslation(obtainV3().set(direction).scl(stemLength).add(x1, y1, z1));
/* 73 */     builder.setVertexTransform(temp.set(transform).mul(userTransform));
/* 74 */     ConeShapeBuilder.build(builder, coneDiameter, coneHeight, coneDiameter, divisions);
/*    */     
/* 76 */     builder.setVertexTransform(userTransform);
/* 77 */     freeAll();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\ArrowShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */