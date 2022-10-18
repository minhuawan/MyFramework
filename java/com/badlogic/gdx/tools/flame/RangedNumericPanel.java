/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ParticleValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.RangedNumericValue;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RangedNumericPanel
/*     */   extends ParticleValuePanel<RangedNumericValue>
/*     */ {
/*     */   Slider minSlider;
/*     */   Slider maxSlider;
/*     */   JButton rangeButton;
/*     */   JLabel label;
/*     */   
/*     */   public RangedNumericPanel(FlameMain editor, RangedNumericValue value, String name, String description) {
/*  40 */     this(editor, value, name, description, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public RangedNumericPanel(FlameMain editor, RangedNumericValue value, String name, String description, boolean isAlwaysActive) {
/*  45 */     super(editor, name, description, isAlwaysActive);
/*  46 */     setValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initializeComponents() {
/*  51 */     super.initializeComponents();
/*  52 */     JPanel contentPanel = getContentPanel();
/*     */     
/*  54 */     this.label = new JLabel("Value:");
/*  55 */     contentPanel.add(this.label, new GridBagConstraints(2, 2, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  59 */     this.minSlider = new Slider(0.0F, -99999.0F, 99999.0F, 1.0F);
/*  60 */     contentPanel.add(this.minSlider, new GridBagConstraints(3, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  64 */     this.maxSlider = new Slider(0.0F, -99999.0F, 99999.0F, 1.0F);
/*  65 */     contentPanel.add(this.maxSlider, new GridBagConstraints(4, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  69 */     this.rangeButton = new JButton("<");
/*  70 */     this.rangeButton.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
/*  71 */     contentPanel.add(this.rangeButton, new GridBagConstraints(5, 2, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 1, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     this.minSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/*  78 */             RangedNumericPanel.this.value.setLowMin(Float.valueOf(RangedNumericPanel.this.minSlider.getValue()).floatValue());
/*  79 */             if (!RangedNumericPanel.this.maxSlider.isVisible()) RangedNumericPanel.this.value.setLowMax(Float.valueOf(RangedNumericPanel.this.minSlider.getValue()).floatValue());
/*     */           
/*     */           }
/*     */         });
/*  83 */     this.maxSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/*  85 */             RangedNumericPanel.this.value.setLowMax(Float.valueOf(RangedNumericPanel.this.maxSlider.getValue()).floatValue());
/*     */           }
/*     */         });
/*     */     
/*  89 */     this.rangeButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  91 */             boolean visible = !RangedNumericPanel.this.maxSlider.isVisible();
/*  92 */             RangedNumericPanel.this.maxSlider.setVisible(visible);
/*  93 */             RangedNumericPanel.this.rangeButton.setText(visible ? "<" : ">");
/*  94 */             Slider slider = visible ? RangedNumericPanel.this.maxSlider : RangedNumericPanel.this.minSlider;
/*  95 */             RangedNumericPanel.this.value.setLowMax(Float.valueOf(slider.getValue()).floatValue());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void setValue(RangedNumericValue value) {
/* 101 */     super.setValue(value);
/* 102 */     if (value == null)
/* 103 */       return;  setValue(this.minSlider, value.getLowMin());
/* 104 */     setValue(this.maxSlider, value.getLowMax());
/*     */     
/* 106 */     if (this.minSlider.getValue() == this.maxSlider.getValue()) {
/* 107 */       this.rangeButton.doClick(0);
/* 108 */     } else if (!this.maxSlider.isVisible()) {
/* 109 */       this.maxSlider.setVisible(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\RangedNumericPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */