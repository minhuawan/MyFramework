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
/*    */ public final class LineSpawnShapeValue
/*    */   extends PrimitiveSpawnShapeValue
/*    */ {
/*    */   public LineSpawnShapeValue(LineSpawnShapeValue value) {
/* 27 */     super(value);
/* 28 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public LineSpawnShapeValue() {}
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 36 */     float width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/* 37 */     float height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/* 38 */     float depth = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*    */     
/* 40 */     float a = MathUtils.random();
/* 41 */     vector.x = a * width;
/* 42 */     vector.y = a * height;
/* 43 */     vector.z = a * depth;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 48 */     return new LineSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\LineSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */