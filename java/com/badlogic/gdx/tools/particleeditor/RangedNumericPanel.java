/*    */ package com.badlogic.gdx.tools.particleeditor;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JButton;
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
/*    */ 
/*    */ class RangedNumericPanel
/*    */   extends EditorPanel
/*    */ {
/*    */   private final ParticleEmitter.RangedNumericValue value;
/*    */   Slider minSlider;
/*    */   Slider maxSlider;
/*    */   JButton rangeButton;
/*    */   JLabel label;
/*    */   
/*    */   public RangedNumericPanel(final ParticleEmitter.RangedNumericValue value, String name, String description) {
/* 40 */     super((ParticleEmitter.ParticleValue)value, name, description);
/* 41 */     this.value = value;
/*    */     
/* 43 */     initializeComponents();
/*    */     
/* 45 */     this.minSlider.setValue(value.getLowMin());
/* 46 */     this.maxSlider.setValue(value.getLowMax());
/*    */     
/* 48 */     this.minSlider.addChangeListener(new ChangeListener() {
/*    */           public void stateChanged(ChangeEvent event) {
/* 50 */             value.setLowMin(Float.valueOf(RangedNumericPanel.this.minSlider.getValue()).floatValue());
/* 51 */             if (!RangedNumericPanel.this.maxSlider.isVisible()) value.setLowMax(Float.valueOf(RangedNumericPanel.this.minSlider.getValue()).floatValue());
/*    */           
/*    */           }
/*    */         });
/* 55 */     this.maxSlider.addChangeListener(new ChangeListener() {
/*    */           public void stateChanged(ChangeEvent event) {
/* 57 */             value.setLowMax(Float.valueOf(RangedNumericPanel.this.maxSlider.getValue()).floatValue());
/*    */           }
/*    */         });
/*    */     
/* 61 */     this.rangeButton.addActionListener(new ActionListener() {
/*    */           public void actionPerformed(ActionEvent event) {
/* 63 */             boolean visible = !RangedNumericPanel.this.maxSlider.isVisible();
/* 64 */             RangedNumericPanel.this.maxSlider.setVisible(visible);
/* 65 */             RangedNumericPanel.this.rangeButton.setText(visible ? "<" : ">");
/* 66 */             Slider slider = visible ? RangedNumericPanel.this.maxSlider : RangedNumericPanel.this.minSlider;
/* 67 */             value.setLowMax(Float.valueOf(slider.getValue()).floatValue());
/*    */           }
/*    */         });
/*    */     
/* 71 */     if (value.getLowMin() == value.getLowMax()) this.rangeButton.doClick(0); 
/*    */   }
/*    */   
/*    */   private void initializeComponents() {
/* 75 */     JPanel contentPanel = getContentPanel();
/*    */     
/* 77 */     this.label = new JLabel("Value:");
/* 78 */     contentPanel.add(this.label, new GridBagConstraints(2, 2, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 82 */     this.minSlider = new Slider(0.0F, -99999.0F, 99999.0F, 1.0F, -400.0F, 400.0F);
/* 83 */     contentPanel.add(this.minSlider, new GridBagConstraints(3, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 87 */     this.maxSlider = new Slider(0.0F, -99999.0F, 99999.0F, 1.0F, -400.0F, 400.0F);
/* 88 */     contentPanel.add(this.maxSlider, new GridBagConstraints(4, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 6, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 92 */     this.rangeButton = new JButton("<");
/* 93 */     this.rangeButton.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
/* 94 */     contentPanel.add(this.rangeButton, new GridBagConstraints(5, 2, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 1, 0, 0), 0, 0));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\RangedNumericPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */