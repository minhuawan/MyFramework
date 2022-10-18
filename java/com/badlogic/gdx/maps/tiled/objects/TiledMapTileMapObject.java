/*    */ package com.badlogic.gdx.maps.tiled.objects;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*    */ import com.badlogic.gdx.maps.objects.TextureMapObject;
/*    */ import com.badlogic.gdx.maps.tiled.TiledMapTile;
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
/*    */ public class TiledMapTileMapObject
/*    */   extends TextureMapObject
/*    */ {
/*    */   private boolean flipHorizontally;
/*    */   private boolean flipVertically;
/*    */   private TiledMapTile tile;
/*    */   
/*    */   public TiledMapTileMapObject(TiledMapTile tile, boolean flipHorizontally, boolean flipVertically) {
/* 38 */     this.flipHorizontally = flipHorizontally;
/* 39 */     this.flipVertically = flipVertically;
/* 40 */     this.tile = tile;
/*    */     
/* 42 */     TextureRegion textureRegion = new TextureRegion(tile.getTextureRegion());
/* 43 */     textureRegion.flip(flipHorizontally, flipVertically);
/* 44 */     setTextureRegion(textureRegion);
/*    */   }
/*    */   
/*    */   public boolean isFlipHorizontally() {
/* 48 */     return this.flipHorizontally;
/*    */   }
/*    */   
/*    */   public void setFlipHorizontally(boolean flipHorizontally) {
/* 52 */     this.flipHorizontally = flipHorizontally;
/*    */   }
/*    */   
/*    */   public boolean isFlipVertically() {
/* 56 */     return this.flipVertically;
/*    */   }
/*    */   
/*    */   public void setFlipVertically(boolean flipVertically) {
/* 60 */     this.flipVertically = flipVertically;
/*    */   }
/*    */   
/*    */   public TiledMapTile getTile() {
/* 64 */     return this.tile;
/*    */   }
/*    */   
/*    */   public void setTile(TiledMapTile tile) {
/* 68 */     this.tile = tile;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\objects\TiledMapTileMapObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */