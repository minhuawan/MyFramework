/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
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
/*     */ class OptionsPanel
/*     */   extends EditorPanel
/*     */ {
/*     */   JCheckBox attachedCheckBox;
/*     */   JCheckBox continuousCheckbox;
/*     */   JCheckBox alignedCheckbox;
/*     */   JCheckBox additiveCheckbox;
/*     */   JCheckBox behindCheckbox;
/*     */   JCheckBox premultipliedAlphaCheckbox;
/*     */   
/*     */   public OptionsPanel(final ParticleEditor editor, String name, String description) {
/*  37 */     super(null, name, description);
/*     */     
/*  39 */     initializeComponents();
/*     */     
/*  41 */     this.attachedCheckBox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  43 */             editor.getEmitter().setAttached(OptionsPanel.this.attachedCheckBox.isSelected());
/*     */           }
/*     */         });
/*     */     
/*  47 */     this.continuousCheckbox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  49 */             editor.getEmitter().setContinuous(OptionsPanel.this.continuousCheckbox.isSelected());
/*     */           }
/*     */         });
/*     */     
/*  53 */     this.alignedCheckbox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  55 */             editor.getEmitter().setAligned(OptionsPanel.this.alignedCheckbox.isSelected());
/*     */           }
/*     */         });
/*     */     
/*  59 */     this.additiveCheckbox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  61 */             editor.getEmitter().setAdditive(OptionsPanel.this.additiveCheckbox.isSelected());
/*     */           }
/*     */         });
/*     */     
/*  65 */     this.behindCheckbox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  67 */             editor.getEmitter().setBehind(OptionsPanel.this.behindCheckbox.isSelected());
/*     */           }
/*     */         });
/*     */     
/*  71 */     this.premultipliedAlphaCheckbox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent event) {
/*  74 */             editor.getEmitter().setPremultipliedAlpha(OptionsPanel.this.premultipliedAlphaCheckbox.isSelected());
/*     */           }
/*     */         });
/*     */     
/*  78 */     ParticleEmitter emitter = editor.getEmitter();
/*  79 */     this.attachedCheckBox.setSelected(emitter.isAttached());
/*  80 */     this.continuousCheckbox.setSelected(emitter.isContinuous());
/*  81 */     this.alignedCheckbox.setSelected(emitter.isAligned());
/*  82 */     this.additiveCheckbox.setSelected(emitter.isAdditive());
/*  83 */     this.behindCheckbox.setSelected(emitter.isBehind());
/*  84 */     this.premultipliedAlphaCheckbox.setSelected(emitter.isPremultipliedAlpha());
/*     */   }
/*     */   
/*     */   private void initializeComponents() {
/*  88 */     JPanel contentPanel = getContentPanel();
/*     */     
/*  90 */     JLabel label = new JLabel("Additive:");
/*  91 */     contentPanel.add(label, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  95 */     this.additiveCheckbox = new JCheckBox();
/*  96 */     contentPanel.add(this.additiveCheckbox, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 100 */     label = new JLabel("Attached:");
/* 101 */     contentPanel.add(label, new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.attachedCheckBox = new JCheckBox();
/* 106 */     contentPanel.add(this.attachedCheckBox, new GridBagConstraints(1, 2, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 110 */     label = new JLabel("Continuous:");
/* 111 */     contentPanel.add(label, new GridBagConstraints(0, 3, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 115 */     this.continuousCheckbox = new JCheckBox();
/* 116 */     contentPanel.add(this.continuousCheckbox, new GridBagConstraints(1, 3, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 120 */     label = new JLabel("Aligned:");
/* 121 */     contentPanel.add(label, new GridBagConstraints(0, 4, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 125 */     this.alignedCheckbox = new JCheckBox();
/* 126 */     contentPanel.add(this.alignedCheckbox, new GridBagConstraints(1, 4, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 130 */     label = new JLabel("Behind:");
/* 131 */     contentPanel.add(label, new GridBagConstraints(0, 5, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 135 */     this.behindCheckbox = new JCheckBox();
/* 136 */     contentPanel.add(this.behindCheckbox, new GridBagConstraints(1, 5, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 140 */     label = new JLabel("Premultiplied Alpha:");
/* 141 */     contentPanel.add(label, new GridBagConstraints(0, 6, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 145 */     this.premultipliedAlphaCheckbox = new JCheckBox();
/* 146 */     contentPanel.add(this.premultipliedAlphaCheckbox, new GridBagConstraints(1, 6, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 6, 0, 0), 0, 0));
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\OptionsPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */