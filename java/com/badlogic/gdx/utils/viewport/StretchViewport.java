/*    */ package com.badlogic.gdx.utils.viewport;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Camera;
/*    */ import com.badlogic.gdx.utils.Scaling;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StretchViewport
/*    */   extends ScalingViewport
/*    */ {
/*    */   public StretchViewport(float worldWidth, float worldHeight) {
/* 15 */     super(Scaling.stretch, worldWidth, worldHeight);
/*    */   }
/*    */   
/*    */   public StretchViewport(float worldWidth, float worldHeight, Camera camera) {
/* 19 */     super(Scaling.stretch, worldWidth, worldHeight, camera);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\viewport\StretchViewport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */