/*    */ package com.badlogic.gdx.graphics.g3d.environment;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Color;
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
/*    */ public class PointLight
/*    */   extends BaseLight<PointLight>
/*    */ {
/* 23 */   public final Vector3 position = new Vector3();
/*    */   public float intensity;
/*    */   
/*    */   public PointLight setPosition(float positionX, float positionY, float positionZ) {
/* 27 */     this.position.set(positionX, positionY, positionZ);
/* 28 */     return this;
/*    */   }
/*    */   
/*    */   public PointLight setPosition(Vector3 position) {
/* 32 */     this.position.set(position);
/* 33 */     return this;
/*    */   }
/*    */   
/*    */   public PointLight setIntensity(float intensity) {
/* 37 */     this.intensity = intensity;
/* 38 */     return this;
/*    */   }
/*    */   
/*    */   public PointLight set(PointLight copyFrom) {
/* 42 */     return set(copyFrom.color, copyFrom.position, copyFrom.intensity);
/*    */   }
/*    */   
/*    */   public PointLight set(Color color, Vector3 position, float intensity) {
/* 46 */     if (color != null) this.color.set(color); 
/* 47 */     if (position != null) this.position.set(position); 
/* 48 */     this.intensity = intensity;
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public PointLight set(float r, float g, float b, Vector3 position, float intensity) {
/* 53 */     this.color.set(r, g, b, 1.0F);
/* 54 */     if (position != null) this.position.set(position); 
/* 55 */     this.intensity = intensity;
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public PointLight set(Color color, float x, float y, float z, float intensity) {
/* 60 */     if (color != null) this.color.set(color); 
/* 61 */     this.position.set(x, y, z);
/* 62 */     this.intensity = intensity;
/* 63 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public PointLight set(float r, float g, float b, float x, float y, float z, float intensity) {
/* 68 */     this.color.set(r, g, b, 1.0F);
/* 69 */     this.position.set(x, y, z);
/* 70 */     this.intensity = intensity;
/* 71 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 76 */     return (obj instanceof PointLight) ? equals((PointLight)obj) : false;
/*    */   }
/*    */   
/*    */   public boolean equals(PointLight other) {
/* 80 */     return (other != null && (other == this || (this.color.equals(other.color) && this.position.equals(other.position) && this.intensity == other.intensity)));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\environment\PointLight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */