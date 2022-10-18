/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
/*    */ 
/*    */ public abstract class InfluencerPanel<T extends Influencer>
/*    */   extends EditorPanel<T>
/*    */ {
/*    */   public InfluencerPanel(FlameMain editor, T influencer, String name, String description) {
/*  9 */     super(editor, name, description, true, true);
/* 10 */     setValue(influencer);
/*    */   }
/*    */   
/*    */   public InfluencerPanel(FlameMain editor, T influencer, String name, String description, boolean isAlwaysActive, boolean isRemovable) {
/* 14 */     super(editor, name, description, isAlwaysActive, isRemovable);
/* 15 */     setValue(influencer);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void removePanel() {
/* 20 */     super.removePanel();
/* 21 */     (this.editor.getEmitter()).influencers.removeValue(this.value, true);
/* 22 */     this.editor.getEmitter().init();
/* 23 */     this.editor.getEmitter().start();
/* 24 */     this.editor.reloadRows();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\InfluencerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */