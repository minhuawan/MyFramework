/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
/*    */ 
/*    */ 
/*    */ public class ColorInfluencerPanel
/*    */   extends InfluencerPanel<ColorInfluencer.Single>
/*    */ {
/*    */   GradientPanel tintPanel;
/*    */   PercentagePanel alphaPanel;
/*    */   
/*    */   public ColorInfluencerPanel(FlameMain particleEditor3D, ColorInfluencer.Single influencer) {
/* 13 */     super(particleEditor3D, influencer, "Color Influencer", "Sets the particle color.");
/* 14 */     initializeComponents(influencer);
/* 15 */     setValue(influencer);
/*    */   }
/*    */   
/*    */   private void initializeComponents(ColorInfluencer.Single emitter) {
/* 19 */     int i = 0;
/* 20 */     addContent(i++, 0, this.tintPanel = new GradientPanel(this.editor, emitter.colorValue, "Tint", "", false));
/* 21 */     addContent(i++, 0, this.alphaPanel = new PercentagePanel(this.editor, emitter.alphaValue, "Life", "Transparency", ""));
/* 22 */     this.tintPanel.showContent(true);
/* 23 */     this.alphaPanel.showContent(true);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\ColorInfluencerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */