/*    */ package com.badlogic.gdx.scenes.scene2d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Color;
/*    */ import com.badlogic.gdx.graphics.g2d.Batch;
/*    */ import com.badlogic.gdx.graphics.g2d.Sprite;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
/*    */ public class SpriteDrawable
/*    */   extends BaseDrawable
/*    */   implements TransformDrawable
/*    */ {
/*    */   private Sprite sprite;
/*    */   
/*    */   public SpriteDrawable() {}
/*    */   
/*    */   public SpriteDrawable(Sprite sprite) {
/* 34 */     setSprite(sprite);
/*    */   }
/*    */   
/*    */   public SpriteDrawable(SpriteDrawable drawable) {
/* 38 */     super(drawable);
/* 39 */     setSprite(drawable.sprite);
/*    */   }
/*    */   
/*    */   public void draw(Batch batch, float x, float y, float width, float height) {
/* 43 */     Color spriteColor = this.sprite.getColor();
/* 44 */     float batchColor = batch.getPackedColor();
/* 45 */     this.sprite.setColor(batch.getColor().mul(spriteColor));
/*    */     
/* 47 */     this.sprite.setRotation(0.0F);
/* 48 */     this.sprite.setScale(1.0F, 1.0F);
/* 49 */     this.sprite.setBounds(x, y, width, height);
/* 50 */     this.sprite.draw(batch);
/*    */     
/* 52 */     this.sprite.setColor(spriteColor);
/* 53 */     batch.setColor(batchColor);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/* 59 */     Color spriteColor = this.sprite.getColor();
/* 60 */     float batchColor = batch.getPackedColor();
/* 61 */     this.sprite.setColor(batch.getColor().mul(spriteColor));
/*    */     
/* 63 */     this.sprite.setOrigin(originX, originY);
/* 64 */     this.sprite.setRotation(rotation);
/* 65 */     this.sprite.setScale(scaleX, scaleY);
/* 66 */     this.sprite.setBounds(x, y, width, height);
/* 67 */     this.sprite.draw(batch);
/*    */     
/* 69 */     this.sprite.setColor(spriteColor);
/* 70 */     batch.setColor(batchColor);
/*    */   }
/*    */   
/*    */   public void setSprite(Sprite sprite) {
/* 74 */     this.sprite = sprite;
/* 75 */     setMinWidth(sprite.getWidth());
/* 76 */     setMinHeight(sprite.getHeight());
/*    */   }
/*    */   
/*    */   public Sprite getSprite() {
/* 80 */     return this.sprite;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpriteDrawable tint(Color tint) {
/*    */     Sprite newSprite;
/* 86 */     if (this.sprite instanceof TextureAtlas.AtlasSprite) {
/* 87 */       TextureAtlas.AtlasSprite atlasSprite = new TextureAtlas.AtlasSprite((TextureAtlas.AtlasSprite)this.sprite);
/*    */     } else {
/* 89 */       newSprite = new Sprite(this.sprite);
/* 90 */     }  newSprite.setColor(tint);
/* 91 */     newSprite.setSize(getMinWidth(), getMinHeight());
/* 92 */     SpriteDrawable drawable = new SpriteDrawable(newSprite);
/* 93 */     drawable.setLeftWidth(getLeftWidth());
/* 94 */     drawable.setRightWidth(getRightWidth());
/* 95 */     drawable.setTopHeight(getTopHeight());
/* 96 */     drawable.setBottomHeight(getBottomHeight());
/* 97 */     return drawable;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\SpriteDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */