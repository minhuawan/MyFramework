/*    */ package com.badlogic.gdx.tools.particleeditor;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.GridBagLayout;
/*    */ import java.awt.Insets;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JPanel;
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
/*    */ class PercentagePanel
/*    */   extends EditorPanel
/*    */ {
/*    */   final ParticleEmitter.ScaledNumericValue value;
/*    */   JButton expandButton;
/*    */   Chart chart;
/*    */   
/*    */   public PercentagePanel(ParticleEmitter.ScaledNumericValue value, String chartTitle, String name, String description) {
/* 38 */     super((ParticleEmitter.ParticleValue)value, name, description);
/* 39 */     this.value = value;
/*    */     
/* 41 */     initializeComponents(chartTitle);
/*    */     
/* 43 */     this.chart.setValues(value.getTimeline(), value.getScaling());
/*    */     
/* 45 */     this.expandButton.addActionListener(new ActionListener() {
/*    */           public void actionPerformed(ActionEvent event) {
/* 47 */             PercentagePanel.this.chart.setExpanded(!PercentagePanel.this.chart.isExpanded());
/* 48 */             boolean expanded = PercentagePanel.this.chart.isExpanded();
/* 49 */             GridBagLayout layout = (GridBagLayout)PercentagePanel.this.getContentPanel().getLayout();
/* 50 */             GridBagConstraints chartConstraints = layout.getConstraints(PercentagePanel.this.chart);
/* 51 */             GridBagConstraints expandButtonConstraints = layout.getConstraints(PercentagePanel.this.expandButton);
/* 52 */             if (expanded) {
/* 53 */               PercentagePanel.this.chart.setPreferredSize(new Dimension(150, 200));
/* 54 */               PercentagePanel.this.expandButton.setText("-");
/* 55 */               chartConstraints.weightx = 1.0D;
/* 56 */               expandButtonConstraints.weightx = 0.0D;
/*    */             } else {
/* 58 */               PercentagePanel.this.chart.setPreferredSize(new Dimension(150, 62));
/* 59 */               PercentagePanel.this.expandButton.setText("+");
/* 60 */               chartConstraints.weightx = 0.0D;
/* 61 */               expandButtonConstraints.weightx = 1.0D;
/*    */             } 
/* 63 */             layout.setConstraints(PercentagePanel.this.chart, chartConstraints);
/* 64 */             layout.setConstraints(PercentagePanel.this.expandButton, expandButtonConstraints);
/* 65 */             PercentagePanel.this.chart.revalidate();
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   private void initializeComponents(String chartTitle) {
/* 71 */     JPanel contentPanel = getContentPanel();
/*    */     
/* 73 */     this.chart = new Chart(chartTitle) {
/*    */         public void pointsChanged() {
/* 75 */           PercentagePanel.this.value.setTimeline(PercentagePanel.this.chart.getValuesX());
/* 76 */           PercentagePanel.this.value.setScaling(PercentagePanel.this.chart.getValuesY());
/*    */         }
/*    */       };
/* 79 */     this.chart.setPreferredSize(new Dimension(150, 62));
/* 80 */     contentPanel.add(this.chart, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 1, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 84 */     this.expandButton = new JButton("+");
/* 85 */     this.expandButton.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
/* 86 */     contentPanel.add(this.expandButton, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 18, 0, new Insets(0, 6, 0, 0), 0, 0));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\PercentagePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */