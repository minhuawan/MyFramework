/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JToggleButton;
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
/*     */ class EditorPanel
/*     */   extends JPanel
/*     */ {
/*     */   private final String name;
/*     */   private final String description;
/*     */   private final ParticleEmitter.ParticleValue value;
/*     */   private JPanel titlePanel;
/*     */   JToggleButton activeButton;
/*     */   private JPanel contentPanel;
/*     */   JToggleButton advancedButton;
/*     */   JPanel advancedPanel;
/*     */   private boolean hasAdvanced;
/*     */   JLabel descriptionLabel;
/*     */   
/*     */   public EditorPanel(ParticleEmitter.ParticleValue value, String name, String description) {
/*  48 */     this.name = name;
/*  49 */     this.value = value;
/*  50 */     this.description = description;
/*     */     
/*  52 */     initializeComponents();
/*     */     
/*  54 */     this.titlePanel.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent event) {
/*  56 */             if (!EditorPanel.this.activeButton.isVisible())
/*  57 */               return;  EditorPanel.this.activeButton.setSelected(!EditorPanel.this.activeButton.isSelected());
/*  58 */             EditorPanel.this.updateActive();
/*     */           }
/*     */         });
/*  61 */     this.activeButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  63 */             EditorPanel.this.updateActive();
/*     */           }
/*     */         });
/*  66 */     this.advancedButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  68 */             EditorPanel.this.advancedPanel.setVisible(EditorPanel.this.advancedButton.isSelected());
/*     */           }
/*     */         });
/*     */     
/*  72 */     if (value != null) {
/*  73 */       this.activeButton.setSelected(value.isActive());
/*  74 */       updateActive();
/*     */     } 
/*     */     
/*  77 */     boolean alwaysActive = (value == null) ? true : value.isAlwaysActive();
/*  78 */     this.activeButton.setVisible(!alwaysActive);
/*  79 */     if (alwaysActive) this.contentPanel.setVisible(true); 
/*  80 */     if (alwaysActive) this.titlePanel.setCursor(null); 
/*     */   }
/*     */   
/*     */   void updateActive() {
/*  84 */     this.contentPanel.setVisible(this.activeButton.isSelected());
/*  85 */     this.advancedPanel.setVisible((this.activeButton.isSelected() && this.advancedButton.isSelected()));
/*  86 */     this.advancedButton.setVisible((this.activeButton.isSelected() && this.hasAdvanced));
/*  87 */     this.descriptionLabel.setText(this.activeButton.isSelected() ? this.description : "");
/*  88 */     if (this.value != null) this.value.setActive(this.activeButton.isSelected());
/*     */   
/*     */   }
/*     */   
/*     */   public void update(ParticleEditor editor) {}
/*     */   
/*     */   public void setHasAdvanced(boolean hasAdvanced) {
/*  95 */     this.hasAdvanced = hasAdvanced;
/*  96 */     this.advancedButton.setVisible((hasAdvanced && (this.value.isActive() || this.value.isAlwaysActive())));
/*     */   }
/*     */   
/*     */   public JPanel getContentPanel() {
/* 100 */     return this.contentPanel;
/*     */   }
/*     */   
/*     */   public JPanel getAdvancedPanel() {
/* 104 */     return this.advancedPanel;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 108 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setEmbedded() {
/* 112 */     GridBagLayout layout = (GridBagLayout)getLayout();
/* 113 */     GridBagConstraints constraints = layout.getConstraints(this.contentPanel);
/* 114 */     constraints.insets = new Insets(0, 0, 0, 0);
/* 115 */     layout.setConstraints(this.contentPanel, constraints);
/*     */     
/* 117 */     this.titlePanel.setVisible(false);
/*     */   }
/*     */   
/*     */   private void initializeComponents() {
/* 121 */     setLayout(new GridBagLayout());
/*     */     
/* 123 */     this.titlePanel = new JPanel(new GridBagLayout());
/* 124 */     add(this.titlePanel, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(3, 0, 3, 0), 0, 0));
/*     */     
/* 126 */     this.titlePanel.setCursor(Cursor.getPredefinedCursor(12));
/*     */     
/* 128 */     JLabel label = new JLabel(this.name);
/* 129 */     this.titlePanel.add(label, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(3, 6, 3, 6), 0, 0));
/*     */     
/* 131 */     label.setFont(label.getFont().deriveFont(1));
/*     */ 
/*     */     
/* 134 */     this.descriptionLabel = new JLabel(this.description);
/* 135 */     this.titlePanel.add(this.descriptionLabel, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(3, 6, 3, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 139 */     this.advancedButton = new JToggleButton("Advanced");
/* 140 */     this.titlePanel.add(this.advancedButton, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */     
/* 142 */     this.advancedButton.setVisible(false);
/*     */ 
/*     */     
/* 145 */     this.activeButton = new JToggleButton("Active");
/* 146 */     this.titlePanel.add(this.activeButton, new GridBagConstraints(3, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     this.contentPanel = new JPanel(new GridBagLayout());
/* 152 */     add(this.contentPanel, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 6, 6, 6), 0, 0));
/*     */     
/* 154 */     this.contentPanel.setVisible(false);
/*     */ 
/*     */     
/* 157 */     this.advancedPanel = new JPanel(new GridBagLayout());
/* 158 */     add(this.advancedPanel, new GridBagConstraints(1, 2, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 6, 6, 6), 0, 0));
/*     */     
/* 160 */     this.advancedPanel.setVisible(false);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\EditorPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */