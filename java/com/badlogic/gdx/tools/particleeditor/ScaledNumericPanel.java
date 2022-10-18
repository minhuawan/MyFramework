/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
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
/*     */ class ScaledNumericPanel
/*     */   extends EditorPanel
/*     */ {
/*     */   final ParticleEmitter.ScaledNumericValue value;
/*     */   Slider lowMinSlider;
/*     */   Slider lowMaxSlider;
/*     */   Slider highMinSlider;
/*     */   Slider highMaxSlider;
/*     */   JCheckBox relativeCheckBox;
/*     */   Chart chart;
/*     */   JPanel formPanel;
/*     */   JButton expandButton;
/*     */   JButton lowRangeButton;
/*     */   JButton highRangeButton;
/*     */   
/*     */   public ScaledNumericPanel(final ParticleEmitter.ScaledNumericValue value, String chartTitle, String name, String description) {
/*  48 */     super((ParticleEmitter.ParticleValue)value, name, description);
/*  49 */     this.value = value;
/*     */     
/*  51 */     initializeComponents(chartTitle);
/*     */     
/*  53 */     this.lowMinSlider.setValue(value.getLowMin());
/*  54 */     this.lowMaxSlider.setValue(value.getLowMax());
/*  55 */     this.highMinSlider.setValue(value.getHighMin());
/*  56 */     this.highMaxSlider.setValue(value.getHighMax());
/*  57 */     this.chart.setValues(value.getTimeline(), value.getScaling());
/*  58 */     this.relativeCheckBox.setSelected(value.isRelative());
/*     */     
/*  60 */     this.lowMinSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/*  62 */             value.setLowMin(Float.valueOf(ScaledNumericPanel.this.lowMinSlider.getValue()).floatValue());
/*  63 */             if (!ScaledNumericPanel.this.lowMaxSlider.isVisible()) value.setLowMax(Float.valueOf(ScaledNumericPanel.this.lowMinSlider.getValue()).floatValue()); 
/*     */           }
/*     */         });
/*  66 */     this.lowMaxSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/*  68 */             value.setLowMax(Float.valueOf(ScaledNumericPanel.this.lowMaxSlider.getValue()).floatValue());
/*     */           }
/*     */         });
/*  71 */     this.highMinSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/*  73 */             value.setHighMin(Float.valueOf(ScaledNumericPanel.this.highMinSlider.getValue()).floatValue());
/*  74 */             if (!ScaledNumericPanel.this.highMaxSlider.isVisible()) value.setHighMax(Float.valueOf(ScaledNumericPanel.this.highMinSlider.getValue()).floatValue()); 
/*     */           }
/*     */         });
/*  77 */     this.highMaxSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/*  79 */             value.setHighMax(Float.valueOf(ScaledNumericPanel.this.highMaxSlider.getValue()).floatValue());
/*     */           }
/*     */         });
/*     */     
/*  83 */     this.relativeCheckBox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  85 */             value.setRelative(ScaledNumericPanel.this.relativeCheckBox.isSelected());
/*     */           }
/*     */         });
/*     */     
/*  89 */     this.lowRangeButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  91 */             boolean visible = !ScaledNumericPanel.this.lowMaxSlider.isVisible();
/*  92 */             ScaledNumericPanel.this.lowMaxSlider.setVisible(visible);
/*  93 */             ScaledNumericPanel.this.lowRangeButton.setText(visible ? "<" : ">");
/*  94 */             GridBagLayout layout = (GridBagLayout)ScaledNumericPanel.this.formPanel.getLayout();
/*  95 */             GridBagConstraints constraints = layout.getConstraints(ScaledNumericPanel.this.lowRangeButton);
/*  96 */             constraints.gridx = visible ? 5 : 4;
/*  97 */             layout.setConstraints(ScaledNumericPanel.this.lowRangeButton, constraints);
/*  98 */             Slider slider = visible ? ScaledNumericPanel.this.lowMaxSlider : ScaledNumericPanel.this.lowMinSlider;
/*  99 */             value.setLowMax(Float.valueOf(slider.getValue()).floatValue());
/*     */           }
/*     */         });
/*     */     
/* 103 */     this.highRangeButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 105 */             boolean visible = !ScaledNumericPanel.this.highMaxSlider.isVisible();
/* 106 */             ScaledNumericPanel.this.highMaxSlider.setVisible(visible);
/* 107 */             ScaledNumericPanel.this.highRangeButton.setText(visible ? "<" : ">");
/* 108 */             GridBagLayout layout = (GridBagLayout)ScaledNumericPanel.this.formPanel.getLayout();
/* 109 */             GridBagConstraints constraints = layout.getConstraints(ScaledNumericPanel.this.highRangeButton);
/* 110 */             constraints.gridx = visible ? 5 : 4;
/* 111 */             layout.setConstraints(ScaledNumericPanel.this.highRangeButton, constraints);
/* 112 */             Slider slider = visible ? ScaledNumericPanel.this.highMaxSlider : ScaledNumericPanel.this.highMinSlider;
/* 113 */             value.setHighMax(Float.valueOf(slider.getValue()).floatValue());
/*     */           }
/*     */         });
/*     */     
/* 117 */     this.expandButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 119 */             ScaledNumericPanel.this.chart.setExpanded(!ScaledNumericPanel.this.chart.isExpanded());
/* 120 */             boolean expanded = ScaledNumericPanel.this.chart.isExpanded();
/* 121 */             GridBagLayout layout = (GridBagLayout)ScaledNumericPanel.this.getContentPanel().getLayout();
/* 122 */             GridBagConstraints chartConstraints = layout.getConstraints(ScaledNumericPanel.this.chart);
/* 123 */             GridBagConstraints expandButtonConstraints = layout.getConstraints(ScaledNumericPanel.this.expandButton);
/* 124 */             if (expanded) {
/* 125 */               ScaledNumericPanel.this.chart.setPreferredSize(new Dimension(150, 200));
/* 126 */               ScaledNumericPanel.this.expandButton.setText("-");
/* 127 */               chartConstraints.weightx = 1.0D;
/* 128 */               expandButtonConstraints.weightx = 0.0D;
/*     */             } else {
/* 130 */               ScaledNumericPanel.this.chart.setPreferredSize(new Dimension(150, 30));
/* 131 */               ScaledNumericPanel.this.expandButton.setText("+");
/* 132 */               chartConstraints.weightx = 0.0D;
/* 133 */               expandButtonConstraints.weightx = 1.0D;
/*     */             } 
/* 135 */             layout.setConstraints(ScaledNumericPanel.this.chart, chartConstraints);
/* 136 */             layout.setConstraints(ScaledNumericPanel.this.expandButton, expandButtonConstraints);
/* 137 */             ScaledNumericPanel.this.relativeCheckBox.setVisible(!expanded);
/* 138 */             ScaledNumericPanel.this.formPanel.setVisible(!expanded);
/* 139 */             ScaledNumericPanel.this.chart.revalidate();
/*     */           }
/*     */         });
/*     */     
/* 143 */     if (value.getLowMin() == value.getLowMax()) this.lowRangeButton.doClick(0); 
/* 144 */     if (value.getHighMin() == value.getHighMax()) this.highRangeButton.doClick(0); 
/*     */   }
/*     */   
/*     */   public JPanel getFormPanel() {
/* 148 */     return this.formPanel;
/*     */   }
/*     */   
/*     */   private void initializeComponents(String chartTitle) {
/* 152 */     JPanel contentPanel = getContentPanel();
/*     */     
/* 154 */     this.formPanel = new JPanel(new GridBagLayout());
/* 155 */     contentPanel.add(this.formPanel, new GridBagConstraints(5, 5, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */     
/* 158 */     JLabel label = new JLabel("High:");
/* 159 */     this.formPanel.add(label, new GridBagConstraints(2, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 163 */     this.highMinSlider = new Slider(0.0F, -99999.0F, 99999.0F, 1.0F, -400.0F, 400.0F);
/* 164 */     this.formPanel.add(this.highMinSlider, new GridBagConstraints(3, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 168 */     this.highMaxSlider = new Slider(0.0F, -99999.0F, 99999.0F, 1.0F, -400.0F, 400.0F);
/* 169 */     this.formPanel.add(this.highMaxSlider, new GridBagConstraints(4, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 173 */     this.highRangeButton = new JButton("<");
/* 174 */     this.highRangeButton.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
/* 175 */     this.formPanel.add(this.highRangeButton, new GridBagConstraints(5, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 1, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 179 */     label = new JLabel("Low:");
/* 180 */     this.formPanel.add(label, new GridBagConstraints(2, 2, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 184 */     this.lowMinSlider = new Slider(0.0F, -99999.0F, 99999.0F, 1.0F, -400.0F, 400.0F);
/* 185 */     this.formPanel.add(this.lowMinSlider, new GridBagConstraints(3, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 189 */     this.lowMaxSlider = new Slider(0.0F, -99999.0F, 99999.0F, 1.0F, -400.0F, 400.0F);
/* 190 */     this.formPanel.add(this.lowMaxSlider, new GridBagConstraints(4, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 194 */     this.lowRangeButton = new JButton("<");
/* 195 */     this.lowRangeButton.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
/* 196 */     this.formPanel.add(this.lowRangeButton, new GridBagConstraints(5, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 1, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     this.chart = new Chart(chartTitle) {
/*     */         public void pointsChanged() {
/* 203 */           ScaledNumericPanel.this.value.setTimeline(ScaledNumericPanel.this.chart.getValuesX());
/* 204 */           ScaledNumericPanel.this.value.setScaling(ScaledNumericPanel.this.chart.getValuesY());
/*     */         }
/*     */       };
/* 207 */     contentPanel.add(this.chart, new GridBagConstraints(6, 5, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/* 209 */     this.chart.setPreferredSize(new Dimension(150, 30));
/*     */ 
/*     */     
/* 212 */     this.expandButton = new JButton("+");
/* 213 */     contentPanel.add(this.expandButton, new GridBagConstraints(7, 5, 1, 1, 1.0D, 0.0D, 16, 0, new Insets(0, 5, 0, 0), 0, 0));
/*     */     
/* 215 */     this.expandButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
/*     */ 
/*     */     
/* 218 */     this.relativeCheckBox = new JCheckBox("Relative");
/* 219 */     contentPanel.add(this.relativeCheckBox, new GridBagConstraints(7, 5, 1, 1, 0.0D, 0.0D, 18, 0, new Insets(0, 6, 0, 0), 0, 0));
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\ScaledNumericPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */