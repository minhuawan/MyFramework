/*    */ package com.badlogic.gdx.scenes.scene2d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Color;
/*    */ import com.badlogic.gdx.graphics.g2d.Batch;
/*    */ import com.badlogic.gdx.graphics.g2d.Sprite;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
/*    */ public class TextureRegionDrawable
/*    */   extends BaseDrawable
/*    */   implements TransformDrawable
/*    */ {
/*    */   private TextureRegion region;
/*    */   
/*    */   public TextureRegionDrawable() {}
/*    */   
/*    */   public TextureRegionDrawable(TextureRegion region) {
/* 36 */     setRegion(region);
/*    */   }
/*    */   
/*    */   public TextureRegionDrawable(TextureRegionDrawable drawable) {
/* 40 */     super(drawable);
/* 41 */     setRegion(drawable.region);
/*    */   }
/*    */   
/*    */   public void draw(Batch batch, float x, float y, float width, float height) {
/* 45 */     batch.draw(this.region, x, y, width, height);
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/* 50 */     batch.draw(this.region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
/*    */   }
/*    */   
/*    */   public void setRegion(TextureRegion region) {
/* 54 */     this.region = region;
/* 55 */     setMinWidth(region.getRegionWidth());
/* 56 */     setMinHeight(region.getRegionHeight());
/*    */   }
/*    */   
/*    */   public TextureRegion getRegion() {
/* 60 */     return this.region;
/*    */   }
/*    */ 
/*    */   
/*    */   public Drawable tint(Color tint) {
/*    */     Sprite sprite;
/* 66 */     if (this.region instanceof TextureAtlas.AtlasRegion) {
/* 67 */       TextureAtlas.AtlasSprite atlasSprite = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasRegion)this.region);
/*    */     } else {
/* 69 */       sprite = new Sprite(this.region);
/* 70 */     }  sprite.setColor(tint);
/* 71 */     sprite.setSize(getMinWidth(), getMinHeight());
/* 72 */     SpriteDrawable drawable = new SpriteDrawable(sprite);
/* 73 */     drawable.setLeftWidth(getLeftWidth());
/* 74 */     drawable.setRightWidth(getRightWidth());
/* 75 */     drawable.setTopHeight(getTopHeight());
/* 76 */     drawable.setBottomHeight(getBottomHeight());
/* 77 */     return drawable;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\TextureRegionDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */