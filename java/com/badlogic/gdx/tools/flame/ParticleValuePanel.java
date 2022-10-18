/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.values.ParticleValue;
/*    */ 
/*    */ public class ParticleValuePanel<T extends ParticleValue>
/*    */   extends EditorPanel<T>
/*    */ {
/*    */   public ParticleValuePanel(FlameMain editor, String name, String description) {
/*  9 */     this(editor, name, description, true);
/*    */   }
/*    */   
/*    */   public ParticleValuePanel(FlameMain editor, String name, String description, boolean isAlwaysActive) {
/* 13 */     this(editor, name, description, isAlwaysActive, false);
/*    */   }
/*    */   
/*    */   public ParticleValuePanel(FlameMain editor, String name, String description, boolean isAlwaysActive, boolean isRemovable) {
/* 17 */     super(editor, name, description, isAlwaysActive, isRemovable);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setHasAdvanced(boolean hasAdvanced) {
/* 22 */     super.setHasAdvanced(hasAdvanced);
/* 23 */     this.advancedButton.setVisible((hasAdvanced && (((ParticleValue)this.value).isActive() || this.isAlwaysActive)));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(T value) {
/* 28 */     super.setValue(value);
/* 29 */     if (value != null) {
/* 30 */       this.activeButton.setSelected(value.isActive());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void activate() {
/* 36 */     super.activate();
/* 37 */     if (this.value != null) ((ParticleValue)this.value).setActive(this.activeButton.isSelected()); 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\ParticleValuePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */