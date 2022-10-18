/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ScaleInfluencer;
/*    */ 
/*    */ 
/*    */ public class ScaleInfluencerPanel
/*    */   extends InfluencerPanel<ScaleInfluencer>
/*    */ {
/*    */   ScaledNumericPanel scalePanel;
/*    */   
/*    */   public ScaleInfluencerPanel(FlameMain editor, ScaleInfluencer influencer) {
/* 12 */     super(editor, influencer, "Scale Influencer", "Particle scale, in world units.");
/* 13 */     setValue(influencer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(ScaleInfluencer value) {
/* 18 */     super.setValue(value);
/* 19 */     if (value == null)
/* 20 */       return;  this.scalePanel.setValue(value.value);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void initializeComponents() {
/* 25 */     super.initializeComponents();
/*    */     
/* 27 */     addContent(0, 0, this.scalePanel = new ScaledNumericPanel(this.editor, null, "Life", "", ""));
/* 28 */     this.scalePanel.setIsAlwayShown(true);
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\ScaleInfluencerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */