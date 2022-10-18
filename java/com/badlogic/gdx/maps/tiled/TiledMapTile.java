/*    */ package com.badlogic.gdx.maps.tiled;
/*    */ public interface TiledMapTile {
/*    */   int getId();
/*    */   
/*    */   void setId(int paramInt);
/*    */   
/*    */   BlendMode getBlendMode();
/*    */   
/*    */   void setBlendMode(BlendMode paramBlendMode);
/*    */   
/*    */   TextureRegion getTextureRegion();
/*    */   
/*    */   void setTextureRegion(TextureRegion paramTextureRegion);
/*    */   
/*    */   float getOffsetX();
/*    */   
/*    */   void setOffsetX(float paramFloat);
/*    */   
/*    */   float getOffsetY();
/*    */   
/*    */   void setOffsetY(float paramFloat);
/*    */   
/*    */   MapProperties getProperties();
/*    */   
/*    */   public enum BlendMode {
/* 26 */     NONE, ALPHA;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\TiledMapTile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */