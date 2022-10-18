/*    */ package com.badlogic.gdx.graphics;
/*    */ 
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
/*    */ public class PerspectiveCamera
/*    */   extends Camera
/*    */ {
/* 27 */   public float fieldOfView = 67.0F;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   final Vector3 tmp;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PerspectiveCamera() {
/* 46 */     this.tmp = new Vector3(); } public PerspectiveCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) { this.tmp = new Vector3();
/*    */     this.fieldOfView = fieldOfViewY;
/*    */     this.viewportWidth = viewportWidth;
/*    */     this.viewportHeight = viewportHeight;
/* 50 */     update(); } public void update() { update(true); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void update(boolean updateFrustum) {
/* 55 */     float aspect = this.viewportWidth / this.viewportHeight;
/* 56 */     this.projection.setToProjection(Math.abs(this.near), Math.abs(this.far), this.fieldOfView, aspect);
/* 57 */     this.view.setToLookAt(this.position, this.tmp.set(this.position).add(this.direction), this.up);
/* 58 */     this.combined.set(this.projection);
/* 59 */     Matrix4.mul(this.combined.val, this.view.val);
/*    */     
/* 61 */     if (updateFrustum) {
/* 62 */       this.invProjectionView.set(this.combined);
/* 63 */       Matrix4.inv(this.invProjectionView.val);
/* 64 */       this.frustum.update(this.invProjectionView);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\PerspectiveCamera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */