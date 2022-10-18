/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SpawnPanel
/*     */   extends EditorPanel
/*     */ {
/*     */   JComboBox shapeCombo;
/*     */   JCheckBox edgesCheckbox;
/*     */   JLabel edgesLabel;
/*     */   JComboBox sideCombo;
/*     */   JLabel sideLabel;
/*     */   
/*     */   public SpawnPanel(final ParticleEditor editor, final ParticleEmitter.SpawnShapeValue spawnShapeValue, String name, String description) {
/*  43 */     super(null, name, description);
/*     */     
/*  45 */     initializeComponents();
/*     */     
/*  47 */     this.edgesCheckbox.setSelected(spawnShapeValue.isEdges());
/*  48 */     this.sideCombo.setSelectedItem(spawnShapeValue.getShape());
/*     */     
/*  50 */     this.shapeCombo.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  52 */             ParticleEmitter.SpawnShape shape = (ParticleEmitter.SpawnShape)SpawnPanel.this.shapeCombo.getSelectedItem();
/*  53 */             spawnShapeValue.setShape(shape);
/*  54 */             switch (shape) {
/*     */               case line:
/*     */               case square:
/*  57 */                 SpawnPanel.this.setEdgesVisible(false);
/*  58 */                 editor.setVisible("Spawn Width", true);
/*  59 */                 editor.setVisible("Spawn Height", true);
/*     */                 break;
/*     */               case ellipse:
/*  62 */                 SpawnPanel.this.setEdgesVisible(true);
/*  63 */                 editor.setVisible("Spawn Width", true);
/*  64 */                 editor.setVisible("Spawn Height", true);
/*     */                 break;
/*     */               case point:
/*  67 */                 SpawnPanel.this.setEdgesVisible(false);
/*  68 */                 editor.setVisible("Spawn Width", false);
/*  69 */                 editor.setVisible("Spawn Height", false);
/*     */                 break;
/*     */             } 
/*     */           
/*     */           }
/*     */         });
/*  75 */     this.edgesCheckbox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  77 */             spawnShapeValue.setEdges(SpawnPanel.this.edgesCheckbox.isSelected());
/*  78 */             SpawnPanel.this.setEdgesVisible(true);
/*     */           }
/*     */         });
/*     */     
/*  82 */     this.sideCombo.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  84 */             ParticleEmitter.SpawnEllipseSide side = (ParticleEmitter.SpawnEllipseSide)SpawnPanel.this.sideCombo.getSelectedItem();
/*  85 */             spawnShapeValue.setSide(side);
/*     */           }
/*     */         });
/*     */     
/*  89 */     this.shapeCombo.setSelectedItem(spawnShapeValue.getShape());
/*     */   }
/*     */   
/*     */   public void update(ParticleEditor editor) {
/*  93 */     this.shapeCombo.setSelectedItem(editor.getEmitter().getSpawnShape().getShape());
/*     */   }
/*     */   
/*     */   void setEdgesVisible(boolean visible) {
/*  97 */     this.edgesCheckbox.setVisible(visible);
/*  98 */     this.edgesLabel.setVisible(visible);
/*  99 */     visible = (visible && this.edgesCheckbox.isSelected());
/* 100 */     this.sideCombo.setVisible(visible);
/* 101 */     this.sideLabel.setVisible(visible);
/*     */   }
/*     */   
/*     */   private void initializeComponents() {
/* 105 */     JPanel contentPanel = getContentPanel();
/*     */     
/* 107 */     JLabel label = new JLabel("Shape:");
/* 108 */     contentPanel.add(label, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 112 */     this.shapeCombo = new JComboBox();
/* 113 */     this.shapeCombo.setModel(new DefaultComboBoxModel<ParticleEmitter.SpawnShape>(ParticleEmitter.SpawnShape.values()));
/* 114 */     contentPanel.add(this.shapeCombo, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 118 */     this.edgesLabel = new JLabel("Edges:");
/* 119 */     contentPanel.add(this.edgesLabel, new GridBagConstraints(2, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 12, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 123 */     this.edgesCheckbox = new JCheckBox();
/* 124 */     contentPanel.add(this.edgesCheckbox, new GridBagConstraints(3, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 128 */     this.sideLabel = new JLabel("Side:");
/* 129 */     contentPanel.add(this.sideLabel, new GridBagConstraints(4, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 12, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 133 */     this.sideCombo = new JComboBox();
/* 134 */     this.sideCombo.setModel(new DefaultComboBoxModel<ParticleEmitter.SpawnEllipseSide>(ParticleEmitter.SpawnEllipseSide.values()));
/* 135 */     contentPanel.add(this.sideCombo, new GridBagConstraints(5, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 139 */     JPanel spacer = new JPanel();
/* 140 */     spacer.setPreferredSize(new Dimension());
/* 141 */     contentPanel.add(spacer, new GridBagConstraints(6, 0, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\SpawnPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */