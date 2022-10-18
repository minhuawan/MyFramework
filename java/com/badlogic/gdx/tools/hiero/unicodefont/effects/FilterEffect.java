/*    */ package com.badlogic.gdx.tools.hiero.unicodefont.effects;
/*    */ 
/*    */ import com.badlogic.gdx.tools.hiero.unicodefont.Glyph;
/*    */ import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.BufferedImageOp;
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
/*    */ public class FilterEffect
/*    */   implements Effect
/*    */ {
/*    */   private BufferedImageOp filter;
/*    */   
/*    */   public FilterEffect() {}
/*    */   
/*    */   public FilterEffect(BufferedImageOp filter) {
/* 35 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public void draw(BufferedImage image, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph) {
/* 39 */     BufferedImage scratchImage = EffectUtil.getScratchImage();
/* 40 */     this.filter.filter(image, scratchImage);
/* 41 */     image.getGraphics().drawImage(scratchImage, 0, 0, null);
/*    */   }
/*    */   
/*    */   public BufferedImageOp getFilter() {
/* 45 */     return this.filter;
/*    */   }
/*    */   
/*    */   public void setFilter(BufferedImageOp filter) {
/* 49 */     this.filter = filter;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\FilterEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */