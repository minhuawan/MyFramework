/*    */ package com.badlogic.gdx.scenes.scene2d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Color;
/*    */ import com.badlogic.gdx.graphics.g2d.Batch;
/*    */ import com.badlogic.gdx.graphics.g2d.NinePatch;
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
/*    */ public class NinePatchDrawable
/*    */   extends BaseDrawable
/*    */   implements TransformDrawable
/*    */ {
/*    */   private NinePatch patch;
/*    */   
/*    */   public NinePatchDrawable() {}
/*    */   
/*    */   public NinePatchDrawable(NinePatch patch) {
/* 40 */     setPatch(patch);
/*    */   }
/*    */   
/*    */   public NinePatchDrawable(NinePatchDrawable drawable) {
/* 44 */     super(drawable);
/* 45 */     setPatch(drawable.patch);
/*    */   }
/*    */   
/*    */   public void draw(Batch batch, float x, float y, float width, float height) {
/* 49 */     this.patch.draw(batch, x, y, width, height);
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
/* 54 */     this.patch.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
/*    */   }
/*    */   
/*    */   public void setPatch(NinePatch patch) {
/* 58 */     this.patch = patch;
/* 59 */     setMinWidth(patch.getTotalWidth());
/* 60 */     setMinHeight(patch.getTotalHeight());
/* 61 */     setTopHeight(patch.getPadTop());
/* 62 */     setRightWidth(patch.getPadRight());
/* 63 */     setBottomHeight(patch.getPadBottom());
/* 64 */     setLeftWidth(patch.getPadLeft());
/*    */   }
/*    */   
/*    */   public NinePatch getPatch() {
/* 68 */     return this.patch;
/*    */   }
/*    */ 
/*    */   
/*    */   public NinePatchDrawable tint(Color tint) {
/* 73 */     NinePatchDrawable drawable = new NinePatchDrawable(this);
/* 74 */     drawable.setPatch(new NinePatch(drawable.getPatch(), tint));
/* 75 */     return drawable;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\NinePatchDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */