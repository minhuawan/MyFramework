/*    */ package com.badlogic.gdx.tools.particleeditor;
/*    */ 
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
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
/*    */ class CountPanel
/*    */   extends EditorPanel
/*    */ {
/*    */   Slider maxSlider;
/*    */   Slider minSlider;
/*    */   
/*    */   public CountPanel(final ParticleEditor editor, String name, String description) {
/* 31 */     super(null, name, description);
/*    */     
/* 33 */     initializeComponents();
/*    */     
/* 35 */     this.maxSlider.setValue(editor.getEmitter().getMaxParticleCount());
/* 36 */     this.maxSlider.addChangeListener(new ChangeListener() {
/*    */           public void stateChanged(ChangeEvent event) {
/* 38 */             editor.getEmitter().setMaxParticleCount((int)CountPanel.this.maxSlider.getValue());
/*    */           }
/*    */         });
/*    */     
/* 42 */     this.minSlider.setValue(editor.getEmitter().getMinParticleCount());
/* 43 */     this.minSlider.addChangeListener(new ChangeListener() {
/*    */           public void stateChanged(ChangeEvent event) {
/* 45 */             editor.getEmitter().setMinParticleCount((int)CountPanel.this.minSlider.getValue());
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   private void initializeComponents() {
/* 51 */     JPanel contentPanel = getContentPanel();
/*    */     
/* 53 */     JLabel label = new JLabel("Min:");
/* 54 */     contentPanel.add(label, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 58 */     this.minSlider = new Slider(0.0F, 0.0F, 99999.0F, 1.0F, 0.0F, 500.0F);
/* 59 */     contentPanel.add(this.minSlider, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 63 */     label = new JLabel("Max:");
/* 64 */     contentPanel.add(label, new GridBagConstraints(2, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 12, 0, 6), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 68 */     this.maxSlider = new Slider(0.0F, 0.0F, 99999.0F, 1.0F, 0.0F, 500.0F);
/* 69 */     contentPanel.add(this.maxSlider, new GridBagConstraints(3, 1, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\CountPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */