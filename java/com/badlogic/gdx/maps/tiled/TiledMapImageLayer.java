/*    */ package com.badlogic.gdx.maps.tiled;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*    */ import com.badlogic.gdx.maps.MapLayer;
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
/*    */ public class TiledMapImageLayer
/*    */   extends MapLayer
/*    */ {
/*    */   private TextureRegion region;
/*    */   private float x;
/*    */   private float y;
/*    */   
/*    */   public TiledMapImageLayer(TextureRegion region, float x, float y) {
/* 30 */     this.region = region;
/* 31 */     this.x = x;
/* 32 */     this.y = y;
/*    */   }
/*    */   
/*    */   public TextureRegion getTextureRegion() {
/* 36 */     return this.region;
/*    */   }
/*    */   
/*    */   public void setTextureRegion(TextureRegion region) {
/* 40 */     this.region = region;
/*    */   }
/*    */   
/*    */   public float getX() {
/* 44 */     return this.x;
/*    */   }
/*    */   
/*    */   public void setX(float x) {
/* 48 */     this.x = x;
/*    */   }
/*    */   
/*    */   public float getY() {
/* 52 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setY(float y) {
/* 56 */     this.y = y;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\TiledMapImageLayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */