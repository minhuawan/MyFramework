/*    */ package com.badlogic.gdx.graphics.g3d.environment;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Color;
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
/*    */ public class SphericalHarmonics
/*    */ {
/* 24 */   private static final float[] coeff = new float[] { 0.282095F, 0.488603F, 0.488603F, 0.488603F, 1.092548F, 1.092548F, 1.092548F, 0.315392F, 0.546274F };
/*    */   public final float[] data;
/*    */   
/*    */   private static final float clamp(float v) {
/* 28 */     return (v < 0.0F) ? 0.0F : ((v > 1.0F) ? 1.0F : v);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SphericalHarmonics() {
/* 34 */     this.data = new float[27];
/*    */   }
/*    */   
/*    */   public SphericalHarmonics(float[] copyFrom) {
/* 38 */     if (copyFrom.length != 27) throw new GdxRuntimeException("Incorrect array size"); 
/* 39 */     this.data = (float[])copyFrom.clone();
/*    */   }
/*    */   
/*    */   public SphericalHarmonics set(float[] values) {
/* 43 */     for (int i = 0; i < this.data.length; i++)
/* 44 */       this.data[i] = values[i]; 
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public SphericalHarmonics set(AmbientCubemap other) {
/* 49 */     return set(other.data);
/*    */   }
/*    */   
/*    */   public SphericalHarmonics set(Color color) {
/* 53 */     return set(color.r, color.g, color.b);
/*    */   }
/*    */   
/*    */   public SphericalHarmonics set(float r, float g, float b) {
/* 57 */     for (int idx = 0; idx < this.data.length; ) {
/* 58 */       this.data[idx++] = r;
/* 59 */       this.data[idx++] = g;
/* 60 */       this.data[idx++] = b;
/*    */     } 
/* 62 */     return this;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\environment\SphericalHarmonics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */