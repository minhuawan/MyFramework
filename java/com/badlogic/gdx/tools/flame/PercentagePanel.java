/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.values.ParticleValue;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
/*    */ import com.badlogic.gdx.tools.particleeditor.Chart;
/*    */ import java.awt.Component;
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
/*    */ class PercentagePanel
/*    */   extends ParticleValuePanel<ScaledNumericValue>
/*    */ {
/*    */   JButton expandButton;
/*    */   Chart chart;
/*    */   
/*    */   public PercentagePanel(FlameMain editor, ScaledNumericValue value, String chartTitle, String name, String description) {
/* 38 */     super(editor, name, description);
/*    */     
/* 40 */     initializeComponents(chartTitle);
/* 41 */     setValue(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(ScaledNumericValue value) {
/* 46 */     super.setValue(value);
/* 47 */     if (value == null)
/* 48 */       return;  this.chart.setValues(this.value.getTimeline(), this.value.getScaling());
/*    */   }
/*    */   
/*    */   private void initializeComponents(String chartTitle) {
/* 52 */     JPanel contentPanel = getContentPanel();
/*    */     
/* 54 */     this.chart = new Chart(chartTitle) {
/*    */         public void pointsChanged() {
/* 56 */           PercentagePanel.this.value.setTimeline(PercentagePanel.this.chart.getValuesX());
/* 57 */           PercentagePanel.this.value.setScaling(PercentagePanel.this.chart.getValuesY());
/*    */         }
/*    */       };
/* 60 */     this.chart.setPreferredSize(new Dimension(150, 62));
/* 61 */     contentPanel.add((Component)this.chart, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 1, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 65 */     this.expandButton = new JButton("+");
/* 66 */     this.expandButton.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
/* 67 */     contentPanel.add(this.expandButton, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 18, 0, new Insets(0, 6, 0, 0), 0, 0));
/*    */ 
/*    */     
/* 70 */     this.expandButton.addActionListener(new ActionListener() {
/*    */           public void actionPerformed(ActionEvent event) {
/* 72 */             PercentagePanel.this.chart.setExpanded(!PercentagePanel.this.chart.isExpanded());
/* 73 */             boolean expanded = PercentagePanel.this.chart.isExpanded();
/* 74 */             GridBagLayout layout = (GridBagLayout)PercentagePanel.this.getContentPanel().getLayout();
/* 75 */             GridBagConstraints chartConstraints = layout.getConstraints((Component)PercentagePanel.this.chart);
/* 76 */             GridBagConstraints expandButtonConstraints = layout.getConstraints(PercentagePanel.this.expandButton);
/* 77 */             if (expanded) {
/* 78 */               PercentagePanel.this.chart.setPreferredSize(new Dimension(150, 200));
/* 79 */               PercentagePanel.this.expandButton.setText("-");
/* 80 */               chartConstraints.weightx = 1.0D;
/* 81 */               expandButtonConstraints.weightx = 0.0D;
/*    */             } else {
/* 83 */               PercentagePanel.this.chart.setPreferredSize(new Dimension(150, 62));
/* 84 */               PercentagePanel.this.expandButton.setText("+");
/* 85 */               chartConstraints.weightx = 0.0D;
/* 86 */               expandButtonConstraints.weightx = 1.0D;
/*    */             } 
/* 88 */             layout.setConstraints((Component)PercentagePanel.this.chart, chartConstraints);
/* 89 */             layout.setConstraints(PercentagePanel.this.expandButton, expandButtonConstraints);
/* 90 */             PercentagePanel.this.chart.revalidate();
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\PercentagePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */