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
/*    */ public final class RectangleSpawnShapeValue
/*    */   extends PrimitiveSpawnShapeValue
/*    */ {
/*    */   public RectangleSpawnShapeValue(RectangleSpawnShapeValue value) {
/* 26 */     super(value);
/* 27 */     load(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public RectangleSpawnShapeValue() {}
/*    */ 
/*    */   
/*    */   public void spawnAux(Vector3 vector, float percent) {
/* 35 */     float width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/* 36 */     float height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/* 37 */     float depth = this.spawnDepth + this.spawnDepthDiff * this.spawnDepthValue.getScale(percent);
/*    */     
/* 39 */     if (this.edges) {
/* 40 */       int a = MathUtils.random(-1, 1);
/* 41 */       float tx = 0.0F, ty = 0.0F, tz = 0.0F;
/* 42 */       if (a == -1) {
/* 43 */         tx = (MathUtils.random(1) == 0) ? (-width / 2.0F) : (width / 2.0F);
/* 44 */         if (tx == 0.0F) {
/* 45 */           ty = (MathUtils.random(1) == 0) ? (-height / 2.0F) : (height / 2.0F);
/* 46 */           tz = (MathUtils.random(1) == 0) ? (-depth / 2.0F) : (depth / 2.0F);
/*    */         } else {
/* 48 */           ty = MathUtils.random(height) - height / 2.0F;
/* 49 */           tz = MathUtils.random(depth) - depth / 2.0F;
/*    */         } 
/* 51 */       } else if (a == 0) {
/*    */         
/* 53 */         tz = (MathUtils.random(1) == 0) ? (-depth / 2.0F) : (depth / 2.0F);
/* 54 */         if (tz == 0.0F) {
/* 55 */           ty = (MathUtils.random(1) == 0) ? (-height / 2.0F) : (height / 2.0F);
/* 56 */           tx = (MathUtils.random(1) == 0) ? (-width / 2.0F) : (width / 2.0F);
/*    */         } else {
/* 58 */           ty = MathUtils.random(height) - height / 2.0F;
/* 59 */           tx = MathUtils.random(width) - width / 2.0F;
/*    */         } 
/*    */       } else {
/*    */         
/* 63 */         ty = (MathUtils.random(1) == 0) ? (-height / 2.0F) : (height / 2.0F);
/* 64 */         if (ty == 0.0F) {
/* 65 */           tx = (MathUtils.random(1) == 0) ? (-width / 2.0F) : (width / 2.0F);
/* 66 */           tz = (MathUtils.random(1) == 0) ? (-depth / 2.0F) : (depth / 2.0F);
/*    */         } else {
/* 68 */           tx = MathUtils.random(width) - width / 2.0F;
/* 69 */           tz = MathUtils.random(depth) - depth / 2.0F;
/*    */         } 
/*    */       } 
/* 72 */       vector.x = tx;
/* 73 */       vector.y = ty;
/* 74 */       vector.z = tz;
/*    */     } else {
/* 76 */       vector.x = MathUtils.random(width) - width / 2.0F;
/* 77 */       vector.y = MathUtils.random(height) - height / 2.0F;
/* 78 */       vector.z = MathUtils.random(depth) - depth / 2.0F;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnShapeValue copy() {
/* 84 */     return new RectangleSpawnShapeValue(this);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\values\RectangleSpawnShapeValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */