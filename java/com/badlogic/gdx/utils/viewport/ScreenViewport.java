/*    */ package com.badlogic.gdx.utils.viewport;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Camera;
/*    */ import com.badlogic.gdx.graphics.OrthographicCamera;
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
/*    */ public class ScreenViewport
/*    */   extends Viewport
/*    */ {
/* 27 */   private float unitsPerPixel = 1.0F;
/*    */ 
/*    */   
/*    */   public ScreenViewport() {
/* 31 */     this((Camera)new OrthographicCamera());
/*    */   }
/*    */   
/*    */   public ScreenViewport(Camera camera) {
/* 35 */     setCamera(camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(int screenWidth, int screenHeight, boolean centerCamera) {
/* 40 */     setScreenBounds(0, 0, screenWidth, screenHeight);
/* 41 */     setWorldSize(screenWidth * this.unitsPerPixel, screenHeight * this.unitsPerPixel);
/* 42 */     apply(centerCamera);
/*    */   }
/*    */   
/*    */   public float getUnitsPerPixel() {
/* 46 */     return this.unitsPerPixel;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setUnitsPerPixel(float unitsPerPixel) {
/* 52 */     this.unitsPerPixel = unitsPerPixel;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\viewport\ScreenViewport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */