/*    */ package com.badlogic.gdx.tools.particleeditor;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JSpinner;
/*    */ import javax.swing.SpinnerNumberModel;
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
/*    */ 
/*    */ class NumericPanel
/*    */   extends EditorPanel
/*    */ {
/*    */   private final ParticleEmitter.NumericValue value;
/*    */   JSpinner valueSpinner;
/*    */   
/*    */   public NumericPanel(final ParticleEmitter.NumericValue value, String name, String description) {
/* 36 */     super((ParticleEmitter.ParticleValue)value, name, description);
/* 37 */     this.value = value;
/*    */     
/* 39 */     initializeComponents();
/*    */     
/* 41 */     this.valueSpinner.setValue(Float.valueOf(value.getValue()));
/*    */     
/* 43 */     this.valueSpinner.addChangeListener(new ChangeListener() {
/*    */           public void stateChanged(ChangeEvent event) {
/* 45 */             value.setValue(((Float)NumericPanel.this.valueSpinner.getValue()).floatValue());
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   private void initializeComponents() {
/* 51 */     JPanel contentPanel = getContentPanel();
/*    */     
/* 53 */     JLabel label = new JLabel("Value:");
/* 54 */     contentPanel.add(label, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 58 */     this.valueSpinner = new JSpinner(new SpinnerNumberModel(new Float(0.0F), new Float(-99999.0F), new Float(99999.0F), new Float(0.1F)));
/* 59 */     contentPanel.add(this.valueSpinner, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\NumericPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */