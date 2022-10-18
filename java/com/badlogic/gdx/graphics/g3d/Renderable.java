/*    */ package com.badlogic.gdx.graphics.g3d;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.model.MeshPart;
/*    */ import com.badlogic.gdx.math.Matrix4;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Renderable
/*    */ {
/* 68 */   public final Matrix4 worldTransform = new Matrix4();
/*    */   
/* 70 */   public final MeshPart meshPart = new MeshPart();
/*    */ 
/*    */ 
/*    */   
/*    */   public Material material;
/*    */ 
/*    */   
/*    */   public Environment environment;
/*    */ 
/*    */   
/*    */   public Matrix4[] bones;
/*    */ 
/*    */   
/*    */   public Shader shader;
/*    */ 
/*    */   
/*    */   public Object userData;
/*    */ 
/*    */ 
/*    */   
/*    */   public Renderable set(Renderable renderable) {
/* 91 */     this.worldTransform.set(renderable.worldTransform);
/* 92 */     this.material = renderable.material;
/* 93 */     this.meshPart.set(renderable.meshPart);
/* 94 */     this.bones = renderable.bones;
/* 95 */     this.environment = renderable.environment;
/* 96 */     this.shader = renderable.shader;
/* 97 */     this.userData = renderable.userData;
/* 98 */     return this;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\Renderable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */