/*    */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*    */ import com.badlogic.gdx.math.Vector3;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PatchShapeBuilder
/*    */   extends BaseShapeBuilder
/*    */ {
/*    */   public static void build(MeshPartBuilder builder, MeshPartBuilder.VertexInfo corner00, MeshPartBuilder.VertexInfo corner10, MeshPartBuilder.VertexInfo corner11, MeshPartBuilder.VertexInfo corner01, int divisionsU, int divisionsV) {
/* 30 */     if (divisionsU < 1 || divisionsV < 1) {
/* 31 */       throw new GdxRuntimeException("divisionsU and divisionV must be > 0, u,v: " + divisionsU + ", " + divisionsV);
/*    */     }
/* 33 */     builder.ensureVertices((divisionsV + 1) * (divisionsU + 1));
/* 34 */     builder.ensureRectangleIndices(divisionsV * divisionsU);
/* 35 */     for (int u = 0; u <= divisionsU; u++) {
/* 36 */       float alphaU = u / divisionsU;
/* 37 */       vertTmp5.set(corner00).lerp(corner10, alphaU);
/* 38 */       vertTmp6.set(corner01).lerp(corner11, alphaU);
/* 39 */       for (int v = 0; v <= divisionsV; v++) {
/* 40 */         short idx = builder.vertex(vertTmp7.set(vertTmp5).lerp(vertTmp6, v / divisionsV));
/* 41 */         if (u > 0 && v > 0) builder.rect((short)(idx - divisionsV - 2), (short)(idx - 1), idx, (short)(idx - divisionsV - 1));
/*    */       
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void build(MeshPartBuilder builder, Vector3 corner00, Vector3 corner10, Vector3 corner11, Vector3 corner01, Vector3 normal, int divisionsU, int divisionsV) {
/* 49 */     build(builder, vertTmp1.set(corner00, normal, null, null).setUV(0.0F, 1.0F), vertTmp2.set(corner10, normal, null, null).setUV(1.0F, 1.0F), vertTmp3
/* 50 */         .set(corner11, normal, null, null).setUV(1.0F, 0.0F), vertTmp4.set(corner01, normal, null, null).setUV(0.0F, 0.0F), divisionsU, divisionsV);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void build(MeshPartBuilder builder, float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ, int divisionsU, int divisionsV) {
/* 57 */     build(builder, vertTmp1.set(null).setPos(x00, y00, z00).setNor(normalX, normalY, normalZ).setUV(0.0F, 1.0F), vertTmp2
/* 58 */         .set(null).setPos(x10, y10, z10).setNor(normalX, normalY, normalZ).setUV(1.0F, 1.0F), vertTmp3
/* 59 */         .set(null).setPos(x11, y11, z11).setNor(normalX, normalY, normalZ).setUV(1.0F, 0.0F), vertTmp4
/* 60 */         .set(null).setPos(x01, y01, z01).setNor(normalX, normalY, normalZ).setUV(0.0F, 0.0F), divisionsU, divisionsV);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\PatchShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */