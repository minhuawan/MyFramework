/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
/*    */ import com.badlogic.gdx.math.MathUtils;
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
/*    */ public final class CylinderSpawnShapeValue
/*    */   extends PrimitiveSpawnShapeValue
/*    */ {
/*    */   public CylinderSpawnShapeValue(CylinderSpawnShapeValue cylinderSpawnShapeValue) {
/* 27 */     super(cylinderSpawnShapeValue);
/* 28 */     load(cylinderSpawnShapeValue);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CylinderSpawnShapeValue() {}
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 37 */     float radiusX, radiusZ, width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/* 38 */     float height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/* 39 */     float depth = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*    */ 
/*    */     
/* 42 */     float hf = height / 2.0F;
/* 43 */     float ty = MathUtils.random(height) - hf;
/*    */ 
/*    */     
/* 46 */     if (this.edges) {
/* 47 */       radiusX = width / 2.0F;
/* 48 */       radiusZ = depth / 2.0F;
/*    */     } else {
/* 50 */       radiusX = MathUtils.random(width) / 2.0F;
/* 51 */       radiusZ = MathUtils.random(depth) / 2.0F;
/*    */     } 
/*    */     
/* 54 */     float spawnTheta = 0.0F;
/*    */ 
/*    */     
/* 57 */     boolean isRadiusXZero = (radiusX == 0.0F), isRadiusZZero = (radiusZ == 0.0F);
/* 58 */     if (!isRadiusXZero && !isRadiusZZero)
/* 59 */     { spawnTheta = MathUtils.random(360.0F); }
/*    */     
/* 61 */     else if (isRadiusXZero)
/* 62 */     { spawnTheta = (MathUtils.random(1) == 0) ? -90.0F : 90.0F; }
/* 63 */     else if (isRadiusZZero) { spawnTheta = (MathUtils.random(1) == 0) ? 0.0F : 180.0F; }
/*    */ 
/*    */     
/* 66 */     vector.set(radiusX * MathUtils.cosDeg(spawnTheta), ty, radiusZ * MathUtils.sinDeg(spawnTheta));
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 71 */     return new CylinderSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\CylinderSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */