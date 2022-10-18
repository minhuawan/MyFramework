/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ParticleValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ScaledNumericValue;
/*     */ import com.badlogic.gdx.tools.particleeditor.Chart;
/*     */ import java.awt.Component;
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
/*     */ class ScaledNumericPanel
/*     */   extends ParticleValuePanel<ScaledNumericValue>
/*     */ {
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
/*     */   public ScaledNumericPanel(FlameMain editor, ScaledNumericValue value, String chartTitle, String name, String description) {
/*  48 */     this(editor, value, chartTitle, name, description, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScaledNumericPanel(FlameMain editor, ScaledNumericValue value, String chartTitle, String name, String description, boolean isAlwaysActive) {
/*  53 */     super(editor, name, description, isAlwaysActive);
/*  54 */     initializeComponents(chartTitle);
/*  55 */     setValue(value);
/*     */   }
/*     */   
/*     */   public JPanel getFormPanel() {
/*  59 */     return this.formPanel;
/*     */   }
/*     */   
/*     */   private void initializeComponents(String chartTitle) {
/*  63 */     JPanel contentPanel = getContentPanel();
/*     */     
/*  65 */     this.formPanel = new JPanel(new GridBagLayout());
/*  66 */     contentPanel.add(this.formPanel, new GridBagConstraints(5, 5, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */     
/*  69 */     JLabel label = new JLabel("High:");
/*  70 */     this.formPanel.add(label, new GridBagConstraints(2, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  74 */     this.highMinSlider = new Slider(0.0F, -999999.0F, 999999.0F, 1.0F);
/*  75 */     this.formPanel.add(this.highMinSlider, new GridBagConstraints(3, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  79 */     this.highMaxSlider = new Slider(0.0F, -999999.0F, 999999.0F, 1.0F);
/*  80 */     this.formPanel.add(this.highMaxSlider, new GridBagConstraints(4, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  84 */     this.highRangeButton = new JButton("<");
/*  85 */     this.highRangeButton.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
/*  86 */     this.formPanel.add(this.highRangeButton, new GridBagConstraints(5, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 1, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  90 */     label = new JLabel("Low:");
/*  91 */     this.formPanel.add(label, new GridBagConstraints(2, 2, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  95 */     this.lowMinSlider = new Slider(0.0F, -999999.0F, 999999.0F, 1.0F);
/*  96 */     this.formPanel.add(this.lowMinSlider, new GridBagConstraints(3, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 100 */     this.lowMaxSlider = new Slider(0.0F, -999999.0F, 999999.0F, 1.0F);
/* 101 */     this.formPanel.add(this.lowMaxSlider, new GridBagConstraints(4, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.lowRangeButton = new JButton("<");
/* 106 */     this.lowRangeButton.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
/* 107 */     this.formPanel.add(this.lowRangeButton, new GridBagConstraints(5, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 1, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     this.chart = new Chart(chartTitle) {
/*     */         public void pointsChanged() {
/* 114 */           ScaledNumericPanel.this.value.setTimeline(ScaledNumericPanel.this.chart.getValuesX());
/* 115 */           ScaledNumericPanel.this.value.setScaling(ScaledNumericPanel.this.chart.getValuesY());
/*     */         }
/*     */       };
/* 118 */     contentPanel.add((Component)this.chart, new GridBagConstraints(6, 5, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/* 120 */     this.chart.setPreferredSize(new Dimension(150, 30));
/*     */ 
/*     */     
/* 123 */     this.expandButton = new JButton("+");
/* 124 */     contentPanel.add(this.expandButton, new GridBagConstraints(7, 5, 1, 1, 1.0D, 0.0D, 16, 0, new Insets(0, 5, 0, 0), 0, 0));
/*     */     
/* 126 */     this.expandButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
/*     */ 
/*     */     
/* 129 */     this.relativeCheckBox = new JCheckBox("Relative");
/* 130 */     contentPanel.add(this.relativeCheckBox, new GridBagConstraints(7, 5, 1, 1, 0.0D, 0.0D, 18, 0, new Insets(0, 6, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 134 */     this.lowMinSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/* 136 */             ScaledNumericPanel.this.value.setLowMin(ScaledNumericPanel.this.lowMinSlider.getValue());
/* 137 */             if (!ScaledNumericPanel.this.lowMaxSlider.isVisible()) ScaledNumericPanel.this.value.setLowMax(ScaledNumericPanel.this.lowMinSlider.getValue()); 
/*     */           }
/*     */         });
/* 140 */     this.lowMaxSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/* 142 */             ScaledNumericPanel.this.value.setLowMax(ScaledNumericPanel.this.lowMaxSlider.getValue());
/*     */           }
/*     */         });
/* 145 */     this.highMinSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/* 147 */             ScaledNumericPanel.this.value.setHighMin(ScaledNumericPanel.this.highMinSlider.getValue());
/* 148 */             if (!ScaledNumericPanel.this.highMaxSlider.isVisible()) ScaledNumericPanel.this.value.setHighMax(ScaledNumericPanel.this.highMinSlider.getValue()); 
/*     */           }
/*     */         });
/* 151 */     this.highMaxSlider.addChangeListener(new ChangeListener() {
/*     */           public void stateChanged(ChangeEvent event) {
/* 153 */             ScaledNumericPanel.this.value.setHighMax(ScaledNumericPanel.this.highMaxSlider.getValue());
/*     */           }
/*     */         });
/*     */     
/* 157 */     this.relativeCheckBox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 159 */             ScaledNumericPanel.this.value.setRelative(ScaledNumericPanel.this.relativeCheckBox.isSelected());
/*     */           }
/*     */         });
/*     */     
/* 163 */     this.lowRangeButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 165 */             boolean visible = !ScaledNumericPanel.this.lowMaxSlider.isVisible();
/* 166 */             ScaledNumericPanel.this.lowMaxSlider.setVisible(visible);
/* 167 */             ScaledNumericPanel.this.lowRangeButton.setText(visible ? "<" : ">");
/* 168 */             GridBagLayout layout = (GridBagLayout)ScaledNumericPanel.this.formPanel.getLayout();
/* 169 */             GridBagConstraints constraints = layout.getConstraints(ScaledNumericPanel.this.lowRangeButton);
/* 170 */             constraints.gridx = visible ? 5 : 4;
/* 171 */             layout.setConstraints(ScaledNumericPanel.this.lowRangeButton, constraints);
/* 172 */             Slider slider = visible ? ScaledNumericPanel.this.lowMaxSlider : ScaledNumericPanel.this.lowMinSlider;
/* 173 */             ScaledNumericPanel.this.value.setLowMax(slider.getValue());
/*     */           }
/*     */         });
/*     */     
/* 177 */     this.highRangeButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 179 */             boolean visible = !ScaledNumericPanel.this.highMaxSlider.isVisible();
/* 180 */             ScaledNumericPanel.this.highMaxSlider.setVisible(visible);
/* 181 */             ScaledNumericPanel.this.highRangeButton.setText(visible ? "<" : ">");
/* 182 */             GridBagLayout layout = (GridBagLayout)ScaledNumericPanel.this.formPanel.getLayout();
/* 183 */             GridBagConstraints constraints = layout.getConstraints(ScaledNumericPanel.this.highRangeButton);
/* 184 */             constraints.gridx = visible ? 5 : 4;
/* 185 */             layout.setConstraints(ScaledNumericPanel.this.highRangeButton, constraints);
/* 186 */             Slider slider = visible ? ScaledNumericPanel.this.highMaxSlider : ScaledNumericPanel.this.highMinSlider;
/* 187 */             ScaledNumericPanel.this.value.setHighMax(slider.getValue());
/*     */           }
/*     */         });
/*     */     
/* 191 */     this.expandButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 193 */             ScaledNumericPanel.this.chart.setExpanded(!ScaledNumericPanel.this.chart.isExpanded());
/* 194 */             boolean expanded = ScaledNumericPanel.this.chart.isExpanded();
/* 195 */             GridBagLayout layout = (GridBagLayout)ScaledNumericPanel.this.getContentPanel().getLayout();
/* 196 */             GridBagConstraints chartConstraints = layout.getConstraints((Component)ScaledNumericPanel.this.chart);
/* 197 */             GridBagConstraints expandButtonConstraints = layout.getConstraints(ScaledNumericPanel.this.expandButton);
/* 198 */             if (expanded) {
/* 199 */               ScaledNumericPanel.this.chart.setPreferredSize(new Dimension(150, 200));
/* 200 */               ScaledNumericPanel.this.expandButton.setText("-");
/* 201 */               chartConstraints.weightx = 1.0D;
/* 202 */               expandButtonConstraints.weightx = 0.0D;
/*     */             } else {
/* 204 */               ScaledNumericPanel.this.chart.setPreferredSize(new Dimension(150, 30));
/* 205 */               ScaledNumericPanel.this.expandButton.setText("+");
/* 206 */               chartConstraints.weightx = 0.0D;
/* 207 */               expandButtonConstraints.weightx = 1.0D;
/*     */             } 
/* 209 */             layout.setConstraints((Component)ScaledNumericPanel.this.chart, chartConstraints);
/* 210 */             layout.setConstraints(ScaledNumericPanel.this.expandButton, expandButtonConstraints);
/* 211 */             ScaledNumericPanel.this.relativeCheckBox.setVisible(!expanded);
/* 212 */             ScaledNumericPanel.this.formPanel.setVisible(!expanded);
/* 213 */             ScaledNumericPanel.this.chart.revalidate();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(ScaledNumericValue value) {
/* 221 */     super.setValue(value);
/* 222 */     if (this.value == null)
/* 223 */       return;  setValue(this.lowMinSlider, this.value.getLowMin());
/* 224 */     setValue(this.lowMaxSlider, this.value.getLowMax());
/* 225 */     setValue(this.highMinSlider, this.value.getHighMin());
/* 226 */     setValue(this.highMaxSlider, this.value.getHighMax());
/* 227 */     this.chart.setValues(this.value.getTimeline(), this.value.getScaling());
/* 228 */     setValue(this.relativeCheckBox, this.value.isRelative());
/*     */     
/* 230 */     if ((this.value.getLowMin() == this.value.getLowMax() && this.lowMaxSlider.isVisible()) || (this.value
/* 231 */       .getLowMin() != this.value.getLowMax() && !this.lowMaxSlider.isVisible())) {
/* 232 */       this.lowRangeButton.doClick(0);
/*     */     }
/* 234 */     if ((this.value.getHighMin() == this.value.getHighMax() && this.highMaxSlider.isVisible()) || (this.value
/* 235 */       .getHighMin() != this.value.getHighMax() && !this.highMaxSlider.isVisible()))
/* 236 */       this.highRangeButton.doClick(0); 
/*     */   }
/*     */   
/*     */   public Chart getChart() {
/* 240 */     return this.chart;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\ScaledNumericPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */