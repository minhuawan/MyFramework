/*    */ package com.badlogic.gdx.utils.viewport;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Camera;
/*    */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*    */ import com.badlogic.gdx.math.Vector2;
/*    */ import com.badlogic.gdx.utils.Scaling;
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
/*    */ public class ScalingViewport
/*    */   extends Viewport
/*    */ {
/*    */   private Scaling scaling;
/*    */   
/*    */   public ScalingViewport(Scaling scaling, float worldWidth, float worldHeight) {
/* 44 */     this(scaling, worldWidth, worldHeight, (Camera)new OrthographicCamera());
/*    */   }
/*    */   
/*    */   public ScalingViewport(Scaling scaling, float worldWidth, float worldHeight, Camera camera) {
/* 48 */     this.scaling = scaling;
/* 49 */     setWorldSize(worldWidth, worldHeight);
/* 50 */     setCamera(camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(int screenWidth, int screenHeight, boolean centerCamera) {
/* 55 */     Vector2 scaled = this.scaling.apply(getWorldWidth(), getWorldHeight(), screenWidth, screenHeight);
/* 56 */     int viewportWidth = Math.round(scaled.x);
/* 57 */     int viewportHeight = Math.round(scaled.y);
/*    */ 
/*    */     
/* 60 */     setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);
/*    */     
/* 62 */     apply(centerCamera);
/*    */   }
/*    */   
/*    */   public Scaling getScaling() {
/* 66 */     return this.scaling;
/*    */   }
/*    */   
/*    */   public void setScaling(Scaling scaling) {
/* 70 */     this.scaling = scaling;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\viewport\ScalingViewport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */