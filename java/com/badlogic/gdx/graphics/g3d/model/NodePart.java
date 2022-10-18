/*    */ package com.badlogic.gdx.graphics.g3d.model;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.Material;
/*    */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*    */ import com.badlogic.gdx.math.Matrix4;
/*    */ import com.badlogic.gdx.utils.ArrayMap;
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
/*    */ public class NodePart
/*    */ {
/*    */   public MeshPart meshPart;
/*    */   public Material material;
/*    */   public ArrayMap<Node, Matrix4> invBoneBindTransforms;
/*    */   public Matrix4[] bones;
/*    */   public boolean enabled = true;
/*    */   
/*    */   public NodePart() {}
/*    */   
/*    */   public NodePart(MeshPart meshPart, Material material) {
/* 52 */     this.meshPart = meshPart;
/* 53 */     this.material = material;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Renderable setRenderable(Renderable out) {
/* 64 */     out.material = this.material;
/* 65 */     out.meshPart.set(this.meshPart);
/* 66 */     out.bones = this.bones;
/* 67 */     return out;
/*    */   }
/*    */   
/*    */   public NodePart copy() {
/* 71 */     return (new NodePart()).set(this);
/*    */   }
/*    */   
/*    */   protected NodePart set(NodePart other) {
/* 75 */     this.meshPart = new MeshPart(other.meshPart);
/* 76 */     this.material = other.material;
/* 77 */     this.enabled = other.enabled;
/* 78 */     if (other.invBoneBindTransforms == null) {
/* 79 */       this.invBoneBindTransforms = null;
/* 80 */       this.bones = null;
/*    */     } else {
/* 82 */       if (this.invBoneBindTransforms == null) {
/* 83 */         this.invBoneBindTransforms = new ArrayMap(true, other.invBoneBindTransforms.size, Node.class, Matrix4.class);
/*    */       } else {
/* 85 */         this.invBoneBindTransforms.clear();
/* 86 */       }  this.invBoneBindTransforms.putAll(other.invBoneBindTransforms);
/*    */       
/* 88 */       if (this.bones == null || this.bones.length != this.invBoneBindTransforms.size) {
/* 89 */         this.bones = new Matrix4[this.invBoneBindTransforms.size];
/*    */       }
/* 91 */       for (int i = 0; i < this.bones.length; i++) {
/* 92 */         if (this.bones[i] == null)
/* 93 */           this.bones[i] = new Matrix4(); 
/*    */       } 
/*    */     } 
/* 96 */     return this;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\model\NodePart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */