/*    */ package com.badlogic.gdx.graphics.g3d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Camera;
/*    */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*    */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*    */ import com.badlogic.gdx.math.Matrix4;
/*    */ import com.badlogic.gdx.math.Vector3;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import java.util.Comparator;
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
/*    */ public class DefaultRenderableSorter
/*    */   implements RenderableSorter, Comparator<Renderable>
/*    */ {
/*    */   private Camera camera;
/* 30 */   private final Vector3 tmpV1 = new Vector3();
/* 31 */   private final Vector3 tmpV2 = new Vector3();
/*    */ 
/*    */   
/*    */   public void sort(Camera camera, Array<Renderable> renderables) {
/* 35 */     this.camera = camera;
/* 36 */     renderables.sort(this);
/*    */   }
/*    */   
/*    */   private Vector3 getTranslation(Matrix4 worldTransform, Vector3 center, Vector3 output) {
/* 40 */     if (center.isZero()) {
/* 41 */       worldTransform.getTranslation(output);
/* 42 */     } else if (!worldTransform.hasRotationOrScaling()) {
/* 43 */       worldTransform.getTranslation(output).add(center);
/*    */     } else {
/* 45 */       output.set(center).mul(worldTransform);
/* 46 */     }  return output;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(Renderable o1, Renderable o2) {
/* 51 */     boolean b1 = (o1.material.has(BlendingAttribute.Type) && ((BlendingAttribute)o1.material.get(BlendingAttribute.Type)).blended);
/* 52 */     boolean b2 = (o2.material.has(BlendingAttribute.Type) && ((BlendingAttribute)o2.material.get(BlendingAttribute.Type)).blended);
/* 53 */     if (b1 != b2) return b1 ? 1 : -1;
/*    */ 
/*    */ 
/*    */     
/* 57 */     getTranslation(o1.worldTransform, o1.meshPart.center, this.tmpV1);
/* 58 */     getTranslation(o2.worldTransform, o2.meshPart.center, this.tmpV2);
/* 59 */     float dst = ((int)(1000.0F * this.camera.position.dst2(this.tmpV1)) - (int)(1000.0F * this.camera.position.dst2(this.tmpV2)));
/* 60 */     int result = (dst < 0.0F) ? -1 : ((dst > 0.0F) ? 1 : 0);
/* 61 */     return b1 ? -result : result;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\DefaultRenderableSorter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */