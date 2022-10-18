/*     */ package com.badlogic.gdx.tools.flame;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ public class RegionPickerPanel extends JPanel {
/*     */   TextureAtlasPanel atlasPanel;
/*     */   TexturePanel texturePanel;
/*     */   JButton selectButton;
/*     */   JButton selectAllButton;
/*     */   JButton clearButton;
/*     */   JButton generateButton;
/*     */   JButton reverseButton;
/*     */   JComboBox generateBox;
/*     */   Slider rowSlider;
/*     */   Slider columnSlider;
/*     */   JPanel generationPanel;
/*     */   JPanel content;
/*     */   Listener listener;
/*     */   
/*     */   private enum GenerationMode {
/*  25 */     ByRows("Generate By Rows"), ByColumns("Generate By Columns"); String string;
/*     */     
/*     */     GenerationMode(String string) {
/*  28 */       this.string = string;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  33 */       return this.string;
/*     */     }
/*     */   }
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
/*     */   public RegionPickerPanel(Listener listener) {
/*  50 */     initializeComponents();
/*  51 */     this.listener = listener;
/*     */   }
/*     */   
/*     */   private void initializeComponents() {
/*  55 */     setLayout(new GridBagLayout());
/*  56 */     this.content = new JPanel();
/*  57 */     this.atlasPanel = new TextureAtlasPanel();
/*  58 */     this.texturePanel = new TexturePanel();
/*  59 */     CustomCardLayout cardLayout = new CustomCardLayout();
/*  60 */     this.content.setLayout(cardLayout);
/*  61 */     this.content.add(this.atlasPanel, "atlas");
/*  62 */     this.content.add(this.texturePanel, "texture");
/*     */ 
/*     */     
/*  65 */     add(this.content, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */     
/*  68 */     JPanel controls = new JPanel(new GridBagLayout());
/*  69 */     controls.add(this.selectButton = new JButton("Select"), new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */     
/*  72 */     controls.add(new JSeparator(0), new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  76 */     JPanel pickPanel = new JPanel(new GridBagLayout());
/*  77 */     pickPanel.add(this.selectAllButton = new JButton("Pick All"), new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/*  79 */     pickPanel.add(this.clearButton = new JButton("Clear Selection"), new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/*  81 */     pickPanel.add(this.reverseButton = new JButton("Reverse Selection"), new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/*  83 */     controls.add(pickPanel, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/*  87 */     this.generationPanel = new JPanel(new GridBagLayout());
/*  88 */     this.generationPanel.add(new JLabel("Rows"), new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/*  90 */     this.generationPanel.add(this.rowSlider = new Slider(1.0F, 1.0F, 9999.0F, 1.0F), new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/*  92 */     this.generationPanel.add(new JLabel("Columns"), new GridBagConstraints(0, 1, 1, 1, 1.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/*  94 */     this.generationPanel.add(this.columnSlider = new Slider(1.0F, 1.0F, 9999.0F, 1.0F), new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/*  96 */     this.generationPanel.add(this.generateBox = new JComboBox(new DefaultComboBoxModel((Object[])GenerationMode.values())), new GridBagConstraints(0, 2, 1, 1, 1.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/*  98 */     this.generationPanel.add(this.generateButton = new JButton("Generate"), new GridBagConstraints(1, 2, 1, 1, 1.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/* 100 */     controls.add(new JSeparator(0), new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 102 */     controls.add(this.generationPanel, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/* 104 */     add(controls, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 18, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */     
/* 107 */     this.selectButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent arg0) {
/* 110 */             JPanel panel = ((CustomCardLayout)RegionPickerPanel.this.content.getLayout()).<JPanel>getCurrentCard(RegionPickerPanel.this.content);
/* 111 */             TexturePanel currentTexturePanel = (panel == RegionPickerPanel.this.atlasPanel) ? RegionPickerPanel.this.atlasPanel.getCurrentRegionPanel() : RegionPickerPanel.this.texturePanel;
/* 112 */             RegionPickerPanel.this.listener.onRegionsSelected(currentTexturePanel.selectedRegions);
/*     */           }
/*     */         });
/*     */     
/* 116 */     this.selectAllButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent arg0) {
/* 119 */             JPanel panel = ((CustomCardLayout)RegionPickerPanel.this.content.getLayout()).<JPanel>getCurrentCard(RegionPickerPanel.this.content);
/* 120 */             TexturePanel currentTexturePanel = (panel == RegionPickerPanel.this.atlasPanel) ? RegionPickerPanel.this.atlasPanel.getCurrentRegionPanel() : RegionPickerPanel.this.texturePanel;
/* 121 */             currentTexturePanel.selectAll();
/*     */           }
/*     */         });
/*     */     
/* 125 */     this.reverseButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent arg0) {
/* 128 */             JPanel panel = ((CustomCardLayout)RegionPickerPanel.this.content.getLayout()).<JPanel>getCurrentCard(RegionPickerPanel.this.content);
/* 129 */             TexturePanel currentTexturePanel = (panel == RegionPickerPanel.this.atlasPanel) ? RegionPickerPanel.this.atlasPanel.getCurrentRegionPanel() : RegionPickerPanel.this.texturePanel;
/* 130 */             currentTexturePanel.selectedRegions.reverse();
/* 131 */             currentTexturePanel.revalidate();
/* 132 */             currentTexturePanel.repaint();
/*     */           }
/*     */         });
/*     */     
/* 136 */     this.clearButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent arg0) {
/* 139 */             JPanel panel = ((CustomCardLayout)RegionPickerPanel.this.content.getLayout()).<JPanel>getCurrentCard(RegionPickerPanel.this.content);
/* 140 */             TexturePanel currentPanel = (panel == RegionPickerPanel.this.atlasPanel) ? RegionPickerPanel.this.atlasPanel.getCurrentRegionPanel() : RegionPickerPanel.this.texturePanel;
/* 141 */             currentPanel.clearSelection();
/*     */           }
/*     */         });
/*     */     
/* 145 */     this.generateButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent arg0) {
/* 148 */             RegionPickerPanel.this.generateRegions((RegionPickerPanel.GenerationMode)RegionPickerPanel.this.generateBox.getSelectedItem());
/* 149 */             RegionPickerPanel.this.texturePanel.revalidate();
/* 150 */             RegionPickerPanel.this.texturePanel.repaint();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void generateRegions(GenerationMode mode) {
/* 158 */     this.texturePanel.clear();
/* 159 */     Texture texture = this.texturePanel.getTexture();
/* 160 */     int rows = (int)this.rowSlider.getValue(), columns = (int)this.columnSlider.getValue();
/* 161 */     int yOffset = texture.getHeight() / rows, xOffset = texture.getWidth() / columns;
/*     */     
/* 163 */     if (mode == GenerationMode.ByRows) {
/* 164 */       for (int j = 0; j < rows; j++) {
/* 165 */         int rowOffset = j * yOffset;
/* 166 */         for (int i = 0; i < columns; i++) {
/* 167 */           this.texturePanel.unselectedRegions.add(new TextureRegion(texture, i * xOffset, rowOffset, xOffset, yOffset));
/*     */         }
/*     */       }
/*     */     
/* 171 */     } else if (mode == GenerationMode.ByColumns) {
/* 172 */       for (int i = 0; i < columns; i++) {
/* 173 */         int columnOffset = i * xOffset;
/* 174 */         for (int j = 0; j < rows; j++) {
/* 175 */           this.texturePanel.unselectedRegions.add(new TextureRegion(texture, columnOffset, j * yOffset, xOffset, yOffset));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setAtlas(TextureAtlas atlas) {
/* 182 */     this.atlasPanel.clearSelection();
/* 183 */     this.atlasPanel.setAtlas(atlas);
/* 184 */     CustomCardLayout cardLayout = (CustomCardLayout)this.content.getLayout();
/* 185 */     cardLayout.show(this.content, "atlas");
/* 186 */     showGenerationPanel(false);
/* 187 */     this.content.revalidate();
/* 188 */     this.content.repaint();
/* 189 */     revalidate();
/* 190 */     repaint();
/*     */   }
/*     */   
/*     */   public void setTexture(Texture texture) {
/* 194 */     this.texturePanel.clearSelection();
/* 195 */     this.texturePanel.setTexture(texture);
/* 196 */     CustomCardLayout cardLayout = (CustomCardLayout)this.content.getLayout();
/* 197 */     cardLayout.show(this.content, "texture");
/* 198 */     showGenerationPanel(true);
/* 199 */     this.content.revalidate();
/* 200 */     this.content.repaint();
/* 201 */     revalidate();
/* 202 */     repaint();
/*     */   }
/*     */   
/*     */   private void showGenerationPanel(boolean isShown) {
/* 206 */     this.generationPanel.setVisible(isShown);
/*     */   }
/*     */   
/*     */   public static interface Listener {
/*     */     void onRegionsSelected(Array<TextureRegion> param1Array);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\RegionPickerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */