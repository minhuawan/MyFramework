/*    */ package com.badlogic.gdx.graphics.g3d.particles.values;
/*    */ 
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
/*    */ public final class PointSpawnShapeValue
/*    */   extends PrimitiveSpawnShapeValue
/*    */ {
/*    */   public PointSpawnShapeValue(PointSpawnShapeValue value) {
/* 26 */     super(value);
/* 27 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public PointSpawnShapeValue() {}
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 35 */     vector.x = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/* 36 */     vector.y = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/* 37 */     vector.z = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 42 */     return new PointSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\PointSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */