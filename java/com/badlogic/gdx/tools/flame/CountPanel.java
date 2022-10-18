/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.event.ChangeEvent;
/*    */ import javax.swing.event.ChangeListener;
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
/*    */ class CountPanel
/*    */   extends EditorPanel
/*    */ {
/*    */   Slider maxSlider;
/*    */   Slider minSlider;
/*    */   
/*    */   public CountPanel(FlameMain editor, String name, String description, int min, int max) {
/* 32 */     super(editor, name, description);
/*    */     
/* 34 */     initializeComponents(min, max);
/* 35 */     setValue(null);
/*    */   }
/*    */ 
/*    */   
/*    */   private void initializeComponents(int min, int max) {
/* 40 */     this.minSlider = new Slider(0.0F, 0.0F, 999999.0F, 1.0F);
/* 41 */     this.minSlider.setValue(min);
/* 42 */     this.minSlider.addChangeListener(new ChangeListener() {
/*    */           public void stateChanged(ChangeEvent event) {
/* 44 */             ParticleController controller = CountPanel.this.editor.getEmitter();
/* 45 */             controller.emitter.minParticleCount = (int)CountPanel.this.minSlider.getValue();
/* 46 */             CountPanel.this.editor.restart();
/*    */           }
/*    */         });
/*    */ 
/*    */     
/* 51 */     this.maxSlider = new Slider(0.0F, 0.0F, 999999.0F, 1.0F);
/* 52 */     this.maxSlider.setValue(max);
/* 53 */     this.maxSlider.addChangeListener(new ChangeListener() {
/*    */           public void stateChanged(ChangeEvent event) {
/* 55 */             ParticleController controller = CountPanel.this.editor.getEmitter();
/* 56 */             controller.emitter.maxParticleCount = (int)CountPanel.this.maxSlider.getValue();
/* 57 */             CountPanel.this.editor.restart();
/*    */           }
/*    */         });
/*    */     
/* 61 */     int i = 0;
/* 62 */     this.contentPanel.add(new JLabel("Min"), new GridBagConstraints(0, i, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*    */     
/* 64 */     this.contentPanel.add(this.minSlider, new GridBagConstraints(1, i++, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*    */     
/* 66 */     this.contentPanel.add(new JLabel("Max"), new GridBagConstraints(0, i, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*    */     
/* 68 */     this.contentPanel.add(this.maxSlider, new GridBagConstraints(1, i++, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\CountPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */