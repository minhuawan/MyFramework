/*     */ package com.badlogic.gdx.graphics.g3d.environment;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpotLight
/*     */   extends BaseLight<SpotLight>
/*     */ {
/*  26 */   public final Vector3 position = new Vector3();
/*  27 */   public final Vector3 direction = new Vector3();
/*     */   public float intensity;
/*     */   public float cutoffAngle;
/*     */   public float exponent;
/*     */   
/*     */   public SpotLight setPosition(float positionX, float positionY, float positionZ) {
/*  33 */     this.position.set(positionX, positionY, positionZ);
/*  34 */     return this;
/*     */   }
/*     */   
/*     */   public SpotLight setPosition(Vector3 position) {
/*  38 */     this.position.set(position);
/*  39 */     return this;
/*     */   }
/*     */   
/*     */   public SpotLight setDirection(float directionX, float directionY, float directionZ) {
/*  43 */     this.direction.set(directionX, directionY, directionZ);
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public SpotLight setDirection(Vector3 direction) {
/*  48 */     this.direction.set(direction);
/*  49 */     return this;
/*     */   }
/*     */   
/*     */   public SpotLight setIntensity(float intensity) {
/*  53 */     this.intensity = intensity;
/*  54 */     return this;
/*     */   }
/*     */   
/*     */   public SpotLight setCutoffAngle(float cutoffAngle) {
/*  58 */     this.cutoffAngle = cutoffAngle;
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public SpotLight setExponent(float exponent) {
/*  63 */     this.exponent = exponent;
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public SpotLight set(SpotLight copyFrom) {
/*  68 */     return set(copyFrom.color, copyFrom.position, copyFrom.direction, copyFrom.intensity, copyFrom.cutoffAngle, copyFrom.exponent);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpotLight set(Color color, Vector3 position, Vector3 direction, float intensity, float cutoffAngle, float exponent) {
/*  73 */     if (color != null) this.color.set(color); 
/*  74 */     if (position != null) this.position.set(position); 
/*  75 */     if (direction != null) this.direction.set(direction).nor(); 
/*  76 */     this.intensity = intensity;
/*  77 */     this.cutoffAngle = cutoffAngle;
/*  78 */     this.exponent = exponent;
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpotLight set(float r, float g, float b, Vector3 position, Vector3 direction, float intensity, float cutoffAngle, float exponent) {
/*  84 */     this.color.set(r, g, b, 1.0F);
/*  85 */     if (position != null) this.position.set(position); 
/*  86 */     if (direction != null) this.direction.set(direction).nor(); 
/*  87 */     this.intensity = intensity;
/*  88 */     this.cutoffAngle = cutoffAngle;
/*  89 */     this.exponent = exponent;
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpotLight set(Color color, float posX, float posY, float posZ, float dirX, float dirY, float dirZ, float intensity, float cutoffAngle, float exponent) {
/*  95 */     if (color != null) this.color.set(color); 
/*  96 */     this.position.set(posX, posY, posZ);
/*  97 */     this.direction.set(dirX, dirY, dirZ).nor();
/*  98 */     this.intensity = intensity;
/*  99 */     this.cutoffAngle = cutoffAngle;
/* 100 */     this.exponent = exponent;
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpotLight set(float r, float g, float b, float posX, float posY, float posZ, float dirX, float dirY, float dirZ, float intensity, float cutoffAngle, float exponent) {
/* 106 */     this.color.set(r, g, b, 1.0F);
/* 107 */     this.position.set(posX, posY, posZ);
/* 108 */     this.direction.set(dirX, dirY, dirZ).nor();
/* 109 */     this.intensity = intensity;
/* 110 */     this.cutoffAngle = cutoffAngle;
/* 111 */     this.exponent = exponent;
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public SpotLight setTarget(Vector3 target) {
/* 116 */     this.direction.set(target).sub(this.position).nor();
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 122 */     return (obj instanceof SpotLight) ? equals((SpotLight)obj) : false;
/*     */   }
/*     */   
/*     */   public boolean equals(SpotLight other) {
/* 126 */     return (other != null && (other == this || (this.color.equals(other.color) && this.position.equals(other.position) && this.direction
/* 127 */       .equals(other.direction) && MathUtils.isEqual(this.intensity, other.intensity) && MathUtils.isEqual(this.cutoffAngle, other.cutoffAngle) && 
/* 128 */       MathUtils.isEqual(this.exponent, other.exponent))));
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\environment\SpotLight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */