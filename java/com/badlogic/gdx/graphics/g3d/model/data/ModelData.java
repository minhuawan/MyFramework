/*    */ package com.badlogic.gdx.graphics.g3d.model.data;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ 
/*    */ public class ModelData
/*    */ {
/*    */   public String id;
/* 29 */   public final short[] version = new short[2];
/* 30 */   public final Array<ModelMesh> meshes = new Array();
/* 31 */   public final Array<ModelMaterial> materials = new Array();
/* 32 */   public final Array<ModelNode> nodes = new Array();
/* 33 */   public final Array<ModelAnimation> animations = new Array();
/*    */   
/*    */   public void addMesh(ModelMesh mesh) {
/* 36 */     for (ModelMesh other : this.meshes) {
/* 37 */       if (other.id.equals(mesh.id)) {
/* 38 */         throw new GdxRuntimeException("Mesh with id '" + other.id + "' already in model");
/*    */       }
/*    */     } 
/* 41 */     this.meshes.add(mesh);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\model\data\ModelData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */