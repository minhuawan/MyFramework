/*     */ package com.badlogic.gdx.graphics.g3d.utils.shapebuilders;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
/*     */ import com.badlogic.gdx.math.Frustum;
/*     */ import com.badlogic.gdx.math.Vector3;
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
/*     */ public class FrustumShapeBuilder
/*     */   extends BaseShapeBuilder
/*     */ {
/*     */   public static void build(MeshPartBuilder builder, Camera camera) {
/*  34 */     build(builder, camera, tmpColor0.set(1.0F, 0.66F, 0.0F, 1.0F), tmpColor1.set(1.0F, 0.0F, 0.0F, 1.0F), tmpColor2.set(0.0F, 0.66F, 1.0F, 1.0F), tmpColor3
/*  35 */         .set(1.0F, 1.0F, 1.0F, 1.0F), tmpColor4.set(0.2F, 0.2F, 0.2F, 1.0F));
/*     */   }
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
/*     */   public static void build(MeshPartBuilder builder, Camera camera, Color frustumColor, Color coneColor, Color upColor, Color targetColor, Color crossColor) {
/*  48 */     Vector3[] planePoints = camera.frustum.planePoints;
/*     */ 
/*     */     
/*  51 */     build(builder, camera.frustum, frustumColor, crossColor);
/*     */ 
/*     */     
/*  54 */     builder.line(planePoints[0], coneColor, camera.position, coneColor);
/*  55 */     builder.line(planePoints[1], coneColor, camera.position, coneColor);
/*  56 */     builder.line(planePoints[2], coneColor, camera.position, coneColor);
/*  57 */     builder.line(planePoints[3], coneColor, camera.position, coneColor);
/*     */ 
/*     */     
/*  60 */     builder.line(camera.position, targetColor, centerPoint(planePoints[4], planePoints[5], planePoints[6]), targetColor);
/*     */ 
/*     */     
/*  63 */     float halfNearSize = tmpV0.set(planePoints[1]).sub(planePoints[0]).scl(0.5F).len();
/*  64 */     Vector3 centerNear = centerPoint(planePoints[0], planePoints[1], planePoints[2]);
/*  65 */     tmpV0.set(camera.up).scl(halfNearSize * 2.0F);
/*  66 */     centerNear.add(tmpV0);
/*     */     
/*  68 */     builder.line(centerNear, upColor, planePoints[2], upColor);
/*  69 */     builder.line(planePoints[2], upColor, planePoints[3], upColor);
/*  70 */     builder.line(planePoints[3], upColor, centerNear, upColor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void build(MeshPartBuilder builder, Frustum frustum, Color frustumColor, Color crossColor) {
/*  79 */     Vector3[] planePoints = frustum.planePoints;
/*     */ 
/*     */     
/*  82 */     builder.line(planePoints[0], frustumColor, planePoints[1], frustumColor);
/*  83 */     builder.line(planePoints[1], frustumColor, planePoints[2], frustumColor);
/*  84 */     builder.line(planePoints[2], frustumColor, planePoints[3], frustumColor);
/*  85 */     builder.line(planePoints[3], frustumColor, planePoints[0], frustumColor);
/*     */ 
/*     */     
/*  88 */     builder.line(planePoints[4], frustumColor, planePoints[5], frustumColor);
/*  89 */     builder.line(planePoints[5], frustumColor, planePoints[6], frustumColor);
/*  90 */     builder.line(planePoints[6], frustumColor, planePoints[7], frustumColor);
/*  91 */     builder.line(planePoints[7], frustumColor, planePoints[4], frustumColor);
/*     */ 
/*     */     
/*  94 */     builder.line(planePoints[0], frustumColor, planePoints[4], frustumColor);
/*  95 */     builder.line(planePoints[1], frustumColor, planePoints[5], frustumColor);
/*  96 */     builder.line(planePoints[2], frustumColor, planePoints[6], frustumColor);
/*  97 */     builder.line(planePoints[3], frustumColor, planePoints[7], frustumColor);
/*     */ 
/*     */     
/* 100 */     builder.line(middlePoint(planePoints[1], planePoints[0]), crossColor, middlePoint(planePoints[3], planePoints[2]), crossColor);
/*     */     
/* 102 */     builder.line(middlePoint(planePoints[2], planePoints[1]), crossColor, middlePoint(planePoints[3], planePoints[0]), crossColor);
/*     */ 
/*     */ 
/*     */     
/* 106 */     builder.line(middlePoint(planePoints[5], planePoints[4]), crossColor, middlePoint(planePoints[7], planePoints[6]), crossColor);
/*     */     
/* 108 */     builder.line(middlePoint(planePoints[6], planePoints[5]), crossColor, middlePoint(planePoints[7], planePoints[4]), crossColor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Vector3 middlePoint(Vector3 point0, Vector3 point1) {
/* 117 */     tmpV0.set(point1).sub(point0).scl(0.5F);
/* 118 */     return tmpV1.set(point0).add(tmpV0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Vector3 centerPoint(Vector3 point0, Vector3 point1, Vector3 point2) {
/* 127 */     tmpV0.set(point1).sub(point0).scl(0.5F);
/* 128 */     tmpV1.set(point0).add(tmpV0);
/* 129 */     tmpV0.set(point2).sub(point1).scl(0.5F);
/* 130 */     return tmpV1.add(tmpV0);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\shapebuilders\FrustumShapeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */