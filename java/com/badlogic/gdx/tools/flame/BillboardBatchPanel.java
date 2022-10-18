/*     */ package com.badlogic.gdx.tools.flame;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleSorter;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ 
/*     */ public class BillboardBatchPanel extends EditorPanel<BillboardParticleBatch> {
/*     */   JComboBox alignCombo;
/*     */   JCheckBox useGPUBox;
/*     */   JComboBox sortCombo;
/*     */   
/*     */   private enum AlignModeWrapper {
/*  22 */     Screen((String)ParticleShader.AlignMode.Screen, "Screen"),
/*  23 */     ViewPoint((String)ParticleShader.AlignMode.ViewPoint, "View Point");
/*     */     
/*     */     public String desc;
/*     */     public ParticleShader.AlignMode mode;
/*     */     
/*     */     AlignModeWrapper(ParticleShader.AlignMode mode, String desc) {
/*  29 */       this.mode = mode;
/*  30 */       this.desc = desc;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  35 */       return this.desc;
/*     */     }
/*     */   }
/*     */   
/*     */   private enum SortMode {
/*  40 */     None("None", new ParticleSorter.None()),
/*  41 */     Distance("Distance", new ParticleSorter.Distance());
/*     */     public String desc;
/*     */     public ParticleSorter sorter;
/*     */     
/*     */     SortMode(String desc, ParticleSorter sorter) {
/*  46 */       this.sorter = sorter;
/*  47 */       this.desc = desc;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  52 */       return this.desc;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BillboardBatchPanel(FlameMain particleEditor3D, BillboardParticleBatch renderer) {
/*  62 */     super(particleEditor3D, "Billboard Batch", "Renderer used to draw billboards particles.");
/*  63 */     initializeComponents(renderer);
/*  64 */     setValue(renderer);
/*     */   }
/*     */ 
/*     */   
/*     */   private void initializeComponents(BillboardParticleBatch renderer) {
/*  69 */     this.alignCombo = new JComboBox();
/*  70 */     this.alignCombo.setModel(new DefaultComboBoxModel<AlignModeWrapper>(AlignModeWrapper.values()));
/*  71 */     this.alignCombo.setSelectedItem(getAlignModeWrapper(renderer.getAlignMode()));
/*  72 */     this.alignCombo.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  74 */             BillboardBatchPanel.AlignModeWrapper align = (BillboardBatchPanel.AlignModeWrapper)BillboardBatchPanel.this.alignCombo.getSelectedItem();
/*  75 */             BillboardBatchPanel.this.editor.getBillboardBatch().setAlignMode(align.mode);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  80 */     this.useGPUBox = new JCheckBox();
/*  81 */     this.useGPUBox.setSelected(renderer.isUseGPU());
/*  82 */     this.useGPUBox.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/*  84 */             BillboardBatchPanel.this.editor.getBillboardBatch().setUseGpu(BillboardBatchPanel.this.useGPUBox.isSelected());
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  89 */     this.sortCombo = new JComboBox();
/*  90 */     this.sortCombo.setModel(new DefaultComboBoxModel<SortMode>(SortMode.values()));
/*  91 */     this.sortCombo.setSelectedItem(getSortMode(renderer.getSorter()));
/*  92 */     this.sortCombo.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  94 */             BillboardBatchPanel.SortMode mode = (BillboardBatchPanel.SortMode)BillboardBatchPanel.this.sortCombo.getSelectedItem();
/*  95 */             BillboardBatchPanel.this.editor.getBillboardBatch().setSorter(mode.sorter);
/*     */           }
/*     */         });
/*     */     
/*  99 */     int i = 0;
/* 100 */     this.contentPanel.add(new JLabel("Align"), new GridBagConstraints(0, i, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/* 102 */     this.contentPanel.add(this.alignCombo, new GridBagConstraints(1, i++, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/* 104 */     this.contentPanel.add(new JLabel("Use GPU"), new GridBagConstraints(0, i, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/* 106 */     this.contentPanel.add(this.useGPUBox, new GridBagConstraints(1, i++, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/* 108 */     this.contentPanel.add(new JLabel("Sort"), new GridBagConstraints(0, i, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/* 110 */     this.contentPanel.add(this.sortCombo, new GridBagConstraints(1, i, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   private Object getSortMode(ParticleSorter sorter) {
/* 115 */     Class<?> type = sorter.getClass();
/* 116 */     for (SortMode wrapper : SortMode.values()) {
/* 117 */       if (wrapper.sorter.getClass() == type)
/* 118 */         return wrapper; 
/*     */     } 
/* 120 */     return null;
/*     */   }
/*     */   
/*     */   private Object getAlignModeWrapper(ParticleShader.AlignMode alignMode) {
/* 124 */     for (AlignModeWrapper wrapper : AlignModeWrapper.values()) {
/* 125 */       if (wrapper.mode == alignMode)
/* 126 */         return wrapper; 
/*     */     } 
/* 128 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\BillboardBatchPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */