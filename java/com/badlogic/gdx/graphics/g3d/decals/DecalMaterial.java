/*    */ package com.badlogic.gdx.graphics.g3d.decals;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
/*    */ public class DecalMaterial
/*    */ {
/*    */   public static final int NO_BLEND = -1;
/*    */   protected TextureRegion textureRegion;
/*    */   protected int srcBlendFactor;
/*    */   protected int dstBlendFactor;
/*    */   
/*    */   public void set() {
/* 31 */     this.textureRegion.getTexture().bind(0);
/* 32 */     if (!isOpaque()) {
/* 33 */       Gdx.gl.glBlendFunc(this.srcBlendFactor, this.dstBlendFactor);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpaque() {
/* 39 */     return (this.srcBlendFactor == -1);
/*    */   }
/*    */   
/*    */   public int getSrcBlendFactor() {
/* 43 */     return this.srcBlendFactor;
/*    */   }
/*    */   
/*    */   public int getDstBlendFactor() {
/* 47 */     return this.dstBlendFactor;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 52 */     if (o == null) return false;
/*    */     
/* 54 */     DecalMaterial material = (DecalMaterial)o;
/*    */     
/* 56 */     return (this.dstBlendFactor == material.dstBlendFactor && this.srcBlendFactor == material.srcBlendFactor && this.textureRegion
/* 57 */       .getTexture() == material.textureRegion.getTexture());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     int result = (this.textureRegion.getTexture() != null) ? this.textureRegion.getTexture().hashCode() : 0;
/* 64 */     result = 31 * result + this.srcBlendFactor;
/* 65 */     result = 31 * result + this.dstBlendFactor;
/* 66 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\decals\DecalMaterial.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */