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
/*    */ public class DirectionalLight
/*    */   extends BaseLight<DirectionalLight>
/*    */ {
/* 23 */   public final Vector3 direction = new Vector3();
/*    */   
/*    */   public DirectionalLight setDirection(float directionX, float directionY, float directionZ) {
/* 26 */     this.direction.set(directionX, directionY, directionZ);
/* 27 */     return this;
/*    */   }
/*    */   
/*    */   public DirectionalLight setDirection(Vector3 direction) {
/* 31 */     this.direction.set(direction);
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public DirectionalLight set(DirectionalLight copyFrom) {
/* 36 */     return set(copyFrom.color, copyFrom.direction);
/*    */   }
/*    */   
/*    */   public DirectionalLight set(Color color, Vector3 direction) {
/* 40 */     if (color != null) this.color.set(color); 
/* 41 */     if (direction != null) this.direction.set(direction).nor(); 
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public DirectionalLight set(float r, float g, float b, Vector3 direction) {
/* 46 */     this.color.set(r, g, b, 1.0F);
/* 47 */     if (direction != null) this.direction.set(direction).nor(); 
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   public DirectionalLight set(Color color, float dirX, float dirY, float dirZ) {
/* 52 */     if (color != null) this.color.set(color); 
/* 53 */     this.direction.set(dirX, dirY, dirZ).nor();
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public DirectionalLight set(float r, float g, float b, float dirX, float dirY, float dirZ) {
/* 58 */     this.color.set(r, g, b, 1.0F);
/* 59 */     this.direction.set(dirX, dirY, dirZ).nor();
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object arg0) {
/* 65 */     return (arg0 instanceof DirectionalLight) ? equals((DirectionalLight)arg0) : false;
/*    */   }
/*    */   
/*    */   public boolean equals(DirectionalLight other) {
/* 69 */     return (other != null && (other == this || (this.color.equals(other.color) && this.direction.equals(other.direction))));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\environment\DirectionalLight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */